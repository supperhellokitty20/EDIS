package com.test;
import com.app.* ; 
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.app.Patient;
import com.app.SystemOne;
import com.app.Controller.EditOptions;
import com.app.Controller;
import com.app.exceptions.* ;

class TestSys {
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
		String id = Integer.toHexString(d.hashCode()) ;
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
		String i1 = Integer.toHexString(p.hashCode())  ;
		String i2 = Integer.toHexString(c.hashCode()); 
		String i3 = Integer.toHexString(d.hashCode()); 
		try {
			String s1 =sys.add(p);
			assertEquals(true,i1.equals(s1)) ;
		} catch (PatientExist e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			String s2 =sys.add(c);
			assertEquals(true,i2.equals(s2)) ;
		} catch (PatientExist e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			String s3=sys.add(d);
			assertEquals(true,i3.equals(s3)) ;
		} catch (PatientExist e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(3,sys.count()) ;
	}
	@Test
	void addSamePatient() {
		//Add the same patient to system 
		Patient d = new Patient("Will Smith",42,new Date()); 
		try {
			sys.add(d);
		}
		catch(Exception e) {
			
		}
		assertThrows(PatientExist.class,()->{
			sys.add(d); 
		}) ;
		//Test Addd not the same person but the same name 
		Patient z = new Patient("Will Smith",43,new Date()); 
		Patient z1 = new Patient("Will Smith",444,new Date()); 
		Patient c = new Patient("Slap the Shit",42,new Date()); 
		Patient z2 = new Patient("Will Smith",324,new Date()); 
		try {
			sys.add(z);
			sys.add(z1);
			sys.add(c);
			sys.add(z2);
		}
		catch(Exception e) {
			fail("The add function auto add subfix _number") ;
		}
		try {
			boolean x =sys.get(z.getId(), false).getName().equals("Will Smith_2");
			boolean t =sys.get(z1.getId(), false).getName().equals("Will Smith_3");
			boolean t2 =sys.get(z2.getId(), false).getName().equals("Will Smith_4");
			assertEquals(true,x) ;
			assertEquals(true,t) ;
			assertEquals(true,t2) ;
		} catch (PatientNotFound e) {
			fail("") ;
		}
		
	}

