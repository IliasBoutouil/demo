package me.ilias.security;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdapterConfig {
        @Bean
        public KeycloakConfigResolver springBootConfigResolver(){
            return new KeycloakSpringBootConfigResolver();
        }

}
