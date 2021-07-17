package org.matsim.run.carFreeZone.modalSplit.modalSplitUserType;
/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2017 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import org.matsim.analysis.AgentAnalysisFilter;
import org.matsim.analysis.modalSplitUserType.ModeAnalysis;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.collections.Tuple;

/**
 * @author ikaddoura
 */

public class RunBerlinModeAnalysis {

    //Residents.
    public static Set<Id> residents = new HashSet<>();
    //Workers.
    public static Set<Id> workers = new HashSet<>();
    //AgentsDoingEducation.
    public static Set<Id> agentsDoingEducation = new HashSet<>();
    //AgentsDoingOtherActivities.
    public static Set<Id> agentsDoingOtherActivities = new HashSet<>();
    //AgentsWithoutActivities.
    public static Set<Id> agentsWithoutActivities = new HashSet<>();
    //NonAffectedAgent.
    public static Set<Id> nonAffectedAgents = new HashSet<>();

    //Allagents.
    public static Set<Id> allAgents = new HashSet<>();

    public static void main(String[] args) throws FileNotFoundException {

        final String runId = "berlin-v5.5-1pct";
        final String runDirectory = "scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-policyCase1_200/";

        String outputDirectory = runDirectory + "/analysis/modalSplit/";
        String outputDirectory_;
        String outputDirectory1;
        String outputDirectory2;
        String outputDirectory3;
        String outputDirectory4;
        String outputDirectory5;
        String outputDirectory6;




        Scanner scanner6 = new Scanner(new File("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/workerIDsList.txt"));
        while (scanner6.hasNextLine()) {
            String id = scanner6.nextLine();
            workers.add(Id.createPersonId(id));
        }
        scanner6.close();

        Scanner scanner7 = new Scanner(new File("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/personInternalIDsList.txt"));
        while (scanner7.hasNextLine()) {
            String id = scanner7.nextLine();
            residents.add(Id.createPersonId(id));
        }
        scanner7.close();

        Scanner scanner11 = new Scanner(new File("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/agentsDoingEducationIDsList.txt"));
        while (scanner11.hasNextLine()) {
            String id = scanner11.nextLine();
            agentsDoingEducation.add(Id.createPersonId(id));
        }
        scanner11.close();

        Scanner scanner8 = new Scanner(new File("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/agentsDoingOtherActivitiesIDsList.txt"));
        while (scanner8.hasNextLine()) {
            String id = scanner8.nextLine();
            agentsDoingOtherActivities.add(Id.createPersonId(id));
        }
        scanner8.close();

        Scanner scanner9 = new Scanner(new File("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/agentsWithoutActivitiesIDsList.txt"));
        while (scanner9.hasNextLine()) {
            String id = scanner9.nextLine();
            agentsWithoutActivities.add(Id.createPersonId(id));
        }
        scanner9.close();

        Scanner scanner10 = new Scanner(new File("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/nonAffectedAgentsIDsList.txt"));
        while (scanner10.hasNextLine()) {
            String id = scanner10.nextLine();
            nonAffectedAgents.add(Id.createPersonId(id));
        }
        scanner10.close();




        Config config = ConfigUtils.createConfig();
        config.network().setInputFile(null);
        config.plans().setInputFile(runDirectory + "/" + runId + ".output_plans.xml.gz");
        config.controler().setRunId(runId);
        config.global().setCoordinateSystem("EPSG:31468");
        config.vehicles().setVehiclesFile(null);
        config.transit().setTransitScheduleFile(null);
        config.transit().setVehiclesFile(null);
        config.facilities().setInputFile(null);
        Scenario scenario = ScenarioUtils.loadScenario(config);
        for(Person person :scenario.getPopulation().getPersons().values()){
            allAgents.add(Id.createPersonId(person.getId().toString()));
        }
        
        Scenario scenario1 = ScenarioUtils.loadScenario(config);
        for (Id<Person> personId: allAgents) {
            if(!residents.contains(personId)){
                scenario1.getPopulation().removePerson(personId);
            }
        }

        Scenario scenario2 = ScenarioUtils.loadScenario(config);
        for (Id<Person> personId: allAgents) {
            if(!workers.contains(personId)){
                scenario2.getPopulation().removePerson(personId);
            }
        }

        Scenario scenario3 = ScenarioUtils.loadScenario(config);
        for (Id<Person> personId: allAgents) {
            if(!agentsDoingEducation.contains(personId)){
                scenario3.getPopulation().removePerson(personId);
            }
        }

        Scenario scenario4 = ScenarioUtils.loadScenario(config);
        for (Id<Person> personId: allAgents) {
            if(!agentsDoingOtherActivities.contains(personId)){
                scenario4.getPopulation().removePerson(personId);
            }
        }

        Scenario scenario5 = ScenarioUtils.loadScenario(config);
        for (Id<Person> personId: allAgents) {
            if(!agentsWithoutActivities.contains(personId)){
                scenario5.getPopulation().removePerson(personId);
            }
        }

        Scenario scenario6 = ScenarioUtils.loadScenario(config);
        for (Id<Person> personId: allAgents) {
            if(!nonAffectedAgents.contains(personId)){
                scenario6.getPopulation().removePerson(personId);
            }
        }
        

/*        AgentAnalysisFilter filter = new AgentAnalysisFilter("A");

        filter.setSubpopulation("person");

*//*        filter.setPersonAttribute("berlin");
        filter.setPersonAttributeName("home-activity-zone");*//*

		filter.setZoneFile("/Users/haowu/Workspace/QGIS/MATSim_HA2/NoCarZone_withRoundabout/NoCarZone_withRoundabout.shp");
		filter.setRelevantActivityType("home");

        filter.preProcess(scenario);*/
        
        
        //allagents
        ModeAnalysis analysis = new ModeAnalysis(scenario, null, null, new DefaultAnalysisMainModeIdentifierWithCarInternal());
        analysis.run();

        File directory = new File(outputDirectory);
        directory.mkdirs();

        outputDirectory_ = outputDirectory + "allAgents.";
        analysis.writeModeShares(outputDirectory_);

        //residents
        ModeAnalysis analysis1 = new ModeAnalysis(scenario1, null, null, new DefaultAnalysisMainModeIdentifierWithCarInternal());
        analysis1.run();

        File directory1 = new File(outputDirectory);
        directory1.mkdirs();

        outputDirectory1 = outputDirectory + "residents.";
        analysis1.writeModeShares(outputDirectory1);

        //workers
        ModeAnalysis analysis2 = new ModeAnalysis(scenario2, null, null, new DefaultAnalysisMainModeIdentifierWithCarInternal());
        analysis2.run();

        File directory2 = new File(outputDirectory);
        directory2.mkdirs();

        outputDirectory2 = outputDirectory + "workers.";
        analysis2.writeModeShares(outputDirectory2);

        //agentsDoingEducation
        ModeAnalysis analysis3 = new ModeAnalysis(scenario3, null, null, new DefaultAnalysisMainModeIdentifierWithCarInternal());
        analysis3.run();

        File directory3 = new File(outputDirectory);
        directory3.mkdirs();

        outputDirectory3 = outputDirectory + "agentsDoingEducation.";
        analysis3.writeModeShares(outputDirectory3);

        //agentsDoingOtherActivities
        ModeAnalysis analysis4 = new ModeAnalysis(scenario4, null, null, new DefaultAnalysisMainModeIdentifierWithCarInternal());
        analysis4.run();

        File directory4 = new File(outputDirectory);
        directory4.mkdirs();

        outputDirectory4 = outputDirectory + "agentsDoingOtherActivities.";
        analysis4.writeModeShares(outputDirectory4);

        //agentsWithoutActivities
        ModeAnalysis analysis5 = new ModeAnalysis(scenario5, null, null, new DefaultAnalysisMainModeIdentifierWithCarInternal());
        analysis5.run();

        File directory5 = new File(outputDirectory);
        directory5.mkdirs();

        outputDirectory5 = outputDirectory + "agentsWithoutActivities.";
        analysis5.writeModeShares(outputDirectory5);

        //nonAffectedAgents
        ModeAnalysis analysis6 = new ModeAnalysis(scenario6, null, null, new DefaultAnalysisMainModeIdentifierWithCarInternal());
        analysis6.run();

        File directory6 = new File(outputDirectory);
        directory6.mkdirs();

        outputDirectory6 = outputDirectory + "nonAffectedAgents.";
        analysis6.writeModeShares(outputDirectory6);


        System.out.println(scenario.getPopulation().getPersons().size());
        System.out.println(scenario1.getPopulation().getPersons().size());
        System.out.println(scenario2.getPopulation().getPersons().size());
        System.out.println(scenario3.getPopulation().getPersons().size());
        System.out.println(scenario4.getPopulation().getPersons().size());
        System.out.println(scenario5.getPopulation().getPersons().size());
        System.out.println(scenario6.getPopulation().getPersons().size());




        //analysis.writeTripRouteDistances(outputDirectory);
        //analysis.writeTripEuclideanDistances(outputDirectory);

/*        final List<Tuple<Double, Double>> distanceGroups = new ArrayList<>();
        distanceGroups.add(new Tuple<>(0., 1000.));
        distanceGroups.add(new Tuple<>(1000., 3000.));
        distanceGroups.add(new Tuple<>(3000., 5000.));
        distanceGroups.add(new Tuple<>(5000., 10000.));
        distanceGroups.add(new Tuple<>(10000., 20000.));
        distanceGroups.add(new Tuple<>(20000., 100000.));
        distanceGroups.add(new Tuple<>(100000., 999999999999.));
        analysis.writeTripRouteDistances(outputDirectory, distanceGroups);
        analysis.writeTripEuclideanDistances(outputDirectory, distanceGroups);*/
    }
}
