package mx.redhat.ericsson.dtra.resources;

import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

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
       System.out.println("########### Creating Cache Configuration ###########");

    	Configuration configuration = new ConfigurationBuilder()
//    		.eviction().strategy(EvictionStrategy.LRU).maxEntries(5)
	        .build();

       GlobalConfigurationBuilder gcb = new GlobalConfigurationBuilder();
       gcb.globalJmxStatistics().allowDuplicateDomains(true);

        return new DefaultCacheManager(gcb.build(), configuration);
    }
}
