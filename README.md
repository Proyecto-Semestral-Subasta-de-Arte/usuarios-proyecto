# Microservicio de Gestión de Usuarios ('usuarios')

## Integrantes
* **Gonzalo Hormazábal**
* **Geraldinne González**


## Descripción
Módulo central encargado de administrar las cuentas, identidades y roles de todos los usuarios de la plataforma de subastas. Es el servicio base para la autenticación y validación de clientes en el ecosistema.
* **Puerto:** `8081`
* **Base de Datos:** `usuarios_db` (MySQL)


## Funcionalidades Clave
* Registro y administración (CRUD) de usuarios y administradores.
* Validación de perfiles y estados de cuenta.
* Endpoint de consulta síncrona para servicios externos (ej: Verificación desde Ofertas).


## Configuración ('application.properties')
* server.port=8081
* spring.datasource.url=jdbc:mysql://localhost:3306/usuarios_db
* spring.datasource.username=root
* spring.datasource.password=
* spring.jpa.hibernate.ddl-auto=update
* logging.level.cl.sda1085.usuarios=DEBUG


## Pasos para Ejecutar

### 1. Preparación de la Base de Datos
Antes de ejecutar el servicio, crear la conexión a la base de datos de MySQL (XAMPP) corriendo en el puerto `3306` y con el nombre 'usuarios_db'.

### 2. Verificación de Credenciales
Revisar que el archivo application.properties tenga por defecto, usuario root y contraseña vacía.

### 3. Lanzamiento del Microservicio
Ejecutar (run) la clase principal con la anotación @SpringBootApplication (UsuariosApplication.java).

### 4. Reglas de Seguridad
Al consumir los endpoints en Postman, ten en cuenta el comportamiento de la cadena de filtros de seguridad:

* Crear Cuenta (POST /api/usuarios): Es de acceso público. Debes configurar la pestaña Authorization en No Auth.
* Listar Usuarios (GET /api/usuarios): Está restringido. Requiere configurar Authorization como Basic Auth e ingresar el correo electrónico y clave de un usuario con el rol de ADMIN que ya exista guardado en tu base de datos.
