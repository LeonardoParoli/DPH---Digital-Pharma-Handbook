package com.dph.informationModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Condition {
	
	private String code;
	private String name;
	private HashMap<String,Dosage> dosageList;
	
	public Condition(String code, String name, List<Dosage> dosageList) {
		if(code.isBlank() || code.isEmpty()) {
			throw new IllegalArgumentException("Cannot create new Condition entry with blank or empty Code.");
		}
		if(name.isBlank() || name.isEmpty()) {
			throw new IllegalArgumentException("Cannot create new Condition entry with blank or empty Name.");
		}
		List<String> codeList = new ArrayList<String>();
		for(Dosage dosage: dosageList ) {
			codeList.add(dosage.getCode());
		}
		Set<String> set = new HashSet<String>(codeList);
		if(codeList.size() > set.size()) {
			throw new IllegalArgumentException("Cannot create new Condition entry with duplicate entries in associated Drug list.");
		}
		HashMap<String,Dosage> pairedMap = new HashMap<String,Dosage>();
		for(Dosage dosage : dosageList) {
			pairedMap.put(dosage.getCode(),dosage);
		}
		this.code= code;
		this.name=name;
		this.dosageList = pairedMap;
	}
	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean hasDrug(String code) {
		if(dosageList.containsKey(code)) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean addDrug(Drug drug, double dosage) {
		if(this.hasDrug(drug.getCode())) {
			throw new IllegalArgumentException("Cannot add a drug with code already existing in the condition's list.");
		}
		dosageList.put(drug.getCode(), new Dosage(drug,dosage));
		return true;
	}
	
	public Dosage removeDrug(String code) {
		if(this.hasDrug(code)) {
			return dosageList.remove(code);
		}else {
			throw new IllegalArgumentException("Cannot remove unexisting drug entry.");
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(Condition.class.isInstance(obj)) {
			Condition condition = (Condition) obj;
			if(this.code.equals(condition.getCode())
					&& this.name.equals(condition.getName())) {
				Collection<Dosage> dosages = condition.getDosageList().values();
				if(dosages.isEmpty() && !this.dosageList.isEmpty()) {
					return false;
				}
				for(Dosage dosage : dosages) {
					if(this.dosageList.containsKey(dosage.getCode())){
						if(!dosageList.get(dosage.getCode()).equals(dosage)) {
							return false;
						}
					}else{
						return false;
					}
				}
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	public HashMap<String,Dosage> getDosageList() {
		return this.dosageList;
	}
}
