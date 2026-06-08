package cl.sda1085.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo requerido (JSON) para registrar o actualizar un usuario en el sistema")

public class UsuarioRequestDTO {

    //DTO de entrada
    //No es necesario el ID, se genera automáticamente

    @NotBlank(message = "El nombre no debe estar vacío.")
    @Schema(description = "Nombre completo del usuario", example = "Juan Perez")
    private String nombre;

    @Email(message = "El email debe tener formato válido")
    @NotBlank(message = "El email no debe estar vacío.")
    @Schema(description = "Correo institucional único del usuario", example = "jperez@gmail.com")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Schema(description = "Clave de acceso", example = "ClaveSegura123")
    private String password;

    @NotBlank(message = "El rol no debe estar vacío.")
    @Schema(description = "Rol asignado dentro del sistema", example = "CLIENTE", allowableValues = {"ADMIN", "VENDEDOR", "CLIENTE"})
    private String rol;
}
