package de.aittr.car_rent.security.config;

import de.aittr.car_rent.security.filter.TokenFilter;
import de.aittr.car_rent.security.handler.AuthAccessDeniedHandler;
import de.aittr.car_rent.security.handler.AuthenticationEntryPointHandler;
import de.aittr.car_rent.security.service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenFilter tokenFilter;
    private final MyUserDetailsService myUserDetailsService;
    private final BCryptPasswordEncoder encoder;

    /**
     * Метод отвечает за цепочку фильтров, которые проходит Http-запрос
     *
     * @param http-запрос
     * @return объект, содержащий цепочку фильтров для проверки http-запросов
     */
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // test auth controller
                        .requestMatchers(HttpMethod.GET, "/test/auth/not-secured").permitAll()

                        //cars Controller
                        .requestMatchers(HttpMethod.GET, "/cars/filter/**", "/cars/{id}", "/cars").permitAll()

                        //Customer Controller
                        .requestMatchers(HttpMethod.POST, "/customers/all-bookings-id/{id}","/customers/all-bookings-email/{email}" ).permitAll()

                        // Auth Controller
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/confirm/{code}").permitAll()

                        // Swagger
                        .requestMatchers("/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-ui/",
                                "/swagger-ui.html",
                                "/swagger-ui/index.html",
                                "/swagger-ui/**").permitAll()

                        .anyRequest().authenticated())

                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling(
                        exception -> exception
                                .defaultAuthenticationEntryPointFor(
                                        new AuthenticationEntryPointHandler(),
                                        new AntPathRequestMatcher("/**")
                                )
                                .accessDeniedHandler(new AuthAccessDeniedHandler()))

                .authenticationProvider(authenticationProvider())
                .sessionManagement(x ->
                        x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(myUserDetailsService);
        authenticationProvider.setPasswordEncoder(encoder);
        return authenticationProvider;
    }
}
