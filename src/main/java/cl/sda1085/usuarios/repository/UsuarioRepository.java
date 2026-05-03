package cl.sda1085.usuarios.repository;

import cl.sda1085.usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    //Busca un usuario por su email exacto
    Optional<Usuario> findByEmail (String email);

    //Busca todos los usuarios que tengan un rol específico
    List<Usuario> findByRol(String rol);

    //Verifica si existe un usuario por su email
    boolean existsByEmail(String email);

    //Encuentra nombres que contengan el texto (sin importar mayúsculas)
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);

    //Verifica si un email ya existe, pero ignora al usuario actual
    boolean existsByEmailAndIdNot(String email, Long id);
}
