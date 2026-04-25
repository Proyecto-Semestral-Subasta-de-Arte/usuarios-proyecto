package cl.sda1085.usuarios.controller;


import cl.sda1085.usuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class UsuarioController {

private final UsuarioService usuarioService;



}
