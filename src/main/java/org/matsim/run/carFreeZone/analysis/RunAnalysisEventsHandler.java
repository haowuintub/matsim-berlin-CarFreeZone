package org.matsim.run.carFreeZone.analysis;

import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsManagerImpl;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class RunAnalysisEventsHandler {
    public static void main(String args[]) throws IOException, ParserConfigurationException, SAXException {


        //-------------For Input (and Output) Files-------------
        // BaseCase
        //String inputFile = "scenarios/berlin-v5.5-1pct/output-berlin-v5.5-1pct_baseCase_100Iterations/berlin-v5.5-1pct.output_events.xml";

        // PlanA
        //String inputFile = "scenarios/berlin-v5.5-1pct/output-berlin-v5.5-1pct-subpop_PlanA_Version1_secondSuccessfulRun_100Iterations/berlin-v5.5-1pct.output_events.xml";

        // PlanB 50Iterations
        //String inputFile = "scenarios/berlin-v5.5-1pct/output-berlin-v5.5-1pct_PlanB_Network2_Version1_firstSuccessfulRun_50Iterations/berlin-v5.5-1pct.output_events.xml";
        // PlanB 100Iterations
        String inputFile = "scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-baseCase_200/berlin-v5.5-1pct.output_events.xml.gz";
        //String outputFile_results = "scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-baseCase_200/analysis/travelTime.txt";
        String outputFile_results = "scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-baseCase_200/analysis/";

        // PlanC
        //String inputFile = "scenarios/berlin-v5.5-1pct/output-berlin-v5.5-1pct_PlanC_firstSuccessfulRun_100Iterations/berlin-v5.5-1pct.output_events.xml";

        //-------------For Input (and Output) Files-------------//




        //EventsManager eventsManager = EventsUtils.createEventsManager();
        EventsManager eventsManager = new EventsManagerImpl();
        LinkAnalysisEventHandler eventHandler = new LinkAnalysisEventHandler();
        eventsManager.addHandler(eventHandler);

        MatsimEventsReader eventsReader = new MatsimEventsReader(eventsManager);
        eventsReader.readFile(inputFile);

        //eventHandler.printResults(outputFile_results);
        eventHandler.printResultsToCSV(outputFile_results);

    }
}

