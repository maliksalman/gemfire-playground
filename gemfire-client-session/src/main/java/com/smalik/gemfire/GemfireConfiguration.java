package com.smalik.gemfire;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientCacheFactoryBean;
import org.springframework.data.gemfire.client.PoolFactoryBean;
import org.springframework.data.gemfire.support.ConnectionEndpoint;
import org.springframework.session.data.gemfire.config.annotation.web.http.EnableGemFireHttpSession;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableGemFireHttpSession(regionName = "session-cache")
public class GemfireConfiguration {

    @Bean
    ClientCacheFactoryBean gemfireCache() {
        ClientCacheFactoryBean gemfireCache = new ClientCacheFactoryBean();
        gemfireCache.setClose(true);

        Properties gemfireProperties = new Properties();
        gemfireProperties.setProperty("name", "gemfire-client-session");
        gemfireProperties.setProperty("log-level", "warning");
        gemfireCache.setProperties(gemfireProperties);

        return gemfireCache;
    }

    @Bean
    PoolFactoryBean gemfirePool(
            @Value("${gemfire.cache.server.host:localhost}") String host,
            @Value("${gemfire.cache.server.port:10334}") int port) {

        PoolFactoryBean gemfirePool = new PoolFactoryBean();

        gemfirePool.setKeepAlive(false);
        gemfirePool.setPingInterval(TimeUnit.SECONDS.toMillis(5));
        gemfirePool.setReadTimeout(Long.valueOf(TimeUnit.SECONDS.toMillis(15)).intValue());
        gemfirePool.setRetryAttempts(1);
        gemfirePool.setSubscriptionEnabled(true);
        gemfirePool.setThreadLocalConnections(false);
        gemfirePool.setLocators(new ConnectionEndpoint[] { new ConnectionEndpoint(host, port) });

        return gemfirePool;
    }
}
