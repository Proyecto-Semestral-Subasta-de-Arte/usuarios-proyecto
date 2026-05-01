package cl.sda1085.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioResponseDTO {

    //DTO de salida (respuesta)
    //No existen las anotaciones de validación

    private Long id;
    private String nombre;
    private String email;
    private String rol;  //Manejar 2-3 roles

}
