package cl.sda1085.usuarios.service;

import cl.sda1085.usuarios.dto.UsuarioRequestDTO;
import cl.sda1085.usuarios.dto.UsuarioResponseDTO;
import cl.sda1085.usuarios.model.Usuario;
import cl.sda1085.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    //Método de apoyo para convertir entidad a DTO
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

    //Obtener todos los usuarios
    public List<UsuarioResponseDTO> obtenerTodos(){
        return usuarioRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    //Obtener usuario por ID
    public Optional<UsuarioResponseDTO> obtenerPorId(Long id){
        return usuarioRepository.findById(id)
                .map(this::convertirADTO);  //Transforma la entidad encontrada a DTO
    }

    //Crear (guardar) usuario con encriptación
    public UsuarioResponseDTO guardar(UsuarioRequestDTO dto){
        if (usuarioRepository.existsByEmail(dto.getEmail())){
            throw new RuntimeException("El email ya existe en el sistema");
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(dto.getRol());

        //Encriptación
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        //Guardar en la base de datos
        return mapToResponseDTO(usuarioRepository.save(usuario));

    }

    //Actualizar usuario
    public Optional<UsuarioResponseDTO> actualizar(Long id, UsuarioRequestDTO dto){
        return usuarioRepository.findById(id).map(usuarioExistente -> {
            usuarioExistente.setNombre(dto.getNombre());
            usuarioExistente.setEmail(dto.getEmail());
            usuarioExistente.setRol(dto.getRol());

            // Si el DTO trae una clave, se encripta antes de actualizar
            usuarioExistente.setPassword(passwordEncoder.encode(dto.getPassword()));
            return mapToResponseDTO(usuarioRepository.save(usuarioExistente));
        });
    }

    //Eliminar usuario
    public void eliminar(Long id){
        usuarioRepository.deleteById(id);
    }


    //CRUD personalizado

    //Buscar usuario por correo
    public Optional<UsuarioResponseDTO> obtenerPorEmail(String email){
        return usuarioRepository.findByEmail(email)
                .map(this::convertirADTO);
    }

    //Listar usuario por rol
    public List<UsuarioResponseDTO> obtenerPorRol(String rol){
        return usuarioRepository.findByRol(rol).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }


    //Filtrar usuarios por nombre
    public List<UsuarioResponseDTO> buscarPorNombre(String nombre){
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }


}
