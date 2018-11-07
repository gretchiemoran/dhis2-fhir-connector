package com.eha.fhir.connector.dhis.orgunit;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.springframework.stereotype.Component;

import com.eha.fhir.connector.dhis.model.Reference;

@Component
public interface OrganisationUnitService
{
    Optional<OrganisationUnit> get( @Nonnull Reference reference );
    
    Optional<OrganisationUnits> getAll();
    
    Optional<OrganisationUnitRows> getRows(String viewId);
}
