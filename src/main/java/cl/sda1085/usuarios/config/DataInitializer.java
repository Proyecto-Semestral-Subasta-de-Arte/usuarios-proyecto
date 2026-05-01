package cl.sda1085.usuarios.config;

import cl.sda1085.usuarios.model.Usuario;
import cl.sda1085.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    //Conexión con 'repository'
    private final UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args){

        //Verificar si existen usuarios para no duplicarlos
        if (usuarioRepository.count() > 0){
            log.info("Base de datos de usuarios ya contiene datos. Omitiendo inicialización.");
            return;
        }
        log.info("Iniciando la creación de usuarios de prueba...");

        //Crear usuarios con los roles requeridos por el proyecto
        Usuario admin = new Usuario();
        admin.setNombre("Administrador");
        admin.setEmail("admin@subastas.cl");
        admin.setPassword("admin1085");
        admin.setRol("ADMIN");

        Usuario vendedor = new Usuario();
        vendedor.setNombre("Artista vendedor");
        vendedor.setEmail("vendedor@subastas.cl");
        vendedor.setPassword("vendedor1085");
        vendedor.setRol("VENDEDOR");

        Usuario cliente = new Usuario();
        cliente.setNombre("Cliente comprador");
        cliente.setEmail("cliente@subastas.cl");
        cliente.setPassword("cliente1085");
        cliente.setRol("CLIENTE");

        //Guardar todos los usuarios en la base de datos
        usuarioRepository.saveAll(List.of(admin, vendedor, cliente));

        log.info("Se han creado 3 usuarios iniciales con roles: ADMIN, VENDEDOR y CLIENTE.");
    }
}
