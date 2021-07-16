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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

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

    public static void main(String[] args) throws IOException {

        String inputFile = "scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-baseCase_200/berlin-v5.5-1pct.output_events.xml.gz";
        String outputFile_results = "scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-baseCase_200/analysis/travelDistance.txt";




        //EventsManager eventsManager = EventsUtils.createEventsManager();
        EventsManager eventsManager = new EventsManagerImpl();
        Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());

        new MatsimNetworkReader(scenario.getNetwork()).readFile("scenarios/berlin-v5.5-1pct/input/berlin-v5.5-network.xml.gz");

        CarTravelDistanceEvaluator carTravelDistanceEvaluator = new CarTravelDistanceEvaluator(scenario.getNetwork());
        eventsManager.addHandler(carTravelDistanceEvaluator);
        eventsManager.initProcessing();
        new MatsimEventsReader(eventsManager).readFile(inputFile);
        eventsManager.finishProcessing();

        writeDistancesToFile(carTravelDistanceEvaluator.getTravelledDistanceMap(), outputFile_results);

    }

    static void writeDistancesToFile(Map<Id<Person>,Double> travelledDistance, String fileName) throws IOException {
        //Residents.
        Set<Id> residents = new HashSet<>();
        double totalKilometerTraveledByResidentsInTraffic = 0.0;

        //Workers.
        Set<Id> workers = new HashSet<>();
        double totalKilometerTraveledByWorkersInTraffic = 0.0;

        //AgentsDoingEducation.
        Set<Id> agentsDoingEducation = new HashSet<>();
        double totalKilometerTraveledByAgentsDoingEducationInTraffic = 0.0;

        //AgentsDoingOtherActivities.
        Set<Id> agentsDoingOtherActivities = new HashSet<>();
        double totalKilometerTraveledByAgentsDoingOtherActivitiesInTraffic = 0.0;

        //AgentsWithoutActivities.
        Set<Id> agentsWithoutActivities = new HashSet<>();
        double totalKilometerTraveledByAgentsWithoutActivitiesInTraffic = 0.0;

        //NonAffectedAgent.
        Set<Id> nonAffectedAgents = new HashSet<>();
        double totalKilometerTraveledByNonAffectedAgentsInTraffic = 0.0;

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


        for(Map.Entry<Id<Person>, Double> map : travelledDistance.entrySet()){
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

}