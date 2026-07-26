# Infraestructura local: Docker Compose + Jenkins

Este directorio y el `docker-compose.yml` de la raíz levantan todo el stack de SmartStore (Postgres, backend, frontend) más una instancia de Jenkins ya preparada para correr el pipeline del repo.

## Levantar el stack

```bash
docker compose up -d
```

Servicios y puertos por defecto:

| Servicio  | URL local                  | Notas |
|-----------|-----------------------------|-------|
| Postgres  | `localhost:5432`            | Cambiar con `POSTGRES_HOST_PORT` si ya tienes otro Postgres corriendo en ese puerto |
| Backend   | http://localhost:8080/api   | Spring Boot |
| Frontend  | http://localhost:3000       | SvelteKit (adapter-node) |
| Jenkins   | http://localhost:8081       | Usuario/clave inicial: ver abajo |

Variables de entorno soportadas (todas opcionales, con default para desarrollo local): `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`, `POSTGRES_HOST_PORT`, `JWT_SECRET`, `JWT_EXPIRATION`, `JWT_REFRESH_EXPIRATION`, `ADMIN_SEED_EMAIL`, `ADMIN_SEED_PASSWORD`, `PUBLIC_API_URL` (usada en build-time del frontend, ya que SvelteKit inlinea `$env/static/public`).

### Usuario ADMIN sembrado

Como el registro público (`POST /api/auth/register`) siempre asigna el rol `USER`, el `DataSeeder` crea automáticamente un usuario `ADMIN` al arrancar (si no existe todavía), usando `ADMIN_SEED_EMAIL`/`ADMIN_SEED_PASSWORD` (default: `admin@smartstore.com` / `Admin123!`). Es la única forma de operar como ADMIN (crear categorías/productos) sin tocar la base de datos a mano.

**Son credenciales de desarrollo/pruebas, no aptas para un despliegue real** — en producción hay que sobreescribir `ADMIN_SEED_PASSWORD` con algo fuerte vía variable de entorno, o retirar el seeding y crear el ADMIN manualmente.

## Primer arranque de Jenkins

1. `docker compose up -d jenkins`
2. Obtén la contraseña inicial de administrador:
   ```bash
   docker exec smartstore-jenkins cat /var/jenkins_home/secrets/initialAdminPassword
   ```
3. Entra a http://localhost:8081, pega la contraseña y completa el setup wizard (puedes omitir la instalación de plugins sugeridos — ya vienen preinstalados desde `docker/jenkins/plugins.txt`).

## Crear el Pipeline job

1. **New Item** → nombre `smartstore-ci` → tipo **Pipeline**.
2. En **Pipeline** → **Definition**: `Pipeline script from SCM`.
3. **SCM**: `Git`, **Repository URL**: `https://github.com/EstivenLemos/qa-enterprise-automation.git`, **Branch**: `*/main` (o `*/dev`).
4. **Script Path**: `Jenkinsfile` (ya está en la raíz del repo).
5. Guarda y ejecuta **Build Now**.

El pipeline corre: tests unitarios + integración del backend (con Testcontainers, por eso el socket de Docker del host está montado en el contenedor de Jenkins), publica reportes de JUnit y cobertura JaCoCo, instala/compila el frontend, construye las imágenes Docker de ambos, y finalmente levanta Postgres+backend con `docker compose` y corre la suite de automatización de API (Playwright, `apps/qa-automation`) contra ese backend real, publicando el reporte con el plugin de Allure.

### Automatización de API (Playwright) contra el stack real

`apps/qa-automation` es una suite de caja negra (no toca código Java ni la base de datos directamente) que valida el contrato HTTP del backend: status codes, forma del `ApiResponse<T>`, validaciones y reglas de autorización (ADMIN vs USER vs anónimo). Para correrla en local:

```bash
docker compose up -d postgres backend
cd apps/qa-automation
pnpm install
BASE_URL=http://localhost:8080/api npx playwright test
pnpm run report   # abre el reporte Allure generado en allure-results/
```

**Nota sobre `baseURL`**: debe terminar en `/` (`http://localhost:8080/api/`) y las rutas dentro de los tests NO deben empezar con `/` (usar `'categories'`, no `'/categories'`). Con un `baseURL` sin barra final y una ruta que empieza en `/`, la resolución de URL de Playwright/WHATWG trata el path como absoluto desde el origen y descarta el `/api` — el backend responde 403 con cuerpo vacío (cae en la regla `anyRequest().authenticated()` en vez del `permitAll()`), y `response.json()` explota con "Unexpected end of JSON input". Esto ya está resuelto en `playwright.config.ts` (normaliza el `baseURL` automáticamente) y en todos los specs — queda documentado aquí porque es un error fácil de reintroducir al agregar rutas nuevas.

En Jenkins, esta stage usa `host.docker.internal` como host (no `localhost`) porque el contenedor de Jenkins es un sibling del stack de `docker compose` (Docker-outside-of-Docker), no un miembro de su red — ver el `Jenkinsfile`.

### Disparo automático de builds

Jenkins corriendo en tu máquina **no es alcanzable desde GitHub** (no hay una URL pública), así que un webhook de GitHub no funcionará aquí. Para desarrollo local, configura en el job un trigger **Poll SCM** (por ejemplo `H/5 * * * *`, cada 5 minutos) en vez de un webhook.

Si en el futuro despliegas Jenkins en un servidor con IP/dominio público, ahí sí conviene cambiar a un webhook de GitHub (Settings → Webhooks → `http://tu-jenkins/github-webhook/`) para builds instantáneos en cada push.

## Notas de la imagen de Jenkins

- `docker/jenkins/Dockerfile` parte de `jenkins/jenkins:lts-jdk21` e instala Maven, Node.js/pnpm (para el frontend y la suite de Playwright) y el CLI de Docker + `docker-compose-plugin` (para levantar el stack de pruebas desde el propio pipeline).
- El contenedor corre con `user: root` y monta `/var/run/docker.sock` del host (patrón *Docker-outside-of-Docker*): así los tests de integración con Testcontainers, los `docker build`/`docker compose` del pipeline usan el mismo motor Docker de tu máquina, sin necesidad de Docker-in-Docker. Es una simplificación válida para un entorno de portafolio/local; en un servidor compartido convendría un agente dedicado con permisos más acotados.
- `plugins.txt` incluye `allure-jenkins-plugin`, que genera el HTML del reporte a partir de `allure-results/` directamente en el pipeline (paso `allure(...)`) — no hace falta instalar el CLI de Allure en la imagen.
