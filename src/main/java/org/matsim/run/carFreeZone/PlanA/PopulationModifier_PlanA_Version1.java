package org.matsim.run.carFreeZone.PlanA;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.population.io.PopulationReader;
import org.matsim.core.population.io.PopulationWriter;
import org.matsim.core.scenario.ScenarioUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author dziemke
 */
public class PopulationModifier_PlanA_Version1 {
    //allAgents
    public static List<Id> allAgentIDsList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        // Input and output files
        String plansInputFile = "https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/berlin-v5.5-1pct/input/berlin-v5.5-1pct.plans.xml.gz";
        String plansOutputFile = "scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/plans-modified-carInternal.xml.gz";
        //String areaShapeFile = "/Users/haowu/Workspace/QGIS/MATSim_HA2/ColoredStreets/NoCarZone_final.shp";

        String outputFile_personInternalIDsList = "scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/personInternalIDsList.txt";
        String outputFile_allAgentIDsList = "scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/allAgentIDsList.txt";


        //******changes in PlanA_Version1******
        //——————Input——————
        String RedStreets = "/Users/haowu/Documents/Planung_und_Betrieb_im_Verkehrswesen/SS2020/Multi-agent_transport_simulation_SoSe_2020/HA/HA2/LinkIDs/LinkIDsList_withoutFormatProblem/LinkIDs_RedStreets.txt";
        //String RedStreets_Add_Lane = "/Users/haowu/Documents/Planung_und_Betrieb_im_Verkehrswesen/SS2020/Multi-agent_transport_simulation_SoSe_2020/HA/HA2/LinkIDs/LinkIDsList_withoutFormatProblem/LinkIDs_RedStreets_Add_Lane.txt";
        String GreenStreets = "/Users/haowu/Documents/Planung_und_Betrieb_im_Verkehrswesen/SS2020/Multi-agent_transport_simulation_SoSe_2020/HA/HA2/LinkIDs/LinkIDsList_withoutFormatProblem/LinkIDs_GreenStreets.txt";
        String YellowStreets = "/Users/haowu/Documents/Planung_und_Betrieb_im_Verkehrswesen/SS2020/Multi-agent_transport_simulation_SoSe_2020/HA/HA2/LinkIDs/LinkIDsList_withoutFormatProblem/LinkIDs_YellowStreets.txt";
        String InternalStreets = "/Users/haowu/Documents/Planung_und_Betrieb_im_Verkehrswesen/SS2020/Multi-agent_transport_simulation_SoSe_2020/HA/HA2/LinkIDs/LinkIDsList_withoutFormatProblem/LinkIDs_InternalStreets.txt";
        String ElongationPedestrianZone = "/Users/haowu/Documents/Planung_und_Betrieb_im_Verkehrswesen/SS2020/Multi-agent_transport_simulation_SoSe_2020/HA/HA2/LinkIDs/LinkIDsList_withoutFormatProblem/LinkIDs_WilmersdorferModifiedZone.txt";

        //——————Input——————//


        //——————Input As List——————
        // RedStreets
        BufferedReader bfrRedStreets = new BufferedReader(new FileReader(RedStreets));
        ArrayList<String> RedStreetsList = new ArrayList<>();
        while (true){
            String s = bfrRedStreets.readLine();
            if(s==null){
                break;
            }
            RedStreetsList.add(s);
        }
        bfrRedStreets.close();
        System.out.println(RedStreetsList);
        System.out.println(RedStreetsList.size());

/*        // RedStreets_Add_Lane
        BufferedReader bfrRedStreets_Add_Lane = new BufferedReader(new FileReader(RedStreets_Add_Lane));
        ArrayList<String> RedStreets_Add_LaneList = new ArrayList<>();
        while (true){
            String s = bfrRedStreets_Add_Lane.readLine();
            if(s==null){
                break;
            }
            RedStreets_Add_LaneList.add(s);
        }
        bfrRedStreets_Add_Lane.close();
        System.out.println(RedStreets_Add_LaneList);
        System.out.println(RedStreets_Add_LaneList.size());*/

        // GreenStreets
        BufferedReader bfrGreenStreets = new BufferedReader(new FileReader(GreenStreets));
        ArrayList<String> GreenStreetsList = new ArrayList<>();
        while (true){
            String s = bfrGreenStreets.readLine();
            if(s==null){
                break;
            }
            GreenStreetsList.add(s);
        }
        bfrGreenStreets.close();
        System.out.println(GreenStreetsList);
        System.out.println(GreenStreetsList.size());

        // YellowStreets
        BufferedReader bfrYellowStreets = new BufferedReader(new FileReader(YellowStreets));
        ArrayList<String> YellowStreetsList = new ArrayList<>();
        while (true){
            String s = bfrYellowStreets.readLine();
            if(s==null){
                break;
            }
            YellowStreetsList.add(s);
        }
        bfrYellowStreets.close();
        System.out.println(YellowStreetsList);
        System.out.println(YellowStreetsList.size());

