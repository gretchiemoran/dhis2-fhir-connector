package com.eha.fhir.connector.dhis.orgunit;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrganisationUnits implements Serializable
{
	private static final long serialVersionUID = 8087111574765445358L;

	@JsonProperty("organisationUnits")
	private List<OrganisationUnit> details;
	
    public List<OrganisationUnit> getOrganisationUnits()
    {
        return details;
    }

    public void setOrganisationUnits( List<OrganisationUnit> units )
    {
        this.details = units;
    }
}
