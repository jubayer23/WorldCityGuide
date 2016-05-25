package com.ismeralda_fabio.worldcityguide.model;

import java.util.List;

public class Places {

	String status;
	List<Place> results;

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the results
	 */
	public List<Place> getResults() {
		return results;
	}

	/**
	 * @param results
	 *            the results to set
	 */
	public void setResults(List<Place> results) {
		this.results = results;
	}

}
