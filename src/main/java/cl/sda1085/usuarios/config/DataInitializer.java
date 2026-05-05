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

    private Usuario crearUsuario(String nombre, String email, String password, String rol){
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setRol(rol);

        return usuario;
    }

    @Override
    public void run(String... args){

        //Verificar si existen usuarios para no duplicarlos
        if (usuarioRepository.count() > 0){
            log.info("Base de datos de usuarios ya contiene datos. Omitiendo inicialización.");
            return;
        }
        log.info("Iniciando la creación de usuarios de prueba...");

        //Crear usuarios con los roles requeridos por el proyecto
        Usuario admin = crearUsuario("Carlos Concha", "cconcha@subastas.cl", "Adm_CarlosC12", "ADMIN");

        Usuario vendedor1 = crearUsuario("Isabel Torres", "itorres@subastas.cl", "Ven_IsabelT24", "VENDEDOR");
        Usuario vendedor2 = crearUsuario("Pedro Rivas", "privas@subastas.cl", "Ven_PedroR90", "VENDEDOR");
        Usuario vendedor3 = crearUsuario("Camila Gil", "cgil@subastas.cl", "Ven_CamilaG77", "VENDEDOR");

        Usuario cliente1 = crearUsuario("Diego Miranda", "dmiranda@gmail.com", "Cli_DiegoM01", "CLIENTE");
        Usuario cliente2 = crearUsuario("Valentina Castro", "vcastro@gmail.com", "Cli_ValenC02", "CLIENTE");
        Usuario cliente3 = crearUsuario("Matias Santos", "msantos@gmail.com", "Cli_MatiasS03", "CLIENTE");
        Usuario cliente4 = crearUsuario("Pamela Vargas", "pvargas@gmail.com", "Cli_SofiaV04", "CLIENTE");
        Usuario cliente5 = crearUsuario("Lucas Morales", "lmorales@gmail.com", "Cli_LucasM05", "CLIENTE");
        Usuario cliente6 = crearUsuario("Martina Dominguez", "mdominguez@gmail.com", "Cli_MartinaD06", "CLIENTE");
        Usuario cliente7 = crearUsuario("Joaquin Reyes", "jreyes@gmail.com", "Cli_JoaquinR07", "CLIENTE");
        Usuario cliente8 = crearUsuario("Antonia Medina", "amedina@gmail.com", "Cli_AntoniaM08", "CLIENTE");
        Usuario cliente9 = crearUsuario("Sebastian Ortiz", "sortiz@gmail.com", "Cli_SebasO09", "CLIENTE");
        Usuario cliente10 = crearUsuario("Javiera Zamora", "jzamora@gmail.com", "Cli_JaviZ10", "CLIENTE");

        //Guardar todos los usuarios en la base de datos
        usuarioRepository.saveAll(List.of(admin,
                vendedor1, vendedor2, vendedor3,
                cliente1, cliente2, cliente3, cliente4, cliente5, cliente6, cliente7, cliente8, cliente9, cliente10));

        log.info("Se han creado 14 usuarios iniciales con roles: ADMIN, 3 VENDEDOR y 10 CLIENTE.");
    }
}
