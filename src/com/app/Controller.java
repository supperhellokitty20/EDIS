package com.app;
/**
 * Interface for the controller the logic might changes in diffrent implementation  
 * but this provide a unify way to implement the two systems.
 * @author Tuan Nguyen 
 *
 */
import java.io.File;

import com.app.exceptions.InvalidDataFormat;
import com.app.exceptions.PatientNotFound;  
 public interface Controller {
	    /**
	     * Remove the patient with that such name  
	     * @param name
	     */
	    void remove(String key , boolean isName) throws PatientNotFound;
	    /**
	     * Add a patient to the system  
	     * @param p
	     * @return  The patient ID (key) 
	     */
	    String add(Patient p) ;

	    /**
	     * Add the patient data to the system.
	     * The string arrays is a an array tokenized from the readline data 
	     * @param data
	     * @return  The patient ID (key) 
	     * @throws InvalidDataFormat 
	     */
		String add(String[] data) throws InvalidDataFormat; 
		/**
		 * 
		 */
		enum EditOptions { 
			NAME,
			AGE,
			ARRIVETIME ,
			ALL
		} ;
	    /**
	     * Edit the patient info with this key can be a name or an id ,the function will update the patient data  
	     * For example, if <code>option == EditOptions.NAME</code> , we will edit the name with the value provided 
	     * @param name
	     * @param isName 
	     * @param option which data field of the patient you want to change 
	     * @throws PatientNotFound 
	     * @throws InvalidDataFormat when value passed in does not match with the option , for example option == EditOptions.AGE but value == hah 
	     */
	    void edit(String key ,boolean isName,EditOptions option,String value) throws PatientNotFound,InvalidDataFormat ;
	    /**
	     * Export file  
	     * @param path
	     */
	    void export(String path) ;
	    /**
	     *  Get the patient with the input id if isName==true  
	     *  Search with name data  
	     * @param id
	     * @return return null  if patient not found . 
	     */
	    Patient get (String key,boolean isName) throws PatientNotFound;
	    /**
	     * Print the summary details for the current system.
	     * Summary details will include: Total patient,Total high risk patient,Most busiest time . 
	     */
	    void summary() ;
	    /**
	     * 
	     * @return the current number of patient in the system
	     */
	    int count();
	    /** 
	     * Do a search in the system , if isName==true  
	     * the key is considered as a name , otherwise search the patient with such id
	     * @param name
	     * @param isName
	     * @return
	     */
	    boolean search(String key, boolean isName) ;
}
