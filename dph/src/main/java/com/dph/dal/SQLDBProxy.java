package com.dph.dal;

import java.io.IOException;
import java.util.List;

import com.dph.informationModel.Condition;
import com.dph.informationModel.Drug;

public class SQLDBProxy implements DBProxy {
  
	@Override
	public List<Condition> findAllConditions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Drug> findAllDrugs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Condition findConditionById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Drug findDrugById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Condition condition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(Drug drug) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCondition(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteDrug(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateDrug(Drug updatedDrug) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateCondition(Condition updatedCondition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasDrugById(String string) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasConditionById(String string) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateDatabase(List<Condition> model) throws IOException {
		// TODO Auto-generated method stub
		
	}
}