	//Test get patient with using patient id. 
	@Test
	void testGet() { 
		//Throws a PatientNotFound error
		Patient p = new Patient("GI jane",42,new Date()); 
		String id = p.getId() ;
		try {
			sys.add(p);
			Patient data =sys.get(id,false) ;
			Patient data2 =sys.get(p.getName(),true) ;
			String expName =  "GI jane" ;
			assertEquals(id,data.getId()) ;
			assertEquals(expName,data2.getName()) ;
		} catch (Exception e) {
			fail("Some thing f") ;
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
		Patient p = new Patient("out of ChrisRock",42,new Date()); 
		Patient c = new Patient("Slap the Shit",42,new Date()); 
		Patient d = new Patient("Will Smith",42,new Date()); 
		try {
			sys.add(p);
			sys.add(c);
			sys.add(d);
		} catch( Exception e ) {
			
		}
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
		String name = "Will Smith" ;
		try {
			sys.add(d) ;
		} catch (Exception e) {
			fail("Failed") ;
		}
		try {
			sys.remove(name, true);
			boolean x = sys.search(name, true) ;
			assertEquals(false,x); 
			assertEquals(0,sys.count()) ;
		} catch (PatientNotFound e) {
			fail("Failed remove") ;
		}
	}
	// Test EDIT
	@Test 
	void testEditInvalid() { 
		//Throws a PatientNotFound 
		assertThrows(PatientNotFound.class, () -> {
			sys.edit("One",false, Controller.EditOptions.AGE,"42") ;
			sys.edit("One",true, Controller.EditOptions.NAME,"hot") ;
		});
	}
	//Test edit function with with invalid name
	@Test 
	void testEditInvalid2() { 
		//Throw invalid data format
		Patient p = new Patient("out of ChrisRock",42,new Date()); 
		Patient c = new Patient("Slap the Shit",42,new Date()); 
		Patient d = new Patient("Will Smith",42,new Date()); 
		try {
			sys.add(p);
			sys.add(c);
			sys.add(d);
		}
		catch (Exception e ) {
			
		}
		/**
		 *  Test edit name
		 */
		assertThrows(InvalidDataFormat.class, () -> {
			sys.edit(p.getId(), false,Controller.EditOptions.NAME,"52"); 
		});
		/**
		 * Test edit age 
		 */
		assertThrows(InvalidDataFormat.class, () -> {
			sys.edit(p.getId(), false,Controller.EditOptions.AGE,"Pushing P"); 
		});
		/**
		 * Test edi arriveal time  
		 */
		assertThrows(InvalidDataFormat.class, () -> {
			//Pass in wrong value for date 
			sys.edit(p.getId(), false,Controller.EditOptions.ARRIVETIME,"Pushing P"); 
		});
		
	}
	//Test Edit with valid data 
	@Test 
	void testEdit() { 
		Patient p = new Patient("out of ChrisRock",42,new Date()); 
		String newName = "Pun" ;
		String newAge ="100";
		try {
			sys.add(p);
			Date newDate;
			SimpleDateFormat formatter = new SimpleDateFormat(Controller.DATE_FORMAT) ;
			newDate = formatter.parse("12-09-1999") ;
			sys.edit(p.getId(), false, EditOptions.ARRIVETIME, "12-09-1999");
			Date sysDate = sys.get(p.getId(),false).getIntakeTime() ;
			assertEquals(newDate,sysDate) ;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			fail("Test data is wrong") ;
		}
		try {
			//Test change name
			sys.edit(p.getId(), false, EditOptions.NAME, newName);
			String actualName = sys.get(p.getId(), false).getName() ;
			assertEquals(newName,actualName) ;
			//Test change age  
			sys.edit(p.getId(), false, EditOptions.AGE, newAge);
			int actualAge = sys.get(p.getId(), false).getAge() ;
			assertEquals(100,actualAge) ;
		} 
			catch(Exception e) { 
				
			}
	}
	//Test edit all	
	@Test 
	void testEditAll() { 
		Patient p = new Patient("out of ChrisRock",42,new Date()); 
		try {
			sys.add(p) ;
		} catch (PatientExist e) {
		}
		//With invalid data 
		String f1= "Foo/Bar foo bar";
		String f2= "Foo/Bar foo";
		assertThrows(InvalidDataFormat.class,()-> {
			sys.edit(p.getId(), false, EditOptions.ALL,f1);
			sys.edit(p.getId(), false, EditOptions.ALL,f2);
		}) ;
		//With valid data 
		try { 
			String s1 = "Jane 23 01-03-1997";
			sys.edit(p.getId(), false, EditOptions.ALL,s1);
			Date expDate;
			SimpleDateFormat formatter = new SimpleDateFormat(Controller.DATE_FORMAT) ;
			expDate= formatter.parse("01-03-1997") ;
			assertEquals(expDate,p.getIntakeTime()) ;
			String expName = "Jane" ;
			boolean validN = expName.equals(p.getName()) ;
			assertEquals(true,validN) ;
			//Test if id change 
			String expId = p.getId() ;
			try {
				Patient x =sys.get(expId, false) ;
				String sysId =x.getId() ;
				assertEquals(true,x.getId().equals(sysId)) ;
			} catch(Exception e) {
				fail("Id dont exist") ;
			}
			
		} catch(Exception e) {
			fail("Wrong data");
		}
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
		//An example input data from a file 
		//We use / in the name to stop Tokenizer 
		String[]  t = {"James/Smith a","42","03-04-2021"} ;
		String[]  x= {"James/Smith b","43","03-04-2021"} ;
		String[]  z = {"James/Smith","44","03-04-2021"} ;
		String id="" ;
		try {
			id = sys.add(t);
			id = sys.add(x);
			id = sys.add(z);
		} catch (Exception e) {
			fail() ;
		}
		Patient p;
		try {
			p = sys.get(id,false);
			assertEquals(3,sys.count()) ;
			assertEquals(44,p.getAge()) ;
			boolean nameEq= p.getName().equals("James Smith") ;
			assertEquals(true, nameEq) ;
		} catch (PatientNotFound e) {}
	}
	@Test
	void processPatientStringInvalid() {
		String[]  z = {"42","Bef","03-04-2021"} ;
		assertThrows(InvalidDataFormat.class,()->{ 
			sys.add(z) ;
		}) ;
	}
}