        // InternalStreets
        BufferedReader bfrInternalStreets = new BufferedReader(new FileReader(InternalStreets));
        ArrayList<String> InternalStreetsList = new ArrayList<>();
        while (true){
            String s = bfrInternalStreets.readLine();
            if(s==null){
                break;
            }
            InternalStreetsList.add(s);
        }
        bfrInternalStreets.close();
        System.out.println(InternalStreetsList);
        System.out.println(InternalStreetsList.size());

        // ElongationPedestrianZone
        BufferedReader bfrElongationPedestrianZone = new BufferedReader(new FileReader(ElongationPedestrianZone));
        ArrayList<String> ElongationPedestrianZoneList = new ArrayList<>();
        while (true){
            String s = bfrElongationPedestrianZone.readLine();
            if(s==null){
                break;
            }
            ElongationPedestrianZoneList.add(s);
        }
        bfrElongationPedestrianZone.close();
        System.out.println(ElongationPedestrianZoneList);
        System.out.println(ElongationPedestrianZoneList.size());


        //combine and clean LinksLists for those not having car mode in No Car Zone
        List<String> personInternalLinkList = new ArrayList<String>();
        personInternalLinkList.addAll(InternalStreetsList);
        personInternalLinkList.addAll(YellowStreetsList);
        personInternalLinkList.addAll(RedStreetsList);
        personInternalLinkList.addAll(GreenStreetsList);
        personInternalLinkList.addAll(ElongationPedestrianZoneList);
        personInternalLinkList = new ArrayList<String>(new LinkedHashSet<>(personInternalLinkList));
        System.out.println(personInternalLinkList);
        System.out.println(personInternalLinkList.size());
        //——————Input As List——————//




/*        // Store relevant area of city as geometry
        Collection<SimpleFeature> features = (new ShapeFileReader()).readFileAndInitialize(areaShapeFile);
        Map<String, Geometry> zoneGeometries = new HashMap<>();
        for (SimpleFeature feature : features) {
            zoneGeometries.put((String) feature.getAttribute("Name"), (Geometry) feature.getDefaultGeometry());
        }
        Geometry areaGeometry = zoneGeometries.get("NoCarZone");*/

        //******changes in PlanA_Version1******//




        // Get population
        Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        PopulationReader populationReader = new PopulationReader(scenario);
        populationReader.readFile(plansInputFile);

        //prepare List
        ArrayList<String> personInternalIDsList = new ArrayList<>();
        //prepare List//

        // Substitute car mode by carInternal mode for people inside relevant area
        for (Person person : scenario.getPopulation().getPersons().values()) {

            if(!person.getId().toString().contains("freight")){
                allAgentIDsList.add(person.getId());
            }


            // for only Residents
            Activity homeActivity = (Activity) person.getPlans().get(0).getPlanElements().get(0);

            //******changes in PlanA_Version1******
            //Point homeActAsPoint = MGC.xy2Point(homeActivity.getCoord().getX(), homeActivity.getCoord().getY());
            String homeActAsLink = homeActivity.getLinkId().toString();


            //not include freightAgent Person !
            if (personInternalLinkList.contains(homeActAsLink)&&(!person.getId().toString().contains("freight"))) {
                //not include freightAgent Person !//
                //******changes in PlanA_Version1******//

                person.getAttributes().putAttribute("subpopulation", "personInternal");

                // print AgentID to List
                personInternalIDsList.add(person.getId().toString());

                for (PlanElement pe : person.getPlans().get(0).getPlanElements()) {
                    if (pe instanceof Leg) {
                        Leg leg = (Leg) pe;
                        if (leg.getMode().equals(TransportMode.car)) {
                            leg.setMode("carInternal");
                            leg.getAttributes().putAttribute("routingMode", "carInternal");
                        }
                        if (leg.getMode().equals(TransportMode.walk) && leg.getAttributes().getAttribute("routingMode").equals(TransportMode.car)) {
                            leg.getAttributes().putAttribute("routingMode", "carInternal");
                        }
                    } else if (pe instanceof Activity) {
                        Activity activity = (Activity) pe;
                        if (activity.getType().equals("car interaction")) {
                            activity.setType("carInternal interaction");
                        }
                    }
                }
            }
        }

        //print List as Txt
        //print the AgentIDs of No Car Zone
        // Insert Path to Output File Here!
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile_personInternalIDsList));
        // traverses the collection
        for (String s : personInternalIDsList) {
            // write data
            bw.write(s);
            bw.newLine();
            bw.flush();
        }
        // release resource
        bw.close();
        //print List as Txt//
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile_allAgentIDsList));
        allAgentIDsList = new ArrayList<>(new LinkedHashSet<>(allAgentIDsList));
        for (Id a : allAgentIDsList) {
            writer.write(a.toString()+ "\n");
        }
        writer.close();

        // Write modified population to file
        PopulationWriter populationWriter = new PopulationWriter(scenario.getPopulation());
        populationWriter.write(plansOutputFile);
    }
}