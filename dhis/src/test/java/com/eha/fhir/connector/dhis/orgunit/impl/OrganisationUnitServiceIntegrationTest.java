package com.eha.fhir.connector.dhis.orgunit.impl;

import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.eha.fhir.connector.dhis.config.DhisConfig;
import com.eha.fhir.connector.dhis.config.DhisEndpointConfig;
import com.eha.fhir.connector.dhis.model.Reference;
import com.eha.fhir.connector.dhis.model.ReferenceType;
import com.eha.fhir.connector.dhis.orgunit.OrganisationUnit;
import com.eha.fhir.connector.dhis.orgunit.OrganisationUnitRows;
import com.eha.fhir.connector.dhis.orgunit.OrganisationUnitService;
import com.eha.fhir.connector.dhis.orgunit.OrganisationUnits;

/**
 * Tests rely on a DHIS2 server being available and loaded with Sierra Leone test sample data. 
 * Configuration for DHIS2 connection parameters resides in the src/test/resources/application-test.yml 
 * file.
 * 
 * @author gmoran
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(	classes = { 		DhisEndpointConfig.class,
										DhisConfig.class,
										OrganisationUnitServiceImpl.class},
 						loader = 		AnnotationConfigContextLoader.class,
 						initializers = 	ConfigFileApplicationContextInitializer.class)
@ActiveProfiles("test") 
public class OrganisationUnitServiceIntegrationTest {
	
	

	@Autowired
	OrganisationUnitService service;
	
	@Test
	public void testGet() {

		Reference reference = new Reference("Ngalu CHC", ReferenceType.NAME);
		
		Optional<OrganisationUnit> unit = service.get(reference);
		assertTrue(unit.isPresent());
		
	}

	@Test
	public void testGetAll() {
		
		Optional<OrganisationUnits> units = service.getAll();
		assertTrue(units.isPresent());
	}
	
	/**
	 * Test is dependent on id of DHIS2 SQL view with the following query: 
	 * 
	 * SELECT ou.name as name, 
	 * ou.shortname as shortName, 
	 * ou.uid as uniqueId, 
	 * par.uid as parentUniqueId, 
	 * ou.code as code, 
	 * ou.coordinates, 
	 * oul.name as levelName 
	 * FROM organisationunit ou
	 * INNER JOIN _orgunitstructure ous ON ou.organisationunitid = ous.organisationunitid
	 * INNER JOIN organisationunit par ON ou.parentid = par.organisationunitid
	 * INNER JOIN orgunitlevel oul ON ous.level = oul.level
	 * WHERE ou.coordinates is not null
	 * ORDER BY oul.level, ou.name
	 * 
	 */
	@Test
	public void testGetRows() {
		
		Optional<OrganisationUnitRows> units = service.getRows("VBC6DfO71Ri");
		assertTrue(units.isPresent());
	}

}


