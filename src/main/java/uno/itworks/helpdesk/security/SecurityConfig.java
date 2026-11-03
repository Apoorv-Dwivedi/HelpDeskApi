package uno.itworks.helpdesk.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import uno.itworks.helpdesk.service.AppOidcUserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AppOidcUserService oidcUserService)throws Exception{
        http.
             authorizeHttpRequests(auth -> auth.requestMatchers("/public/**").permitAll()
                     .requestMatchers("/admin/**").hasRole("ADMIN")
                     .anyRequest().authenticated()).
             oauth2Login(oauth ->
                     oauth.userInfoEndpoint(ui -> ui.oidcUserService(oidcUserService)));

        return http.build();

    }
}
