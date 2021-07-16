package org.matsim.run.carFreeZone.identifyAgentGroup;

import org.matsim.api.core.v01.events.ActivityEndEvent;
import org.matsim.api.core.v01.events.LinkEnterEvent;
import org.matsim.api.core.v01.events.handler.ActivityEndEventHandler;
import org.matsim.api.core.v01.events.handler.LinkEnterEventHandler;
import playground.vsp.openberlinscenario.cemdap.output.ActivityTypes;

import java.io.*;
import java.util.*;

public class IdentifyAgentGroupEventHandler implements LinkEnterEventHandler, ActivityEndEventHandler {

    public static List<String> personInternalLinkIDsList = new ArrayList<String>();
    public static ArrayList<String> allAgentIDsList = new ArrayList<>();
    public static ArrayList<String> residentIDsList = new ArrayList<>();

    //worker
    public static List<String> workerIDsList = new ArrayList<>();
    //agentsDoingLeisure
    public static List<String> agentsDoingEducationIDsList = new ArrayList<>();
    //agentsDoingOtherActivities
    public static List<String> agentsDoingOtherActivitiesIDsList = new ArrayList<>();
    //agentsPassingThrough
    public static List<String> agentsPassingThroughIDsList = new ArrayList<>();
    //agentsPassingThroughNoTAnymore
    public static List<String> agentsWithoutActivitiesIDsList = new ArrayList<>();
    //nonAffectedAgent
    public static List<String> nonAffectedAgentIDsList = new ArrayList<>();

    public IdentifyAgentGroupEventHandler() throws IOException {

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
        //System.out.println(RedStreetsList);
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
        //System.out.println(GreenStreetsList);
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
        //System.out.println(YellowStreetsList);
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
        //System.out.println(InternalStreetsList);
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
        //System.out.println(ElongationPedestrianZoneList);
        System.out.println(ElongationPedestrianZoneList.size());


        //combine and clean LinksLists for those not having car mode in No Car Zone
        personInternalLinkIDsList.addAll(InternalStreetsList);
        personInternalLinkIDsList.addAll(YellowStreetsList);
        personInternalLinkIDsList.addAll(RedStreetsList);
        personInternalLinkIDsList.addAll(GreenStreetsList);
        personInternalLinkIDsList.addAll(ElongationPedestrianZoneList);
        personInternalLinkIDsList = new ArrayList<>(new LinkedHashSet<>(personInternalLinkIDsList));
        //System.out.println(personInternalLinkIDsList);
        System.out.println(personInternalLinkIDsList.size());
        //——————Input As List——————//

        String inputFile_allAgentIDsList = "scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/allAgentIDsList.txt";
        //allAgents
        BufferedReader bfrallAgentIDsList = new BufferedReader(new FileReader(inputFile_allAgentIDsList));
        while (true){
            String s = bfrallAgentIDsList.readLine();
            if(s==null){
                break;
            }
            allAgentIDsList.add(s);
        }
        bfrallAgentIDsList.close();
        //System.out.println(allAgentIDsList);
        System.out.println(allAgentIDsList.size());

        String inputFile_residentIDsList = "scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/personInternalIDsList.txt";
        //allAgents
        BufferedReader bfrresidentIDsList = new BufferedReader(new FileReader(inputFile_residentIDsList));
        while (true){
            String s = bfrresidentIDsList.readLine();
            if(s==null){
                break;
            }
            residentIDsList.add(s);
        }
        bfrresidentIDsList.close();
        //System.out.println(residentIDsList);
        System.out.println(residentIDsList.size());
    }

    @Override
    public void handleEvent(ActivityEndEvent event){
        if (personInternalLinkIDsList.contains(event.getLinkId().toString())){
            if (event.getActType().contains(ActivityTypes.WORK)){
                workerIDsList.add(event.getPersonId().toString());
            }else if(event.getActType().contains(ActivityTypes.EDUCATION)){
                agentsDoingEducationIDsList.add(event.getPersonId().toString());
            }else if(event.getActType().contains(ActivityTypes.HOME)){

            }else{
                //ActivityTypes: LEISURE, SHOPPING, OTHER
                agentsDoingOtherActivitiesIDsList.add(event.getPersonId().toString());
            }
        }
    }

