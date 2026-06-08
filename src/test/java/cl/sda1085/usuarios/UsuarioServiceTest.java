package cl.sda1085.usuarios;


import cl.sda1085.usuarios.dto.UsuarioResponseDTO;
import cl.sda1085.usuarios.model.Usuario;
import cl.sda1085.usuarios.repository.UsuarioRepository;
import cl.sda1085.usuarios.service.UsuarioService;
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

    @InjectMocks
    private UsuarioService usuarioService; // Apunta directo a tu clase UsuarioService sin "Impl"

    @Test
    @DisplayName("Debería retornar un UsuarioResponseDTO válido cuando el ID existe")
    void testObtenerPorIdExitoso() {
        Long usuarioId = 1L;
        Usuario usuarioSimulado = new Usuario();
        usuarioSimulado.setId(usuarioId);
        usuarioSimulado.setNombre("Isabel Torres");
        usuarioSimulado.setEmail("itorres@subastas.cl");
        usuarioSimulado.setRol("VENDEDOR");

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioSimulado));

        UsuarioResponseDTO resultado = usuarioService.obtenerPorId(usuarioId);

        assertNotNull(resultado);
        assertEquals(usuarioId, resultado.getId());
        verify(usuarioRepository, times(1)).findById(usuarioId);
    }
}