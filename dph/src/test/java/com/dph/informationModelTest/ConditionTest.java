package com.dph.informationModelTest;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import com.dph.informationModel.Condition;
import com.dph.informationModel.Dosage;
import com.dph.informationModel.Drug;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConditionTest {
	private String name;
	private String code;
	private Condition condition;
	private List<Dosage> dosageList;

	@Before
	public void setup() {
		name = "Test Name";
		code = "0001TestCode";
		dosageList = new ArrayList<Dosage>();
		Dosage dosageOne = new Dosage(new Drug("001", "drugOne", "drugOne description"), 1);
		Dosage dosageTwo = new Dosage(new Drug("002", "drugTwo", "drugTwo description"), 2);
		Dosage dosageThree = new Dosage(new Drug("003", "drugThree", "drugThree description"), 3);
		dosageList.add(dosageOne);
		dosageList.add(dosageTwo);
		dosageList.add(dosageThree);
		condition = new Condition(code, name, dosageList);
	}

	@Test
	public void instancedConditionMustBeConsistentAtStartTest() {
		assertThat(condition).isNotNull();
		assertThat(condition.getName()).isInstanceOf(String.class).isNotEmpty().isNotBlank();
		assertThat(condition.getCode()).isInstanceOf(String.class).isNotEmpty().isNotBlank();
		assertThat(condition.getDosageList()).isInstanceOf(HashMap.class);
		assertThat(condition.getDosageList().values()).extracting(Dosage::getCode).doesNotHaveDuplicates();
	}

	@Test
	public void createdConditionCannotHaveBlankName() {
		assertThatThrownBy(() -> {
			@SuppressWarnings("unused")
			Condition badCondition = new Condition(code, "   ", dosageList);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Cannot create new Condition entry with blank or empty Name.");
	}

	@Test
	public void createdConditionCannotHaveEmptyName() {
		assertThatThrownBy(() -> {
			@SuppressWarnings("unused")
			Condition badCondition = new Condition(code, "", dosageList);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Cannot create new Condition entry with blank or empty Name.");
	}

	@Test
	public void createdConditionCannotHaveBlankCode() {
		assertThatThrownBy(() -> {
			@SuppressWarnings("unused")
			Condition badCondition = new Condition("   ", name, dosageList);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Cannot create new Condition entry with blank or empty Code.");
	}

	@Test
	public void createdConditionCannotHaveEmptyCode() {
		assertThatThrownBy(() -> {
			@SuppressWarnings("unused")
			Condition badCondition = new Condition("", name, dosageList);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Cannot create new Condition entry with blank or empty Code.");
	}

	@Test
	public void createdConditionCannotHaveDuplicateEntriesInDrugList() {
		List<Dosage> badDosageList = new ArrayList<Dosage>();
		Dosage dosageOne = new Dosage(new Drug("001", "drugOne", "drugOne description"), 1); // same code
		Dosage dosageTwo = new Dosage(new Drug("001", "drugTwo", "drugTwo description"), 2); // same code
		Dosage dosageThree = new Dosage(new Drug("003", "drugThree", "drugThree description"), 3);
		badDosageList.add(dosageOne);
		badDosageList.add(dosageTwo);
		badDosageList.add(dosageThree);
		assertThatThrownBy(() -> {
			@SuppressWarnings("unused")
			Condition badCondition = new Condition(code, name, badDosageList);
		}).isInstanceOf(IllegalArgumentException.class).hasMessageContaining(
				"Cannot create new Condition entry with duplicate entries in associated Drug list.");
	}

	@Test
	public void getConditionNameTest() {
		(assertThat(condition.getName()).isInstanceOf(String.class)).isNotEmpty().isNotBlank();
	}

	@Test
	public void getConditionCodeTest() {
		assertThat(condition.getCode()).isInstanceOf(String.class).isNotEmpty().isNotBlank();
	}

	@Test
	public void getConditionDrugListTest() {
		assertThat(condition.getDosageList()).isInstanceOf(HashMap.class);
	}

	@Test
	public void addDrugToConditionDrugListTest() {
		Drug testDrug = new Drug("004", "Test Drug", "A test Drug");
		assertThat(condition.addDrug(testDrug, 4)).isInstanceOf(Boolean.class).isTrue();
		assertThat(condition.getDosageList().values()).extracting(Dosage::getCode)
				.anySatisfy(code -> assertThat(code).matches("004"));
	}

	@Test
	public void cannotAddDrugToConditionDrugListWithSameCodeTest() {
		Drug testBadDrug = new Drug("003", "Bad Test Drug", "A bad test Drug"); // same code, different name
		assertThatThrownBy(() -> {	
			condition.addDrug(testBadDrug, 3);
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Cannot add a drug with code already existing in the condition's list.");
	}

	@Test
	public void addDrugToConditionDrugListWithSameNameTest() {
		Drug testDrug = new Drug("004", "Test Drug", "A test Drug");
		assertThat(condition.addDrug(testDrug, 4)).isInstanceOf(Boolean.class).isTrue();
		Drug testDrug2 = new Drug("005", "Test Drug", "A test Drug"); // same name, different code
		assertThat(condition.addDrug(testDrug2, 5)).isInstanceOf(Boolean.class).isTrue();
	}

	@Test
	public void ConditionHasSpecificDrugByCodeTest() {
		assertThat(condition.hasDrug("001")).isInstanceOf(Boolean.class).isTrue();
		assertThat(condition.hasDrug("004")).isInstanceOf(Boolean.class).isFalse();
	}

	@Test
	public void removeUnexistantDrugFromConditionDrugListTest() {
		assertThatThrownBy(() -> {
			condition.removeDrug("004");
		}).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Cannot remove unexisting drug entry.");
	}

	@Test
	public void removeExistantDrugFromConditionDrugListTest() {
		assertThat(condition.getDosageList().values()).extracting(Dosage::getCode)
				.anySatisfy(code -> assertThat(code).matches("003"));
		assertThat(condition.removeDrug("003")).isInstanceOf(Dosage.class);
		assertThat(condition.getDosageList().values()).extracting(Dosage::getCode)
				.noneSatisfy(code -> assertThat(code).matches("003"));
	}

	@Test
	public void conditionEqualsSuccessfulTest() {
		Condition otherCondition = new Condition(code, name, dosageList);
		assertThat(condition.equals(condition)).isInstanceOf(Boolean.class).isTrue();
		assertThat(condition.hashCode() == condition.hashCode()).isInstanceOf(Boolean.class).isTrue();
		assertThat(condition.equals(otherCondition)).isInstanceOf(Boolean.class).isTrue();
		assertThat(condition.hashCode() == otherCondition.hashCode()).isInstanceOf(Boolean.class).isTrue();
	}

	@Test
	public void conditionEqualsFailByDifferentIdTest() {
		String differentCode = "DifferentTestCode";
		Condition otherCondition = new Condition(differentCode, name, dosageList);
		assertThat(condition.equals(otherCondition)).isInstanceOf(Boolean.class).isFalse();
		assertThat(condition.hashCode() == otherCondition.hashCode()).isInstanceOf(Boolean.class).isFalse();
	}

	@Test
	public void conditionEqualsFailByDifferentNameTest() {
		String differentName = "DifferentTestName";
		Condition otherCondition = new Condition(code, differentName, dosageList);
		assertThat(condition.equals(otherCondition)).isInstanceOf(Boolean.class).isFalse();
		assertThat(condition.hashCode() == otherCondition.hashCode()).isInstanceOf(Boolean.class).isFalse();
	}

	@Test
	public void conditionEqualsFailByDifferentDosageListTest() {
		List<Dosage> differentDosageList = new ArrayList<Dosage>();
		Dosage differentdrugOne = new Dosage(new Drug("001different", "drugOne", "drugOne description"), 1);
		Dosage drugTwo = new Dosage(new Drug("002", "drugTwo", "drugTwo description"), 2);
		Dosage drugThree = new Dosage(new Drug("003", "drugThree", "drugThree description"), 3);
		differentDosageList.add(differentdrugOne);
		differentDosageList.add(drugTwo);
		differentDosageList.add(drugThree);
		Condition otherCondition = new Condition(code, name, differentDosageList);
		assertThat(condition.equals(otherCondition)).isInstanceOf(Boolean.class).isFalse();
		assertThat(condition.hashCode() == otherCondition.hashCode()).isInstanceOf(Boolean.class).isFalse();
	}

	@Test
	public void conditionEqualsFailByDifferentSingleDosageTest() {
		List<Dosage> differentDosageList = new ArrayList<Dosage>();
		Dosage differentdrugOne = new Dosage(new Drug("001", "drugOne", "drugOne description"), 1);
		Dosage drugTwo = new Dosage(new Drug("002", "drugTwo", "drugTwo description"), 2);
		Dosage drugThree = new Dosage(new Drug("003", "drugThree", "drugThree description"), 2); // different dosage
																									// value
		differentDosageList.add(differentdrugOne);
		differentDosageList.add(drugTwo);
		differentDosageList.add(drugThree);
		Condition otherCondition = new Condition(code, name, differentDosageList);
		assertThat(condition.equals(otherCondition)).isInstanceOf(Boolean.class).isFalse();
		assertThat(condition.hashCode() == otherCondition.hashCode()).isInstanceOf(Boolean.class).isFalse();
	}

	@Test
	public void conditionEqualsFailByEmptyDosageListTest() {
		List<Dosage> differentDosageList = new ArrayList<Dosage>();
		Condition otherCondition = new Condition(code, name, differentDosageList);
		assertThat(condition.equals(otherCondition)).isInstanceOf(Boolean.class).isFalse();
		assertThat(condition.hashCode() == otherCondition.hashCode()).isInstanceOf(Boolean.class).isFalse();
	}

	@Test
	public void conditionEqualsFailBydifferentClassTest() {
		Object object = new Object();
		assertThat(condition.equals(object)).isInstanceOf(Boolean.class).isFalse();
		assertThat(condition.hashCode() == object.hashCode()).isInstanceOf(Boolean.class).isFalse();
	}

	@After
	public void teardown() {
		condition = null;
		dosageList = null;
	}
}
