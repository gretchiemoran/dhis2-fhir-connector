package com.eha.fhir.connector.dhis.orgunit.impl;

import java.util.Collections;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eha.fhir.connector.dhis.model.Reference;
import com.eha.fhir.connector.dhis.orgunit.OrganisationUnit;
import com.eha.fhir.connector.dhis.orgunit.OrganisationUnitRows;
import com.eha.fhir.connector.dhis.orgunit.OrganisationUnitService;
import com.eha.fhir.connector.dhis.orgunit.OrganisationUnits;

@Service
public class OrganisationUnitServiceImpl implements OrganisationUnitService
{
    protected static final String ORGANISATION_UNIT_BY_CODE_URI = "/organisationUnits.json?paging=false&fields=id,code&filter=code:eq:{code}";

    protected static final String ORGANISATION_UNIT_BY_NAME_URI = "/organisationUnits.json?paging=false&fields=id,code&filter=name:eq:{name}";

    protected static final String ORGANISATION_UNIT_BY_ID_URI = "/organisationUnits/{id}.json";
    
    protected static final String ORGANISATION_UNIT_ROWS = "/sqlViews/{id}/data.json";
    
    protected static final String ORGANISATION_UNIT_DETAILS = "/metadata.json?assumeTrue=false"
														    		+ "&organisationUnits=true"
														    		+ "&organisationUnitGroups=true"
														    		+ "&organisationUnitLevels=true"
														    		+ "&organisationUnitGroupSets=true"
														    		+ "&categoryOptions=true"
														    		+ "&optionSets=true"
														    		+ "&dataElementGroupSets=true"
														    		+ "&categoryOptionGroupSets=true"
														    		+ "&categoryCombos=true&options=true"
														    		+ "&categoryOptionCombos=true"
														    		+ "&dataSets=true"
														    		+ "&dataElementGroups=true"
														    		+ "&dataElements=true"
														    		+ "&categoryOptionGroups=true"
														    		+ "&categories=true&users=false"
														    		+ "&userGroups=false"
														    		+ "&userRoles=false";

    private final RestTemplate restTemplate;

    @Autowired
    public OrganisationUnitServiceImpl( @Qualifier( "systemDhis2RestTemplate" ) RestTemplate restTemplate )
    {
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<OrganisationUnit> get( @Nonnull Reference reference )
    {
        final ResponseEntity<OrganisationUnits> result;
        switch ( reference.getType() )
        {
            case CODE:
                result = restTemplate.getForEntity( ORGANISATION_UNIT_BY_CODE_URI, OrganisationUnits.class, reference.getValue() );
                break;
            case NAME:
                result = restTemplate.getForEntity( ORGANISATION_UNIT_BY_NAME_URI, OrganisationUnits.class, reference.getValue() );
                break;
            case ID:
                return Optional.ofNullable( restTemplate.getForEntity( ORGANISATION_UNIT_BY_ID_URI, OrganisationUnit.class, reference.getValue() ).getBody() );
            default:
                throw new AssertionError( "Unhandled reference type: " + reference.getType() );
        }
        return Optional.ofNullable( Optional.ofNullable( result.getBody() ).orElse( new OrganisationUnits() ).getOrganisationUnits() ).orElse( Collections.emptyList() ).stream().findFirst();
    }

    
	@Override
	public Optional<OrganisationUnits> getAll() {
		final ResponseEntity<OrganisationUnits> result;
		
		result = restTemplate.getForEntity( ORGANISATION_UNIT_DETAILS, OrganisationUnits.class);
        
		return Optional.ofNullable(Optional.ofNullable( result.getBody() ).orElse( new OrganisationUnits()));
	}

	/* (non-Javadoc)
	 * @see com.eha.fhir.connector.dhis.orgunit.OrganisationUnitService#getRows()
	 */
	@Override
	public Optional<OrganisationUnitRows> getRows(@Nonnull String viewId) {
		final ResponseEntity<OrganisationUnitRows> result;
		
		result = restTemplate.getForEntity( ORGANISATION_UNIT_ROWS, OrganisationUnitRows.class, viewId);
        
		return Optional.ofNullable(Optional.ofNullable( result.getBody() ).orElse( new OrganisationUnitRows()));
	}
}
