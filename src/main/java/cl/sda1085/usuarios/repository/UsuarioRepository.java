package cl.sda1085.usuarios.repository;

import cl.sda1085.usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository  extends JpaRepository<Usuario,Long> {

    Usuario findByEmail (String email);

}
