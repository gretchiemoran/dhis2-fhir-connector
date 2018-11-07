package com.eha.fhir.connector.fhir.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eha.fhir.connector.dhis.orgunit.OrganisationUnitService;
import com.eha.fhir.connector.fhir.provider.LocationResourceProvider;
import com.eha.fhir.connector.fhir.server.ConnectorFhirServer;

@Configuration
public class FhirConfig {

	@Bean LocationResourceProvider locationResourceProvider(OrganisationUnitService organisationUnitService) {
		return new LocationResourceProvider(organisationUnitService);
	}
	
	@Bean
	public ServletRegistrationBean<ConnectorFhirServer> connectorServletBean(LocationResourceProvider locationResourceProvider) {
	    ServletRegistrationBean<ConnectorFhirServer> bean = new ServletRegistrationBean<ConnectorFhirServer>(
	      new ConnectorFhirServer(locationResourceProvider), "/fhir/*");
	    bean.setLoadOnStartup(1);
	    return bean;
	}
	
	
}
