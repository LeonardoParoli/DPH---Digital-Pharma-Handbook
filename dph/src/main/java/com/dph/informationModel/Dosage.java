package com.dph.informationModel;

import java.util.Objects;

public class Dosage {
	private Drug drug;
	private double drugDosage;

	public Dosage(Drug drug, double dosage) {
		if (drug == null) {
			throw new IllegalArgumentException("Dosage drug entry cannot be null.");
		}
		if (dosage <= 0) {
			throw new IllegalArgumentException("Dosage value entry must be positive.");
		}
		this.drug = drug;
		this.drugDosage = dosage;
	}

	public String getCode() {
		return this.drug.getCode();
	}

	public Drug getDrug() {
		return drug;
	}

	public double getDrugDosage() {
		return this.drugDosage;
	}

	@Override
	public int hashCode() {
		return Objects.hash(drug, drugDosage);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Dosage)) {
			return false;
		}
		Dosage other = (Dosage) obj;
		return Objects.equals(drug, other.drug)
				&& Double.doubleToLongBits(drugDosage) == Double.doubleToLongBits(other.drugDosage);
	}

	public boolean setDosage(double dosage) {
		if (dosage <= 0) {
			throw new IllegalArgumentException("Dosage value entry must be positive.");
		} else {
			this.drugDosage = dosage;
			return true;
		}

	}
}
