package com.dph.dal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
		List<Document> dosages = (List<Document>) document.get("dosageList");
		List<Dosage> dsgList = new ArrayList<>();
		for (Document dosage : dosages) {
			Document drugInfo = drugCollection.find(Filters.eq("code", dosage.get("drugCode"))).first();
			if (drugInfo == null) {
				throw new IOException("Database doesn't have the required drug entry.");
			}
			dsgList.add(new Dosage(
					new Drug("" + drugInfo.get("code"), "" + drugInfo.get("name"), "" + drugInfo.get("description")),
					(double) dosage.get("dosage")));
		}
		return new Condition("" + document.get("code"), "" + document.get("name"), dsgList);
	}

	@Override
	public List<Drug> findAllDrugs() {
		return StreamSupport.stream(drugCollection.find().spliterator(), false).map(this::fromDocumentToDrug)
				.collect(Collectors.toList());
	}

	private Drug fromDocumentToDrug(Document d) {
		return new Drug("" + d.get("code"), "" + d.get("name"), "" + d.get("description"));
	}

	@Override
	public boolean hasConditionById(String id) {
		return conditionCollection.find(Filters.eq("code", id)).first() == null;
		
	}

	@Override
	public boolean hasDrugById(String id) {
		return drugCollection.find(Filters.eq("code", id)).first() == null;
	}

	@Override
	public Condition findConditionById(String id) throws IOException {
		if (this.hasConditionById(id)) {
			return fromDocumentToCondition(conditionCollection.find(Filters.eq("code", id)).first());
		} else {
			throw new IOException("Database contains no Condition entry with given ID.");
		}
	}

	@Override
	public Drug findDrugById(String id) throws IOException {
		if (this.hasDrugById(id)) {
			return fromDocumentToDrug(drugCollection.find(Filters.eq("code", id)).first());
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
						new Document().append("drugCode", dosage.getCode()).append("dosage", dosage.getDrugDosage()));
			}
			conditionCollection.insertOne(new Document().append("code", condition.getCode())
					.append("name", condition.getName()).append("dosageList", dosages));
		} else {
			throw new IOException("Database already contains a Condition with given code.");
		}
	}

	@Override
	public void save(Drug drug) throws IOException {
		if (!this.hasDrugById(drug.getCode())) {
			drugCollection.insertOne(new Document().append("code", drug.getCode()).append("name", drug.getName())
					.append("description", drug.getDescription()));
		} else {
			throw new IOException("Database already contains a Drug entry with given code.");
		}
	}

	@Override
	public void deleteCondition(String id) throws IOException {
		if (this.hasConditionById(id)) {
			conditionCollection.deleteOne(Filters.eq("code", id));
		} else {
			throw new IOException("Given Condition code doesn't match any existing entry.");
		}

	}

	@Override
	public void deleteDrug(String id) throws IOException {
		if (this.hasDrugById(id)) {
			drugCollection.deleteOne(Filters.eq("code", id));
		} else {
			throw new IOException("Given Drug code doesn't match any existing entry.");
		}

	}

	@Override
	public void updateDrug(Drug updatedDrug) throws IOException {
		if (this.hasDrugById(updatedDrug.getCode())) {
			drugCollection.replaceOne(Filters.eq("code", updatedDrug.getCode()),
					new Document().append("code", updatedDrug.getCode()).append("name", updatedDrug.getName())
							.append("description", updatedDrug.getDescription()));
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
						new Document().append("drugCode", dosage.getCode()).append("dosage", dosage.getDrugDosage()));
			}
			conditionCollection.replaceOne(Filters.eq("code", updatedCondition.getCode()),
					new Document().append("code", updatedCondition.getCode()).append("name", updatedCondition.getName())
							.append("dosageList", dosages));
		} else {
			throw new IOException("Database contains no Condition entry with given ID.");
		}

	}

}