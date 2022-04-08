package com.app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.regex.*;
import java.util.concurrent.ConcurrentSkipListMap;
import com.app.Patient;
import com.app.exceptions.InvalidDataFormat;
import com.app.exceptions.InvalidTokensNum;
import com.app.exceptions.PatientExist;
import com.app.exceptions.PatientNotFound;

public class SystemOne implements Controller {
	ConcurrentNavigableMap<String, Patient> map;

	public SystemOne() {
		map = new ConcurrentSkipListMap<String, Patient>();
	}

	public ArrayList<String> getIDs() {
		return new ArrayList<String>(this.map.keySet());
	}

	public ArrayList<String> getNames() {
		ArrayList<Patient> patients = new ArrayList<Patient>(this.map.values());
		ArrayList<String> names = new ArrayList<String>();
		for (int i = 0; i < patients.size(); i++) {
			names.add(patients.get(i).getName());
		}
		return names;
	}

	/**
	 * A remove operation of a skip list sorted Map Analysis : Textbook
	 */
	@Override
	public void remove(String key, boolean isName) throws PatientNotFound {
		// Remove the patient with such id
		if (!isName) {
			if (this.map.containsKey(key)) {
				this.map.remove(key);
			} else {
				throw new PatientNotFound("The patient id " + key + " is not found");
			}
			return;
		}
		// Remove the patient with such name
		if (this.search(key, true)) {
			ArrayList<Patient> patients = new ArrayList<Patient>(this.map.values());
			for (int i = 0; i < patients.size(); i++) {
				if (patients.get(i).getName().equals(key)) {
					this.map.remove(patients.get(i).getId());
					break;
				}
			}
		} else {
			throw new PatientNotFound("The patient name " + key + " is not found");
		}
	}
	protected int countSubfix(String name) { 
			int c =1 ;
			ArrayList<String> nameList = this.getNames() ;
			String sp = ""+name;
			for(int i =0;i<nameList.size();i++) { 
				String currentName = nameList.get(i) ;
				boolean match=  Pattern.compile(sp+"_[0-9]").matcher(currentName).matches()  ;
				if(match) { 
					c++ ;
				}
			}

		return c+1 ;
	}
	/**
	 * Put the patient in the system Analysis: from the textbook
	 */
	@Override
	public String add(Patient p) throws PatientExist {
		// TODO Auto-generated method stub
		p.setName(p.getName().replaceAll(" ","")) ;
		if (this.map.containsValue(p)) {
			throw new PatientExist("The patient \"" + p.getId() + "\":\"" + p.getName() + "\" had exist!\n");
		}
		if(this.getNames().contains(p.getName())) { 
			//Write test for this  
			String oldName = p.getName() ;
			int c = countSubfix(oldName) ;
			//Loop through all the name , match the patient regex pattern
			p.setName(oldName+"_"+c) ;
			System.out.println("\""+oldName+"\" has existed , adding a prefix\n"
					+ "Patient new name:"+p.getName()+"(id :"+p.getId()+")") ;
		} 
		String id = p.getId();
		this.map.putIfAbsent(id, p);
		return id;
	}

	@Override
	public String add(String[] data) throws InvalidDataFormat, InvalidTokensNum, PatientExist {
		/**
		 * Input : A tokenized patient String data and construct new patient .
		 * <name><age><arriveTime><Data> Output: The ID of the patient
		 */

		if (data.length < STRING_ARRAY_DATA_SIZE) {
			throw new InvalidTokensNum(
					"The number of input tokens is " + data.length + " expected " + STRING_ARRAY_DATA_SIZE);
		}
		Patient p;
		String pName = cleanName(data[0]);
		if (pName.isBlank()) {
			throw new InvalidDataFormat("Patient name \"" + pName + "\" is not valid");
		}
		// Parse the date from string
		Date pArriveTime = null;
		int pAge;
		try {
			pAge = Integer.parseInt(data[1]);
		} catch (Exception e) {
			throw new InvalidDataFormat("Input age \"" + data[1] + "\" can't be parse to int");
		}
		try {
			pArriveTime = this.parseDate(data[2]);
		} catch (Exception e) {
			// Print out errormessage
			throw new InvalidDataFormat("Input date \"" + data[2] + "\" not in the format of dd-MM-yyyy");
		}
		p = new Patient(pName, pAge, pArriveTime);
		try {
			return this.add(p);
		} catch (PatientExist e) {
			throw new PatientExist(e.getMessage());
		}
	}

	//Helper method for summary and export to get all the info on the patients in the system.
	public String info() {
		String line = "__________________________________________________________\n";
		String info = "\n" + line + "Number of Patients currently inside the system: " + this.count() +"\n";
		ArrayList<Patient> patients = new ArrayList<Patient>(this.map.values());
		for (int i = 0; i < patients.size(); i++) {
			info += patients.get(i).toString() + "\n";
		}
		info += line;
		return info;
	}
	
