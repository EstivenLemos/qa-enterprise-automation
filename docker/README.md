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

Variables de entorno soportadas (todas opcionales, con default para desarrollo local): `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`, `POSTGRES_HOST_PORT`, `JWT_SECRET`, `JWT_EXPIRATION`, `JWT_REFRESH_EXPIRATION`, `PUBLIC_API_URL` (usada en build-time del frontend, ya que SvelteKit inlinea `$env/static/public`).

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

El pipeline corre: tests unitarios + integración del backend (con Testcontainers, por eso el socket de Docker del host está montado en el contenedor de Jenkins), publica reportes de JUnit y cobertura JaCoCo, instala/compila el frontend, y construye las imágenes Docker de ambos.

### Disparo automático de builds

Jenkins corriendo en tu máquina **no es alcanzable desde GitHub** (no hay una URL pública), así que un webhook de GitHub no funcionará aquí. Para desarrollo local, configura en el job un trigger **Poll SCM** (por ejemplo `H/5 * * * *`, cada 5 minutos) en vez de un webhook.

Si en el futuro despliegas Jenkins en un servidor con IP/dominio público, ahí sí conviene cambiar a un webhook de GitHub (Settings → Webhooks → `http://tu-jenkins/github-webhook/`) para builds instantáneos en cada push.

## Notas de la imagen de Jenkins

- `docker/jenkins/Dockerfile` parte de `jenkins/jenkins:lts-jdk21` e instala Maven y el CLI de Docker.
- El contenedor corre con `user: root` y monta `/var/run/docker.sock` del host (patrón *Docker-outside-of-Docker*): así los tests de integración con Testcontainers y los `docker build` del pipeline usan el mismo motor Docker de tu máquina, sin necesidad de Docker-in-Docker. Es una simplificación válida para un entorno de portafolio/local; en un servidor compartido convendría un agente dedicado con permisos más acotados.
