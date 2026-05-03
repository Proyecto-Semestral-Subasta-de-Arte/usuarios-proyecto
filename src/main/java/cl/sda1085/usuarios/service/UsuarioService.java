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

    //Método de apoyo para cumplir con el encapsulamiento de datos
    private UsuarioResponseDTO mapToResponseDTO(Usuario usuario){
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol()
        );
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

    //Obtener todos con mapeo a DTO
    public List<UsuarioResponseDTO> obtenerTodos(){
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
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        //Devolver la respuesta como DTO
        return convertirADTO(usuarioGuardado);
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

    public void eliminar(Long id){
        usuarioRepository.deleteById(id);
    }

    public Optional<UsuarioResponseDTO> obtenerPorEmail(String email){
        return usuarioRepository.findByEmail(email)
                .map(this::convertirADTO);
    }

    public List<UsuarioResponseDTO> obtenerPorRol(String rol){
        return usuarioRepository.findByRol(rol).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO guardarPorEmail(UsuarioRequestDTO dto){
        if (usuarioRepository.existsByEmail(dto.getEmail())){
            throw new RuntimeException("El email ya existe en el sistema");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(dto.getNombre());
        nuevoUsuario.setEmail(dto.getEmail());
        nuevoUsuario.setRol(dto.getRol());
        nuevoUsuario.setPassword(dto.getPassword());  //PENDIENTE: Encriptar con BCrypt

        return mapToResponseDTO(usuarioRepository.save(nuevoUsuario));
    }

    public List<UsuarioResponseDTO> buscarPorNombre(String nombre){
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<UsuarioResponseDTO> actualizarPorEmail(Long id, UsuarioRequestDTO dto){
        return usuarioRepository.findById(id).map(usuario -> {
            if (usuarioRepository.existsByEmailAndIdNot(dto.getEmail(), id)){
                throw new RuntimeException("El nuevo email ya está ocupado por otra cuenta.");
            }

            usuario.setNombre(dto.getNombre());
            usuario.setEmail(dto.getEmail());
            usuario.setRol(dto.getRol());
            usuario.setPassword(dto.getPassword());

            return mapToResponseDTO(usuarioRepository.save(usuario));
        });
    }
}
