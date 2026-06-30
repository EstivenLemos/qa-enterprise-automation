# SmartStore QA Enterprise Automation

SmartStore es un proyecto full-stack desarrollado con el objetivo de simular un entorno empresarial real, desde el desarrollo, QA Automation, DevOps y QA impulsado por IA.

El proyecto se construirá progresivamente, incorporando buenas prácticas de arquitectura, automatización de pruebas, integración continua, seguridad y herramientas modernas utilizadas en la industria.

## Objetivos del proyecto

* Desarrollar una aplicación de e-commerce funcional utilizando tecnologías modernas.
* Implementar una arquitectura escalable y mantenible tanto en frontend como en backend.
* Incorporar progresivamente estrategias de calidad de software y automatización.
* Integrar herramientas de DevOps y DevSecOps utilizadas en entornos empresariales.
* Aplicar inteligencia artificial para optimizar procesos de QA y generación de artefactos de prueba.

---

## Stack tecnológico actual

### Frontend

* SvelteKit
* TypeScript
* PNPM
* TailwindCSS
* Zod
* Lucide Svelte

### Backend

* Java 21
* Spring Boot 3
* Spring Data JPA
* Maven
* Lombok
* PostgreSQL

---

## Funcionalidades previstas

### Autenticación

* Registro de usuarios
* Inicio de sesión
* Recuperación de contraseña
* Cambio de contraseña
* Gestión de perfiles

### Productos

* Catálogo de productos
* Categorías
* Búsqueda y filtros
* Detalle de productos

### Carrito de compras

* Agregar productos
* Eliminar productos
* Actualizar cantidades

### Pedidos

* Checkout
* Historial de compras
* Estados del pedido

---

## Arquitectura del proyecto

```text
qa-enterprise-automation/

├── apps/
│   ├── frontend/     # SvelteKit
│   └── backend/      # Spring Boot
│
├── docs/             # Documentación del proyecto
│
└── docker/           # Configuraciones de contenedores
```

---

## Modelo de dominio

```text
User
 └── Order
       └── OrderItem
             └── Product
                    └── Category
```

---

## Roadmap

### Sprint 1

* Configuración inicial del proyecto
* SvelteKit + Spring Boot + PostgreSQL
* Estructura base del repositorio

### Sprint 2

* Modelado de entidades
* Diseño de la base de datos
* Definición del dominio del negocio

### Sprint 3

* Repositories
* DTOs
* Services
* Controllers
* Datos iniciales (seeders)

### Sprint 4

* Frontend funcional
* Login
* Registro
* Catálogo de productos
* Carrito de compras

### Sprint 5

* Spring Security
* JWT
* Roles y permisos
* Refresh Tokens

### Sprint 6

* Docker
* Docker Compose
* Variables de entorno
* Configuración para despliegue

### Sprint 7

* Pruebas unitarias
* Pruebas de integración
* Cobertura de código

### Sprint 8

* Automatización de APIs
* Playwright API Testing
* Validaciones de contratos

### Sprint 9

* Automatización UI
* Playwright E2E
* Page Object Model
* Screenplay Pattern

### Sprint 10

* Performance Testing con k6
* Security Testing con OWASP ZAP
* SonarQube
* GitHub Actions
* DevSecOps

### Sprint 11

* Integración de IA para QA
* Generación automática de casos de prueba
* Generación de datos de prueba
* Documentación asistida por IA

---

## Estado del proyecto

Actualmente el proyecto se encuentra en desarrollo activo y se construye siguiendo una metodología incremental basada en sprints, simulando un entorno de trabajo empresarial real.

Cada sprint incorpora nuevas funcionalidades, mejoras de arquitectura y prácticas de calidad que posteriormente serán automatizadas mediante pipelines de CI/CD y herramientas de QA modernas.
