package org.matsim.run.carFreeZone.PlanA;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.NetworkFactory;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.network.algorithms.NetworkCleaner;
import org.matsim.core.network.algorithms.TransportModeNetworkFilter;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.network.io.NetworkWriter;
import org.matsim.core.scenario.ScenarioUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author dziemke
 */

/**
!!!do not use!!!
 */
public class NetworkModifier_PlanA_Version1_2 {
    private static final Logger LOG = Logger.getLogger(NetworkModifier_PlanA_Version1_2.class);

    public static void main (String[] args) throws IOException {
        // Input and output files
        String networkInputFile = "https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/berlin-v5.5-10pct/input/berlin-v5.5-network.xml.gz";
        String networkOutputFile = "scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/network-modified-carInternal.xml.gz";
        String areaShapeFile = "../../shared-svn/studies/countries/de/open_berlin_scenario/input/shapefiles/berlin_hundekopf/berlin_hundekopf.shp";

        //——————输入——————
        String RedStreets = "/Users/haowu/Documents/Planung_und_Betrieb_im_Verkehrswesen/SS2020/Multi-agent_transport_simulation_SoSe_2020/HA/HA2/LinkIDs/LinkIDsList_withoutFormatProblem/LinkIDs_RedStreets.txt";
        String RedStreets_Add_Lane = "/Users/haowu/Documents/Planung_und_Betrieb_im_Verkehrswesen/SS2020/Multi-agent_transport_simulation_SoSe_2020/HA/HA2/LinkIDs/LinkIDsList_withoutFormatProblem/LinkIDs_RedStreets_Add_Lane.txt";
        String GreenStreets = "/Users/haowu/Documents/Planung_und_Betrieb_im_Verkehrswesen/SS2020/Multi-agent_transport_simulation_SoSe_2020/HA/HA2/LinkIDs/LinkIDsList_withoutFormatProblem/LinkIDs_GreenStreets.txt";
        String YellowStreets = "/Users/haowu/Documents/Planung_und_Betrieb_im_Verkehrswesen/SS2020/Multi-agent_transport_simulation_SoSe_2020/HA/HA2/LinkIDs/LinkIDsList_withoutFormatProblem/LinkIDs_YellowStreets.txt";
        String InternalStreets = "/Users/haowu/Documents/Planung_und_Betrieb_im_Verkehrswesen/SS2020/Multi-agent_transport_simulation_SoSe_2020/HA/HA2/LinkIDs/LinkIDsList_withoutFormatProblem/LinkIDs_InternalStreets.txt";
        String ElongationPedestrianZone = "/Users/haowu/Documents/Planung_und_Betrieb_im_Verkehrswesen/SS2020/Multi-agent_transport_simulation_SoSe_2020/HA/HA2/LinkIDs/LinkIDsList_withoutFormatProblem/LinkIDs_WilmersdorferModifiedZone.txt";

        //——————输入——————//


        //——————输入转换为List——————
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

        // RedStreets_Add_Lane
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
        System.out.println(RedStreets_Add_LaneList.size());

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


        //******changes in PlanA_Version1_1******
        //combine and clean LinksLists for those not having car mode in focused Zone
        List<String> notCarModeList = new ArrayList<String>();
        notCarModeList.addAll(InternalStreetsList);
        notCarModeList.addAll(GreenStreetsList);

        //******changes in PlanA_Version1_2******
        notCarModeList.addAll(ElongationPedestrianZoneList);
        //******changes in PlanA_Version1_2******//

        notCarModeList = new ArrayList<String>(new LinkedHashSet<>(notCarModeList));
        System.out.println(notCarModeList);
        System.out.println(notCarModeList.size());

        //combine and clean LinksLists for those having carInternal mode in focused Zone
        List<String> carInternalList = new ArrayList<String>();
        carInternalList.addAll(InternalStreetsList);
        carInternalList.addAll(YellowStreetsList);
        carInternalList.addAll(RedStreetsList);
        carInternalList = new ArrayList<String>(new LinkedHashSet<>(carInternalList));
        System.out.println(carInternalList);
        System.out.println(carInternalList.size());
        //******changes in PlanA_Version1_1******//

        //——————输入转换为List——————//




        //******changes in PlanA_Version1_1******
/*        // Store relevant area of city as geometry
        Collection<SimpleFeature> features = (new ShapeFileReader()).readFileAndInitialize(areaShapeFile);
        Map<String, Geometry> zoneGeometries = new HashMap<>();
        for (SimpleFeature feature : features) {
            zoneGeometries.put((String) feature.getAttribute("SCHLUESSEL"), (Geometry) feature.getDefaultGeometry());
        }
        Geometry areaGeometry = zoneGeometries.get("Hundekopf");*/
        //******changes in PlanA_Version1_1******//

        // Get network
        Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        MatsimNetworkReader reader = new MatsimNetworkReader(scenario.getNetwork());
        reader.readFile(networkInputFile);


        //******changes in PlanA_Version1_1******
        //——————for mode in pedestrian zone: Wilmersdorfer Str.——————
        Set<String> allowedModes = new HashSet<String>();
        //allowedModes.add("ride");
        allowedModes.add("walk");
        allowedModes.add("bicycle");

        for(int i=0; i < ElongationPedestrianZoneList.size(); i++){
            scenario.getNetwork().getLinks().get(Id.createLinkId(ElongationPedestrianZoneList.get(i))).setAllowedModes(allowedModes);
        }

        //——————for mode in pedestrian zone: Wilmersdorfer Str.——————//
        //******changes in PlanA_Version1_1******//


        // Get pt subnetwork
        Scenario ptScenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        TransportModeNetworkFilter transportModeNetworkFilterPt = new TransportModeNetworkFilter(scenario.getNetwork());
        transportModeNetworkFilterPt.filter(ptScenario.getNetwork(), new HashSet<>(Arrays.asList(TransportMode.pt)));

        // Modify the car network
        for (Link link : scenario.getNetwork().getLinks().values()) {
            Set<String> allowedModesBefore = link.getAllowedModes();
            Set<String> allowedModesAfter = new HashSet<>();

            /*Point linkCenterAsPoint = MGC.xy2Point(link.getCoord().getX(), link.getCoord().getY());*/

            for (String mode : allowedModesBefore) {
                if (mode.equals(TransportMode.car)) {

                    //******changes in PlanA_Version1_2******//
                    if (carInternalList.contains(link.getId().toString())) {
                    allowedModesAfter.add("carInternal");
                    }
                    //******changes in PlanA_Version1_2******//

                    /*if (!areaGeometry.contains(linkCenterAsPoint)) {*/
                    if (!notCarModeList.contains(link.getId().toString())) {
                        allowedModesAfter.add(TransportMode.car);
                    }
                } else {
                    allowedModesAfter.add(mode);
                }
            }
            link.setAllowedModes(allowedModesAfter);
        }
        LOG.info("Finished modifying car vs. carInternal network");

        // Get car subnetwork and clean it
        Scenario carScenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        TransportModeNetworkFilter transportModeNetworkFilterCar = new TransportModeNetworkFilter(scenario.getNetwork());
        transportModeNetworkFilterCar.filter(carScenario.getNetwork(), new HashSet<>(Arrays.asList(TransportMode.car)));
        (new NetworkCleaner()).run(carScenario.getNetwork());
        LOG.info("Finished creating and cleaning car subnetwork");

        // Store remaining car links after cleaning in list
        List<Id<Link>> remainingCarlinksAfterCleaning = new ArrayList<>();
        for (Link link : carScenario.getNetwork().getLinks().values()) {
            remainingCarlinksAfterCleaning.add(link.getId());
        }
        LOG.info("There are " + remainingCarlinksAfterCleaning.size() + " car links left.");

        // Get carInternal subnetwork and clean it
        Scenario carInternalScenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        TransportModeNetworkFilter transportModeNetworkFilterCarInternal = new TransportModeNetworkFilter(scenario.getNetwork());
        transportModeNetworkFilterCarInternal.filter(carInternalScenario.getNetwork(), new HashSet<>(Arrays.asList("carInternal")));
        (new NetworkCleaner()).run(carInternalScenario.getNetwork());
        LOG.info("Finished creating and cleaning carInternal subnetwork");

        //******changes in PlanA_Version1_2******
        // Store remaining carInternal links after cleaning in list
        List<Id<Link>> remainingCarInternalLinksAfterCleaning = new ArrayList<>();
        for (Link link : carInternalScenario.getNetwork().getLinks().values()) {
            remainingCarInternalLinksAfterCleaning.add(link.getId());
        }
        LOG.info("There are " + remainingCarInternalLinksAfterCleaning.size() + " carInternal links left.");

        //******changes in PlanA_Version1_2******//

        // Add car mode to all links where appropriate
        int counter = 0;
        int divisor = 1;
        for (Link link : carInternalScenario.getNetwork().getLinks().values()) {
            Set<String> allowedModesAfter = new HashSet<>();

            //******changes in PlanA_Version1_1******
            //for Links in carInternal zone
            if (carInternalList.contains(link.getId().toString())) {
                //for Links in carInternal zone//

                for (String mode : link.getAllowedModes()) { //这行需要吗？？？

                    //******changes in PlanA_Version1_2******
                    if (remainingCarInternalLinksAfterCleaning.contains(link.getId())) {

                        allowedModesAfter.add("carInternal"); // This is the carInternal list, so carInternal needs to be re-added

                    }
                    //******changes in PlanA_Version1_2******//

                    allowedModesAfter.add(TransportMode.ride); // Checked: All (previous) car links were also ride links before, so re-add this mode
                    allowedModesAfter.add("freight"); // Checked: All (previous) car links were also freight links before, so re-add this mode

                    //******changes in PlanA_Version1_1******
                    //加walk、bicycle
                    allowedModesAfter.add("walk");
                    allowedModesAfter.add("bicycle");
                    //加walk、bicycle//
                    //******changes in PlanA_Version1_1******//

                }

                if (!InternalStreetsList.contains(link.getId().toString())) {

                    if (remainingCarlinksAfterCleaning.contains(link.getId())) {
                        allowedModesAfter.add(TransportMode.car);
                    }

                }

            //for Links in pedestrian zone: Wilmersdorfer Str.
            }else if (ElongationPedestrianZoneList.contains(link.getId().toString())){
                for (String mode : link.getAllowedModes()) { //这行需要吗？？？
                    //allowedModesAfter.add("carInternal"); // This is the carInternal list, so carInternal needs to be re-added
                    //allowedModesAfter.add(TransportMode.ride); // Checked: All (previous) car links were also ride links before, so re-add this mode
                    //allowedModesAfter.add("freight"); // Checked: All (previous) car links were also freight links before, so re-add this mode

                    //******changes in PlanA_Version1_1******
                    //加walk、bicycle
                    allowedModesAfter.add("walk");
                    allowedModesAfter.add("bicycle");
                    //加walk、bicycle//
                    //******changes in PlanA_Version1_1******//

                }
                //if (remainingCarlinksAfterCleaning.contains(link.getId())) {
                //    allowedModesAfter.add(TransportMode.car);
                //}

                //for Links in pedestrian zone: Wilmersdorfer Str.//

            //for Links in green streets
            }else if (GreenStreetsList.contains(link.getId().toString())) {
                for (String mode : link.getAllowedModes()) { //这行需要吗？？？
                    //allowedModesAfter.add("carInternal"); // This is the carInternal list, so carInternal needs to be re-added
                    //allowedModesAfter.add(TransportMode.ride); // Checked: All (previous) car links were also ride links before, so re-add this mode
                    allowedModesAfter.add("freight"); // Checked: All (previous) car links were also freight links before, so re-add this mode

                    //******changes in PlanA_Version1_1******
                    //加walk、bicycle
                    allowedModesAfter.add("walk");
                    allowedModesAfter.add("bicycle");
                    //加walk、bicycle//
                    //******changes in PlanA_Version1_1******//

                }
                //if (remainingCarlinksAfterCleaning.contains(link.getId())) {
                //    allowedModesAfter.add(TransportMode.car);
                //}
                //for Links in green streets//
            }else {
                //for the links not in our focused zone

                for (String mode : link.getAllowedModes()) { //这行需要吗？？？
                    //******changes in PlanA_Version1_2******
                    if (remainingCarInternalLinksAfterCleaning.contains(link.getId())) {
                        allowedModesAfter.add("carInternal"); // This is the carInternal list, so carInternal needs to be re-added
                    }
                    //******changes in PlanA_Version1_2******//
                    allowedModesAfter.add(TransportMode.ride); // Checked: All (previous) car links were also ride links before, so re-add this mode
                    allowedModesAfter.add("freight"); // Checked: All (previous) car links were also freight links before, so re-add this mode

                    //******changes in PlanA_Version1_1******
                    //加walk、bicycle
                    allowedModesAfter.add("walk");
                    allowedModesAfter.add("bicycle");
                    //加walk、bicycle//
                    //******changes in PlanA_Version1_1******//

                }
                if (remainingCarlinksAfterCleaning.contains(link.getId())) {
                    allowedModesAfter.add(TransportMode.car);
                }

                //for the links not in our focused zone//
            }
            //******changes in PlanA_Version1_1******//

            link.setAllowedModes(allowedModesAfter);
            counter++;
            if (counter % divisor == 0) {
                LOG.info(counter + " links handled.");
                divisor = divisor * 2;
            }
        }
        LOG.info("Finished adding car back to links where required");

        // Add pt back into the other network
        // *** Note: Customized attributes are not considered here ***
        NetworkFactory factory = carInternalScenario.getNetwork().getFactory();
        for (Node node : ptScenario.getNetwork().getNodes().values()) {
            Node node2 = factory.createNode(node.getId(), node.getCoord());
            carInternalScenario.getNetwork().addNode(node2);
        }
        for (Link link : ptScenario.getNetwork().getLinks().values()) {
            Node fromNode = carInternalScenario.getNetwork().getNodes().get(link.getFromNode().getId());
            Node toNode = carInternalScenario.getNetwork().getNodes().get(link.getToNode().getId());
            Link link2 = factory.createLink(link.getId(), fromNode, toNode);
            link2.setAllowedModes(link.getAllowedModes());
            link2.setCapacity(link.getCapacity());
            link2.setFreespeed(link.getFreespeed());
            link2.setLength(link.getLength());
            link2.setNumberOfLanes(link.getNumberOfLanes());
            carInternalScenario.getNetwork().addLink(link2);
        }
        LOG.info("Finished merging pt network layer back into network");




        //更改 NumberOfLanes and Freespeed
        // save LinkIDs of cleaned Network as List
        ArrayList<String> cleanedNetworkLinkIDList = new ArrayList<>();
        for (Link link : carInternalScenario.getNetwork().getLinks().values()) {
            cleanedNetworkLinkIDList.add(link.getId().toString());
        }


        //——————加Lane——————
        // for YellowStreets
        for (int i = 0; i < YellowStreetsList.size(); i++) {
            if(cleanedNetworkLinkIDList.contains(YellowStreetsList.get(i))) {
                double numberOfLanes_After_YellowStreets = scenario.getNetwork().getLinks().get(Id.createLinkId(YellowStreetsList.get(i))).getNumberOfLanes() + 1.;
                scenario.getNetwork().getLinks().get(Id.createLinkId(YellowStreetsList.get(i))).setNumberOfLanes(numberOfLanes_After_YellowStreets);

                double capacity_After_YellowStreets = scenario.getNetwork().getLinks().get(Id.createLinkId(YellowStreetsList.get(i))).getCapacity() + 1800.;
                scenario.getNetwork().getLinks().get(Id.createLinkId(YellowStreetsList.get(i))).setCapacity(capacity_After_YellowStreets);
            }
        }
        //******changes in PlanA_Version1_1******
        // for RedStreets_Add_Lane
        for (int i = 0; i < RedStreets_Add_LaneList.size(); i++) {
            if(cleanedNetworkLinkIDList.contains(RedStreets_Add_LaneList.get(i))) {
                double numberOfLanes_After_RedStreets_Add_Lane = scenario.getNetwork().getLinks().get(Id.createLinkId(RedStreets_Add_LaneList.get(i))).getNumberOfLanes() + 1.;
                scenario.getNetwork().getLinks().get(Id.createLinkId(RedStreets_Add_LaneList.get(i))).setNumberOfLanes(numberOfLanes_After_RedStreets_Add_Lane);

                double capacity_After_RedStreets_Add_Lane = scenario.getNetwork().getLinks().get(Id.createLinkId(RedStreets_Add_LaneList.get(i))).getCapacity() + 1800.;
                scenario.getNetwork().getLinks().get(Id.createLinkId(RedStreets_Add_LaneList.get(i))).setCapacity(capacity_After_RedStreets_Add_Lane);
            }
        }
        //******changes in PlanA_Version1_1******//

        //——————加Lane——————//


        //——————SpeedModifier——————
        // for GreyStreets/InternalStreets
        for (int i = 0; i < InternalStreetsList.size(); i++) {
            if(cleanedNetworkLinkIDList.contains(InternalStreetsList.get(i))) {
                carInternalScenario.getNetwork().getLinks().get(Id.createLinkId(InternalStreetsList.get(i))).setFreespeed(1.38889);
            }
        }
        // for GreenStreets
        for (int i = 0; i < GreenStreetsList.size(); i++) {
            if(cleanedNetworkLinkIDList.contains(GreenStreetsList.get(i))) {
                carInternalScenario.getNetwork().getLinks().get(Id.createLinkId(GreenStreetsList.get(i))).setFreespeed(4.86);
            }
        }
        // for YellowStreets
        for (int i = 0; i < YellowStreetsList.size(); i++) {
            if(cleanedNetworkLinkIDList.contains(YellowStreetsList.get(i))) {
                carInternalScenario.getNetwork().getLinks().get(Id.createLinkId(YellowStreetsList.get(i))).setFreespeed(4.86);
            }
        }
        //——————SpeedModifier——————


        // Write modified network to file
        NetworkWriter writer = new NetworkWriter(carInternalScenario.getNetwork());
        writer.write(networkOutputFile);
    }
}