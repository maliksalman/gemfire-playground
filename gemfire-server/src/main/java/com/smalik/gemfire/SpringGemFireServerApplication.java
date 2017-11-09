/*
 * Copyright 2014-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smalik.gemfire;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.RegionAttributes;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.PartitionedRegionFactoryBean;
import org.springframework.data.gemfire.RegionAttributesFactoryBean;
import org.springframework.data.gemfire.server.CacheServerFactoryBean;
import org.springframework.session.data.gemfire.config.annotation.web.http.EnableGemFireHttpSession;

import java.util.Properties;

@SpringBootApplication
@EnableGemFireHttpSession(regionName = "session-cache")
public class SpringGemFireServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringGemFireServerApplication.class, args);
	}

	@Bean
	static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertyPlaceholderConfigurer();
	}

	@Bean
	Properties gemfireProperties(
		  @Value("${gemfire.log.level:config}") String logLevel,
		  @Value("${gemfire.locator.host-port:localhost[10334]}") String locatorHostPort,
		  @Value("${gemfire.manager.port:1099}") String managerPort) {

		Properties gemfireProperties = new Properties();

		gemfireProperties.setProperty("name", "SpringGemFireServerApplication");
		gemfireProperties.setProperty("mcast-port", "0");
		gemfireProperties.setProperty("log-level", logLevel);
		gemfireProperties.setProperty("start-locator", locatorHostPort);
		gemfireProperties.setProperty("jmx-manager", "true");
		gemfireProperties.setProperty("jmx-manager-port", managerPort);
		gemfireProperties.setProperty("jmx-manager-start", "true");

		return gemfireProperties;
	}

	@Bean
	CacheFactoryBean gemfireCache(@Qualifier("gemfireProperties") Properties gemfireProperties) {
		CacheFactoryBean gemfireCache = new CacheFactoryBean();

		gemfireCache.setClose(true);
		gemfireCache.setProperties(gemfireProperties);

		return gemfireCache;
	}

	@Bean
	CacheServerFactoryBean gemfireCacheServer(Cache gemfireCache,
			@Value("${gemfire.cache.server.bind-address:localhost}") String bindAddress,
			@Value("${gemfire.cache.server.hostname-for-clients:localhost}") String hostNameForClients,
			@Value("${gemfire.cache.server.port:40404}") int port) {

		CacheServerFactoryBean gemfireCacheServer = new CacheServerFactoryBean();

		gemfireCacheServer.setCache(gemfireCache);
		gemfireCacheServer.setAutoStartup(true);
		gemfireCacheServer.setBindAddress(bindAddress);
		gemfireCacheServer.setHostNameForClients(hostNameForClients);
		gemfireCacheServer.setPort(port);

		return gemfireCacheServer;
	}

	@Bean
	PartitionedRegionFactoryBean<String, String> genericRegion(Cache gemfireCache,
                                                           @Qualifier("genericRegionAttributes") RegionAttributes<String, String> factorialsRegionAttributes) {

		PartitionedRegionFactoryBean<String, String> factorialsRegion = new PartitionedRegionFactoryBean<>();

		factorialsRegion.setAttributes(factorialsRegionAttributes);
		factorialsRegion.setCache(gemfireCache);
		factorialsRegion.setClose(false);
		factorialsRegion.setName("generic-cache");
		factorialsRegion.setPersistent(false);

		return factorialsRegion;
	}


	@Bean
	@SuppressWarnings("unchecked")
	RegionAttributesFactoryBean genericRegionAttributes() {
		RegionAttributesFactoryBean factorialsRegionAttributes = new RegionAttributesFactoryBean();

		factorialsRegionAttributes.setKeyConstraint(String.class);
		factorialsRegionAttributes.setValueConstraint(String.class);

		return factorialsRegionAttributes;
	}
}
