package com.dph.informationModel;

import java.util.HashMap;
import java.util.List;

public class Condition {

	public Condition(String code, String name, List<Dosage> dosageList) {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCode() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public HashMap<String,Dosage> getDosageList() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean addDrug(Drug testDrug, double dosage) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean hasDrug(String code) {
		// TODO Auto-generated method stub
		return false;
	}

	public Dosage removeDrug(String code) {
		// TODO Auto-generated method stub
		return null;
	}
}
