package com.dph.dal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.dph.informationModel.Condition;
import com.dph.informationModel.Dosage;
import com.dph.informationModel.Drug;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class MongoDBProxy implements DBProxy {

	private MongoCollection<Document> conditionCollection;
	private MongoCollection<Document> drugCollection;
	private final static String dosageListKey = "dosageList";
	private final static String codeKey = "code";
	private final static String drugCodeKey = "drugCode";
	private final static String nameKey = "name";
	private final static String descriptionKey = "description";
	private final static String dosageKey = "dosage";
	

	public MongoDBProxy(MongoClient client) {
		conditionCollection = client.getDatabase(DB_NAME).getCollection(DB_CONDITIONS_COLLECTION_NAME);
		drugCollection = client.getDatabase(DB_NAME).getCollection(DB_DRUGS_COLLECTION_NAME);
	}

	@Override
	public List<Condition> findAllConditions() throws IOException {
		List<Condition> conditions = new ArrayList<>();
		for (Document document : conditionCollection.find()) {
			conditions.add(this.fromDocumentToCondition(document));
		}
		return conditions;
	}

	private Condition fromDocumentToCondition(Document document) throws IOException {
		@SuppressWarnings("unchecked")
		List<Document> dosages = (List<Document>) document.get(dosageListKey);
		List<Dosage> dsgList = new ArrayList<>();
		for (Document dosage : dosages) {
			Document drugInfo = drugCollection.find(Filters.eq(codeKey, dosage.get(drugCodeKey))).first();
			if (drugInfo == null) {
				throw new IOException("Database doesn't have the required drug entry.");
			}
			dsgList.add(new Dosage(
					new Drug("" + drugInfo.get(codeKey), "" + drugInfo.get(nameKey), "" + drugInfo.get(descriptionKey)),
					(double) dosage.get(dosage)));
		}
		return new Condition("" + document.get(codeKey), "" + document.get(nameKey), dsgList);
	}

	@Override
	public List<Drug> findAllDrugs() {
		return StreamSupport.stream(drugCollection.find().spliterator(), false).map(this::fromDocumentToDrug)
				.collect(Collectors.toList());
	}

	private Drug fromDocumentToDrug(Document d) {
		return new Drug("" + d.get(codeKey), "" + d.get(nameKey), "" + d.get(descriptionKey));
	}

	@Override
	public boolean hasConditionById(String id) {
		return conditionCollection.find(Filters.eq(codeKey, id)).first() != null;
	}

	@Override
	public boolean hasDrugById(String id) {
		return drugCollection.find(Filters.eq(codeKey, id)).first() != null;
	}

	@Override
	public Condition findConditionById(String id) throws IOException {
		if (this.hasConditionById(id)) {
			return fromDocumentToCondition(conditionCollection.find(Filters.eq(codeKey, id)).first());
		} else {
			throw new IOException("Database contains no Condition entry with given ID.");
		}
	}

	@Override
	public Drug findDrugById(String id) throws IOException {
		if (this.hasDrugById(id)) {
			return fromDocumentToDrug(drugCollection.find(Filters.eq(codeKey, id)).first());
		} else {
			throw new IOException("Database contains no Drug entry with given ID.");
		}
	}

	@Override
	public void save(Condition condition) throws IOException {
		for (Map.Entry<String, Dosage> entry : condition.getDosageList().entrySet()) {
			if (!this.hasDrugById(entry.getKey())) {
				throw new IOException("Database doesn't have a required Drug entry.");
			}
		}
		if (!this.hasConditionById(condition.getCode())) {
			List<Document> dosages = new ArrayList<>();
			for (Dosage dosage : condition.getDosageList().values()) {
				dosages.add(
						new Document().append(drugCodeKey, dosage.getCode()).append(dosageKey, dosage.getDrugDosage()));
			}
			conditionCollection.insertOne(new Document().append(codeKey, condition.getCode())
					.append(nameKey, condition.getName()).append(dosageListKey, dosages));
		} else {
			throw new IOException("Database already contains a Condition with given code.");
		}
	}

	@Override
	public void save(Drug drug) throws IOException {
		if (!this.hasDrugById(drug.getCode())) {
			drugCollection.insertOne(new Document().append(codeKey, drug.getCode()).append(nameKey, drug.getName())
					.append(descriptionKey, drug.getDescription()));
		} else {
			throw new IOException("Database already contains a Drug entry with given code.");
		}
	}

	@Override
	public void deleteCondition(String id) throws IOException {
		if (this.hasConditionById(id)) {
			conditionCollection.deleteOne(Filters.eq(codeKey, id));
		} else {
			throw new IOException("Given Condition code doesn't match any existing entry.");
		}

	}

	@Override
	public void deleteDrug(String id) throws IOException {
		if (this.hasDrugById(id)) {
			drugCollection.deleteOne(Filters.eq(codeKey, id));
		} else {
			throw new IOException("Given Drug code doesn't match any existing entry.");
		}

	}

	@Override
	public void updateDrug(Drug updatedDrug) throws IOException {
		if (this.hasDrugById(updatedDrug.getCode())) {
			drugCollection.replaceOne(Filters.eq(codeKey, updatedDrug.getCode()),
					new Document().append(codeKey, updatedDrug.getCode()).append(nameKey, updatedDrug.getName())
							.append(descriptionKey, updatedDrug.getDescription()));
		} else {
			throw new IOException("Cannot find required Drug entry in the database.");
		}
	}

	@Override
	public void updateCondition(Condition updatedCondition) throws IOException {
		for (Map.Entry<String, Dosage> entry : updatedCondition.getDosageList().entrySet()) {
			if (!this.hasDrugById(entry.getKey())) {
				throw new IOException("Cannot find required Drug entries in the database.");
			}
		}
		if (this.hasConditionById(updatedCondition.getCode())) {
			List<Document> dosages = new ArrayList<>();
			for (Dosage dosage : updatedCondition.getDosageList().values()) {
				dosages.add(
						new Document().append(drugCodeKey, dosage.getCode()).append(dosageKey, dosage.getDrugDosage()));
			}
			conditionCollection.replaceOne(Filters.eq(codeKey, updatedCondition.getCode()),
					new Document().append(codeKey, updatedCondition.getCode()).append(nameKey, updatedCondition.getName())
							.append(dosageListKey, dosages));
		} else {
			throw new IOException("Database contains no Condition entry with given ID.");
		}

	}

	@Override
	public void updateDatabase(List<Condition> model) throws IOException {
		List<Drug> drugList = new ArrayList<>();
		for(Condition condition : model) {
			for(Entry<String,Dosage> entry : condition.getDosageList().entrySet()) {
				Drug drug = entry.getValue().getDrug();
				boolean alreadyInserted=false;
				for(Drug d : drugList) {
					if(d.getCode().equals(drug.getCode())) {
						if(!d.getName().equals(drug.getName()) ||
								!d.getDescription().equals(drug.getDescription())) {
							throw new IOException("Given Model is inconsistent. Cannot update the database.");
						}
						alreadyInserted = true;
					}
				}
				if(!alreadyInserted) {
					drugList.add(drug);
				}
			}
		}
		for(Drug drug : drugList) {
			if(this.hasDrugById(drug.getCode())) {
				if(!this.findDrugById(drug.getCode()).equals(drug)){
					this.updateDrug(drug);
				}
			}else {
				this.save(drug);
			}
		}
		for(Condition condition : model) {
			if(this.hasConditionById(condition.getCode())) {
				if(!this.findConditionById(condition.getCode()).equals(condition)) {
					this.updateCondition(condition);
				}
			}else {
				this.save(condition);
			}
		}
	}



}