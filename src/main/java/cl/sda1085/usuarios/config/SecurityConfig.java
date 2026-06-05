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

                        //Autorización swagger
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs",          // Ruta base del JSON
                                "/v3/api-docs/**",       // Sub-rutas (incluye swagger-config)
                                "/doc/**"
                        ).permitAll()

                        //Registro público de usuarios
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()

                        //Consultar un usuario por ID o por Email de forma pública (WebClient)

                        .requestMatchers(HttpMethod.GET, "/api/usuarios/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/email/{email}").permitAll()

                        //Listado total de usuarios queda exclusivo para el rol ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/usuarios").hasRole("ADMIN")

                        //Cualquier otra ruta interna requiere autenticación básica
                        .requestMatchers("/api/usuarios/**").authenticated()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }


    @org.springframework.context.annotation.Bean
    public org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html"
        );
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
