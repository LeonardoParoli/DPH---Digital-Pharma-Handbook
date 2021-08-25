package com.dph.dal;

import java.io.IOException;
import java.util.List;
import java.util.function.IntPredicate;

import com.dph.informationModel.Condition;
import com.dph.informationModel.Drug;

public interface DBProxy {
	public static String DB_NAME = "PharmaHandbook";
	public static String DB_CONDITIONS_COLLECTION_NAME = "Conditions";
	public static String DB_DRUGS_COLLECTION_NAME = "Drugs";
	
	public List<Condition> findAllConditions() throws IOException;
	public List<Drug> findAllDrugs();
	
	public Condition findConditionById(String id) throws IOException;
	public Drug findDrugById(String id)throws IOException;

	public void save(Condition condition) throws IOException;
	public void save(Drug drug)throws IOException;
	
	public void deleteCondition(String id)throws IOException;
	public void deleteDrug(String id)throws IOException;
	public void updateDrug(Drug updatedDrug)throws IOException;
	public void updateCondition(Condition updatedCondition)throws IOException;
	public boolean hasDrugById(String string);
	public boolean hasConditionById(String string);
}
