package com.smalik.gemfire;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.mapping.ClientRegion;

@SpringBootApplication
public class SampleGemfireApplication implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(SampleGemfireApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SampleGemfireApplication.class, args);
	}

	@Autowired
    private Region<String, String> region;

    @Override
    public void run(String... args) throws Exception {

        String magicKey = "magic-key";
        String magicValue = "24";

        String cachedValue = region.get(magicKey);
        if (cachedValue != null) {

            logger.info("*** FOUND: key=" + magicKey +  ", value=" + cachedValue);

        } else {

            region.put(magicKey, magicValue);
            logger.info("*** ADDED: key=" + magicKey + ", value=" + magicValue);
        }
    }
}
