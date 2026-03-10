# API Reactiva de Catalogo de Videojuegos

Proyecto con Spring Boot WebFlux + PostgreSQL + R2DBC, preparado para ejecutarse localmente con Docker y desplegarse en Google Cloud Run con Cloud SQL PostgreSQL.

## Stack

- Java 21
- Spring Boot 4
- Spring WebFlux
- Spring Data R2DBC
- PostgreSQL
- Docker / Docker Compose
- Google Cloud Run
- Google Cloud SQL
- Cloud Build

## Funcionalidad

- Crear videojuegos
- Listar videojuegos
- Buscar por id
- Filtrar por genero
- Filtrar por disponibilidad
- Buscar por texto en titulo, estudio o plataforma
- Actualizar videojuegos
- Cambiar disponibilidad
- Eliminar videojuegos

## Endpoints

```http
GET /api/v1/games
GET /api/v1/games?genre=RPG
GET /api/v1/games?available=true
GET /api/v1/games?q=hades
GET /api/v1/games/{id}
POST /api/v1/games
PUT /api/v1/games/{id}
PATCH /api/v1/games/{id}/availability?available=false
DELETE /api/v1/games/{id}
GET /
GET /actuator/health
GET /actuator/health/liveness
GET /actuator/health/readiness
```

## Variables de entorno

Configuracion local o para cualquier PostgreSQL administrado:

```env
SPRING_PROFILES_ACTIVE=local
DATABASE_URL=
DB_HOST=localhost
DB_PORT=5432
DB_NAME=bolasdejairo
DB_USERNAME=postgres
DB_PASSWORD=postgres
DB_INIT_ENABLED=true
DB_POOL_INITIAL_SIZE=0
DB_POOL_MAX_SIZE=20
DB_POOL_MAX_IDLE_TIME=30m
PORT=8080
```

Configuracion especifica para Google Cloud Run + Cloud SQL:

```env
SPRING_PROFILES_ACTIVE=gcp
GCP_CLOUD_SQL_ENABLED=true
INSTANCE_CONNECTION_NAME=tu-proyecto:tu-region:tu-instancia
DB_NAME=bolasdejairo
DB_USERNAME=postgres
DB_PASSWORD=secret-manager-o-variable
DB_INIT_ENABLED=false
PORT=8080
```

Tambien se aceptan variables tipo `PGHOST`, `PGPORT`, `PGDATABASE`, `PGUSER` y `PGPASSWORD`.

## Ejecucion local con Docker

```bash
docker compose up --build
```

Prueba rapida:

```bash
curl http://localhost:8080/api/v1/games
curl http://localhost:8080/actuator/health/readiness
```

## Ejecucion local sin Docker

Levanta PostgreSQL y luego en PowerShell:

```powershell
$env:SPRING_PROFILES_ACTIVE="local"
$env:DB_HOST="localhost"
$env:DB_PORT="5432"
$env:DB_NAME="bolasdejairo"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="postgres"
.\mvnw.cmd spring-boot:run
```

## Despliegue limpio en Google Cloud

### 1. Recursos que debes crear

1. Un repositorio en GitHub con este codigo.
2. Un servicio de Artifact Registry.
3. Una instancia de Cloud SQL PostgreSQL.
4. Una base de datos y un usuario de aplicacion.
5. Un secreto en Secret Manager para `DB_PASSWORD`.

### 2. Roles minimos recomendados

- Cuenta de servicio de Cloud Run:
  - `Cloud SQL Client`
  - `Secret Manager Secret Accessor`
- Cuenta de servicio de Cloud Build:
  - `Cloud Run Admin`
  - `Artifact Registry Writer`
  - `Service Account User`

### 3. Conexion de GitHub con Google Cloud

1. En Google Cloud abre `Cloud Build -> Triggers`.
2. Crea un trigger conectado al repositorio de GitHub.
3. Selecciona la rama `main`.
4. Indica que use el archivo `cloudbuild.yaml`.
5. Define estas sustituciones en el trigger:

```text
_SERVICE_NAME=bolasdejairo-api
_REGION=us-central1
_AR_REPOSITORY=backend-services
_INSTANCE_CONNECTION_NAME=tu-proyecto:tu-region:tu-instancia
_DB_NAME=bolasdejairo
_DB_USER=postgres
_DB_PASSWORD_SECRET=bolasdejairo-db-password:latest
```

### 4. Conexion con Cloud SQL

La aplicacion ya queda preparada para Cloud SQL usando el conector oficial R2DBC de Google. Solo necesitas:

1. Adjuntar la instancia con `--add-cloudsql-instances`.
2. Establecer `SPRING_PROFILES_ACTIVE=gcp`.
3. Establecer `GCP_CLOUD_SQL_ENABLED=true`.
4. Establecer `INSTANCE_CONNECTION_NAME`, `DB_NAME`, `DB_USERNAME`.
5. Inyectar `DB_PASSWORD` desde Secret Manager.

El perfil `gcp` desactiva la carga automatica de datos semilla para evitar cambios no deseados en produccion.

### 5. Despliegue manual opcional

```bash
gcloud builds submit --config cloudbuild.yaml \
  --substitutions=_SERVICE_NAME=bolasdejairo-api,_REGION=us-central1,_AR_REPOSITORY=backend-services,_INSTANCE_CONNECTION_NAME=tu-proyecto:tu-region:tu-instancia,_DB_NAME=bolasdejairo,_DB_USER=postgres,_DB_PASSWORD_SECRET=bolasdejairo-db-password:latest
```

## Buenas practicas aplicadas

- Health checks de liveness y readiness listos para Cloud Run.
- `DB_INIT_ENABLED=false` por defecto para no sembrar datos en produccion por accidente.
- Pool de conexiones configurable.
- Imagen final basada en distroless para reducir superficie de ataque.
- Pipeline CI en GitHub Actions para validar tests en cada cambio.
- `cloudbuild.yaml` listo para despliegue automatizado desde GitHub.

## Respuesta ejemplo

```json
{
  "title": "Celeste",
  "studio": "Maddy Makes Games",
  "genre": "Platformer",
  "platform": "PC",
  "releaseYear": 2018,
  "rating": 9.3,
  "available": true
}
```
