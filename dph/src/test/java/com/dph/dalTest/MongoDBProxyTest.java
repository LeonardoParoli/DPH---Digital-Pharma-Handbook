package com.dph.dalTest;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import static com.dph.dal.DBProxy.DB_NAME;
import static com.dph.dal.DBProxy.DB_CONDITIONS_COLLECTION_NAME;
import static com.dph.dal.DBProxy.DB_DRUGS_COLLECTION_NAME;
import com.dph.dal.DBProxy;
import com.dph.dal.MongoDBProxy;
import com.dph.informationModel.Condition;
import com.dph.informationModel.Dosage;
import com.dph.informationModel.Drug;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

public class MongoDBProxyTest {
	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	private MongoClient client;
	private MongoCollection<Document> conditionCollection;
	private MongoCollection<Document> drugCollection;
	private DBProxy proxy;
	
	@BeforeClass
	public static void setupServer() {
		server = new MongoServer(new MemoryBackend());
		serverAddress = server.bind();
	}

	@AfterClass
	public static void shutdownServer() {
		server.shutdown();
	}

	@Before
	public void setup() {
		client = new MongoClient(new ServerAddress(serverAddress));
		proxy = new MongoDBProxy(client);
		MongoDatabase database = client.getDatabase(DB_NAME);
		database.drop();
		conditionCollection = database.getCollection(DB_CONDITIONS_COLLECTION_NAME);
		drugCollection = database.getCollection(DB_DRUGS_COLLECTION_NAME);
	}
	
	@After
	public void tearDown() {
		client.getDatabase(DB_NAME).drop();
		client.close();
	}
	
	@Test
	public void findAllWhenDatabaseIsEmptyTest() throws IOException {
		assertThat(proxy.findAllConditions()).isInstanceOf(List.class).isEmpty();
		assertThat(proxy.findAllDrugs()).isInstanceOf(List.class).isEmpty();
	}
	
	@Test
	public void findAllConditionsWhenDatabaseIsNotEmptyTest() throws IOException {
		addTestDrugToDatabase("001","test1","testDesc");
		addTestDrugToDatabase("002","test2","testDesc");
		List<Dosage> dosages = new ArrayList<>();
		dosages.add(new Dosage(new Drug("001","test1","testDesc"),1.0));
		dosages.add(new Dosage(new Drug("002","test2","testDesc"),1.0));
		addTestConditionToDatabase("ConditionCode1", "ConditionName1", dosages);
		addTestConditionToDatabase("ConditionCode2", "ConditionName2", dosages);
		assertThat(proxy.findAllConditions())
			.containsExactly(
				new Condition("ConditionCode1", "ConditionName1", dosages),
				new Condition("ConditionCode2", "ConditionName2", dosages));
	}
	
	@Test
	public void findAllConditionWhenDatabaseIsNotEmptyButInconsistentTest() {
		List<Dosage> dosages = new ArrayList<>();
		dosages.add(new Dosage(new Drug("001","test1","testDesc"),1.0));
		addTestConditionToDatabase("ConditionCode1", "ConditionName1", dosages);
		assertThatThrownBy(() -> {
			proxy.findAllConditions();
		}).isInstanceOf(IOException.class)
		  .hasMessageContaining("Database doesn't have the required drug entry.");
	}
	
	@Test
	public void findAllDrugsWhenDatabaseIsNotEmptyTest() {
		addTestDrugToDatabase("001","test1","testDesc");
		addTestDrugToDatabase("002","test2","testDesc");
		assertThat(proxy.findAllDrugs())
			.containsExactly(
				new Drug("001","test1","testDesc"),
				new Drug("002","test2","testDesc"));
	}
	
	@Test
	public void findDrugByIdNotFoundTest() {
		assertThatThrownBy(() -> {
			proxy.findDrugById("001");
		}).isInstanceOf(IOException.class)
		  .hasMessageContaining("Database contains no Drug entry with given ID.");
	}
	
