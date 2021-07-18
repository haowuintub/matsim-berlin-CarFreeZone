package org.matsim.run.carFreeZone.analysis;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.events.*;
import org.matsim.api.core.v01.events.handler.*;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class LinkAnalysisEventHandler implements LinkEnterEventHandler, PersonDepartureEventHandler, PersonArrivalEventHandler, PersonLeavesVehicleEventHandler {

    //Internal links
    public static List<Id> internalLinks = new ArrayList<Id>();
    public static Map<Id, Double> vehicleHasEnteredInternalZone = new HashMap<Id, Double>();
    public static double totalTimeSpentInInternalZone = 0.0;
    public static double totalDistanceTravelledInInternalZone = 0.0;
    public static Map<Id, Double> distanceOfLinks = new HashMap<Id, Double>();
    public static Set<Id> vehiclesGoingThroughInternalZone = new HashSet();

    //Red Streets
    public static List<Id> redLinks = new ArrayList<Id>();
    public static double totalTimeSpentInRedLinks = 0.0;
    public static double totalDistanceTravelledInRedLinks = 0.0;
    public static Set<Id> vehiclesGoingThroughRedLinks = new HashSet();
    public static Map<Id, Double> vehicleHasEnteredRedLinks = new HashMap<Id, Double>();

    //Yellow Streets
    public static List<Id> yellowLinks = new ArrayList<Id>();
    public static double totalTimeSpentInYellowLinks = 0.0;
    public static double totalDistanceTravelledInYellowLinks = 0.0;
    public static Set<Id> vehiclesGoingThroughYellowLinks = new HashSet();
    public static Map<Id, Double> vehicleHasEnteredYellowLinks = new HashMap<Id, Double>();

    //Green Streets
    public static List<Id> greenLinks = new ArrayList<Id>();
    public static double totalTimeSpentInGreenLinks = 0.0;
    public static double totalDistanceTravelledInGreenLinks = 0.0;
    public static Set<Id> vehiclesGoingThroughGreenLinks = new HashSet();
    public static Map<Id, Double> vehicleHasEnteredGreenLinks = new HashMap<Id, Double>();

    //Additional.
    public static List<Id> ernstReuterLinks = new ArrayList<Id>();
    public static double totalTimeSpentInErnstReuter = 0.0;
    public static double totalDistanceTravelledInErnstReuter = 0.0;
    public static Set<Id> vehiclesGoingThroughErnstReuter = new HashSet();
    public static Map<Id, Double> vehicleHasEnteredErnstReuter = new HashMap<Id, Double>();


    //Affected Vehicles.
    public static Set<Id> affectedVehicles = new HashSet<>();
    public static double totalDistanceTravelledByAffectedVehicles = 0.0;
    int sum_affectedVehicles;


    //Residents.
    public static Set<Id> residents = new HashSet<>();
    public static double totalTimeSpentByResidentsInTraffic = 0.0;
    public static Map<Id, Double> timeMap_residents = new HashMap<>();
    public static Set<Id> residentsUsingPT = new HashSet<>();

    //Workers.
    public static Set<Id> workers = new HashSet<>();
    public static double totalTimeSpentByWorkersInTraffic = 0.0;
    public static Map<Id, Double> timeMap_workers = new HashMap<>();
    public static Set<Id> workersUsingPT = new HashSet<>();

    //AgentsDoingEducation.
    public static Set<Id> agentsDoingEducation = new HashSet<>();
    public static double totalTimeSpentByAgentsDoingEducationInTraffic = 0.0;
    public static Map<Id, Double> timeMap_agentsDoingEducation = new HashMap<>();
    public static Set<Id> agentsDoingEducationUsingPT = new HashSet<>();

    //AgentsDoingOtherActivities.
    public static Set<Id> agentsDoingOtherActivities = new HashSet<>();
    public static double totalTimeSpentByAgentsDoingOtherActivitiesInTraffic = 0.0;
    public static Map<Id, Double> timeMap_agentsDoingOtherActivities = new HashMap<>();
    public static Set<Id> agentsDoingOtherActivitiesUsingPT = new HashSet<>();

    //AgentsWithoutActivities.
    public static Set<Id> agentsWithoutActivities = new HashSet<>();
    public static double totalTimeSpentByAgentsWithoutActivitiesInTraffic = 0.0;
    public static Map<Id, Double> timeMap_agentsWithoutActivities = new HashMap<>();
    public static Set<Id> agentsWithoutActivitiesUsingPT = new HashSet<>();

    //NonAffectedAgent.
    public static Set<Id> nonAffectedAgents = new HashSet<>();
    public static double totalTimeSpentByNonAffectedAgentsInTraffic = 0.0;
    public static Map<Id, Double> timeMap_nonAffectedAgents = new HashMap<>();
    public static Set<Id> nonAffectedAgentsUsingPT = new HashSet<>();

    //Other
    public static Set<Id> agentsUsingPt = new HashSet<>();


    public LinkAnalysisEventHandler() throws IOException {
        //Create every link list.
        Scanner scanner = new Scanner(new File("/Users/haowu/Documents/Planung_und_Betrieb_im_Verkehrswesen/SS2020/Multi-agent_transport_simulation_SoSe_2020/HA/HA2/LinkIDs/LinkIDsList_withoutFormatProblem/LinkIDs_YellowStreets.txt"));
        while(scanner.hasNextLine())
        {
            String id = scanner.nextLine();
            yellowLinks.add(Id.createLinkId(id));

        }
        scanner.close();

        Scanner scanner1 = new Scanner(new File("/Users/haowu/Documents/Planung_und_Betrieb_im_Verkehrswesen/SS2020/Multi-agent_transport_simulation_SoSe_2020/HA/HA2/LinkIDs/LinkIDsList_withoutFormatProblem/LinkIDs_GreenStreets.txt"));
        while(scanner1.hasNextLine())
        {
            String id = scanner1.nextLine();
            greenLinks.add(Id.createLinkId(id));

        }
        scanner1.close();

        Scanner scanner2 =  new Scanner(new File("/Users/haowu/Documents/Planung_und_Betrieb_im_Verkehrswesen/SS2020/Multi-agent_transport_simulation_SoSe_2020/HA/HA2/LinkIDs/LinkIDsList_withoutFormatProblem/LinkIDs_RedStreets.txt"));
        while(scanner2.hasNextLine())
        {
            String id = scanner2.nextLine();
            redLinks.add(Id.createLinkId(id));

        }
        scanner2.close();

        Scanner scanner3 =  new Scanner(new File("/Users/haowu/Documents/Planung_und_Betrieb_im_Verkehrswesen/SS2020/Multi-agent_transport_simulation_SoSe_2020/HA/HA2/LinkIDs/LinkIDsList_withoutFormatProblem/LinkIDs_InternalStreets.txt"));
        while(scanner3.hasNextLine())
        {
            String id = scanner3.nextLine();
            internalLinks.add(Id.createLinkId(id));

        }
        scanner3.close();

        Scanner scanner4 =  new Scanner(new File("/Users/haowu/Documents/Planung_und_Betrieb_im_Verkehrswesen/SS2020/Multi-agent_transport_simulation_SoSe_2020/HA/HA2/LinkIDs/LinkIDsList_withoutFormatProblem/LinkIDs_ErnstReuter.txt"));
        while(scanner4.hasNextLine())
        {
            String id = scanner4.nextLine();
            ernstReuterLinks.add(Id.createLinkId(id));

        }
        scanner4.close();


        Scanner scanner5 = new Scanner(new File("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/agentsPassingThroughIDsList.txt"));
        while (scanner5.hasNextLine()){
            String id = scanner5.nextLine();
            affectedVehicles.add(Id.createVehicleId(id));
        }
        scanner5.close();


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




        //Now we are going to store all the link distances in the map:
/*        File network = new File("scenarios/berlin-v5.5-1pct/input/berlin-v5.5-network.xml.gz");
        Scanner sc = new Scanner(network);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document dct = builder.parse(network);
        dct.getDocumentElement().normalize();
        NodeList nodeList = dct.getElementsByTagName("link");
        for(int i=0; i<nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE){
                Element e = (Element) node;
                Double linkDist = Double.parseDouble(e.getAttribute("length"));
                distanceOfLinks.put(Id.createLinkId(e.getAttribute("id")), linkDist);
            }
        }*/
        Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        MatsimNetworkReader reader = new MatsimNetworkReader(scenario.getNetwork());
        reader.readFile("scenarios/berlin-v5.5-1pct/input/berlin-v5.5-network.xml.gz");
        for(Link link: scenario.getNetwork().getLinks().values()) {
            distanceOfLinks.put(link.getId(), link.getLength());
        }
    }




    @Override
    public void handleEvent(LinkEnterEvent event){
        Id link = event.getLinkId();
        Id vehicle = event.getVehicleId();

        if(affectedVehicles.contains(vehicle)){
            totalDistanceTravelledByAffectedVehicles += distanceOfLinks.get(link);
            sum_affectedVehicles++;
        }

        if (internalLinks.contains(link)){
            //Add vehicle to the set of vehicles going through the internal streets.
            vehiclesGoingThroughInternalZone.add(event.getVehicleId());
            //Add the link's distance to the total distance travelled counter.
            totalDistanceTravelledInInternalZone += distanceOfLinks.get(event.getLinkId());
            //Vehicle enters for the first time in our zone, so we record the time.
            vehicleHasEnteredInternalZone.putIfAbsent(vehicle, event.getTime());
        }else{
            if (vehicleHasEnteredInternalZone.containsKey(vehicle)){
                //Vehicle is leaving the internal streets, we add the time spent in traffic to the counter.
                Double currentTime = event.getTime();
                totalTimeSpentInInternalZone += (currentTime-vehicleHasEnteredInternalZone.get(vehicle));
                //Vehicle's id removed from the map so that next time it enters zone the time is stored.
                vehicleHasEnteredInternalZone.remove(vehicle);
            }
        }

        if (redLinks.contains(link)){
            //Add vehicle to the set of vehicles going through the red links.
            vehiclesGoingThroughRedLinks.add(event.getVehicleId());
            //Add the link's distance to the total distance travelled counter.
            totalDistanceTravelledInRedLinks += distanceOfLinks.get(event.getLinkId());
            //Vehicle enters for the first time in the red links, so we record the time.
            vehicleHasEnteredRedLinks.putIfAbsent(vehicle, event.getTime());
        }else{
            if (vehicleHasEnteredRedLinks.containsKey(vehicle)){
                //Vehicle is leaving the red links, we add the time spent in traffic to the counter.
                Double currentTime = event.getTime();
                totalTimeSpentInRedLinks += (currentTime-vehicleHasEnteredRedLinks.get(vehicle));
                //Vehicle's id removed from the map so that next time it enters zone the time is stored.
                vehicleHasEnteredRedLinks.remove(vehicle);
            }
        }

        if (yellowLinks.contains(link)){
            //Add vehicle to the set of vehicles going through the yellow links.
            vehiclesGoingThroughYellowLinks.add(event.getVehicleId());
            //Add the link's distance to the total distance travelled counter.
            totalDistanceTravelledInYellowLinks += distanceOfLinks.get(event.getLinkId());
            //Vehicle enters for the first time in the yellow links, so we record the time.
            vehicleHasEnteredYellowLinks.putIfAbsent(vehicle, event.getTime());
        }else{
            if (vehicleHasEnteredYellowLinks.containsKey(vehicle)){
                //Vehicle is leaving the yellow links, we add the time spent in traffic to the counter.
                Double currentTime = event.getTime();
                totalTimeSpentInYellowLinks += (currentTime-vehicleHasEnteredYellowLinks.get(vehicle));
                //Vehicle's id removed from the map so that next time it enters zone the time is stored.
                vehicleHasEnteredYellowLinks.remove(vehicle);
            }
        }

        if (greenLinks.contains(link)){
            //Add vehicle to the set of vehicles going through the green links.
            vehiclesGoingThroughGreenLinks.add(event.getVehicleId());
            //Add the link's distance to the total distance travelled counter.
            totalDistanceTravelledInGreenLinks += distanceOfLinks.get(event.getLinkId());
            //Vehicle enters for the first time in the green links, so we record the time.
            vehicleHasEnteredGreenLinks.putIfAbsent(vehicle, event.getTime());
        }else{
            if (vehicleHasEnteredGreenLinks.containsKey(vehicle)){
                //Vehicle is leaving the green links, we add the time spent in traffic to the counter.
                Double currentTime = event.getTime();
                totalTimeSpentInGreenLinks += (currentTime-vehicleHasEnteredGreenLinks.get(vehicle));
                //Vehicle's id removed from the map so that next time it enters zone the time is stored.
                vehicleHasEnteredGreenLinks.remove(vehicle);
            }
        }

        if (ernstReuterLinks.contains(link)){
            //Add vehicle to the set of vehicles going through the Ernst Reuter.
            vehiclesGoingThroughErnstReuter.add(event.getVehicleId());
            //Add the link's distance to the total distance travelled counter.
            totalDistanceTravelledInErnstReuter += distanceOfLinks.get(event.getLinkId());
            //Vehicle enters for the first time in ernst Reuter, so we record the time.
            vehicleHasEnteredErnstReuter.putIfAbsent(vehicle, event.getTime());
        }else{
            if (vehicleHasEnteredErnstReuter.containsKey(vehicle)){
                //Vehicle is leaving the ernst Reuter, we add the time spent in traffic to the counter.
                Double currentTime = event.getTime();
                totalTimeSpentInErnstReuter += (currentTime-vehicleHasEnteredErnstReuter.get(vehicle));
                //Vehicle's id removed from the map so that next time it enters zone the time is stored.
                vehicleHasEnteredErnstReuter.remove(vehicle);
            }
        }

    }

    @Override
    public void handleEvent(PersonDepartureEvent event){
        Id person = event.getPersonId();
        if(residents.contains(person)){
            timeMap_residents.putIfAbsent(person, event.getTime());
        } else if(workers.contains(person)){
            timeMap_workers.putIfAbsent(person, event.getTime());
        } else if(agentsDoingEducation.contains(person)) {
            timeMap_agentsDoingEducation.putIfAbsent(person, event.getTime());
        } else if(agentsDoingOtherActivities.contains(person)) {
            timeMap_agentsDoingOtherActivities.putIfAbsent(person, event.getTime());
        } else if(agentsWithoutActivities.contains(person)){
            timeMap_agentsWithoutActivities.putIfAbsent(person, event.getTime());
        } else if(nonAffectedAgents.contains(person)){
            timeMap_nonAffectedAgents.putIfAbsent(person, event.getTime());
        }
    }

    @Override
    public void handleEvent(PersonArrivalEvent event){
        Id person = event.getPersonId();
        if(timeMap_residents.containsKey(person)){
            totalTimeSpentByResidentsInTraffic += (event.getTime()- timeMap_residents.get(person));
            timeMap_residents.remove(person);
        } else if(timeMap_workers.containsKey(person)){
            totalTimeSpentByWorkersInTraffic += (event.getTime()- timeMap_workers.get(person));
            timeMap_workers.remove(person);
        } else if(timeMap_agentsDoingEducation.containsKey(person)){
            totalTimeSpentByAgentsDoingEducationInTraffic += (event.getTime()- timeMap_agentsDoingEducation.get(person));
            timeMap_agentsDoingEducation.remove(person);
        } else if(timeMap_agentsDoingOtherActivities.containsKey(person)){
            totalTimeSpentByAgentsDoingOtherActivitiesInTraffic += (event.getTime()- timeMap_agentsDoingOtherActivities.get(person));
            timeMap_agentsDoingOtherActivities.remove(person);
        } else if(timeMap_agentsWithoutActivities.containsKey(person)){
            totalTimeSpentByAgentsWithoutActivitiesInTraffic += (event.getTime()- timeMap_agentsWithoutActivities.get(person));
            timeMap_agentsWithoutActivities.remove(person);
        } else if(timeMap_nonAffectedAgents.containsKey(person)){
            totalTimeSpentByNonAffectedAgentsInTraffic += (event.getTime()- timeMap_nonAffectedAgents.get(person));
            timeMap_nonAffectedAgents.remove(person);
        }
    }

    @Override
    public void handleEvent(PersonLeavesVehicleEvent event){
        //String activityType = event.getActType();
        Id person = event.getPersonId();
        if (event.getVehicleId().toString().contains("pt")){
            if(residents.contains(person)){
                residentsUsingPT.add(person);
            } else if(workers.contains(person)){
                workersUsingPT.add(person);
            } else if(agentsDoingEducation.contains(person)){
                agentsDoingEducationUsingPT.add(person);
            } else if(agentsDoingOtherActivities.contains(person)){
                agentsDoingOtherActivitiesUsingPT.add(person);
            } else if(agentsWithoutActivities.contains(person)){
                agentsWithoutActivitiesUsingPT.add(person);
            } else if(nonAffectedAgents.contains(person)){
                nonAffectedAgentsUsingPT.add(person);
            }
            agentsUsingPt.add(person);
        }
    }





    public void printResults(String outputFile_results) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile_results));

        writer.write("***************************************************************" + "\n");
        writer.write("INTERNAL LINKS" + "\n");
        writer.write("Total distance travelled in internal streets: " + totalDistanceTravelledInInternalZone + "\n");
        writer.write("Total time travelled in internal streets: " + totalTimeSpentInInternalZone + "\n");
        writer.write("Total number of vehicles going through internal streets: " + vehiclesGoingThroughInternalZone.size() + "\n");
        writer.write("---------------------------------------------------------------" + "\n");
        writer.write("RED LINKS:" + "\n");
        writer.write("Total distance travelled in red streets: " + totalDistanceTravelledInRedLinks + "\n");
        writer.write("Total time travelled in red streets: " + totalTimeSpentInRedLinks + "\n");
        writer.write("Total number of vehicles going through red streets: " + vehiclesGoingThroughRedLinks.size() + "\n");
        writer.write("---------------------------------------------------------------" + "\n");
        writer.write("YELLOW LINKS:" + "\n");
        writer.write("Total distance travelled in yellow streets: " + totalDistanceTravelledInYellowLinks + "\n");
        writer.write("Total time travelled in yellow streets: " + totalTimeSpentInYellowLinks + "\n");
        writer.write("Total number of vehicles going through yellow streets: " + vehiclesGoingThroughYellowLinks.size() + "\n");
        writer.write("---------------------------------------------------------------" + "\n");
        writer.write("GREEN LINKS:" + "\n");
        writer.write("Total distance travelled in green streets: " + totalDistanceTravelledInGreenLinks + "\n");
        writer.write("Total time travelled in green streets: " + totalTimeSpentInGreenLinks + "\n");
        writer.write("Total number of vehicles going through green streets: " + vehiclesGoingThroughGreenLinks.size() + "\n");
        writer.write("---------------------------------------------------------------" + "\n");
        writer.write("ERNST Reuter:" + "\n");
        writer.write("Total distance travelled in Ernst Reuter: " + totalDistanceTravelledInErnstReuter + "\n");
        writer.write("Total time travelled in Ernst Reuter: " + totalTimeSpentInErnstReuter + "\n");
        writer.write("Total number of vehicles going through Ernst Reuter: " + vehiclesGoingThroughErnstReuter.size() + "\n");
        writer.write("---------------------------------------------------------------" + "\n");
        writer.write("\n");
        writer.write("\n");


        writer.write("AFFECTED VEHICLES:" + "\n");
        writer.write("Number of affected vehicles: " + sum_affectedVehicles + "\n");
        writer.write("Total distance travelled by the affected vehicles: " + totalDistanceTravelledByAffectedVehicles + "\n");
        writer.write("Average distance travelled by the affected vehicles: " + totalDistanceTravelledByAffectedVehicles/sum_affectedVehicles + "\n");
        writer.write("---------------------------------------------------------------" + "\n");
        writer.write("\n");
        writer.write("\n");


        writer.write("Residents:" + "\n");
        writer.write("Number of residents: " + residents.size() + "\n");
        writer.write("Total time spent in traffic by the residents: " + totalTimeSpentByResidentsInTraffic + "\n");
        writer.write("Average time spent in traffic by the residens: " + totalTimeSpentByResidentsInTraffic / residents.size() + "\n");
        writer.write("Number of residents using pt: " + residentsUsingPT.size() + "\n");
        writer.write("---------------------------------------------------------------" + "\n");
        writer.write("Workers:" + "\n");
        writer.write("Number of affected workers: " + workers.size() + "\n");
        writer.write("Total time spent in traffic by the workers: " + totalTimeSpentByWorkersInTraffic + "\n");
        writer.write("Average time spent in traffic by the workers: " + totalTimeSpentByWorkersInTraffic / workers.size() + "\n");
        writer.write("Number of workers using pt: " + workersUsingPT.size() + "\n");
        writer.write("---------------------------------------------------------------" + "\n");
        writer.write("AgentsDoingEducation:" + "\n");
        writer.write("Number of agentsDoingEducation: " + agentsDoingEducation.size() + "\n");
        writer.write("Total time spent in traffic by the agentsDoingEducation: " + totalTimeSpentByAgentsDoingEducationInTraffic + "\n");
        writer.write("Average time spent in traffic by the agentsDoingEducation: " + totalTimeSpentByAgentsDoingEducationInTraffic / agentsDoingEducation.size() + "\n");
        writer.write("Number of agentsDoingEducation using pt: " + agentsDoingEducationUsingPT.size() + "\n");
        writer.write("---------------------------------------------------------------" + "\n");
        writer.write("AgentsDoingOtherActivities:" + "\n");
        writer.write("Number of agentsDoingOtherActivities: " + agentsDoingOtherActivities.size() + "\n");
        writer.write("Total time spent in traffic by the agentsDoingOtherActivities: " + totalTimeSpentByAgentsDoingOtherActivitiesInTraffic + "\n");
        writer.write("Average time spent in traffic by the agentsDoingOtherActivities: " + totalTimeSpentByAgentsDoingOtherActivitiesInTraffic / agentsDoingOtherActivities.size() + "\n");
        writer.write("Number of agentsDoingOtherActivities using pt: " + agentsDoingOtherActivitiesUsingPT.size() + "\n");
        writer.write("---------------------------------------------------------------" + "\n");
        writer.write("AgentsWithoutActivities:" + "\n");
        writer.write("Number of agentsWithoutActivities: " + agentsWithoutActivities.size() + "\n");
        writer.write("Total time spent in traffic by the agentsWithoutActivities: " + totalTimeSpentByAgentsWithoutActivitiesInTraffic + "\n");
        writer.write("Average time spent in traffic by the agentsWithoutActivities: " + totalTimeSpentByAgentsWithoutActivitiesInTraffic / agentsWithoutActivities.size() + "\n");
        writer.write("Number of agentsWithoutActivities using pt: " + agentsWithoutActivitiesUsingPT.size() + "\n");
        writer.write("---------------------------------------------------------------" + "\n");
        writer.write("NonAffectedAgents:" + "\n");
        writer.write("Number of nonAffectedAgent: " + nonAffectedAgents.size() + "\n");
        writer.write("Total time spent in traffic by the nonAffectedAgents: " + totalTimeSpentByNonAffectedAgentsInTraffic + "\n");
        writer.write("Average time spent in traffic by the nonAffectedAgents: " + totalTimeSpentByNonAffectedAgentsInTraffic / nonAffectedAgents.size() + "\n");
        writer.write("Number of nonAffectedAgents using pt: " + nonAffectedAgentsUsingPT.size() + "\n");
        writer.write("---------------------------------------------------------------" + "\n");
        writer.write("\n");
        writer.write("\n");


        writer.write("OTHER:" + "\n");
        writer.write("Number of agents using pt: " + agentsUsingPt.size() + "\n");
        writer.write("***************************************************************" + "\n");

        writer.close();
    }




    public void printResultsToCSV(String outputFile_results) {
        String outputFile_results_travelTime;
        String outputFile_results_roadInformation;

        outputFile_results_travelTime = outputFile_results + "travelTime.csv";
        try (Writer writer = Files.newBufferedWriter(Paths.get(outputFile_results_travelTime))) {
            try (CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

                printer.print("Travel Time");
                printer.print("Number of this agentGroup");
                printer.print("Total time spent in traffic by this agentGroup");
                printer.print("Average time spent in traffic by this agentGroup");
                printer.print("Number of this agentGroup using pt");
                printer.println();

                printer.print("residents");
                printer.print(residents.size());
                printer.print(totalTimeSpentByResidentsInTraffic);
                printer.print(totalTimeSpentByResidentsInTraffic / residents.size());
                printer.print(residentsUsingPT.size());
                printer.println();

                printer.print("workers");
                printer.print(workers.size());
                printer.print(totalTimeSpentByWorkersInTraffic);
                printer.print(totalTimeSpentByWorkersInTraffic / workers.size());
                printer.print(workersUsingPT.size());
                printer.println();

                printer.print("agentsDoingEducation");
                printer.print(agentsDoingEducation.size());
                printer.print(totalTimeSpentByAgentsDoingEducationInTraffic);
                printer.print(totalTimeSpentByAgentsDoingEducationInTraffic / agentsDoingEducation.size());
                printer.print(agentsDoingEducationUsingPT.size());
                printer.println();

                printer.print("agentsDoingOtherActivities");
                printer.print(agentsDoingOtherActivities.size());
                printer.print(totalTimeSpentByAgentsDoingOtherActivitiesInTraffic);
                printer.print(totalTimeSpentByAgentsDoingOtherActivitiesInTraffic / agentsDoingOtherActivities.size());
                printer.print(agentsDoingOtherActivitiesUsingPT.size());
                printer.println();

                printer.print("agentsWithoutActivities");
                printer.print(agentsWithoutActivities.size());
                printer.print(totalTimeSpentByAgentsWithoutActivitiesInTraffic);
                printer.print(totalTimeSpentByAgentsWithoutActivitiesInTraffic / agentsWithoutActivities.size());
                printer.print(agentsWithoutActivitiesUsingPT.size());
                printer.println();

                printer.print("nonAffectedAgents");
                printer.print(nonAffectedAgents.size());
                printer.print(totalTimeSpentByNonAffectedAgentsInTraffic);
                printer.print(totalTimeSpentByNonAffectedAgentsInTraffic / nonAffectedAgents.size());
                printer.print(nonAffectedAgentsUsingPT.size());
                printer.println();
                printer.println();


                printer.print("Number of agents using pt");
                printer.print(agentsUsingPt.size());
                printer.println();




                printer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




        outputFile_results_roadInformation = outputFile_results + "roadInformation.csv";
        try (Writer writer = Files.newBufferedWriter(Paths.get(outputFile_results_roadInformation))) {
            try (CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

                printer.print("Road information");
                printer.print("INTERNAL LINKS");
                printer.print("RED LINKS");
                printer.print("YELLOW LINKS");
                printer.print("GREEN LINKS");
                printer.print("ERNST Reuter");
                printer.println();

                printer.print("Total distance travelled");
                printer.print(totalDistanceTravelledInInternalZone);
                printer.print(totalDistanceTravelledInRedLinks);
                printer.print(totalDistanceTravelledInYellowLinks);
                printer.print(totalDistanceTravelledInGreenLinks);
                printer.print(totalDistanceTravelledInErnstReuter);
                printer.println();

                printer.print("Total time travelled");
                printer.print(totalTimeSpentInInternalZone);
                printer.print(totalTimeSpentInRedLinks);
                printer.print(totalTimeSpentInYellowLinks);
                printer.print(totalTimeSpentInGreenLinks);
                printer.print(totalTimeSpentInErnstReuter);
                printer.println();

                printer.print("Total number of vehicles going through");
                printer.print(vehiclesGoingThroughInternalZone.size());
                printer.print(vehiclesGoingThroughRedLinks.size());
                printer.print(vehiclesGoingThroughYellowLinks.size());
                printer.print(vehiclesGoingThroughGreenLinks.size());
                printer.print(vehiclesGoingThroughErnstReuter.size());
                printer.println();
                printer.println();
                printer.println();
                printer.println();
                printer.println();




                printer.print("Number of affected vehicles");
                printer.print(sum_affectedVehicles);
                printer.println();

                printer.print("Total distance travelled by the affected vehicles");
                printer.print(totalDistanceTravelledByAffectedVehicles);
                printer.println();

                printer.print("Average distance travelled by the affected vehicles");
                printer.print(totalDistanceTravelledByAffectedVehicles/sum_affectedVehicles);
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
