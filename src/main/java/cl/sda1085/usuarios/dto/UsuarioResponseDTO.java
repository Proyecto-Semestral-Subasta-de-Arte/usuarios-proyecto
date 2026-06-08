package cl.sda1085.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true) // Necesario al heredar campos en clases con @Data de Lombok
@Schema(description = "Estructura de respuesta segura que retorna el servidor incluyendo enlaces HATEOAS")

public class UsuarioResponseDTO extends RepresentationModel<UsuarioResponseDTO> {

    //DTO de salida (respuesta)
    //No existen las anotaciones de validación


    @Schema(description = "Identificador único incremental autogenerado", example = "1")
    private Long id;

    @Schema(description = "Nombre completo guardado en la base de datos", example = "Juan Perez")
    private String nombre;

    @Schema(description = "Dirección de correo electrónico confirmada", example = "jperez@gmail.com")
    private String email;

    @Schema(description = "Rol activo asignado al usuario", example = "CLIENTE")
    private String rol;  //Manejar 2-3 roles
}