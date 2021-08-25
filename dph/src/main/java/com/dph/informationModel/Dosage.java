package com.dph.informationModel;

public class Dosage {
	private Drug drug;
	private double dosage;

	public Dosage(Drug drug, double dosage) {
		if (drug == null) {
			throw new IllegalArgumentException("Dosage drug entry cannot be null.");
		}
		if (dosage <= 0) {
			throw new IllegalArgumentException("Dosage value entry must be positive.");
		}
		this.drug = drug;
		this.dosage = dosage;
	}

	public String getCode() {
		return this.drug.getCode();
	}

	public Drug getDrug() {
		return drug;
	}

	public double getDrugDosage() {
		return this.dosage;
	}

	@Override
	public boolean equals(Object obj) {
		if (Dosage.class.isInstance(obj)) {
			Dosage dosage = (Dosage) obj;
			if (dosage.getDrug().equals(this.drug) && Double.compare(this.dosage,dosage.getDrugDosage())==0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean setDosage(double dosage) {
		if (dosage <= 0) {
			throw new IllegalArgumentException("Dosage value entry must be positive.");
		} else {
			this.dosage = dosage;
			return true;
		}

	}
}
