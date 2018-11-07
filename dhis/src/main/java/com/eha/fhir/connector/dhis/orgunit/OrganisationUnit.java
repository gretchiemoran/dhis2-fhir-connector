package com.eha.fhir.connector.dhis.orgunit;

import java.io.Serializable;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrganisationUnit implements  Serializable {

	private static final long serialVersionUID = -4132354928868329615L;
	
	private String name;
	
	private String shortName;
	
	@JsonProperty("id")
	private String uniqueId;
	
	private String code;
	
	private String coordinates;
	
	private String path;
	
	@JsonProperty("leaf")
	private boolean isLeaf;

	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}


	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}


	/**
	 * @return the uniqueId
	 */
	public String getUniqueId() {
		return uniqueId;
	}


	/**
	 * @param uniqueId the uniqueId to set
	 */
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}


	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}


	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}


	/**
	 * @return the coordinates
	 */
	public String getCoordinates() {
		return coordinates;
	}

	/**
	 * @param coordinates the coordinates to set
	 * 
	 * @TODO coordinates come in as a string formatted as: "coordinates": "[-10.5741,8.2762]" 
	 *       Need to write a mapper to replace the  parse code currently used. 
	 */
	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}
	
	public Double getLatitude() {
		return getCoordinate(1);
	}
	
	public Double getLongitude() {
		return getCoordinate(2);
	}
	
	private Double getCoordinate(int position) {

		Optional<String[]> coors = null;
		
		Optional<String> coor = Optional.ofNullable(coordinates);
		coors = Optional.ofNullable(coor.map(val -> val.split("[\\[\\],]")).orElse(null));
		
		return coors.filter(val -> StringUtils.isNumeric(val[position])).map(val -> Double.valueOf(val[position])).orElse(null);
		
	}

	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * 
	 * The logic here operates on the path attribute that is returned when DHIS2 is queried
	 * The format is: "path": "/ImspTQPwCqd/jUb8gELQApl/byp7w6Xd9Df/DQHGtTGOP6b"
	 * We could also create another pojo called Parent as the JSON also has the following node:
	 * "parent": {"id": "byp7w6Xd9Df"}
	 * 
	 * @return the uid of the parent to this location
	 */
	public String getParentUniqueId() {
		
		String[] tree = path.split("/");
		
		if (tree.length >= 2) {
			return tree[tree.length-2];			
		}
		return "";
    }

	/**
	 * @return the isLeaf
	 */
	public boolean isLeaf() {
		return isLeaf;
	}


	/**
	 * @param isLeaf the isLeaf to set
	 */
	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}


	@Override
	public String toString() {
		return name + ":" + uniqueId + ":" + code + ":parent:"  +  path + ":" + coordinates;
	}

}
