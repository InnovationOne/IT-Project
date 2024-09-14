package org.springframework.samples.petclinic.system;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.configuration.MutableConfiguration;

// Diese Klasse konfiguriert das Caching für die Anwendung
// Sie erstellt einen Cache namens "vets" und aktiviert das Caching insgesamt
@Configuration(proxyBeanMethods = false)
@EnableCaching
class CacheConfiguration {

	//Definiert einen Bean, der den CacheManager anpasst
	// Erstellt einen Cache mit dem Namen "vets" und der angegebenen Konfiguration
	@Bean
	public JCacheManagerCustomizer petclinicCacheConfigurationCustomizer() {
		// Lambda-Ausdruck, der den Cache "vets" mit der spezifizierten Konfiguration erstellt
		return cm -> cm.createCache("vets", cacheConfiguration());
	}

	// Definiert die Cache-Konfiguration
	// Aktiviert die Statistik für den Cache, um Monitoring zu ermöglichen
	private javax.cache.configuration.Configuration<Object, Object> cacheConfiguration() {
		// Erstellt eine neue MutableConfiguration und aktiviert die Statistiken
		return new MutableConfiguration<>().setStatisticsEnabled(true);
	}
}