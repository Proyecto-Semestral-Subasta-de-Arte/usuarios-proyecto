package cl.sda1085.usuarios.service;

import cl.sda1085.usuarios.dto.UsuarioRequestDTO;
import cl.sda1085.usuarios.dto.UsuarioResponseDTO;
import cl.sda1085.usuarios.exception.EmailDuplicadoException;
import cl.sda1085.usuarios.exception.UsuarioNoEncontradoException;
import cl.sda1085.usuarios.model.Usuario;
import cl.sda1085.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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


    //------------------------------
    //CRUD estándar
    //------------------------------

    //Obtener todos los usuarios
    public List<UsuarioResponseDTO> obtenerTodos(){
        log.info("Buscando la lista completa de usuarios del sistema");
        return usuarioRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    //Obtener usuario por ID
    public UsuarioResponseDTO obtenerPorId(Long id){
        log.info("Buscando usuario con ID: {}", id);
        return usuarioRepository.findById(id)
                .map(this::mapToResponseDTO)
                .orElseThrow(()-> new UsuarioNoEncontradoException(id));
    }

    //Crear (guardar) nuevo usuario con encriptación
    @Transactional
    public UsuarioResponseDTO guardar(UsuarioRequestDTO dto){
        log.info("Iniciando proceso de guardado para el usuario con email: {}", dto.getEmail());

        if (usuarioRepository.existsByEmail(dto.getEmail())){
            log.warn("Intento de registro fallido: El email {} ya existe", dto.getEmail());
            throw new EmailDuplicadoException(dto.getEmail());
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setRol(dto.getRol());

        //Encriptación directa de contraseña
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        Usuario guardado = usuarioRepository.save(usuario);
        log.info("Usuario guardado exitosamente con ID: {}", guardado.getId());

        //Guardar en la base de datos
        return mapToResponseDTO(guardado);
    }

    //Actualizar usuario existente
    @Transactional
    public UsuarioResponseDTO actualizar(Long id, UsuarioRequestDTO dto){
        log.info("Iniciando actualización del usuario ID: {}", id);
        return usuarioRepository.findById(id).map(usuarioExistente -> {
            usuarioExistente.setNombre(dto.getNombre());
            usuarioExistente.setEmail(dto.getEmail());
            usuarioExistente.setRol(dto.getRol());

            // Si el DTO trae una clave, se encripta antes de actualizar
            log.info("Encriptando nueva contraseña para el usuario ID: {}", id);
            usuarioExistente.setPassword(passwordEncoder.encode(dto.getPassword()));
            return mapToResponseDTO(usuarioRepository.save(usuarioExistente));
        }).orElseThrow(() -> new UsuarioNoEncontradoException(id));
    }

    //Eliminar usuario
    @Transactional
    public void eliminar(Long id){
       log.info("Solicitud para eliminar usuario con ID: {}", id);

        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNoEncontradoException(id);
        }

        usuarioRepository.deleteById(id);
        log.info("Usuario con ID: {} eliminado correctamente de la base de datos", id);
    }


    //------------------------------
    //CRUD personalizado
    //------------------------------

    //Buscar usuario por su email
    public Optional<UsuarioResponseDTO> obtenerPorEmail(String email){
        log.info("Buscando usuario por email exacto: {}", email);
        return usuarioRepository.findByEmail(email)
                .map(this::mapToResponseDTO);
    }

    //Listar usuario por rol
    public List<UsuarioResponseDTO> obtenerPorRol(String rol){
        log.info("Filtrando usuarios con rol: {}", rol);
        return usuarioRepository.findByRol(rol).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    //Filtrar usuarios por nombre
    public List<UsuarioResponseDTO> buscarPorNombre(String nombre){
        log.info("Ejecutando buscador parcial de usuarios por nombre: '{}'", nombre);
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
}
