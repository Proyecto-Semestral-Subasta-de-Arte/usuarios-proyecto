package cl.sda1085.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioResponseDTO {

    private Long id;

    private String nombre;

    private String email;

    private String rol;

}
