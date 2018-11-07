package com.eha.fhir.connector.fhir.server;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;

import com.eha.fhir.connector.fhir.provider.LocationResourceProvider;

import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

public class ConnectorFhirServer extends RestfulServer {
	
    private static final long serialVersionUID = 1L;
    
    LocationResourceProvider locationResourceProvider;
    
    public ConnectorFhirServer(LocationResourceProvider locationResourceProvider) {
    		this.locationResourceProvider = locationResourceProvider;
    }

   @Override
   @PostConstruct
   protected void initialize() throws ServletException {
	  
	  setDefaultResponseEncoding(EncodingEnum.JSON);
      List<IResourceProvider> resourceProviders = new ArrayList<IResourceProvider>();
      resourceProviders.add(locationResourceProvider);
      setResourceProviders(resourceProviders);
   }

	
     
}