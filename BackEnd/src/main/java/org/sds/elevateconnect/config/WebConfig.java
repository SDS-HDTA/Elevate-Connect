package org.sds.elevateconnect.config;

import org.sds.elevateconnect.config.security.HTTPSessionCheckInterceptor;
import org.sds.elevateconnect.config.security.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;
    @Autowired
    private HTTPSessionCheckInterceptor httpSessionCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // This is a list of endpoints that do not require authentication to hit
        List<String> excludedEndpointUrls = new ArrayList<>();

        // Populate the list
        excludedEndpointUrls.add("/login");
        excludedEndpointUrls.add("/register");
        excludedEndpointUrls.add("/password/resetCode");
        excludedEndpointUrls.add("/password/update");
        excludedEndpointUrls.add("/projects/all");
        excludedEndpointUrls.add("/projects/search");

        // Register the following interceptors
        registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/**").excludePathPatterns(excludedEndpointUrls);
        registry.addInterceptor(httpSessionCheckInterceptor).addPathPatterns("/**").excludePathPatterns(excludedEndpointUrls);
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
