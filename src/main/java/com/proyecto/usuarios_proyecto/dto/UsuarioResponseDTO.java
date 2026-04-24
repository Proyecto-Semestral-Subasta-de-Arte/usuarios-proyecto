package com.proyecto.usuarios_proyecto.dto;

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
