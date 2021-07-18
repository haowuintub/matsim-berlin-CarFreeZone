/* *********************************************************************** *
 * project: org.matsim.*												   *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
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
package org.matsim.run.carFreeZone.analysis.travelDistance;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.events.EventsManagerImpl;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;

public class RunEventsHandler {

    //Residents.
    public static Set<Id> residents = new HashSet<>();
    public static double totalKilometerTraveledByResidentsInTraffic = 0.0;

    //Workers.
    public static Set<Id> workers = new HashSet<>();
    public static double totalKilometerTraveledByWorkersInTraffic = 0.0;

    //AgentsDoingEducation.
    public static Set<Id> agentsDoingEducation = new HashSet<>();
    public static double totalKilometerTraveledByAgentsDoingEducationInTraffic = 0.0;

    //AgentsDoingOtherActivities.
    public static Set<Id> agentsDoingOtherActivities = new HashSet<>();
    public static double totalKilometerTraveledByAgentsDoingOtherActivitiesInTraffic = 0.0;

    //AgentsWithoutActivities.
    public static Set<Id> agentsWithoutActivities = new HashSet<>();
    public static double totalKilometerTraveledByAgentsWithoutActivitiesInTraffic = 0.0;

    //NonAffectedAgent.
    public static Set<Id> nonAffectedAgents = new HashSet<>();
    public static double totalKilometerTraveledByNonAffectedAgentsInTraffic = 0.0;

    public static void main(String[] args) throws IOException {

        //-------------For Input (and Output) Files-------------
        // BaseCase
        final String runDirectory = "scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-baseCase_200";
        // Plan1
//        final String runDirectory = "scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-policyCase1_200";
        // Plan2
//        final String runDirectory = "scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-policyCase2_200";
        // Plan3
//        final String runDirectory = "scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-policyCase3_200";
        //-------------For Input (and Output) Files-------------//

        String inputFile = runDirectory + "/berlin-v5.5-1pct.output_events.xml.gz";
        //String outputFile_results = runDirectory + "/analysis/travelDistance.txt";
        String outputFile_results = runDirectory + "/analysis/travelDistance.csv";




        Scanner scanner6 = new Scanner(new File("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/workerIDsList.txt"));
        while (scanner6.hasNextLine()){
            String id = scanner6.nextLine();
            workers.add(Id.createPersonId(id));
        }
        scanner6.close();

        Scanner scanner7 = new Scanner(new File("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/personInternalIDsList.txt"));
        while (scanner7.hasNextLine()){
            String id = scanner7.nextLine();
            residents.add(Id.createPersonId(id));
        }
        scanner7.close();

        Scanner scanner11 = new Scanner(new File("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/agentsDoingEducationIDsList.txt"));
        while (scanner11.hasNextLine()){
            String id = scanner11.nextLine();
            agentsDoingEducation.add(Id.createPersonId(id));
        }
        scanner11.close();

        Scanner scanner8 = new Scanner(new File("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/agentsDoingOtherActivitiesIDsList.txt"));
        while (scanner8.hasNextLine()){
            String id = scanner8.nextLine();
            agentsDoingOtherActivities.add(Id.createPersonId(id));
        }
        scanner8.close();

        Scanner scanner9 = new Scanner(new File("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/agentsWithoutActivitiesIDsList.txt"));
        while (scanner9.hasNextLine()){
            String id = scanner9.nextLine();
            agentsWithoutActivities.add(Id.createPersonId(id));
        }
        scanner9.close();

        Scanner scanner10 = new Scanner(new File("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/nonAffectedAgentsIDsList.txt"));
        while (scanner10.hasNextLine()){
            String id = scanner10.nextLine();
            nonAffectedAgents.add(Id.createPersonId(id));
        }
        scanner10.close();




        //EventsManager eventsManager = EventsUtils.createEventsManager();
        EventsManager eventsManager = new EventsManagerImpl();
        Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());

        new MatsimNetworkReader(scenario.getNetwork()).readFile("scenarios/berlin-v5.5-1pct/input/berlin-v5.5-network.xml.gz");

        CarTravelDistanceEvaluator carTravelDistanceEvaluator = new CarTravelDistanceEvaluator(scenario.getNetwork());
        eventsManager.addHandler(carTravelDistanceEvaluator);
        eventsManager.initProcessing();
        new MatsimEventsReader(eventsManager).readFile(inputFile);
        eventsManager.finishProcessing();


        for(Map.Entry<Id<Person>, Double> map : carTravelDistanceEvaluator.getTravelledDistanceMap().entrySet()){
            if(residents.contains(map.getKey())){
                totalKilometerTraveledByResidentsInTraffic += map.getValue();
            } else if(workers.contains(map.getKey())){
                totalKilometerTraveledByWorkersInTraffic += map.getValue();
            } else if(agentsDoingEducation.contains(map.getKey())){
                totalKilometerTraveledByAgentsDoingEducationInTraffic += map.getValue();
            } else if(agentsDoingOtherActivities.contains(map.getKey())){
                totalKilometerTraveledByAgentsDoingOtherActivitiesInTraffic += map.getValue();
            } else if(agentsWithoutActivities.contains(map.getKey())){
                totalKilometerTraveledByAgentsWithoutActivitiesInTraffic += map.getValue();
            } else if(nonAffectedAgents.contains(map.getKey())){
                totalKilometerTraveledByNonAffectedAgentsInTraffic += map.getValue();
            }
        }


        //writeDistancesToFile(outputFile_results);
        writeDistancesToCSV(outputFile_results);

    }

    static void writeDistancesToFile(String fileName) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        writer.write("Residents:" + "\n");
        writer.write("Number of residents: " + residents.size() + "\n");
        writer.write("Total kilometer traveled in traffic by the residents: " + totalKilometerTraveledByResidentsInTraffic + "\n");
        writer.write("Average kilometer traveled in traffic by the residens: " + totalKilometerTraveledByResidentsInTraffic / residents.size() + "\n");
        writer.write("---------------------------------------------------------------" + "\n");
        writer.write("Workers:" + "\n");
        writer.write("Number of affected workers: " + workers.size() + "\n");
        writer.write("Total kilometer traveled in traffic by the workers: " + totalKilometerTraveledByWorkersInTraffic + "\n");
        writer.write("Average kilometer traveled in traffic by the workers: " + totalKilometerTraveledByWorkersInTraffic / workers.size() + "\n");
        writer.write("---------------------------------------------------------------" + "\n");
        writer.write("AgentsDoingEducation:" + "\n");
        writer.write("Number of agentsDoingEducation: " + agentsDoingEducation.size() + "\n");
        writer.write("Total kilometer traveled in traffic by the agentsDoingEducation: " + totalKilometerTraveledByAgentsDoingEducationInTraffic + "\n");
        writer.write("Average kilometer traveled in traffic by the agentsDoingEducation: " + totalKilometerTraveledByAgentsDoingEducationInTraffic / agentsDoingEducation.size() + "\n");
        writer.write("---------------------------------------------------------------" + "\n");
        writer.write("AgentsDoingOtherActivities:" + "\n");
        writer.write("Number of agentsDoingOtherActivities: " + agentsDoingOtherActivities.size() + "\n");
        writer.write("Total kilometer traveled in traffic by the agentsDoingOtherActivities: " + totalKilometerTraveledByAgentsDoingOtherActivitiesInTraffic + "\n");
        writer.write("Average kilometer traveled in traffic by the agentsDoingOtherActivities: " + totalKilometerTraveledByAgentsDoingOtherActivitiesInTraffic / agentsDoingOtherActivities.size() + "\n");
        writer.write("---------------------------------------------------------------" + "\n");
        writer.write("AgentsWithoutActivities:" + "\n");
        writer.write("Number of agentsWithoutActivities: " + agentsWithoutActivities.size() + "\n");
        writer.write("Total kilometer traveled in traffic by the agentsWithoutActivities: " + totalKilometerTraveledByAgentsWithoutActivitiesInTraffic + "\n");
        writer.write("Average kilometer traveled in traffic by the agentsWithoutActivities: " + totalKilometerTraveledByAgentsWithoutActivitiesInTraffic / agentsWithoutActivities.size() + "\n");
        writer.write("---------------------------------------------------------------" + "\n");
        writer.write("NonAffectedAgents:" + "\n");
        writer.write("Number of nonAffectedAgent: " + nonAffectedAgents.size() + "\n");
        writer.write("Total kilometer traveled in traffic by the nonAffectedAgents: " + totalKilometerTraveledByNonAffectedAgentsInTraffic + "\n");
        writer.write("Average kilometer traveled in traffic by the nonAffectedAgents: " + totalKilometerTraveledByNonAffectedAgentsInTraffic / nonAffectedAgents.size() + "\n");
        writer.write("---------------------------------------------------------------" + "\n");

        writer.close();
    }




    static void writeDistancesToCSV(String fileName) {
        try (Writer writer = Files.newBufferedWriter(Paths.get(fileName))) {
            try (CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

                printer.print("Travel Distance");
                printer.print("Number of this agentGroup");
                printer.print("Total kilometer traveled in traffic by this agentGroup");
                printer.print("Average kilometer traveled in traffic by this agentGroup");
                printer.println();

                printer.print("residents");
                printer.print(residents.size());
                printer.print(totalKilometerTraveledByResidentsInTraffic);
                printer.print(totalKilometerTraveledByResidentsInTraffic / residents.size());
                printer.println();

                printer.print("workers");
                printer.print(workers.size());
                printer.print(totalKilometerTraveledByWorkersInTraffic);
                printer.print(totalKilometerTraveledByWorkersInTraffic / workers.size());
                printer.println();

                printer.print("agentsDoingEducation");
                printer.print(agentsDoingEducation.size());
                printer.print(totalKilometerTraveledByAgentsDoingEducationInTraffic);
                printer.print(totalKilometerTraveledByAgentsDoingEducationInTraffic / agentsDoingEducation.size());
                printer.println();

                printer.print("agentsDoingOtherActivities");
                printer.print(agentsDoingOtherActivities.size());
                printer.print(totalKilometerTraveledByAgentsDoingOtherActivitiesInTraffic);
                printer.print(totalKilometerTraveledByAgentsDoingOtherActivitiesInTraffic / agentsDoingOtherActivities.size());
                printer.println();

                printer.print("agentsWithoutActivities");
                printer.print(agentsWithoutActivities.size());
                printer.print(totalKilometerTraveledByAgentsWithoutActivitiesInTraffic);
                printer.print(totalKilometerTraveledByAgentsWithoutActivitiesInTraffic / agentsWithoutActivities.size());
                printer.println();

                printer.print("nonAffectedAgents");
                printer.print(nonAffectedAgents.size());
                printer.print(totalKilometerTraveledByNonAffectedAgentsInTraffic);
                printer.print(totalKilometerTraveledByNonAffectedAgentsInTraffic / nonAffectedAgents.size());
                printer.println();


                printer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}