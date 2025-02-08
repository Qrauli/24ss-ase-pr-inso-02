package at.ase.respond.categorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class OAuth2ResourceServerSecurityConfiguration {

    /**
     * Custom converter to extract roles stored inside JWT tokens in the form of:
     * "realm_access": { "roles": [ "calltaker", "dispatcher" ] } By default, only
     * authorities are extracted from the "scope" claim for use with hasAuthority() and
     * hasAnyAuthority().
     */
    private final class CustomJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            var realmAccess = (Map<String, List<String>>) jwt.getClaim("realm_access");

            return realmAccess.get("roles")
                .stream()
                .map(role -> "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        }

        @Override
        public <U> Converter<Jwt, U> andThen(Converter<? super Collection<GrantedAuthority>, ? extends U> after) {
            return Converter.super.andThen(after);
        }

    }

    @Bean
    public JwtAuthenticationConverter customJwtAuthenticationConverter() {
        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new CustomJwtGrantedAuthoritiesConverter());
        return jwtAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
            .requestMatchers(HttpMethod.POST, "/categorization").hasRole("calltaker")
            .requestMatchers(HttpMethod.GET, "/categorization/**").hasRole("calltaker")
            .requestMatchers(HttpMethod.PUT, "/categorization/**").hasRole("calltaker")

            // Swagger UI
            .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()

            // Signature verification
            .requestMatchers(HttpMethod.POST, "/signatures/**").hasAnyRole("calltaker", "dispatcher"))
            .oauth2ResourceServer(
                    (oauth2) -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(customJwtAuthenticationConverter())));
        return http.build();
    }

}
