package org.matsim.run.carFreeZone.identifyAgentGroup;

import java.io.*;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.population.io.PopulationReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.geometry.geotools.MGC;
import org.matsim.core.utils.gis.ShapeFileReader;
import org.opengis.feature.simple.SimpleFeature;
import java.util.*;

public class IdentifyAgentInBerlin {
    public static void main(String[] args) throws IOException {
        //read txt file
        // agentsDoingEducationIDsList.txt
        BufferedReader agentsDoingEducationIDsListReader = new BufferedReader(new FileReader("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/agentsDoingEducationIDsList.txt"));
        ArrayList<String> agentsDoingEducationIDsList = new ArrayList<>();
        while (true){
            String s = agentsDoingEducationIDsListReader.readLine();
            if(s==null){
                break;
            }
            agentsDoingEducationIDsList.add(s);
        }
        agentsDoingEducationIDsListReader.close();
        System.out.println(agentsDoingEducationIDsList.size());

        // agentsDoingOtherActivitiesIDsList.txt
        BufferedReader agentsDoingOtherActivitiesIDsListReader = new BufferedReader(new FileReader("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/agentsDoingOtherActivitiesIDsList.txt"));
        ArrayList<String> agentsDoingOtherActivitiesIDsList = new ArrayList<>();
        while (true){
            String s = agentsDoingOtherActivitiesIDsListReader.readLine();
            if(s==null){
                break;
            }
            agentsDoingOtherActivitiesIDsList.add(s);
        }
        agentsDoingOtherActivitiesIDsListReader.close();
        System.out.println(agentsDoingOtherActivitiesIDsList.size());

        // agentsPassingThroughIDsList.txt
        BufferedReader agentsPassingThroughIDsListReader = new BufferedReader(new FileReader("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/agentsPassingThroughIDsList.txt"));
        ArrayList<String> agentsPassingThroughIDsList = new ArrayList<>();
        while (true){
            String s = agentsPassingThroughIDsListReader.readLine();
            if(s==null){
                break;
            }
            agentsPassingThroughIDsList.add(s);
        }
        agentsPassingThroughIDsListReader.close();
        System.out.println(agentsPassingThroughIDsList.size());

        // agentsWithoutActivitiesIDsList.txt
        BufferedReader agentsWithoutActivitiesIDsListReader = new BufferedReader(new FileReader("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/agentsWithoutActivitiesIDsList.txt"));
        ArrayList<String> agentsWithoutActivitiesIDsList = new ArrayList<>();
        while (true){
            String s = agentsWithoutActivitiesIDsListReader.readLine();
            if(s==null){
                break;
            }
            agentsWithoutActivitiesIDsList.add(s);
        }
        agentsWithoutActivitiesIDsListReader.close();
        System.out.println(agentsWithoutActivitiesIDsList.size());

        // allAgentIDsList.txt
        BufferedReader allAgentIDsListReader = new BufferedReader(new FileReader("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/allAgentIDsList.txt"));
        ArrayList<String> allAgentIDsList = new ArrayList<>();
        while (true){
            String s = allAgentIDsListReader.readLine();
            if(s==null){
                break;
            }
            allAgentIDsList.add(s);
        }
        allAgentIDsListReader.close();
        System.out.println(allAgentIDsList.size());

        // nonAffectedAgentsIDsList.txt
        BufferedReader nonAffectedAgentsIDsListReader = new BufferedReader(new FileReader("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/nonAffectedAgentsIDsList.txt"));
        ArrayList<String> nonAffectedAgentsIDsList = new ArrayList<>();
        while (true){
            String s = nonAffectedAgentsIDsListReader.readLine();
            if(s==null){
                break;
            }
            nonAffectedAgentsIDsList.add(s);
        }
        nonAffectedAgentsIDsListReader.close();
        System.out.println(nonAffectedAgentsIDsList.size());

        // personInternalIDsList.txt
        BufferedReader personInternalIDsListReader = new BufferedReader(new FileReader("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/personInternalIDsList.txt"));
        ArrayList<String> personInternalIDsList = new ArrayList<>();
        while (true){
            String s = personInternalIDsListReader.readLine();
            if(s==null){
                break;
            }
            personInternalIDsList.add(s);
        }
        personInternalIDsListReader.close();
        System.out.println(personInternalIDsList.size());

        // workerIDsList.txt
        BufferedReader workerIDsListReader = new BufferedReader(new FileReader("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/workerIDsList.txt"));
        ArrayList<String> workerIDsList = new ArrayList<>();
        while (true){
            String s = workerIDsListReader.readLine();
            if(s==null){
                break;
            }
            workerIDsList.add(s);
        }
        workerIDsListReader.close();
        System.out.println(workerIDsList.size());

        //identify agent in Berlin
        String areaShapeFile = "scenarios/berlin-v5.5-1pct/DRZ/shapefile_Biao/berlin-shp/berlin.shp";
        // Store relevant area of city as geometry
        Collection<SimpleFeature> features = (new ShapeFileReader()).readFileAndInitialize(areaShapeFile);
        Map<String, Geometry> zoneGeometries = new HashMap<>();
        for (SimpleFeature feature : features) {
            zoneGeometries.put((String) feature.getAttribute("SCHLUESSEL"), (Geometry) feature.getDefaultGeometry());
        }
        Geometry areaGeometry = zoneGeometries.get("010113");

        String plansInputFile = "scenarios/berlin-v5.5-1pct/DRZ/input/plans-modified-carInternal.xml.gz";
        // Get population
        Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        PopulationReader populationReader = new PopulationReader(scenario);
        populationReader.readFile(plansInputFile);

        // save the agent id in the shape file in the txt file
        List<Id> berlinAgentList = new ArrayList<>();
        for (Person person : scenario.getPopulation().getPersons().values()) {
            Activity homeActivity = (Activity) person.getPlans().get(0).getPlanElements().get(0);
            Point homeActAsPoint = MGC.xy2Point(homeActivity.getCoord().getX(), homeActivity.getCoord().getY());
            if (areaGeometry.contains(homeActAsPoint)) {
                berlinAgentList.add(person.getId());
            }
        }

        //check if the agent is in the berlinAgentList for all agent groups
        //agentsDoingEducationIDsList
        List<Id> agentsDoingEducationIDsListInBerlin = new ArrayList<>();
        for (String agentID : agentsDoingEducationIDsList) {
            Id agentId = Id.createPersonId(agentID);
            if (berlinAgentList.contains(agentId)) {
                agentsDoingEducationIDsListInBerlin.add(agentId);
            }
        }
        System.out.println("agentsDoingEducationIDsListInBerlin: " + agentsDoingEducationIDsListInBerlin.size());

        //agentsDoingOtherActivitiesIDsList
        List<Id> agentsDoingOtherActivitiesIDsListInBerlin = new ArrayList<>();
        for (String agentID : agentsDoingOtherActivitiesIDsList) {
            Id agentId = Id.createPersonId(agentID);
            if (berlinAgentList.contains(agentId)) {
                agentsDoingOtherActivitiesIDsListInBerlin.add(agentId);
            }
        }
        System.out.println("agentsDoingOtherActivitiesIDsListInBerlin: " + agentsDoingOtherActivitiesIDsListInBerlin.size());

        //agentsPassingThroughIDsList
        List<Id> agentsPassingThroughIDsListInBerlin = new ArrayList<>();
        for (String agentID : agentsPassingThroughIDsList) {
            Id agentId = Id.createPersonId(agentID);
            if (berlinAgentList.contains(agentId)) {
                agentsPassingThroughIDsListInBerlin.add(agentId);
            }
        }
        System.out.println("agentsPassingThroughIDsListInBerlin: " + agentsPassingThroughIDsListInBerlin.size());

        //agentsWithoutActivitiesIDsList
        List<Id> agentsWithoutActivitiesIDsListInBerlin = new ArrayList<>();
        for (String agentID : agentsWithoutActivitiesIDsList) {
            Id agentId = Id.createPersonId(agentID);
            if (berlinAgentList.contains(agentId)) {
                agentsWithoutActivitiesIDsListInBerlin.add(agentId);
            }
        }
        System.out.println("agentsWithoutActivitiesIDsListInBerlin: " + agentsWithoutActivitiesIDsListInBerlin.size());

        //allAgentIDsList
        List<Id> allAgentIDsListInBerlin = new ArrayList<>();
        for (String agentID : allAgentIDsList) {
            Id agentId = Id.createPersonId(agentID);
            if (berlinAgentList.contains(agentId)) {
                allAgentIDsListInBerlin.add(agentId);
            }
        }
        System.out.println("allAgentIDsListInBerlin: " + allAgentIDsListInBerlin.size());

        //nonAffectedAgentsIDsList
        List<Id> nonAffectedAgentsIDsListInBerlin = new ArrayList<>();
        for (String agentID : nonAffectedAgentsIDsList) {
            Id agentId = Id.createPersonId(agentID);
            if (berlinAgentList.contains(agentId)) {
                nonAffectedAgentsIDsListInBerlin.add(agentId);
            }
        }
        System.out.println("nonAffectedAgentsIDsListInBerlin: " + nonAffectedAgentsIDsListInBerlin.size());

        //personInternalIDsList
        List<Id> personInternalIDsListInBerlin = new ArrayList<>();
        for (String agentID : personInternalIDsList) {
            Id agentId = Id.createPersonId(agentID);
            if (berlinAgentList.contains(agentId)) {
                personInternalIDsListInBerlin.add(agentId);
            }
        }
        System.out.println("personInternalIDsListInBerlin: " + personInternalIDsListInBerlin.size());

        //workerIDsList
        List<Id> workerIDsListInBerlin = new ArrayList<>();
        for (String agentID : workerIDsList) {
            Id agentId = Id.createPersonId(agentID);
            if (berlinAgentList.contains(agentId)) {
                workerIDsListInBerlin.add(agentId);
            }
        }
        System.out.println("workerIDsListInBerlin: " + workerIDsListInBerlin.size());

        //write agent ids of different agent group (in Berlin) to txt file
        //agentsDoingEducationIDsListInBerlin
        BufferedWriter writer1 = new BufferedWriter(new FileWriter("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/berlinAgents/agentsDoingEducationIDsList.txt"));
        for (Id a : agentsDoingEducationIDsListInBerlin) {
            writer1.write(a.toString()+ "\n");
        }
        writer1.close();

        //agentsDoingOtherActivitiesIDsListInBerlin
        BufferedWriter writer2 = new BufferedWriter(new FileWriter("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/berlinAgents/agentsDoingOtherActivitiesIDsList.txt"));
        for (Id a : agentsDoingOtherActivitiesIDsListInBerlin) {
            writer2.write(a.toString()+ "\n");
        }
        writer2.close();

        //agentsPassingThroughIDsListInBerlin
        BufferedWriter writer3 = new BufferedWriter(new FileWriter("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/berlinAgents/agentsPassingThroughIDsList.txt"));
        for (Id a : agentsPassingThroughIDsListInBerlin) {
            writer3.write(a.toString()+ "\n");
        }
        writer3.close();

        //agentsWithoutActivitiesIDsListInBerlin
        BufferedWriter writer4 = new BufferedWriter(new FileWriter("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/berlinAgents/agentsWithoutActivitiesIDsList.txt"));
        for (Id a : agentsWithoutActivitiesIDsListInBerlin) {
            writer4.write(a.toString()+ "\n");
        }
        writer4.close();

        //allAgentIDsListInBerlin
        BufferedWriter writer5 = new BufferedWriter(new FileWriter("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/berlinAgents/allAgentIDsList.txt"));
        for (Id a : allAgentIDsListInBerlin) {
            writer5.write(a.toString()+ "\n");
        }
        writer5.close();

        //nonAffectedAgentsIDsListInBerlin
        BufferedWriter writer6 = new BufferedWriter(new FileWriter("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/berlinAgents/nonAffectedAgentsIDsList.txt"));
        for (Id a : nonAffectedAgentsIDsListInBerlin) {
            writer6.write(a.toString()+ "\n");
        }
        writer6.close();

        //personInternalIDsListInBerlin
        BufferedWriter writer7 = new BufferedWriter(new FileWriter("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/berlinAgents/personInternalIDsList.txt"));
        for (Id a : personInternalIDsListInBerlin) {
            writer7.write(a.toString()+ "\n");
        }
        writer7.close();

        //workerIDsListInBerlin
        BufferedWriter writer8 = new BufferedWriter(new FileWriter("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/berlinAgents/workerIDsList.txt"));
        for (Id a : workerIDsListInBerlin) {
            writer8.write(a.toString()+ "\n");
        }
        writer8.close();

    }
}
