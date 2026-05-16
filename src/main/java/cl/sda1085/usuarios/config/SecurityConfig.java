package cl.sda1085.usuarios.config;

import cl.sda1085.usuarios.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        //Permitir que cualquier persona cree una cuenta
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()

                        //Solo los administradores pueden ver la lista completa de usuarios
                        .requestMatchers(HttpMethod.GET, "/api/usuarios").hasRole("ADMIN")

                        //Cualquier otra acción interna (ver por ID, editar, eliminar, ...) requiere login
                        .requestMatchers("/api/usuarios/**").authenticated()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());  //Activa la ventana de autenticación básica

        return http.build();
    }

    //Encriptador de contraseñas oficial (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Buscador dinámico de credenciales en la base de datos de MySQL
    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository repository) {
        return email -> {
            cl.sda1085.usuarios.model.Usuario usuario = repository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

            return User.builder()
                    .username(usuario.getEmail())
                    .password(usuario.getPassword())  //Aquí la contraseña ya debe estar encriptada en la base de datos
                    .roles(usuario.getRol())  //Spring le agrega automáticamente "ROLE_" al texto de la base de datos
                    .build();
        };
    }
}
