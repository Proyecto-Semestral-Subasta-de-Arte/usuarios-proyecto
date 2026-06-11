package cl.sda1085.usuarios.config;

import cl.sda1085.usuarios.model.Usuario;
import cl.sda1085.usuarios.repository.UsuarioRepository;
import cl.sda1085.usuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private Usuario crearUsuario(String nombre, String email, String password, String rol){
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(password));
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
        log.info("Generando datos masivos de prueba para el Sistema de Subastas...");
        List<Usuario> usuariosNuevos = new ArrayList<>();

        //Crear usuarios con los roles requeridos por el proyecto

        //Administrador
        usuariosNuevos.add(crearUsuario("Carlos Concha", "cconcha@subas.cl", "Admin_CarlosC01", "ADMIN"));

        //Vendedores
        usuariosNuevos.add(crearUsuario("Isabel Torres", "itorres@subastas.cl", "Ven_IsabelT24", "VENDEDOR"));
        usuariosNuevos.add(crearUsuario("Pedro Rivas", "privas@subastas.cl", "Ven_PedroR90", "VENDEDOR"));
        usuariosNuevos.add(crearUsuario("Camila Gil", "cgil@subastas.cl", "Ven_CamilaG77", "VENDEDOR"));

        //Inicializar DataFaker en Español
        Faker faker = new Faker(new Locale("es"));

        //Generar 40 clientes dinámicos con correos realistas comerciales o de subastas
        for (int i = 1; i <= 40; i++) {
            String nombreFalso = faker.name().fullName();

            //Limpieza básica de caracteres para construir el email
            String apellidoLimpio = faker.name().lastName().toLowerCase()
                    .replace(" ", "")
                    .replaceAll("[áéíóúñ]", "a");

            //Alternamos entre correos gmail y correos de la plataforma de subastas
            String dominio = (i % 2 == 0) ? "@gmail.com" : "@subastasuser.cl";
            String emailFalso = apellidoLimpio + i + dominio;
            String passwordFalsa = "Cli_Pass" + i;

            usuariosNuevos.add(crearUsuario(nombreFalso, emailFalso, passwordFalsa, "CLIENTE"));
        }

        usuarioRepository.saveAll(usuariosNuevos);
        log.info("¡Listo! Se han cargado 44 usuarios en la base de datos.");
    }
}
