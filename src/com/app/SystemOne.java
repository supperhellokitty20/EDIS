package com.app;
import java.io.File;
import java.util.concurrent.ConcurrentNavigableMap ;
import java.util.concurrent.ConcurrentSkipListMap;
import com.app.Patient;
public class SystemOne implements Controller {
	ConcurrentNavigableMap<Integer,Patient>  map;
	public SystemOne(){
		map= new ConcurrentSkipListMap() ;
	}
	@Override
	public void remove(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(String name) {
		// TODO Auto-generated method stub

	}

	/** 
	 * Put the patient in the system  
	 * Analysis: 
	 */
	@Override
	public void add(Patient p) {
		// TODO Auto-generated method stub
		int hash = p.hashCode();
		map.putIfAbsent(hash, p) ;
	}

	@Override
	public void add(String[] data) {
		// TODO Auto-generated method stub
	}

	@Override
	public void edit(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void edit(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void load(File file) {
		// TODO Auto-generated method stub

	}

	@Override
	public void export(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void summary() {
		// TODO Auto-generated method stub

	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		return map.size();
	}
	@Override
	public boolean search(String name) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean search(int id) {
		// TODO Auto-generated method stub
		return false;
	}

}
