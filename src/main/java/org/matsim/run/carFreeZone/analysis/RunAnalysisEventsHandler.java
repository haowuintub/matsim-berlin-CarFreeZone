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
        final String runDirectory = "scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-baseCase_200";
/*        // Plan1
        final String runDirectory = "scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-policyCase1_200";
        // Plan2
        final String runDirectory = "scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-policyCase2_200";
        // Plan3
        final String runDirectory = "scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-policyCase3_200";*/
        //-------------For Input (and Output) Files-------------//

        String inputFile = runDirectory + "/berlin-v5.5-1pct.output_events.xml.gz";
        //String outputFile_results = runDirectory + "/analysis/travelTime.txt";
        String outputFile_results = runDirectory + "/analysis/";



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