    @Override
    public void handleEvent(LinkEnterEvent event){
        if((!event.getVehicleId().toString().contains("freight"))&(!event.getVehicleId().toString().contains("pt"))){
            if (personInternalLinkIDsList.contains(event.getLinkId().toString())){
                agentsPassingThroughIDsList.add(event.getVehicleId().toString());
            }else{
                nonAffectedAgentIDsList.add(event.getVehicleId().toString());
            }
        }
    }

    public void printResults(String outputFile_personInternalLinkIDsList, String outputFile_workerIDsList, String outputFile_agentsDoingEducationIDsList, String outputFile_agentsDoingOtherActivitiesIDsList, String outputFile_agentsWithoutActivitiesIDsList, String outputFile_nonAffectedAgentIDsList) throws IOException {
        BufferedWriter writer1 = new BufferedWriter(new FileWriter(outputFile_personInternalLinkIDsList));
        for (String a : personInternalLinkIDsList) {
            writer1.write(a+ "\n");
        }
        writer1.close();

        BufferedWriter writer2 = new BufferedWriter(new FileWriter(outputFile_workerIDsList));
        workerIDsList = new ArrayList<>(new LinkedHashSet<>(workerIDsList));
        for (String a : workerIDsList) {
            writer2.write(a+ "\n");
        }
        writer2.close();

        BufferedWriter writer3 = new BufferedWriter(new FileWriter(outputFile_agentsDoingEducationIDsList));
        agentsDoingEducationIDsList = new ArrayList<>(new LinkedHashSet<>(agentsDoingEducationIDsList));
        for (String a : agentsDoingEducationIDsList) {
            writer3.write(a+ "\n");
        }
        writer3.close();

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile_agentsDoingOtherActivitiesIDsList));
        agentsDoingOtherActivitiesIDsList = new ArrayList<>(new LinkedHashSet<>(agentsDoingOtherActivitiesIDsList));
        for (String a : agentsDoingOtherActivitiesIDsList) {
            writer.write(a+ "\n");
        }
        writer.close();

        for (String agentsPassingThrough : agentsPassingThroughIDsList) {
            // --- risidentIDsList应该和这个里面抓出来的IDsList一样 ---
            if((!residentIDsList.contains(agentsPassingThrough))&(!workerIDsList.contains(agentsPassingThrough))&(!agentsDoingEducationIDsList.contains(agentsPassingThrough))&(!agentsDoingOtherActivitiesIDsList.contains(agentsPassingThrough))/*&(!agentsPassingThrough.contains("freight"))*/){
                agentsWithoutActivitiesIDsList.add(agentsPassingThrough);
            }
        }
        BufferedWriter writer4 = new BufferedWriter(new FileWriter(outputFile_agentsWithoutActivitiesIDsList));
        agentsWithoutActivitiesIDsList = new ArrayList<>(new LinkedHashSet<>(agentsWithoutActivitiesIDsList));
        for (String a : agentsWithoutActivitiesIDsList) {
            writer4.write(a+ "\n");
        }
        writer4.close();

        List<String> deletedAgentIDsList = new ArrayList<>();
        nonAffectedAgentIDsList = new ArrayList<>(new LinkedHashSet<>(nonAffectedAgentIDsList));
        for (String noneAffectedAgent : nonAffectedAgentIDsList) {
            if(agentsPassingThroughIDsList.contains(noneAffectedAgent)){
                deletedAgentIDsList.add(noneAffectedAgent);
            }
        }
        for (String deletedAgent : deletedAgentIDsList) {
            nonAffectedAgentIDsList.remove(deletedAgent);
        }
        BufferedWriter writer5 = new BufferedWriter(new FileWriter(outputFile_nonAffectedAgentIDsList));
        nonAffectedAgentIDsList = new ArrayList<>(new LinkedHashSet<>(nonAffectedAgentIDsList));
        for (String a : nonAffectedAgentIDsList) {
            writer5.write(a+ "\n");
        }
        writer5.close();

        //agentsDoingOtherActivitiesIDsList
        //agentsPassingThroughIDsList
    }
}
