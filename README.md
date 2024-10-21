# Hotel Availability Search API

## Descripción General

La **Hotel Availability Search API** es un servicio RESTful diseñado para gestionar búsquedas de disponibilidad de hoteles y proporcionar información relevante sobre las búsquedas almacenadas. Este servicio permite a los usuarios: enviar los detalles sobre búsquedas de hoteles para ser persistidos en MongoDB mediante un procesamiento asíncrono mediante Kafka y, por otro lado, obtener un recuento de las búsquedas similares almacenadas en MongoDB basadas en ciertos criterios como el `hotelId`, las fechas de `checkIn` y `checkOut`, y las edades de los huéspedes.

Este proyecto utiliza tecnologías y herramientas como Spring Boot, Spring Kafka, Spring JPA, Spring MongoDB, Jakarta Bean Validation, Spring Retry, Swagger, JUnit, Mockito...

## Instalación

### 1. Configuración del Entorno
Para poder ejecutar el proyecto correctamente, asegúrate de tener los siguientes requisitos instalados en tu sistema:

- Java 21: El proyecto está configurado para usar Java 21. Asegúrate de tenerlo instalado.
- Maven 3.8+: El proyecto utiliza Apache Maven como herramienta de construcción. Puedes instalar Maven desde aquí.
- Docker y Docker Compose: Se requiere Docker para ejecutar Kafka, Zookeeper y MongoDB en contenedores.

### 2. Configuración del Archivo application.yml
Antes de ejecutar el proyecto, debes configurar las propiedades de Kafka y MongoDB en el archivo application.yml, ubicado en src/main/resources/.

Ejemplo de configuración para Kafka y MongoDB:

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: hotel-search-group
    listener:
      concurrency: 3

  data:
    mongodb:
      uri: mongodb://localhost:27017/hotelAvailabilitySearchDB

app:
  kafka:
    topic: hotel_availability_searches
```

### 3. Ejecutar Servicios Dependientes con Docker (si no están disponibles)
El proyecto depende de Kafka, Zookeeper y MongoDB para funcionar correctamente. Puedes iniciar estos servicios utilizando cualquier entorno de contenedores como docker-compose o Kubernetes

### 4. Compilar el Proyecto
Compila el proyecto utilizando Maven para descargar las dependencias y construir los artefactos necesarios.

```bash
mvn clean install
```

### 5. Ejecutar la Aplicación
Una vez que los servicios de Kafka, Zookeper y MongoDB estén en funcionamiento, puedes ejecutar la aplicación de dos formas:

- Mediante Maven
Ejecutando el comando:

```bash
mvn spring-boot:run
```

- A partir del fichero JAR generado
Ejecutando el comando:

```bash
java -jar target/search-api-0.0.1-SNAPSHOT
```

(El nombre del fichero podría cambiar dependiendo de la versión con la que se esté trabajando)

La API debería estar corriendo en **http://localhost:8080**

## Documentación de la API con Swagger

Este proyecto incluye la integración con **Swagger** para documentar y visualizar de manera interactiva los endpoints de la API REST. Swagger proporciona una interfaz gráfica que permite explorar los endpoints, visualizar las solicitudes y respuestas, y probar la API sin necesidad de herramientas externas.

### 1. Acceso a la Documentación de Swagger por UI

Después de arrancar la aplicación, puedes acceder a la documentación de la API generada automáticamente por Swagger de forma gráfica en la siguiente URL:

```bash
http://localhost:8080/swagger-ui.html
```

Desde esa interfaz, tendrás acceso a todas las rutas disponibles, incluyendo descripciones detalladas de los parámetros requeridos, los tipos de datos esperados y los posibles códigos de respuesta HTTP.

### 2. OpenAPI Definition
La aplicación también expone la especificación de OpenAPI en formato JSON, la cual puede ser consumida por otras herramientas para la generación de clientes o servidores de API.

Puedes acceder a la especificación de OpenAPI en:

```bash
http://localhost:8080/v3/api-docs
```

## Endpoints

### 1. Busqueda de Hoteles
**Endpoint:** `/search`

**Método:** `POST`

**Descripción:** Este endpoint permite enviar los criterios de una búsqueda específica

**Request Body (JSON):**

```json
{
  "hotelId": "1234",
  "checkIn": "29/12/2023",
  "checkOut": "12/01/2024",
  "ages": [12, 32, 61, 19]
}
```
**Respuesta Exitosa (200 OK):**

```json
{
  "searchId": "some-unique-search-id"
}
```

- `searchId`: el identificador único de la búsqueda 

### 2. Obtener recuento de búsquedas similares
**Endpoint:** `/count`

**Método:** `GET`

**Descripción:** Este endpoint devuelve la cuenta de búsquedas similares a la búsqueda especificada por el `searchId` proporcionado.

**Parámetros de URL:**
- `searchId`: El ID de búsqueda de la cual se desean obtener las búsquedas similares.

**Ejemplo de request:**

```bash
GET /count?searchId=some-unique-search-id
```
**Respuesta Exitosa (200 OK):**

```json
{
  "searchId": "some-unique-search-id",
  "search": {
    "hotelId": "1234",
    "checkIn": "29/12/2023",
    "checkOut": "12/01/2024",
    "ages": [12, 32, 61, 19]
  },
  "count": 3
}
```

- `searchId`: El ID de la búsqueda original.
- `search`: Detalles de la búsqueda original.
     - `hotelId`: ID del hotel buscado.
     - `checkIn`: Fecha de check-in en formato dd/MM/yyyy.
     - `checkOut`: Fecha de check-out en formato dd/MM/yyyy.
     - `ages`: Lista de edades de las personas incluidas en la búsqueda.
- `count`: Número de búsquedas similares encontradas.

**Respuesta de búsqueda no encontrada (404 NOT FOUND):**

```json
{
  "message": "Search not found"
}
```

## Pruebas

El proyecto incluye tanto **pruebas unitarias** como **pruebas de integración** para garantizar el correcto funcionamiento del sistema. Las pruebas están diseñadas para verificar la API, la lógica de negocio, las validaciones, la integración con Kafka, la persistencia en MongoDB y el correcto manejo de excepciones, entre otras cosas.

### 1. Ejecución de Pruebas Unitarias

Las pruebas unitarias verifican el comportamiento de componentes individuales, como los servicios y validadores, sin depender de otros sistemas externos.

Para ejecutar las pruebas unitarias, utiliza el siguiente comando:

```bash
mvn test
```

### 2. Ejecución de Pruebas de Integración
Las pruebas de integración comprueban el flujo completo del sistema, desde la recepción de una solicitud HTTP hasta el envío de eventos a Kafka y la persistencia en MongoDB.

Para ejecutar las pruebas de integración, usa el siguiente comando:

```bash
mvn test
```

### 3. Pruebas Manuales con Postman
Si prefieres realizar pruebas manuales, puedes utilizar una herramienta como Postman para interactuar con los endpoints REST y verificar el comportamiento del servicio.

## Contacto

Si tienes alguna duda, sugerencia o problema relacionado con el proyecto, no dudes en ponerte en contacto con el equipo de desarrollo.

- **Nombre:** Francisco Cilleruelo
- **Correo Electrónico:** [francisco.cilleruelo@gmail.com](mailto:francisco.cilleruelo@gmail.com)