	@Test
	public void findConditionByIdNotFoundTest() {
		assertThatThrownBy(() -> {
			proxy.findConditionById("ConditionCode");
		}).isInstanceOf(IOException.class)
		  .hasMessageContaining("Database contains no Condition entry with given ID.");
	}

	@Test
	public void findConditionByIdFoundTest() throws IOException {
		addTestDrugToDatabase("001","test1","testDesc");
		addTestDrugToDatabase("002","test2","testDesc");
		List<Dosage> dosages = new ArrayList<>();
		dosages.add(new Dosage(new Drug("001","test1","testDesc"),1.0));
		dosages.add(new Dosage(new Drug("002","test2","testDesc"),1.0));
		addTestConditionToDatabase("ConditionCode1", "ConditionName1", dosages);
		addTestConditionToDatabase("ConditionCode2", "ConditionName2", dosages);
		assertThat(proxy.findConditionById("ConditionCode2"))
			.isEqualTo(new Condition("ConditionCode2", "ConditionName2", dosages));
	}
	
	@Test
	public void findDrugByIdFoundTest() throws IOException {
		addTestDrugToDatabase("001", "test1","testDesc");
		addTestDrugToDatabase("002", "test2","testDesc");
		assertThat(proxy.findDrugById("002"))
			.isEqualTo(new Drug("002", "test2","testDesc"));
	}
	
	@Test
	public void hasConditionByIDTest() {
		addTestDrugToDatabase("001","test1","testDesc");
		addTestDrugToDatabase("002","test2","testDesc");
		List<Dosage> dosages = new ArrayList<>();
		dosages.add(new Dosage(new Drug("001","test1","testDesc"),1.0));
		dosages.add(new Dosage(new Drug("002","test2","testDesc"),1.0));
		addTestConditionToDatabase("ConditionCode1", "ConditionName1", dosages);
		assertThat(proxy.hasConditionById("ConditionCode1")).isInstanceOf(Boolean.class).isTrue();
	}
	
	@Test
	public void hasConditionByIDFailureByMissingEntryTest() {
		assertThat(proxy.hasConditionById("ConditionCode1")).isInstanceOf(Boolean.class).isFalse();
	}
	
	@Test
	public void hasDrugByIDTest() {
		addTestDrugToDatabase("001", "test1","testDesc");
		assertThat(proxy.hasDrugById("001")).isInstanceOf(Boolean.class).isTrue();
	}
	
	@Test
	public void hasDrugByIDFailureByMissingEntryTest() {
		assertThat(proxy.hasDrugById("001")).isInstanceOf(Boolean.class).isFalse();
	}
	
	@Test
	public void saveConditionTest() throws IOException {
		addTestDrugToDatabase("001","testDrug","testDescription");
		Drug drug = new Drug("001", "testDrug","testDescription");
		Dosage dosage = new Dosage(drug,1.0);
		List<Dosage> list = new ArrayList<>();
		list.add(dosage);
		Condition condition = new Condition("C1", "TestCondition",list);
		proxy.save(condition);
		assertThat(readAllConditionsFromDatabase().size()==1).isTrue();
		Condition condition2 = readAllConditionsFromDatabase().get(0);
		assertThat(condition.equals(condition2)).isTrue();
	}
	
	@Test
	public void saveConditionFailureBySameIDTest() {
		addTestDrugToDatabase("001","test1","testDesc");
		addTestDrugToDatabase("002","test2","testDesc");
		List<Dosage> dosages = new ArrayList<>();
		dosages.add(new Dosage(new Drug("001","test1","testDesc"),1.0));
		dosages.add(new Dosage(new Drug("002","test2","testDesc"),1.0));
		addTestConditionToDatabase("ConditionCode1", "ConditionName1", dosages);
		assertThatThrownBy(() -> {
			Condition condition = new Condition("ConditionCode1", "ConditionName1", dosages);
			proxy.save(condition);
		}).isInstanceOf(IOException.class)
		  .hasMessageContaining("Database already contains a Condition with given code.");
	}
	
