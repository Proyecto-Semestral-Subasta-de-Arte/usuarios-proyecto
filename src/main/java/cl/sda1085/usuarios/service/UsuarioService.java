package cl.sda1085.usuarios.service;

import cl.sda1085.usuarios.model.Usuario;
import cl.sda1085.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<Usuario> obtenerTodosUsuarios(){
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorId(Long id){
        return usuarioRepository.findById(id);
    }

    public Usuario guardar(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id){
        usuarioRepository.deleteById(id);
    }

    public Usuario findByEmail (String email){
        return usuarioRepository.findByEmail(email);
    }

}
