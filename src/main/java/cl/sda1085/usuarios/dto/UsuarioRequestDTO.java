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

    @NotBlank(message = "El nombre es obligatorio.")
    private String nombre;

    @Email(message = "El email debe tener formato válido")
    @NotBlank(message = "El email es obligatorio.")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria.")
    private String password;

    @NotBlank(message = "El rol es obligatorio.")
    private String rol;

}
