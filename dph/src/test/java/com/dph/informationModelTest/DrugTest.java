package com.dph.informationModelTest;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import com.dph.informationModel.Drug;

import static org.assertj.core.api.Assertions.*;

public class DrugTest {

	private Drug drug;
	private String code="00001TestCode";
	private String name="Test Name";
	private String description="Test description";
	
	@Before
	public void setup() {
		drug = new Drug(code,name,description);
	}
	
	@Test
	public void instancedDrugCannotBeInconsistentTest() {
		assertThat(drug.getCode()).isNotNull().isInstanceOf(String.class).isNotBlank().isNotEmpty();
		assertThat(drug.getName()).isNotNull().isInstanceOf(String.class).isNotBlank().isNotEmpty();
		assertThat(drug.getDescription()).isNotNull().isInstanceOf(String.class).isNotBlank().isNotEmpty();
	}
	
	@Test 
	public void drugCannotBeConstructedWithBlankCodeTest() {
		assertThatThrownBy(() -> {
			@SuppressWarnings("unused")
			Drug badDrug = new Drug("   ",name,description);
		}).isInstanceOf(IllegalArgumentException.class)
		  .hasMessageContaining("Cannot create new Drug entry with blank or empty Code");
	} 
	
	@Test 
	public void drugCannotBeConstructedWithEmptyCodeTest() {
		assertThatThrownBy(() -> {
			@SuppressWarnings("unused")
			Drug badDrug = new Drug("",name,description);
		}).isInstanceOf(IllegalArgumentException.class)
		  .hasMessageContaining("Cannot create new Drug entry with blank or empty Code");
	} 
	
	@Test 
	public void drugCannotBeConstructedWithBlankNameTest() {
		assertThatThrownBy(() -> {
			@SuppressWarnings("unused")
			Drug badDrug = new Drug(code,"   ",description);
		}).isInstanceOf(IllegalArgumentException.class)
		  .hasMessageContaining("Cannot create new Drug entry with blank or empty Name");
	} 
	
	@Test 
	public void drugCannotBeConstructedWithEmptyNameTest() {
		assertThatThrownBy(() -> {
			@SuppressWarnings("unused")
			Drug badDrug = new Drug(code,"",description);
		}).isInstanceOf(IllegalArgumentException.class)
		  .hasMessageContaining("Cannot create new Drug entry with blank or empty Name");
	} 
	
	@Test
	public void drugCannotBeConstructedWithBlankDescriptionTest() {
		assertThatThrownBy(() -> {
			@SuppressWarnings("unused")
			Drug badDrug = new Drug(code,name,"   ");
		}).isInstanceOf(IllegalArgumentException.class)
		  .hasMessageContaining("Cannot create new Drug entry with blank or empty Description");
	} 
	
	@Test
	public void drugCannotBeConstructedWithEmptyDescriptionTest() {
		assertThatThrownBy(() -> {
			@SuppressWarnings("unused")
			Drug badDrug = new Drug(code,name,"");
		}).isInstanceOf(IllegalArgumentException.class)
		  .hasMessageContaining("Cannot create new Drug entry with blank or empty Description");
	} 
	
	@Test
	public void drugDescriptionCannotBeSetToBlankTest() {
		assertThatThrownBy(() -> {
			drug.setDescription("   ");
		}).isInstanceOf(IllegalArgumentException.class)
		  .hasMessageContaining("Cannot modify Drug description to blank or empty");
	}
	
	@Test
	public void drugDescriptionCannotBeSetToEmptyTest() {
		assertThatThrownBy(() -> {
			drug.setDescription("");
		}).isInstanceOf(IllegalArgumentException.class)
		  .hasMessageContaining("Cannot modify Drug description to blank or empty");
	}
	
	@Test
	public void drugDescriptionShouldReturnTrueTest() {
		assertThat(drug.setDescription(description)).isNotNull().isInstanceOf(Boolean.class).isTrue();
	}
	
	@Test
	public void drugGetCodeTest() {
		assertThat(drug.getCode()).isNotNull().isInstanceOf(String.class).isNotBlank().isNotEmpty();
	}
	
	@Test
	public void drugGetNameTest() {
		assertThat(drug.getName()).isNotNull().isInstanceOf(String.class).isNotBlank().isNotEmpty();
	}
	
	@Test
	public void drugGetDescriptionTest() {
		assertThat(drug.getDescription()).isNotNull().isInstanceOf(String.class).isNotBlank().isNotEmpty();
	}
	
	@Test
	public void DrugEqualsSuccessfullTest() {
		Drug otherDrug = new Drug(code,name,description);
		assertThat(otherDrug.equals(drug)).isNotNull().isInstanceOf(Boolean.class).isTrue();
	}
	
	@Test
	public void DrugEqualsFailByDifferentCodeTest() {
		String differentCode = "differentTestCode";
		Drug otherDrug = new Drug(differentCode,name,description);
		assertThat(otherDrug.equals(drug)).isNotNull().isInstanceOf(Boolean.class).isFalse();
	}
	
	@Test
	public void DrugEqualsFailByDifferentNameTest() {
		String differentName = "DifferentTestName";
		Drug otherDrug = new Drug(code,differentName,description);
		assertThat(otherDrug.equals(drug)).isNotNull().isInstanceOf(Boolean.class).isFalse();
	}
	
	@Test
	public void DrugEqualsFailByDifferentDescriptionTest() {
		String differentDescription = "DifferentTestDescription";
		Drug otherDrug = new Drug(code,name,differentDescription);
		assertThat(otherDrug.equals(drug)).isNotNull().isInstanceOf(Boolean.class).isFalse();
	}
	
	@After
	public void teardown() {
		drug=null;
	}
}
