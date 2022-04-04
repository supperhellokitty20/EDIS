package com.test;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.Test;

import com.app.Patient;
import com.app.SystemOne;
import com.app.exceptions.* ;

class TestSystemOne {
	//Test add function
	@Test
	void addPatient() {
		SystemOne  sys = new SystemOne() ;
		Patient p = new Patient("out of ChrisRock",42,new Date()); 
		Patient c = new Patient("Slap the Shit",42,new Date()); 
		Patient d = new Patient("Will Smith",42,new Date()); 
		sys.add(p);
		sys.add(c);
		sys.add(d);
		assertEquals(3,sys.count()) ;
	}

	//Test get patient 
	@Test
	void testGet() { 
		//Throws a PatientNotFound error
		SystemOne  sys = new SystemOne() ;
		Patient p = new Patient("GI jane",42,new Date()); 
		sys.add(p);
		int id = p.getId() ;
		try {
			sys.get(id) ;
			assertEquals(1,sys.count()) ;
		} catch (PatientNotFound e) {
			fail("Cant get patient with the correct id") ;
		}
	}
	@Test
	void testGet2() { 
		//Find a not found patient expect to throws a Patient Not found  
		SystemOne  sys = new SystemOne() ;
		assertThrows(PatientNotFound.class, () -> {
			sys.get(1) ;
		});
	}

	//Test remove function
	@Test
	void testRemove() { 
		//Throws a PatientNotFound if patient not found 
	}
	//Test edit function  
	@Test 
	void testEdit() { 
		//Throws a PatientNotFound if patient not found 
		
	}
	@Test 
	void testEditWithName() { 
		//Throws a PatientNotFound if patient not found 
		
	}
	@Test 
	void testEditWithId() { 
		//Throws a PatientNotFound if patient not found 
		
	}
	@Test 
	void testSearch() { 
		//Throws a PatientNotFound if patient not found 
		
	}
	// Test process Patient string  
	@Test
	void processPatientStringData() { 
		SystemOne  sys = new SystemOne() ;
		String[]  t = {"James/Smith","42","03-04-2021"} ;
		int id = 0;
		try {
			id = sys.add(t);
		} catch (InvalidDataFormat e) {

		}
		Patient p;
		try {
			p = sys.get(id);
			assertEquals(1,sys.count()) ;
			assertEquals(42,p.getAge()) ;
			assertEquals("James/Smith",p.getName()) ;
		} catch (PatientNotFound e) {
			fail("No patient found") ;
		}
	}
	@Test
	void getName() { 
	}
	@Test 
	void getId() { 
	}
}
