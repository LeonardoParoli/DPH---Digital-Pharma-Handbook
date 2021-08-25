package com.dph.informationModelTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dph.informationModel.Dosage;
import com.dph.informationModel.Drug;

import static org.assertj.core.api.Assertions.*;

public class DosageTest {
	private Dosage dosage;
	private Drug drug;
	private double volume = 1.0;

	@Before
	public void setup() {
		drug = new Drug("001", "TestDrug", "TestDrugDescription");
		volume = 1.0;
		dosage = new Dosage(drug, volume);
	}

	@Test
	public void newDosageCannotBeInconsistentTest() {
		assertThat(dosage.getDrug()).isInstanceOf(Drug.class);
		assertThat(dosage.getDrugDosage()).isInstanceOf(Double.class).isEqualTo(1.0);
	}

	@Test
	public void newDosageThrowsExceptionIfDrugIsNullTest() {
		assertThatThrownBy(() -> {
			@SuppressWarnings("unused")
			Dosage badDosage = new Dosage(null, volume);
		}).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Dosage drug entry cannot be null.");
	}

	@Test
	public void newDosageThrowsExceptionIfDosageIsNegativeOrZeroTest() {
		assertThatThrownBy(() -> {
			@SuppressWarnings("unused")
			Dosage badDosage = new Dosage(drug, -1);
		}).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Dosage value entry must be positive.");
		assertThatThrownBy(() -> {
			@SuppressWarnings("unused")
			Dosage badDosage = new Dosage(drug, 0);
		}).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Dosage value entry must be positive.");
	}

	@Test
	public void setDosageTest() {
		assertThat(dosage.getDrugDosage()).isEqualTo(1.0);
		assertThat(dosage.setDosage(2.0)).isInstanceOf(Boolean.class).isTrue();
		assertThat(dosage.getDrugDosage()).isEqualTo(2.0);
	}

	@Test
	public void setDosageFailTest() {
		assertThatThrownBy(() -> {
			dosage.setDosage(-1.0);
		}).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Dosage value entry must be positive.");
		assertThatThrownBy(() -> {
			dosage.setDosage(0.0);
		}).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Dosage value entry must be positive.");
		
	}

	@Test
	public void equalsTest() {
		Dosage sameDosage = new Dosage(drug, volume);
		assertThat(dosage.equals(dosage)).isInstanceOf(Boolean.class).isTrue();
		assertThat(dosage.hashCode() == dosage.hashCode()).isInstanceOf(Boolean.class).isTrue();
		assertThat(dosage.equals(sameDosage)).isInstanceOf(Boolean.class).isTrue();
		assertThat(dosage.hashCode() == sameDosage.hashCode()).isInstanceOf(Boolean.class).isTrue();
	}

	@Test
	public void equalsFalseByDifferentDrugTest() {
		Drug differentDrug = new Drug("002", "difDrug", "difDrugDesc");
		Dosage differentDosage = new Dosage(differentDrug, 2.0);
		assertThat(dosage.equals(differentDosage)).isInstanceOf(Boolean.class).isFalse();
		assertThat(dosage.hashCode() == differentDosage.hashCode()).isInstanceOf(Boolean.class).isFalse();
		
	}

	@Test
	public void equalsFalseByDifferentDosageTest() {
		Dosage differentDosage = new Dosage(drug, 2.0);
		assertThat(dosage.equals(differentDosage)).isInstanceOf(Boolean.class).isFalse();
		assertThat(dosage.hashCode() == differentDosage.hashCode()).isInstanceOf(Boolean.class).isFalse();
	}

	@Test
	public void equalsDifferentClassTest() {
		Drug drug = new Drug("002", "DrugNotDosage", "DrugDesc");
		assertThat(dosage.equals(drug)).isInstanceOf(Boolean.class).isFalse();
		assertThat(dosage.hashCode() == drug.hashCode()).isInstanceOf(Boolean.class).isFalse();
	}

	@After
	public void teardown() {
		drug = null;
		dosage = null;
	}
}
