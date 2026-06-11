package cl.sda1085.usuarios.service;

import cl.sda1085.usuarios.dto.UsuarioResponseDTO;
import cl.sda1085.usuarios.model.Usuario;
import cl.sda1085.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    //Añadir el mock para el codificador de contraseñas de Spring Security.
    //De esta manera, el constructor de UsuarioService se inicializa con todas sus dependencias y se evita el quiebre por NullPointerException.
    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;  //Apunta directo a tu clase UsuarioService sin "Impl"

    @Test
    @DisplayName("Debería retornar un UsuarioResponseDTO válido cuando el ID existe.")
    void testObtenerPorIdExitoso() {

        //ARRANGE
        Long usuarioId = 1L;
        Usuario usuarioSimulado = new Usuario();
        usuarioSimulado.setId(usuarioId);
        usuarioSimulado.setNombre("Isabel Torres");
        usuarioSimulado.setEmail("itorres@subastas.cl");
        usuarioSimulado.setRol("VENDEDOR");
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioSimulado));

        //ACT
        UsuarioResponseDTO resultado = usuarioService.obtenerPorId(usuarioId);

        //ASSERT
        assertNotNull(resultado);
        assertEquals(usuarioId, resultado.getId());
        verify(usuarioRepository, times(1)).findById(usuarioId);
    }

    //Test unitario que valida que el proceso de registro intercepte la contraseña en texto plano, invoque al codificador y almacene la credencial de forma irreversible en la persistencia.
    @Test
    @DisplayName("Debería registrar un nuevo usuario encriptando su clave de acceso de forma exitosa.")
    void testGuardarUsuarioEncriptandoClave() {

        //ARRANGE
        cl.sda1085.usuarios.dto.UsuarioRequestDTO requestDTO = new cl.sda1085.usuarios.dto.UsuarioRequestDTO(
                "Juan Perez", "jperez@gmail.com", "ClaveSegura123", "CLIENTE"
        );

        Usuario usuarioEsperado = new Usuario();
        usuarioEsperado.setId(1L);
        usuarioEsperado.setNombre(requestDTO.getNombre());
        usuarioEsperado.setEmail(requestDTO.getEmail());
        usuarioEsperado.setRol(requestDTO.getRol());
        usuarioEsperado.setPassword("$2a$10$falsaCadenaCifradaBCrypt60Characters");

        //Programar las respuestas del entorno aislado de simulación
        when(usuarioRepository.existsByEmail(requestDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(requestDTO.getPassword())).thenReturn("$2a$10$falsaCadenaCifradaBCrypt60Characters");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEsperado);

        //ACT
        UsuarioResponseDTO resultado = usuarioService.guardar(requestDTO);

        //ASSERT
        assertNotNull(resultado);
        assertEquals(requestDTO.getEmail(), resultado.getEmail());
        verify(passwordEncoder, times(1)).encode("ClaveSegura123");  //Comprueba la invocación de seguridad
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
}