	@Test
	public void saveConditionFailureByUnexistingDrugEntryTest() {
		addTestDrugToDatabase("001","test1","testDesc");
		List<Dosage> dosages = new ArrayList<>();
		dosages.add(new Dosage(new Drug("001","test1","testDesc"),1.0));
		dosages.add(new Dosage(new Drug("002","test2","testDesc"),1.0));
		addTestConditionToDatabase("ConditionCode1", "ConditionName1", dosages);
		assertThatThrownBy(() -> {
			Condition condition = new Condition("ConditionCode1", "ConditionName1", dosages);
			proxy.save(condition);
		}).isInstanceOf(IOException.class)
		  .hasMessageContaining("Database doesn't have a required Drug entry.");
	}
	
	@Test
	public void saveDrugTest() throws IOException {
		Drug drug = new Drug("001", "testDrug", "testDescription");
		proxy.save(drug);
		assertThat(readAllDrugsFromDatabase())
			.containsExactly(drug);
	}
	
	@Test
	public void saveDrugFailureBySameIDTest() {
		addTestDrugToDatabase("001","test1","testDesc");
		Drug drug = new Drug("001", "testDrug", "testDescription");
		assertThatThrownBy(() -> {
			proxy.save(drug);
		}).isInstanceOf(IOException.class)
		  .hasMessageContaining("Database already contains a Drug entry with given code.");
	}
	
	@Test
	public void updateConditionTest() throws IOException {
		addTestDrugToDatabase("001","test1","testDesc");
		addTestDrugToDatabase("002","test2","testDesc");
		addTestDrugToDatabase("003","test3","testDesc");
		List<Dosage> dosages = new ArrayList<>();
		dosages.add(new Dosage(new Drug("001","test1","testDesc"),1.0));
		dosages.add(new Dosage(new Drug("002","test2","testDesc"),1.0));
		addTestConditionToDatabase("ConditionCode1", "ConditionName1", dosages);
		dosages.add(new Dosage(new Drug("003", "test3", "testDesc"),2.0));
		Condition updatedCondition = new Condition("ConditionCode1", "DifferentConditionName", dosages);
		proxy.updateCondition(updatedCondition);
	}
	
	@Test
	public void updateConditionFailureByMissingEntryTest() {
		addTestDrugToDatabase("001","test1","testDesc");
		addTestDrugToDatabase("002","test2","testDesc");
		List<Dosage> dosages = new ArrayList<>();
		dosages.add(new Dosage(new Drug("001","test1","testDesc"),1.0));
		dosages.add(new Dosage(new Drug("002","test2","testDesc"),1.0));
		Condition updatedCondition = new Condition("ConditionCode1", "DifferentConditionName", dosages);
		assertThatThrownBy(() -> {
			proxy.updateCondition(updatedCondition);
		}).isInstanceOf(IOException.class)
		  .hasMessageContaining("Database contains no Condition entry with given ID.");
	}
	
	@Test
	public void updateConditionFailureByMissingDrugEntryTest() {
		addTestDrugToDatabase("001","test1","testDesc");
		addTestDrugToDatabase("002","test2","testDesc");
		List<Dosage> dosages = new ArrayList<>();
		dosages.add(new Dosage(new Drug("001","test1","testDesc"),1.0));
		dosages.add(new Dosage(new Drug("002","test2","testDesc"),1.0));
		addTestConditionToDatabase("ConditionCode1", "ConditionName1", dosages);
		dosages.add(new Dosage(new Drug("003", "test3", "testDesc"),2.0));
		Condition updatedCondition = new Condition("ConditionCode1", "DifferentConditionName", dosages);
		assertThatThrownBy(() -> {
			proxy.updateCondition(updatedCondition);
		}).isInstanceOf(IOException.class)
		  .hasMessageContaining("Cannot find required Drug entries in the database.");
	}
	
