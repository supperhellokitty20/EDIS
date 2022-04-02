package com.test;
import com.app.* ;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestSystemOne {
	@Test
	void addOnePatient() {
		SystemOne  sys = new SystemOne() ;
		Patient p = new Patient("Jonh Smith",42,new Date()); 
		sys.add(p);
		System.out.println(sys.count()) ;
		assertEquals(1,sys.count()) ;

	}
	@Test
	void getName() { 
		SystemOne  sys = new SystemOne() ;
		
	}

	@Test 
	void getId() { 
		
	}

}
