package com.proyecto.usuarios_proyecto.controller;


import com.proyecto.usuarios_proyecto.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor

public class UsuarioController {

private final UsuarioService usuarioService;



}