	@Test
	public void updateDrugTest() throws IOException {
		addTestDrugToDatabase("001","test1","testDesc");
		Drug updatedDrug = new Drug("001", "test1", "UpdatedTestDesc");
		proxy.updateDrug(updatedDrug);
	}
	
	@Test
	public void updateDrugFailureByMissingEntryTest() {
		Drug updatedDrug = new Drug("001", "test1", "UpdatedTestDesc");
		assertThatThrownBy(() -> {
			proxy.updateDrug(updatedDrug);
		}).isInstanceOf(IOException.class)
		  .hasMessageContaining("Cannot find required Drug entry in the database.");
	}
	
	@Test
	public void deleteConditionTest() throws IOException {
		Drug drug = new Drug("001", "testDrug", "testDescription");
		Dosage dosage = new Dosage(drug,1.0);
		List<Dosage> list = new ArrayList<>();
		list.add(dosage);
		addTestConditionToDatabase("C1", "testCondition", list);
		proxy.deleteCondition("C1");
		assertThat(readAllConditionsFromDatabase())
			.isEmpty();
	}
	
	@Test
	public void deleteConditionFailureByMissingEntryTest() {
		assertThatThrownBy(() -> {
			proxy.deleteCondition("C1");
		}).isInstanceOf(IOException.class)
		  .hasMessageContaining("Given Condition code doesn't match any existing entry.");
	}
	
	@Test
	public void deleteDrugTest() throws IOException {
		addTestDrugToDatabase("001", "testDrug", "testDescription");
		proxy.deleteDrug("001");
		assertThat(readAllDrugsFromDatabase())
			.isEmpty();
	}
	
	@Test
	public void deleteDrugFailureByMissingEntryTest() {
		assertThatThrownBy(() -> {
			proxy.deleteDrug("001");
		}).isInstanceOf(IOException.class)
		  .hasMessageContaining("Given Drug code doesn't match any existing entry.");
	}
	
	private void addTestConditionToDatabase(String code, String name, List<Dosage> dosageList) {
		List<Document> dosages = new ArrayList<>();
		for (Dosage dosage : dosageList) {
			dosages.add(new Document()
					.append("drugCode", dosage.getCode())
					.append("dosage", dosage.getDrugDosage())
					);
		}
		conditionCollection.insertOne(
				new Document()
					.append("code", code)
					.append("name", name)
					.append("dosageList", dosages)
					);
	}
	
	private void addTestDrugToDatabase(String code, String name, String description) {
		drugCollection.insertOne(
				new Document()
				.append("code", code)
				.append("name", name)
				.append("description", description)
				);
	}

	private List<Condition> readAllConditionsFromDatabase() throws IOException {
		List<Condition> conditions = new ArrayList<>();
		for(Document document : conditionCollection.find()) {
			List<Document> dosages = (List<Document>) document.get("dosageList");
			List<Dosage> dsgList= new ArrayList<>();
			for(Document dosage : dosages) {
				Document drugInfo = drugCollection.find(Filters.eq("code",dosage.get("drugCode"))).first();
				if(drugInfo==null) {
					throw new IOException("Database doesn't have the required drug entry.");
				}
				dsgList.add(new Dosage( new Drug(""+drugInfo.get("code"), ""+drugInfo.get("name"), ""+drugInfo.get("description")),(double)dosage.get("dosage")));
			}
			Condition condition = new Condition (""+document.get("code"), "" + document.get("name"), dsgList);
			conditions.add(condition);
		}
		return conditions;
	}
	
	private List<Drug> readAllDrugsFromDatabase() {
		return StreamSupport.
			stream(drugCollection.find().spliterator(), false)
				.map(d -> new Drug(""+d.get("code"), ""+d.get("name"),""+ d.get("description")))
				.collect(Collectors.toList());
	}
}
