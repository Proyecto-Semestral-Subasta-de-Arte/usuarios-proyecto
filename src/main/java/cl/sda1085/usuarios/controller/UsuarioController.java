package cl.sda1085.usuarios.controller;

import cl.sda1085.usuarios.dto.UsuarioRequestDTO;
import cl.sda1085.usuarios.dto.UsuarioResponseDTO;
import cl.sda1085.usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Operaciones relacionadas con los usuarios")

public class UsuarioController {

    //Conexion con 'service'
    private final UsuarioService usuarioService;

    // Método helper para añadir enlaces individuales de autoreferencia, actualización y eliminación
    private void agregarEnlacesHipermedia(UsuarioResponseDTO dto) {
        if (dto != null) {
            // Enlace al propio recurso (self) -> GET /api/usuarios/{id}
            dto.add(linkTo(methodOn(UsuarioController.class).obtenerPorId(dto.getId())).withSelfRel());
            // Enlace alternativo para actualizar este usuario -> PUT /api/usuarios/{id}
            dto.add(linkTo(methodOn(UsuarioController.class).actualizar(dto.getId(), null)).withRel("actualizar"));
            // Enlace alternativo para eliminar este usuario -> DELETE /api/usuarios/{id}
            dto.add(linkTo(methodOn(UsuarioController.class).eliminar(dto.getId())).withRel("eliminar"));
            // Enlace para volver a la lista general -> GET /api/usuarios
            dto.add(linkTo(methodOn(UsuarioController.class).obtenerTodos()).withRel("lista-completa"));
        }
    }


    //------------------------------
    //CRUD estándar con HATEOAS
    //------------------------------

    //Obtener todos los usuarios
    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Obtiene una lista de todos los usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDTO.class)))),
            @ApiResponse(responseCode = "401", description = "No autorizado - Requiere credenciales válidas"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol ADMIN")
    })
        public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodos() {
        List<UsuarioResponseDTO> usuarios = usuarioService.obtenerTodos();
        usuarios.forEach(this::agregarEnlacesHipermedia);
        return ResponseEntity.ok(usuarios);
    }

    //Obtener usuario por ID
    @GetMapping("/{id}")
    @Operation (summary = "Obtener usuario por ID", description = "Obtiene un usuario usando su identificador único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado en la base de datos")
    })
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long id) {
        UsuarioResponseDTO dto = usuarioService.obtenerPorId(id);
        agregarEnlacesHipermedia(dto);
        return ResponseEntity.ok(dto);
    }

    //Guardar (crear) nuevo usuario
    @PostMapping
    @Operation (summary = "Crea un nuevo usuario", description = "Registra un nuevo usuario en el sistema con el nombre, email, contraseña y rol especificados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "211", description = "Usuario creado de forma exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos de entrada o email ya duplicado")
    })
    public ResponseEntity<UsuarioResponseDTO> crear(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos requeridos para el registro del usuario", required = true)
    @Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO creado = usuarioService.guardar(dto);
        agregarEnlacesHipermedia(creado);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    //Actualizar usuario existente
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un usuario existente", description = "Modifica los datos de un usuario existente identificándolo por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<UsuarioResponseDTO> actualizar(
            @Parameter(description = "ID del usuario a modificar", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos para el usuario", required = true)
            @Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO actualizado = usuarioService.actualizar(id, dto);
        agregarEnlacesHipermedia(actualizado);
        return ResponseEntity.ok(actualizado);
    }

    //Eliminar usuario
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario", description = "Remueve físicamente de la base de datos al usuario que coincida con el ID proporcionado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado de forma exitosa"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del usuario que se desea eliminar", required = true, example = "2")
            @PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    //------------------------------
    //CRUD personalizado
    //------------------------------

    //Buscar un usuario por su email
    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar usuario por email", description = "Retorna un usuario único basándose en su dirección de correo electrónico exacta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario localizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró ningún usuario con ese email")
    })
    public ResponseEntity<UsuarioResponseDTO> obtenerPorEmail(
            @Parameter(description = "Correo electrónico del usuario a buscar", required = true, example = "cconcha@subas.cl")
            @PathVariable String email) {
        UsuarioResponseDTO dto = usuarioService.obtenerPorEmail(email);
        agregarEnlacesHipermedia(dto);
        return ResponseEntity.ok(dto);
    }

    //Obtener usuario por rol
    @GetMapping("/rol/{rol}")
    @Operation(summary = "Listar usuarios por rol", description = "Obtiene una sublista de usuarios filtrada de acuerdo al rol ingresado (ej: ADMIN, CLIENTE).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coincidencias encontradas y listadas",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDTO.class)))),
            @ApiResponse(responseCode = "404", description = "No existen usuarios registrados con el rol especificado")
    })
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerPorRol(
            @Parameter(description = "Nombre del rol para filtrar", required = true, example = "CLIENTE")
            @PathVariable String rol) {
        List<UsuarioResponseDTO> usuarios = usuarioService.obtenerPorRol(rol);
        usuarios.forEach(this::agregarEnlacesHipermedia);
        return ResponseEntity.ok(usuarios);
    }

    //Obtener usuario por nombre
    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar usuarios por nombre", description = "Realiza una búsqueda parcial e insensible a mayúsculas de usuarios por su nombre.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultados de la búsqueda",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Ningún usuario coincide con el término de búsqueda")
    })
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorNombre(
            @Parameter(description = "Texto o parte del nombre a buscar", required = true, example = "Carlos")
            @PathVariable String nombre) {
        List<UsuarioResponseDTO> usuarios = usuarioService.buscarPorNombre(nombre);
        usuarios.forEach(this::agregarEnlacesHipermedia);
        return ResponseEntity.ok(usuarios);
    }

    //Buscador multiparámetro de usuario
    @GetMapping("/buscar")
    @Operation(summary = "Buscador multiparámetro", description = "Permite filtrar de forma flexible la base de datos utilizando combinaciones opcionales de rol y nombre.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtrado completado con éxito",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDTO.class))))
    })
    public ResponseEntity<List<UsuarioResponseDTO>> buscarUsuariosPorRol(
            @Parameter(description = "Filtro opcional por rol", required = false, example = "ADMIN")
            @RequestParam(required = false) String rol,
            @Parameter(description = "Filtro opcional por coincidencia de nombre", required = false, example = "Diego")
            @RequestParam(required = false) String nombre){

        List<UsuarioResponseDTO> usuarios = usuarioService.filtrarUsuarios(rol, nombre);
        usuarios.forEach(this::agregarEnlacesHipermedia);
        return ResponseEntity.ok(usuarios);
    }
}
