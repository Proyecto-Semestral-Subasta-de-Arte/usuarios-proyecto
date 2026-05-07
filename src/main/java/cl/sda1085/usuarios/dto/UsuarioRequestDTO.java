package cl.sda1085.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioRequestDTO {

    //DTO de entrada
    //No es necesario el ID, se genera automáticamente

    @NotBlank(message = "El nombre no debe estar vacío.")
    private String nombre;

    @Email(message = "El email debe tener formato válido")
    @NotBlank(message = "El email no debe estar vacío.")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria.")
    private String password;

    @NotBlank(message = "El rol no debe estar vacío.")
    private String rol;
}
