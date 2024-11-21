package com.em.taskmanager.configurations;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Slf4j
@Getter
@Configuration
public class SecurityKeyConfiguration {
    private final String key;

    @Autowired
    public SecurityKeyConfiguration(Environment environment) {
        this.key = environment.getProperty("SECURITY_KEY");
        log.info("Security key initiated (openai)");
    }
}
