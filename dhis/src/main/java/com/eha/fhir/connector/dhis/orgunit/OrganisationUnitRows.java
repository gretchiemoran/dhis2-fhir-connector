package com.eha.fhir.connector.dhis.orgunit;

import java.io.Serializable;
import java.util.List;

public class OrganisationUnitRows implements Serializable {

	private static final long serialVersionUID = 797490287196598488L;

	private List<List<String>> rows;


	/**
	 * @return the rows
	 */
	public List<List<String>> getRows() {
		return rows;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(List<List<String>> rows) {
		this.rows = rows;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}

}
