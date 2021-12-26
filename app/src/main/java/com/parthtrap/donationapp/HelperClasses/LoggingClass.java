/*
Class Defined to contain all the Logging details for the logcat
 */

package com.parthtrap.donationapp.HelperClasses;

public class LoggingClass{

	private final String DATA_LOG = "DATA_LOGS";
	private final String PAGES_LOG = "PAGES_LOGS";
	private String LOCAL_LOG = "";

	public LoggingClass(){
	}

	public LoggingClass(String LOCAL_LOG){
		this.LOCAL_LOG = LOCAL_LOG;
	}

	public String getDATA_LOG(){
		return DATA_LOG;
	}
	public String getPAGES_LOG(){
		return PAGES_LOG;
	}

	public String getLOCAL_LOG(){
		return LOCAL_LOG;
	}
	public void setLOCAL_LOG(String LOCAL_LOG){
		this.LOCAL_LOG = LOCAL_LOG;
	}
}
