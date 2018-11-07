package com.eha.fhir.connector.fhir.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.Location.LocationMode;
import org.hl7.fhir.dstu3.model.Location.LocationPositionComponent;
import org.hl7.fhir.dstu3.model.Location.LocationStatus;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eha.fhir.connector.dhis.model.Reference;
import com.eha.fhir.connector.dhis.model.ReferenceType;
import com.eha.fhir.connector.dhis.orgunit.OrganisationUnit;
import com.eha.fhir.connector.dhis.orgunit.OrganisationUnitService;
import com.eha.fhir.connector.dhis.orgunit.OrganisationUnits;
import com.eha.fhir.connector.fhir.model.PhysicalType;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;

@Component
public class LocationResourceProvider implements IResourceProvider {
	
	@Autowired
	OrganisationUnitService svc;

	
	public LocationResourceProvider(OrganisationUnitService svc) {
		this.svc = svc;
	}
	
	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return Location.class;
	}
	
	@Read()
    public Location getResourceById(@IdParam IdType theId) {
		
		Location location = new Location();
		
		if (null != svc) {
			
			Reference reference = new Reference(theId.getIdPart(), ReferenceType.ID);
			Optional<OrganisationUnit> organisationUnit = svc.get(reference);
			organisationUnit.ifPresent(unit -> mapLocation(unit, location));
			
		}
      
        return location;
    }
	
	@Search()
    public List<Location> getLocation() {
		List<Location> locations = new ArrayList<Location>();
		
		if (null != svc) {
			
			Optional<OrganisationUnits> organisationUnits = svc.getAll();
			organisationUnits.ifPresent(units -> mapLocations(locations, units));
			
		}
      
        return locations;
        
    }
	
	private void mapLocations(List<Location> locations, OrganisationUnits units ) {
		
		List<OrganisationUnit> listOfUnits = units.getOrganisationUnits();

		listOfUnits.forEach( unit -> {
			Location location = new Location();
			mapLocation(unit, location);
			locations.add(location);
		});
		
	}

	private void mapLocation(OrganisationUnit unit, Location location) {
		 
		location.setIdElement(IdType.newRandomUuid());
		location.addIdentifier().setSystem("http://dhis2.org/code").setValue(unit.getCode());
        location.addIdentifier().setSystem("http://dhis2.org/id").setValue(unit.getUniqueId());
        location.addAlias(unit.getShortName());
        location.setName(unit.getName());
        location.setMode(LocationMode.INSTANCE);
        location.setStatus(LocationStatus.ACTIVE);
        
        LocationPositionComponent position = new LocationPositionComponent();
        if (unit.getLatitude() != null) {
        		position.setLatitude(unit.getLatitude());
        }
        if (unit.getLongitude() != null) {
        		position.setLongitude(unit.getLongitude());
        		location.setPosition(position);
        }
        
        location.setPhysicalType(unit.isLeaf() ? PhysicalType.BUILDING.toCodeableConcept() : PhysicalType.AREA.toCodeableConcept());
        
        org.hl7.fhir.dstu3.model.Reference parent = new org.hl7.fhir.dstu3.model.Reference();
		parent.setReference(location.getResourceType().name() + "/" + unit.getParentUniqueId());
		location.setPartOf(parent);
		
	}
}
