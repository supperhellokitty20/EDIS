package com.test;
import com.app.* ; 
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.app.Patient;
import com.app.SystemOne;
import com.app.exceptions.* ;

class TestSystemOne {
	//Test add function
	static Controller sys;
	@BeforeEach
	public void init() {
		try {
			//To test system  two comment out the below line 
			sys = new SystemOne() ;
			//sys  = new SystemTwo() ; 
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		
	}
	@Test 
	//Testing get id to see whether hexString hashcode is id of that patient 
	void testGetId() { 
		Patient d = new Patient("Will Smith",42,new Date()); 
		String id = d.getId() ;
		try {
			String sysId ;
			sysId=sys.add(d) ;
			assertEquals(id,sysId) ;
		} catch(Exception e) {
			
		};
	} 
	@Test
	void addPatient() {
		Patient p = new Patient("out of ChrisRock",42,new Date()); 
		Patient c = new Patient("Slap the Shit",42,new Date()); 
		Patient d = new Patient("Will Smith",42,new Date()); 
		sys.add(p);
		sys.add(c);
		sys.add(d);
		assertEquals(3,sys.count()) ;
	}

	//Test get patient with using patient id. 
	@Test
	void testGet() { 
		//Throws a PatientNotFound error
		Patient p = new Patient("GI jane",42,new Date()); 
		sys.add(p);
		String id = p.getId() ;
		try {
			Patient data =sys.get(id,false) ;
			Patient data2 =sys.get(p.getName(),true) ;
			String expName =  "GI jane" ;
			assertEquals(id,data.getId()) ;
			assertEquals(expName,data2.getName()) ;
		} catch (PatientNotFound e) {
		}
	}
	@Test
	void testGet2() { 
		//Find a not found patient expect to throws a Patient Not found  
		assertThrows(PatientNotFound.class, () -> {
			sys.get("One",false) ;
		});
	}

	//Test remove function with invalid Id 
	@Test
	void testRemoveInvalid() { 
		//Throws a PatientNotFound if patient not found 
		assertThrows(PatientNotFound.class, () -> {
			sys.remove("One",false) ;
		});
	}
	// Test remove with invalid name
	@Test
	void testRemoveInvalid2() { 
		assertThrows(PatientNotFound.class, () -> {
			sys.remove("KI",true) ;
		});
	}

	//Test remove with a valid id 
	@Test
	void testRemove2() { 
		Patient d = new Patient("Will Smith",42,new Date()); 
		String id = d.getId();
		try {
			sys.add(d) ;
		} catch (Exception e) {
			fail("Failed") ;
		}
		try {
			sys.remove(id, false);
		} catch (PatientNotFound e) {
			fail("Failed remove") ;
		}
		assertEquals(0,sys.count()) ;
	}

	//Test remove with a valid name 
	@Test
	void testRemove3() { 
		Patient d = new Patient("Will Smith",42,new Date()); 
		String name = d.getName() ;
		try {
			sys.add(d) ;
		} catch (Exception e) {
			fail("Failed") ;
		}
		try {
			sys.remove(name, true);
			assertEquals(0,sys.count()) ;
		} catch (PatientNotFound e) {
			fail("Failed remove") ;
		}
	}
	// Test EDIT
	//TODO : Need to reimplement the edit test , not ready for use now  
	@Test 
	void testEditInvalid() { 
		//Throws a PatientNotFound 
		assertThrows(PatientNotFound.class, () -> {
			sys.edit("One",false, null, null) ;
		});
	}
	//Test edit function with with invalid name
	@Test 
	void testEditInvalid2() { 
		//Throws a PatientNotFound if patient not found 
		assertThrows(PatientNotFound.class, () -> {
			sys.edit("Chris",true, null, null) ;
		});
		
	}
	@Test 
	void testEdit() { 
		//Throws a PatientNotFound if patient not found 
		assertThrows(PatientNotFound.class, () -> {
			sys.edit("Some false id",false, null, null) ;
		});
		
	}
	//END test edit 
	@Test 
	void testSearch() { 
		Patient p = new Patient("out of ChrisRock",42,new Date()); 
		Patient c = new Patient("Slap the Shit",42,new Date()); 
		Patient d = new Patient("Will Smith",42,new Date()); 
		try {
			sys.add(p) ;
			sys.add(d) ;
			sys.add(c) ;
		}
		catch( Exception e){
		}
		boolean f =sys.search("Doom",true) ;
		boolean f1 =sys.search("Karen",false) ;
		assertEquals(false,f) ;
		assertEquals(false,f1) ;

		boolean s =sys.search(d.getName(),true) ;
		boolean s1 =sys.search(c.getId(),false) ;
		assertEquals(true,s) ;
		assertEquals(true,s1) ;
	}
	// Test process Patient string  
	@Test
	void processPatientStringData() { 
		SystemOne  sys = new SystemOne() ;
		//An example input data from a file 
		//We use / in the name to stop Tokenizer 
		String[]  t = {"James/Smith","42","03-04-2021"} ;
		String id="" ;
		try {
			id = sys.add(t);
		} catch (InvalidDataFormat e) {

		}
		Patient p;
		try {
			p = sys.get(id,false);
			assertEquals(1,sys.count()) ;
			assertEquals(42,p.getAge()) ;
			assertEquals("James Smith",p.getName()) ;
		} catch (PatientNotFound e) {
			fail("No patient found") ;
		}
	}
}
