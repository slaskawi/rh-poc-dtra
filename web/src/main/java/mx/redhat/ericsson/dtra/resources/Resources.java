package mx.redhat.ericsson.dtra.resources;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Resources 
{
    @Produces
	public Logger produceLog(InjectionPoint injectionPoint) {
		return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
	}

    @Produces
    @ApplicationScoped
    @ConfiguredCacheManager
    public EmbeddedCacheManager embeddedCacheManager() {
    	Configuration configuration = new ConfigurationBuilder()
//    		.eviction().strategy(EvictionStrategy.LRU).maxEntries(5)
	        .build();
        return new DefaultCacheManager(configuration);
    }
}
