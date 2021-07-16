package org.matsim.run.carFreeZone.modalSplit.simple;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.population.*;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.population.io.PopulationReader;
import org.matsim.core.scenario.ScenarioUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ModalSplit {

    public static AgentGroup residentGroup;
    public static AgentGroup workerGroup;
    public static AgentGroup agentsDoingEducationGroup;
    public static AgentGroup agentsDoingOtherActivitiesGroup;
    public static AgentGroup agentsWithoutActivitiesGroup;
    public static AgentGroup nonAffectedAgentsGroup;

    public static List<AgentGroup> agentGroupList;

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

    public static void main(String[] args) throws IOException {

        String input_planFile = "scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-baseCase_200/berlin-v5.5-1pct.output_plans.xml.gz";
        String outputPath = "scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-baseCase_200/analysis/modalSplit.csv";

        residentGroup = new AgentGroup("residentGroup");
        workerGroup = new AgentGroup("workerGroup");
        agentsDoingEducationGroup = new AgentGroup("agentsDoingEducationGroup");
        agentsDoingOtherActivitiesGroup = new AgentGroup("agentsDoingOtherActivitiesGroup");
        agentsWithoutActivitiesGroup = new AgentGroup("agentsWithoutActivitiesGroup");
        nonAffectedAgentsGroup = new AgentGroup("nonAffectedAgentsGroup");

        List<AgentGroup> agentGroupList = new ArrayList<>();
        agentGroupList.add(residentGroup);
        agentGroupList.add(workerGroup);
        agentGroupList.add(agentsDoingEducationGroup);
        agentGroupList.add(agentsDoingOtherActivitiesGroup);
        agentGroupList.add(agentsWithoutActivitiesGroup);
        agentGroupList.add(nonAffectedAgentsGroup);


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


        // Get population
        Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        PopulationReader populationReader = new PopulationReader(scenario);
        populationReader.readFile(input_planFile);

        // Substitute car mode by carInternal mode for people inside relevant area
        for (Person person : scenario.getPopulation().getPersons().values()) {
            if (residents.contains(person.getId()) /*&& (!AffectedAgentsIDsList.contains(person.getId().toString()))*/) {
                process(person, residentGroup);
            } else if (workers.contains(person.getId())) {
                process(person, workerGroup);
            } else if (agentsDoingEducation.contains(person.getId())) {
                process(person, agentsDoingEducationGroup);
            } else if (agentsDoingOtherActivities.contains(person.getId())) {
                process(person, agentsDoingOtherActivitiesGroup);
            } else if (agentsWithoutActivities.contains(person.getId())) {
                process(person, agentsWithoutActivitiesGroup);
            } else if (nonAffectedAgents.contains(person.getId())) {
                process(person, nonAffectedAgentsGroup);
            }
        }

        for (AgentGroup agentGroup : agentGroupList) {
            agentGroup.calculate();
        }

        writeResults(outputPath);
    }

    static void process(Person person, AgentGroup agentGroup) {
        double highestscore = 0;
        for (Plan pa : person.getPlans()) {
            if(pa.getScore() > highestscore){
                highestscore = pa.getScore();
            }
        }
        for (Plan pa : person.getPlans()) {
            if (pa.getScore()==highestscore) {
                for (PlanElement pe : pa.getPlanElements()) {
                    if (pe instanceof Leg) {
                        Leg leg = (Leg) pe;
                        if (leg.getMode().equals("carInternal")) {
                            agentGroup.carInternalCounter++;
                            agentGroup.carInternalCounter++;
                        } else if (leg.getMode().equals(TransportMode.car)) {
                            agentGroup.carCounter++;
                            agentGroup.carCounter++;
                        } else if (leg.getMode().equals("freight")) {
                            //agentGroup.freightCounter++;
                            //agentGroup.freightCounter++;
                        } else if (leg.getMode().equals(TransportMode.ride)) {
                            agentGroup.rideCounter++;
                            agentGroup.rideCounter++;
                        } else if (leg.getMode().equals("bicycle")) {
                            agentGroup.bicycleCounter++;
                            agentGroup.bicycleCounter++;
                        } else if (leg.getMode().equals(TransportMode.walk)) {
                            if(leg.getAttributes().getAttribute("routingMode").equals("walk")) {
                                agentGroup.walkCounter++;
                                agentGroup.walkCounter++;
                            }
                        } else if (leg.getMode().equals(TransportMode.pt)) {
                            agentGroup.ptCounter++;
                            agentGroup.ptCounter++;
                        } else {
                            throw new RuntimeException("there are not only mode 'car, carInternal, freight, ride, bicycle, walk, pt', but also something else mode! ");
                        }
                    } else if (pe instanceof Activity) {

                    } else {
                        throw new RuntimeException("Plan element can either be activity or leg.");
                    }
                }
            }
        }
    }

    static void writeResults(String outputPath) {
        try (Writer writer = Files.newBufferedWriter(Paths.get(outputPath))) {
            try (CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

                //print header with all possible pollutants
                printer.print("AgentGroup");
                printer.print("carInternal");
                printer.print("car");
                //printer.print("freight");
                printer.print("ride");
                printer.print("bicycle");
                printer.print("walk");
                printer.print("pt");
                printer.println();

                /*for (AgentGroup agentGroup : agentGroupList) {
                    if(agentGroup != null) {
                        printer.print(agentGroup.name);
                        printer.print(agentGroup.modeShare_carInternal);
                        printer.print(agentGroup.modeShare_car);
                        //printer.print(agentGroup.modeShare_freight);
                        printer.print(agentGroup.modeShare_ride);
                        printer.print(agentGroup.modeShare_bicycle);
                        printer.print(agentGroup.modeShare_walk);
                        printer.print(agentGroup.modeShare_pt);
                        printer.println();
                    }
                }*/

                printer.print(residentGroup.name);
                printer.print(residentGroup.modeShare_carInternal);
                printer.print(residentGroup.modeShare_car);
                //printer.print(residentGroup.modeShare_freight);
                printer.print(residentGroup.modeShare_ride);
                printer.print(residentGroup.modeShare_bicycle);
                printer.print(residentGroup.modeShare_walk);
                printer.print(residentGroup.modeShare_pt);
                printer.println();

                printer.print(workerGroup.name);
                printer.print(workerGroup.modeShare_carInternal);
                printer.print(workerGroup.modeShare_car);
                //printer.print(workerGroup.modeShare_freight);
                printer.print(workerGroup.modeShare_ride);
                printer.print(workerGroup.modeShare_bicycle);
                printer.print(workerGroup.modeShare_walk);
                printer.print(workerGroup.modeShare_pt);
                printer.println();

                printer.print(agentsDoingEducationGroup.name);
                printer.print(agentsDoingEducationGroup.modeShare_carInternal);
                printer.print(agentsDoingEducationGroup.modeShare_car);
                //printer.print(agentsDoingEducationGroup.modeShare_freight);
                printer.print(agentsDoingEducationGroup.modeShare_ride);
                printer.print(agentsDoingEducationGroup.modeShare_bicycle);
                printer.print(agentsDoingEducationGroup.modeShare_walk);
                printer.print(agentsDoingEducationGroup.modeShare_pt);
                printer.println();

                printer.print(agentsDoingOtherActivitiesGroup.name);
                printer.print(agentsDoingOtherActivitiesGroup.modeShare_carInternal);
                printer.print(agentsDoingOtherActivitiesGroup.modeShare_car);
                //printer.print(agentsDoingOtherActivitiesGroup.modeShare_freight);
                printer.print(agentsDoingOtherActivitiesGroup.modeShare_ride);
                printer.print(agentsDoingOtherActivitiesGroup.modeShare_bicycle);
                printer.print(agentsDoingOtherActivitiesGroup.modeShare_walk);
                printer.print(agentsDoingOtherActivitiesGroup.modeShare_pt);
                printer.println();

                printer.print(agentsWithoutActivitiesGroup.name);
                printer.print(agentsWithoutActivitiesGroup.modeShare_carInternal);
                printer.print(agentsWithoutActivitiesGroup.modeShare_car);
                //printer.print(agentsWithoutActivitiesGroup.modeShare_freight);
                printer.print(agentsWithoutActivitiesGroup.modeShare_ride);
                printer.print(agentsWithoutActivitiesGroup.modeShare_bicycle);
                printer.print(agentsWithoutActivitiesGroup.modeShare_walk);
                printer.print(agentsWithoutActivitiesGroup.modeShare_pt);
                printer.println();

                printer.print(nonAffectedAgentsGroup.name);
                printer.print(nonAffectedAgentsGroup.modeShare_carInternal);
                printer.print(nonAffectedAgentsGroup.modeShare_car);
                //printer.print(nonAffectedAgentsGroup.modeShare_freight);
                printer.print(nonAffectedAgentsGroup.modeShare_ride);
                printer.print(nonAffectedAgentsGroup.modeShare_bicycle);
                printer.print(nonAffectedAgentsGroup.modeShare_walk);
                printer.print(nonAffectedAgentsGroup.modeShare_pt);
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
