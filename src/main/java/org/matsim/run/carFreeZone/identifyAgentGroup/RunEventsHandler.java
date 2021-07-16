package org.matsim.run.carFreeZone.identifyAgentGroup;

import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsManagerImpl;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class RunEventsHandler {
    public static void main(String args[]) throws IOException {

        String inputFile = "scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-baseCase_200/berlin-v5.5-1pct.output_events.xml.gz";
        String outputFile_personInternalLinkIDsList = "scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/personInternalLinkIDsList.txt";
        String outputFile_workerIDsList = "scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/workerIDsList.txt";
        String outputFile_agentsDoingEducationIDsList = "scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/agentsDoingEducationIDsList.txt";
        String outputFile_agentsDoingOtherActivitiesIDsList = "scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/agentsDoingOtherActivitiesIDsList.txt";
        String outputFile_agentsWithoutActivitiesIDsList = "scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/agentsWithoutActivitiesIDsList.txt";
        String outputFile_nonAffectedAgentIDsList = "scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/nonAffectedAgentsIDsList.txt";
        String outputFile_agentsPassingThroughIDsList = "scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/agentsPassingThroughIDsList.txt";

        //EventsManager eventsManager = EventsUtils.createEventsManager();
        EventsManager eventsManager = new EventsManagerImpl();
        IdentifyAgentGroupEventHandler eventHandler = new IdentifyAgentGroupEventHandler();
        eventsManager.addHandler(eventHandler);

        MatsimEventsReader eventsReader = new MatsimEventsReader(eventsManager);
        eventsReader.readFile(inputFile);

        eventHandler.printResults(outputFile_personInternalLinkIDsList, outputFile_workerIDsList, outputFile_agentsDoingEducationIDsList, outputFile_agentsDoingOtherActivitiesIDsList, outputFile_agentsWithoutActivitiesIDsList, outputFile_nonAffectedAgentIDsList, outputFile_agentsPassingThroughIDsList);

    }
}

