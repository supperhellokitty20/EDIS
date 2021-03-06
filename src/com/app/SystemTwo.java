package com.app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.regex.Pattern;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.app.Patient;
import com.app.exceptions.InvalidDataFormat;
import com.app.exceptions.InvalidTokensNum;
import com.app.exceptions.PatientExist;
import com.app.exceptions.PatientNotFound;

public class SystemTwo implements Controller {

	//The tree map for this system uses to hold the id and Patient classes
	protected TreeMap<String, Patient> map;

	//Constructor for the class to initilize the TreeMap map
	public SystemTwo() {
		this.map = new TreeMap<String, Patient>();
	}

	/* Accessor for this map
	 * @return this class's map as a clone
	 */
	public TreeMap<String, Patient> getMap() {
		return (TreeMap<String, Patient>) this.map.clone();
	}

	/* Accessor for the IDs of this system
	 * @return the all the IDs in the TreeMap
	 */
	public ArrayList<String> getIDs() {
		return new ArrayList<String>(this.map.keySet());
	}

	/* Accessor for the Patients of this system
	 * @return all the Patients in the TreeMap
	 */
	public ArrayList<Patient> getPatient(){
		return new ArrayList<Patient>(this.map.values());
	}
	
	/* Accessor for the Patient's names of this system
	 * @return all the names of the patients in the TreeMap
	 */
	public ArrayList<String> getNames() {
		ArrayList<String> names = new ArrayList<String>();
		Set<Map.Entry<String, Patient>> entries = this.getMap().entrySet();
		for (Map.Entry<String, Patient> entry : entries) {
			names.add(entry.getValue().getName());
		}
		return names;
	}

	/*
	 * Gets a Patient and adds it to the map, only if the patient isn't 
	 * already in the system. If the Patient were to have the same name 
	 * as another in the system, it would add the new patient with a number
	 * next to its name
	 * @Param p holds the value of type Patient, given by the user
	 * @throws PatientExist if the patient is already in the system, it will throw this exception
	 * @return 
	 */
	@Override
	public String add(Patient p) throws PatientExist {
		p.setName(p.getName().replaceAll(" ","")) ;
		if (this.map.containsValue(p)) {
			throw new PatientExist("The patient with id" + p.getId() + " with name " + p.getName() + " had exist!\n");
		}
		String name = p.getName();
		if (this.search(name, true)) {
			p.setName(name + "_" + countSubfix(name));
			System.out.println("\"" + name + "\" has existed , adding a prefix\n" + "Patient new name:" + p.getName()
					+ "(id :" + p.getId() + ")");
		}
		this.map.put(p.getId(), p);
		return p.getId();
	}

	@Override
	public String add(String[] data) throws InvalidDataFormat, InvalidTokensNum, PatientExist {
		/**
		 * Input : A tokenized patient String data and construct new patient .
		 * <name><age><arriveTime><Data> Output: The ID of the patient
		 */
		// Parse the date from string
		if (data.length < STRING_ARRAY_DATA_SIZE) {
			throw new InvalidTokensNum(
					"The number of input tokens is" + data.length + " expected " + STRING_ARRAY_DATA_SIZE);
		}
		String oldName=data[0] ;
		if (cleanName(data[0]).isBlank()) {
			throw new InvalidDataFormat("Patient name" + oldName + " is not valid");
		}

		try {
			Patient p = new Patient(cleanName(data[0]), Integer.parseInt(data[1]), this.parseDate(data[2]));
			return this.add(p);
		} catch (PatientExist e) {
			throw new PatientExist(e.getMessage());
		} catch (Exception e) {
			// Print out error message
			throw new InvalidDataFormat(
					"Invalid input data, date must be in the format dd-MM-yyyy or age is not a number");
		}
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
		// TODO Auto-generated method stub
		return this.map.size();
	}

	@Override
	public void remove(String key, boolean isName) throws PatientNotFound {
		if (this.search(key, isName)) {
			if (isName) {
				Set<Map.Entry<String, Patient>> entries = this.getMap().entrySet();
				for (Map.Entry<String, Patient> entry : entries) {
					if (entry.getValue().getName().equals(key)) {
						this.map.remove(entry.getKey());
						break;
					}
				}
			} else {
				this.map.remove(key);
			}
		} else {
			throw new PatientNotFound("The paitient record is not in the system.");
		}

	}

	@Override
	public void edit(String key, boolean isName, EditOptions option, String value)
			throws PatientNotFound, InvalidDataFormat {
		/**
		 * Try to get the patient first
		 */
		Patient p;
		try {
			p = this.get(key, isName);
		} catch (PatientNotFound e) {
			if (isName) {
				throw new PatientNotFound("Patient name" + key + " is not found");
			} else {
				throw new PatientNotFound("Patient id " + key + " is not found");
			}
		}

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

	@Override
	public Patient get(String key, boolean isName) throws PatientNotFound {
		if (this.search(key, isName)) {
			if (isName) {
				Set<Map.Entry<String, Patient>> entries = this.getMap().entrySet();
				Patient p = null;
				for (Map.Entry<String, Patient> entry : entries) {
					if (entry.getValue().getName().equals(key)) {
						p = entry.getValue();
						break;
					}
				}
				return p;
			} else {
				return this.map.get(key);
			}
		} else {
			throw new PatientNotFound("The paitient record is not in the system.");
		}
	}

	@Override
	public boolean search(String key, boolean isName) {
		if (isName) {
			Set<Map.Entry<String, Patient>> entries = this.getMap().entrySet();
			for (Map.Entry<String, Patient> entry : entries) {
				if (entry.getValue().getName().equals(key)) {
					return true;
				}
			}
			return false;
		} else {
			return this.map.containsKey(key);
		}
	}

	// Helper Method

	/**
	 * Replace all number from the name with empty String
	 * 
	 * @param n
	 * @return
	 */
	public String cleanName(String n) {
		// Clean special character
		n = n.replaceAll("[^a-zA-Z0-9]", " ");
		n = n.replaceAll("[0-9]", "");
		return n.trim();
	}

	public Date parseDate(String value) throws InvalidDataFormat {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		Date pArriveTime = null;
		try {
			pArriveTime = formatter.parse(value);
		} catch (ParseException e) {
			// Print out error message
			throw new InvalidDataFormat("Invalid input data, date must be in the format dd-MM-yyyy");
		}
		return pArriveTime;
	}

	protected int countSubfix(String name) {
		int c = 1;
		ArrayList<String> nameList = this.getNames();
		String sp = "" + name;
		for (int i = 0; i < nameList.size(); i++) {
			String currentName = nameList.get(i);
			boolean match = Pattern.compile(sp + "_[0-9]").matcher(currentName).matches();
			if (match) {
				c++;
			}
		}

		return c + 1;
	}
	
	public String info() {
		String line = "__________________________________________________________\n";
		String info = "\n" + line + "Number of Patients currently inside the system: " + this.count() +"\n";
		Set<Map.Entry<String, Patient>> entries = this.getMap().entrySet();
		for (Map.Entry<String, Patient> entry : entries) {
			Patient p = entry.getValue() ;
			DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);  
			String date = dateFormat.format(p.getIntakeTime()) ;
			//Export to readable format for the software
			String patientInfo =""+p.getName()+" "+p.getAge()+" "+date;
			info += patientInfo + "\n";
		}
		info += line;
		return info;
	}
}
