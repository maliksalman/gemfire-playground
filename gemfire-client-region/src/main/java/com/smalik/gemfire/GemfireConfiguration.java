package com.smalik.gemfire;

import com.gemstone.gemfire.cache.GemFireCache;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.Pool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientCacheFactoryBean;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.client.PoolFactoryBean;
import org.springframework.data.gemfire.support.ConnectionEndpoint;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Configuration
public class GemfireConfiguration {

	@Bean
	public ClientCacheFactoryBean gemfireCache() {
		ClientCacheFactoryBean gemfireCache = new ClientCacheFactoryBean();
		gemfireCache.setClose(true);

		Properties gemfireProperties = new Properties();
		gemfireProperties.setProperty("name", "gemfire-client-region");
		gemfireProperties.setProperty("log-level", "warning");
		gemfireCache.setProperties(gemfireProperties);

		return gemfireCache;
	}

	@Bean
	public PoolFactoryBean gemfirePool(
			@Value("${gemfire.locator.host:localhost}") String host,
			@Value("${gemfire.locator.port:10334}") int port) {

		PoolFactoryBean gemfirePool = new PoolFactoryBean();

		gemfirePool.setKeepAlive(false);
		gemfirePool.setPingInterval(TimeUnit.SECONDS.toMillis(5));
		gemfirePool.setReadTimeout(Long.valueOf(TimeUnit.SECONDS.toMillis(15)).intValue());
		gemfirePool.setRetryAttempts(1);
		gemfirePool.setSubscriptionEnabled(true);
		gemfirePool.setThreadLocalConnections(false);
		gemfirePool.setLocators(new ConnectionEndpoint[]{new ConnectionEndpoint(host, port)});

		return gemfirePool;
	}

	@Bean
	public ClientRegionFactoryBean<String, String> clientRegion(
			GemFireCache cache,
			Pool pool,
			@Value("${gemfire.region.name:generic-cache}") String regionName) {

		ClientRegionFactoryBean<String, String> region = new ClientRegionFactoryBean<>();
		region.setRegionName(regionName);
		region.setPool(pool);
		region.setCache(cache);
		return region;
	}

	@Bean
	public Region<String, String> region(
			@Qualifier("clientRegion") Region localRegion) {

		return localRegion;
	}
}
