package cl.sda1085.usuarios.config;

import cl.sda1085.usuarios.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // MODIFICACIÓN: Nombre estandarizado según PPT [cite: 299]
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        //Registro público de usuarios
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()

                        //Listado total de usuarios queda exclusivo para el rol ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/usuarios").hasRole("ADMIN")

                        //Cualquier otra ruta interna requiere autenticación básica
                        .requestMatchers("/api/usuarios/**").authenticated()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository repository) {
        return email -> {
            cl.sda1085.usuarios.model.Usuario usuario = repository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

            String rolFinal = "ROLE_" + usuario.getRol().toUpperCase().trim().replace("ROLE_", "");

            return new User(
                    usuario.getEmail(),
                    usuario.getPassword(),
                    AuthorityUtils.createAuthorityList(rolFinal)
            );
        };
    }
}
