package cl.sda1085.usuarios.service;

import cl.sda1085.usuarios.dto.UsuarioRequestDTO;
import cl.sda1085.usuarios.dto.UsuarioResponseDTO;
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

    public List<UsuarioResponseDTO> obtenerTodosUsuarios(){
        List<Usuario> usuarios = usuarioRepository.findAll();

        //Bajo ningún concepto se va a exponer la contraseña
        return usuarios.stream()
                .map(usuario -> new UsuarioResponseDTO(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getEmail(),
                        usuario.getRol()
                ))
                .toList();
    }

    public Optional<Usuario> obtenerPorId(Long id){
        return usuarioRepository.findById(id);
    }

    public Usuario guardar(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public Optional<UsuarioResponseDTO> actualizar(Long id, UsuarioRequestDTO dto){
        return usuarioRepository.findById(id).map(usuarioExistente -> {
            usuarioExistente.setNombre(dto.getNombre());
            usuarioExistente.setEmail(dto.getEmail());
            usuarioExistente.setRol(dto.getRol());

            //Encriptar contraseña si se incluye en el DTO
            usuarioExistente.setPassword(passwordEncoder.encode(dto.getPassword()));
            usuarioExistente.setPassword(dto.getPassword());

            //Persistir los cambios en la base de datos
            Usuario actualizado = usuarioRepository.save(usuarioExistente);

            //Convertir la entidad actualizada al DTO de respuesta
            return convertirADTO(actualizado);
        });
    }

    public void eliminar(Long id){
        usuarioRepository.deleteById(id);
    }

    public Usuario findByEmail (String email){
        return usuarioRepository.findByEmail(email);
    }

}
