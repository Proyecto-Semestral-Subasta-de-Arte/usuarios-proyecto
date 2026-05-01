package cl.sda1085.usuarios.service;

import cl.sda1085.usuarios.dto.UsuarioRequestDTO;
import cl.sda1085.usuarios.dto.UsuarioResponseDTO;
import cl.sda1085.usuarios.model.Usuario;
import cl.sda1085.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    //Obtener todos con mapeo a DTO
    public List<UsuarioResponseDTO> obtenerTodosUsuarios(){
        return usuarioRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<UsuarioResponseDTO> obtenerPorId(Long id){
        return usuarioRepository.findById(id)
                .map(this::convertirADTO);  //Transforma la entidad encontrada a DTO
    }

    public UsuarioResponseDTO guardar(UsuarioRequestDTO dto){
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(dto.getRol());

        //Guardar en la base de datos
        Usuario guardado = usuarioRepository.save(usuario);

        //Devolver la respuesta como DTO
        return convertirADTO(guardado);
    }

    //Método auxiliar de conversión (reutilizable)
    private UsuarioResponseDTO convertirADTO(Usuario usuario){
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol()
        );
    }

    public Optional<UsuarioResponseDTO> actualizar(Long id, UsuarioRequestDTO dto){
        return usuarioRepository.findById(id).map(usuarioExistente -> {
            usuarioExistente.setNombre(dto.getNombre());
            usuarioExistente.setEmail(dto.getEmail());
            usuarioExistente.setRol(dto.getRol());

            usuarioExistente.setPassword(dto.getPassword());
            return mapToResponseDTO(usuarioRepository.save(usuarioExistente));
        });
    }

    //Método de apoyo para evitar repetición del código
    private UsuarioResponseDTO mapToResponseDTO(Usuario usuario){
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol()
        );
    }

    public void eliminar(Long id){
        usuarioRepository.deleteById(id);
    }

    public Usuario findByEmail (String email){
        return usuarioRepository.findByEmail(email);
    }

}
