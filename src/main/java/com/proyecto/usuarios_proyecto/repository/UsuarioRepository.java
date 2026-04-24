package com.proyecto.usuarios_proyecto.repository;

import com.proyecto.usuarios_proyecto.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository  extends JpaRepository<Usuario,Long> {

    Usuario findByEmail (String email);

}
