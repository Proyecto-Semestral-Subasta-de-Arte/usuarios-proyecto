package cl.sda1085.usuarios;

import cl.sda1085.usuarios.dto.UsuarioResponseDTO;
import cl.sda1085.usuarios.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
// Desactivamos los filtros de Spring Security para probar el comportamiento REST y HATEOAS puro del controlador
@AutoConfigureMockMvc(addFilters = false)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc; // Permite simular peticiones HTTP (GET, POST, etc.)

    @MockBean
    private UsuarioService usuarioService; // Crea un clon simulado (Mock) del servicio para el controlador

    @Test
    @DisplayName("GET /api/usuarios/{id} debería retornar 200 OK y el JSON con HATEOAS")
    void testEndpointObtenerPorId() throws Exception {
        // 1. ARRANGE (Preparar el escenario)
        Long idTest = 1L;
        UsuarioResponseDTO dtoSimulado = new UsuarioResponseDTO(idTest, "Juan Perez", "jperez@gmail.com", "CLIENTE");

        // Configuramos el comportamiento del Mock del Servicio
        when(usuarioService.obtenerPorId(idTest)).thenReturn(dtoSimulado);

        // 2. ACT & 3. ASSERT (Ejecución y Verificación)
        mockMvc.perform(get("/api/usuarios/{id}", idTest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Verifica código HTTP 200
                .andExpect(jsonPath("$.id").value(idTest))
                .andExpect(jsonPath("$.nombre").value("Juan Perez"))
                .andExpect(jsonPath("$.email").value("jperez@gmail.com"))
                .andExpect(jsonPath("$._links.self.href").exists()); // Verifica soporte HATEOAS activo

        // Asegura que el controlador interactuó con el servicio exactamente una vez
        verify(usuarioService, times(1)).obtenerPorId(idTest);
    }

    @Test
    @DisplayName("GET /api/usuarios/email/{email} debería retornar 200 OK")
    void testEndpointObtenerPorEmail() throws Exception {
        // 1. ARRANGE
        String emailTest = "jperez@gmail.com";
        UsuarioResponseDTO dtoSimulado = new UsuarioResponseDTO(1L, "Juan Perez", emailTest, "CLIENTE");

        // IMPORTANTE: Mapeo directo al método correspondiente del Service
        when(usuarioService.obtenerPorEmail(emailTest)).thenReturn(dtoSimulado);

        // 2. ACT & 3. ASSERT (Ruta corregida sin el sub-path /buscar sobrante)
        mockMvc.perform(get("/api/usuarios/email/{email}", emailTest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(emailTest))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(usuarioService, times(1)).obtenerPorEmail(emailTest);
    }

    @Test
    @DisplayName("GET /api/usuarios debería retornar una lista de usuarios")
    void testEndpointObtenerTodos() throws Exception {
        // 1. ARRANGE
        UsuarioResponseDTO dtoSimulado = new UsuarioResponseDTO(1L, "Juan Perez", "jperez@gmail.com", "CLIENTE");
        List<UsuarioResponseDTO> listaSimulada = Collections.singletonList(dtoSimulado);

        when(usuarioService.obtenerTodos()).thenReturn(listaSimulada);

        // 2. ACT & 3. ASSERT
        mockMvc.perform(get("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan Perez"));

        verify(usuarioService, times(1)).obtenerTodos();
    }
}
