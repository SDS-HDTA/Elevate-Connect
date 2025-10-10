package org.sds.elevateconnect.config;

import org.sds.elevateconnect.config.security.AdminInterceptor;
import org.sds.elevateconnect.config.security.AuthorizationInterceptor;
import org.sds.elevateconnect.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class ApplicationConfig implements WebMvcConfigurer {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthorizationInterceptor authorizationInterceptor;
    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
         registry.addInterceptor(authorizationInterceptor)
                 .addPathPatterns("/**");

        registry.addInterceptor(adminInterceptor())
                .addPathPatterns("/admin/**");
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userMapper.getUserByEmail(username);
    }

    public AdminInterceptor adminInterceptor() {
        return new AdminInterceptor(adminPassword);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnProperty(name = "websocket.enabled", havingValue = "true", matchIfMissing = true)
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
