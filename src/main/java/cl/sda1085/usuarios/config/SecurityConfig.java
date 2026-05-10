package cl.sda1085.usuarios.config;

import cl.sda1085.usuarios.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                    // El registro de usuarios podría ser público, pero el listado es solo para ADMIN
                    .requestMatchers("/api/usuarios").hasRole("ADMIN")
                    .requestMatchers("/api/usuarios/**").authenticated()
                    .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

    return http.build();
}

    //Encriptador de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository repository) {
        return email -> {
            cl.sda1085.usuarios.model.Usuario usuario = repository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

            return User.builder()
                    .username(usuario.getEmail())
                    .password(usuario.getPassword()) // Aquí la contraseña ya debe estar encriptada en la DB
                    .roles(usuario.getRol())
                    .build();
        };
    }
}
