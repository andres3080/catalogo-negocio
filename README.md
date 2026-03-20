# Catalogo Negocio

Catalogo web para taller de aluminio y acero con:
- catalogo publico sin precios
- categorias de productos
- boton de cotizacion por WhatsApp
- login administrador
- panel para crear, editar y eliminar productos

## Tecnologias

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- Thymeleaf
- Docker

## Ejecucion local

### 1. Levantar PostgreSQL con Docker

```bash
docker compose up -d
```

### 2. Ejecutar la app

```bash
mvn spring-boot:run
```

### 3. Abrir en el navegador

- Catalogo: `http://localhost:8080/catalog`
- Login admin: `http://localhost:8080/login`
- Panel admin: `http://localhost:8080/admin/products`

## Variables de entorno

La app ya esta preparada para produccion usando variables de entorno.

Variables principales:

- `PORT`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_JPA_HIBERNATE_DDL_AUTO`
- `APP_UPLOAD_DIR`
- `APP_WHATSAPP_NUMBER`
- `APP_BUSINESS_NAME`
- `APP_LOGO_PATH`
- `APP_ABOUT_TITLE`
- `APP_ABOUT_TEXT`
- `APP_SOCIAL_INSTAGRAM`
- `APP_SOCIAL_FACEBOOK`
- `APP_ADMIN_REGISTRATION_ENABLED`

## Docker

Construir imagen:

```bash
docker build -t catalogo-negocio .
```

Ejecutar contenedor:

```bash
docker run -p 8080:8080 ^
  -e PORT=8080 ^
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/catalogo_db ^
  -e SPRING_DATASOURCE_USERNAME=postgres ^
  -e SPRING_DATASOURCE_PASSWORD=postgres ^
  catalogo-negocio
```

## Despliegue en Railway

### 1. Subir el proyecto a GitHub

Si aun no tienes repo:

```bash
git init
git add .
git commit -m "Primer deploy del catalogo"
```

Luego crea un repositorio en GitHub y subelo.

### 2. Crear proyecto en Railway

- crea un nuevo proyecto
- conecta tu repositorio de GitHub
- Railway detectara el `Dockerfile`

### 3. Agregar PostgreSQL

- en el mismo proyecto agrega un servicio PostgreSQL
- copia las credenciales o la URL JDBC

### 4. Configurar variables en Railway

Configura al menos estas:

```text
PORT=8080
SPRING_DATASOURCE_URL=jdbc:postgresql://HOST:PUERTO/DB
SPRING_DATASOURCE_USERNAME=USUARIO
SPRING_DATASOURCE_PASSWORD=CLAVE
SPRING_JPA_HIBERNATE_DDL_AUTO=update
APP_WHATSAPP_NUMBER=57XXXXXXXXXX
APP_BUSINESS_NAME=Nombre de tu negocio
APP_LOGO_PATH=/img/logo.jpg
APP_ADMIN_REGISTRATION_ENABLED=false
APP_SOCIAL_INSTAGRAM=https://instagram.com/tu_cuenta
APP_SOCIAL_FACEBOOK=https://facebook.com/tu_pagina
```

### 5. Publicar

- espera el primer deploy
- Railway te dara un dominio publico

## Importante para produccion

- la carpeta `uploads/` en plataformas cloud puede no ser persistente
- para un uso real conviene mover imagenes a Cloudinary, S3 o similar
- el registro publico de administradores debe mantenerse en `false`

## Verificacion movil

Para revisar como se ve en telefono:

1. Abre la web en Chrome o Edge
2. Presiona `F12`
3. Activa el modo movil
4. Prueba con `iPhone 12`, `Galaxy S20` o anchos pequenos

Si quieres probar en tu celular real dentro de tu casa:

1. conecta PC y celular al mismo Wi-Fi
2. busca tu IP local en Windows con:

```bash
ipconfig
```

3. abre en el celular:

```text
http://TU_IP_LOCAL:8080/catalog
```
