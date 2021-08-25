package com.dph.informationModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Condition {

	private String code;
	private String name;
	private HashMap<String, Dosage> dosageList;

	public Condition(String code, String name, List<Dosage> dosageList) {
		if (code.isBlank() || code.isEmpty()) {
			throw new IllegalArgumentException("Cannot create new Condition entry with blank or empty Code.");
		}
		if (name.isBlank() || name.isEmpty()) {
			throw new IllegalArgumentException("Cannot create new Condition entry with blank or empty Name.");
		}
		List<String> codeList = new ArrayList<String>();
		for (Dosage dosage : dosageList) {
			codeList.add(dosage.getCode());
		}
		Set<String> set = new HashSet<String>(codeList);
		if (codeList.size() > set.size()) {
			throw new IllegalArgumentException(
					"Cannot create new Condition entry with duplicate entries in associated Drug list.");
		}
		HashMap<String, Dosage> pairedMap = new HashMap<String, Dosage>();
		for (Dosage dosage : dosageList) {
			pairedMap.put(dosage.getCode(), dosage);
		}
		this.code = code;
		this.name = name;
		this.dosageList = pairedMap;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public boolean hasDrug(String code) {
		if (dosageList.containsKey(code)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean addDrug(Drug drug, double dosage) {
		if (this.hasDrug(drug.getCode())) {
			throw new IllegalArgumentException("Cannot add a drug with code already existing in the condition's list.");
		}
		dosageList.put(drug.getCode(), new Dosage(drug, dosage));
		return true;
	}

	public Dosage removeDrug(String code) {
		if (this.hasDrug(code)) {
			return dosageList.remove(code);
		} else {
			throw new IllegalArgumentException("Cannot remove unexisting drug entry.");
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, dosageList, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Condition)) {
			return false;
		}
		Condition other = (Condition) obj;
		return Objects.equals(code, other.code) && Objects.equals(dosageList, other.dosageList)
				&& Objects.equals(name, other.name);
	}

	public HashMap<String, Dosage> getDosageList() {
		return this.dosageList;
	}
}
