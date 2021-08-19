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
	private double volume=1.0;
	
	@Before
	public void setup() {
		drug = new Drug("001", "TestDrug", "TestDrugDescription");
		volume = 1.0;
		dosage = new Dosage(drug,volume);
	}
	
	@Test
	public void newDosageCannotBeInconsistentTest() {
		assertThat(dosage.getDrug()).isNotNull().isInstanceOf(Drug.class);
		assertThat(dosage.getDrugDosage()).isNotNull().isInstanceOf(Double.class).isEqualTo(1.0);
	}
	
	@Test 
	public void newDosageThrowsExceptionIfDrugIsNullTest() {
		assertThatThrownBy(() -> {
			@SuppressWarnings("unused")
			Dosage badDosage = new Dosage(null,volume);
		}).isInstanceOf(IllegalArgumentException.class)
		  .hasMessageContaining("Dosage drug entry cannot be null.");
	}	
	
	@Test
	public void newDosageThrowsExceptionIfDosageIsNegativeOrZeroTest() {
		assertThatThrownBy(() -> {
			@SuppressWarnings("unused")
			Dosage badDosage = new Dosage(drug,-1);
		}).isInstanceOf(IllegalArgumentException.class)
		  .hasMessageContaining("Dosage value entry must be positive.");
		assertThatThrownBy(() -> {
			@SuppressWarnings("unused")
			Dosage badDosage = new Dosage(drug,0);
		}).isInstanceOf(IllegalArgumentException.class)
		  .hasMessageContaining("Dosage value entry must be positive.");
	}
	
	@After
	public void teardown() {
		drug=null;
		dosage=null;
	}
}
