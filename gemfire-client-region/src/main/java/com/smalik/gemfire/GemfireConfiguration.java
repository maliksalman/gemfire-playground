package com.smalik.gemfire;

import com.gemstone.gemfire.cache.Region;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("gemfire-context.xml")
public class GemfireConfiguration {

    @Bean
    public Region<Long, Long> region(@Qualifier("my-region") Region localRegion) {
        return localRegion;
    }
}