	@Override
	public void export(String path) {
		// TODO Auto-generated method stub
		try {
			FileWriter fw = new FileWriter(path);
			fw.write(this.info());
			fw.close();		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void summary() {
		// TODO Auto-generated method stub
		System.out.println(this.info());
	}

	@Override
	public int count() {
		return map.size();
	}

	@Override
	// Analysis see textbook
	public boolean search(String key, boolean isName) {
		// Search the system for d
		if (!isName) {
			return this.map.containsKey(key);
		}
		/**
		 * Here we do a loop on ArrayList Complexity: idk
		 */
		ArrayList<Patient> patients = new ArrayList<Patient>(this.map.values());
		for (int i = 0; i < patients.size(); i++) {
			if (patients.get(i).getName().equals(key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param id
	 * @throws PatientNotFound
	 * @return the Patient Object if the key is found A get operation on the
	 *         skipList implementation of sorted set We dont need to check if get
	 *         return a null object because if it's null the function already throws
	 *         a PatientNotFound exception Analysys: Textbook
	 */
	@Override
	public Patient get(String key, boolean isName) throws PatientNotFound {
		// Get the patient with such id
		Patient found = null;
		if (!isName) {
			if (map.containsKey(key)) {
				found = map.get(key);
			}
		} else {
			//TODO:  What if there is a patient with the same name 
			if (this.getNames().contains(key)) {
				if (this.search(key, true)) {
					ArrayList<Patient> patients = new ArrayList<Patient>(this.map.values());
					for (int i = 0; i < patients.size(); i++) {
						if (patients.get(i).getName().equals(key)) {
							found = patients.get(i);
							break;
						}
					}
				}
			}

		}
		// Get the patient with such name
		if (found == null) {
			if (!isName) {
				throw new PatientNotFound("Patient id " + key + " is not found");
			} else {
				throw new PatientNotFound("The patient name " + key + " is not found on the system");
			}
		}
		return found;
	}

	public Date parseDate(String value) throws InvalidDataFormat {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		Date pArriveTime = null;
		try {
			pArriveTime = formatter.parse(value);
		} catch (ParseException e) {
			// Print out errormessage
			throw new InvalidDataFormat("Invalid input data, date must be in the format dd-MM-yyyy");
		}
		return pArriveTime;
	}

	/**
	 * Repplace all number from the name with empty String
	 * 
	 * @param n
	 * @return
	 */
	public String cleanName(String n) {
		// Clean special character
		n = n.replaceAll("[^a-zA-Z0-9]", "");
		n = n.replaceAll("[0-9]", "");
		return n.trim();
	}

	@Override
	public void edit(String key, boolean isName, EditOptions option, String value)
			throws PatientNotFound, InvalidDataFormat {
		/**
		 * Try to get the patient first
		 */
		Patient p;
		// If key is ID
		if (!isName) {
			try {
				p = this.get(key, false);
			} catch (PatientNotFound e) {
				throw new PatientNotFound("Patient id " + key + " is not found");
			}
		} else {
			try {
				p = this.get(key, true);
			} catch (PatientNotFound e) {
				throw new PatientNotFound("Patient name " + key + " is not found");
			}

		}
		// If key is name
		/*
		 * Edit the patient info
		 */
		switch (option) {
		case NAME: {
			// Check and validate name
			value = cleanName(value);
			if (value.isBlank()) {
				throw new InvalidDataFormat("Name may contains special character or is empty");
			}
			p.setName(value);
			break;
		}
		case AGE: {
			try {
				int v = Integer.parseInt(value);
				p.setAge(v);
				break;
			} catch (Exception e) {
				throw new InvalidDataFormat("The inputed age value " + value + " cannot be parse to a number");
			}

		}
		case ARRIVETIME: {
			try {
				Date newDate;
				newDate = this.parseDate(value);
				p.setIntakeTime(newDate);
				break;
			} catch (Exception e) {
				throw new InvalidDataFormat("The input date value" + value + " can't be parsed");
			}
		}
		case ALL: {
			// Tokenize the input value <name> <age> <arriveTime>
			StringTokenizer st = new StringTokenizer(value, " ");
			if (st.countTokens() < STRING_ARRAY_DATA_SIZE) {
				throw new InvalidDataFormat("Expecting " + STRING_ARRAY_DATA_SIZE + "got " + st.countTokens());
			}
			String[] newPatientData = new String[STRING_ARRAY_DATA_SIZE];
			int i = 0;
			while (st.hasMoreTokens()) {
				newPatientData[i] = st.nextToken();
				i++;
			}
			String newName = this.cleanName(newPatientData[0]);
			if (newName.isBlank()) {
				throw new InvalidDataFormat("Name may contains special character or is empty");
			}
			p.setName(newName);
			try {
				int v = Integer.parseInt(newPatientData[1]);
				p.setAge(v);
			} catch (Exception e) {
				throw new InvalidDataFormat("The inputed age value " + value + " cannot be parse to a number");
			}
			try {
				Date newDate = this.parseDate(newPatientData[2]);
				p.setIntakeTime(newDate);
			} catch (Exception e) {
				throw new InvalidDataFormat(e.getMessage());
			}

		}

		}

	}
}
