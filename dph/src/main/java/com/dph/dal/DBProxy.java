package com.dph.dal;

import java.util.List;

import com.dph.informationModel.Condition;
import com.dph.informationModel.Drug;

public interface DBProxy {
	public static String DB_NAME = "PharmaHandbook";
	public static String DB_CONDITIONS_COLLECTION_NAME = "Conditions";
	public static String DB_DRUGS_COLLECTION_NAME = "Drugs";
	
	public List<Condition> findAllConditions();
	public List<Drug> findAllDrugs();
	
	public Condition findConditionById(String id);
	public Drug findDrugById(String id);

	public void save(Condition condition);
	public void save(Drug drug);
	
	public void deleteCondition(String id);
	public void deleteDrug(String id);
	public void updateDrug(Drug updatedDrug);
	public void updateCondition(Condition updatedCondition);
}
