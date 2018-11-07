package com.eha.fhir.connector.fhir.model;

import org.hl7.fhir.dstu3.model.CodeableConcept;

public enum PhysicalType {
    AREA("http://hl7.org/fhir/location-physical-type","Area", "area", "Administrative Area"),
    BUILDING("http://hl7.org/fhir/location-physical-type", "Building", "bu", "Facility" );
    
    private final String system;
    private final String display;
    private final String code;
    private final String text;
    
    private PhysicalType(String system, String display, String code, String text) {
    		this.system = system; 
    		this.display = display;
    		this.code = code;
    		this.text = text;
    }

    public CodeableConcept toCodeableConcept() {
    		CodeableConcept cc = new CodeableConcept();
    		cc.addCoding().setCode(code).setSystem(system).setDisplay(display);
    		cc.setText(text);
    		return cc;
    }
}