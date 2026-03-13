package com.evolveum.midpoint.schrodinger;

import com.evolveum.midpoint.common.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {

    @Bean
    public Clock clock() {
        return new Clock();
    }
}
