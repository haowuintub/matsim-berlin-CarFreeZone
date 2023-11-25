package org.matsim.run.carFreeZone.emission;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.matsim.api.core.v01.Id;
import org.matsim.contrib.emissions.Pollutant;
import org.matsim.contrib.emissions.events.*;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsManagerImpl;
import org.matsim.core.utils.collections.Tuple;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class GenerateAgentGroupBasedSingleAirPollutionValue {

    // read agent groups from txt files
    static Map<String, Set<Id>> agentGroups = new HashMap<>();

    private final Map<Pollutant, Double> pollution = new HashMap<>();

    @Parameter(names = {"-events", "-e"}, required = true)
    private List<String> eventsFiles = new ArrayList<>();

    @Parameter(names = {"-output", "-o"}, required = true)
    private String outputFile = "";

    /**
     * Run the script with command line args
     *
     * @param args e.g. -e /path/to/your/emission/events-file.xml.gz -e /path/to/your/other/emission/events-file.xml.gz -o /path/to/your/output/file.csv
     */
    // --- haowu ---
    // Program Arguments:
    //base Case: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-baseCase_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-baseCase_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.basecase.SingleAirPollutionValue
    //Plan1: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase1_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase1_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.policyCase1.SingleAirPollutionValue

    //Plan3: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.policyCase3.SingleAirPollutionValue
    // --- haowu ---
    public static void main(String[] args) throws IOException {

        // read agent groups (residents, workers, students, visitors, passing drivers, DRZ-unrelated drivers, captured agents in Berlin) from txt files
        agentGroups.put("residents", readAgentIds("/home/tumtse/Documents/haowu/DRZ/matsim-berlin-CarFreeZone/scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/personInternalIDsList.txt"));
        agentGroups.put("workers", readAgentIds("/home/tumtse/Documents/haowu/DRZ/matsim-berlin-CarFreeZone/scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/workerIDsList.txt"));
        agentGroups.put("students", readAgentIds("/home/tumtse/Documents/haowu/DRZ/matsim-berlin-CarFreeZone/scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/studentIDsList.txt"));
        agentGroups.put("visitors", readAgentIds("/home/tumtse/Documents/haowu/DRZ/matsim-berlin-CarFreeZone/scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/visitorIDsList.txt"));
        agentGroups.put("passingDrivers", readAgentIds("/home/tumtse/Documents/haowu/DRZ/matsim-berlin-CarFreeZone/scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/passingDriverIDsList.txt"));
        agentGroups.put("DRZ-unrelatedDrivers", readAgentIds("/home/tumtse/Documents/haowu/DRZ/matsim-berlin-CarFreeZone/scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/DRZ-unrelatedDriverIDsList.txt"));
        agentGroups.put("capturedAgentsInBerlin", readAgentIds("/home/tumtse/Documents/haowu/DRZ/matsim-berlin-CarFreeZone/scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/capturedAgentsInBerlinIDsList.txt"));

        // run the script
        var analysis = new GenerateAgentGroupBasedSingleAirPollutionValue();
        JCommander.newBuilder().addObject(analysis).build().parse(args);
        analysis.run();
    }

    public void run() throws IOException {

        // iterate over all agent groups
        for (Map.Entry<String, Set<Id>> agentGroup : agentGroups.entrySet()) {
            // reset pollution map
            pollution.clear();


            var pollutionPerRun = eventsFiles.parallelStream()
                    .map(file -> {
                        Map<Pollutant, Double> pollution = new TreeMap<>();

                        // haowu ***
                        //var manager = EventsUtils.createEventsManager();
                        EventsManager manager = new EventsManagerImpl();
                        // haowu ***

                        manager.addHandler(new Handler(pollution, agentGroup.getValue()));
                        var reader = new EmissionEventsReader(manager);
                        reader.readFile(file);
                        return Tuple.of(file, pollution);
                    })
                    .peek(tuple -> {
                        // some debugging output
                        for (var p : tuple.getSecond().entrySet()) {
                            System.out.println(extractFilename(tuple.getFirst()) + ": " + p.getKey() + ": \t\t" + p.getValue());
                        }
                    })
                    .collect(Collectors.toList());

            // create the header out of the pollution map of the first run. Assuming, all runs emitted the same set of pollutants
            var csvHeader = pollutionPerRun.get(0).getSecond().keySet().stream()
                    .map(pollutant -> pollutant.toString())
                    .collect(Collectors.toList());

            csvHeader.add(0, "Run");


            try (Writer writer = Files.newBufferedWriter(Paths.get(outputFile + "_" + agentGroup.getKey() + ".csv"))) {
                try (CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
                    printer.printRecord(csvHeader);

                    for (var tuple : pollutionPerRun) {

                        // this most probably only works if the filename is the standard offline emissions events file
                        var filename = extractFilename(tuple.getFirst());
                        var runId = filename.substring(0, filename.indexOf('.'));
                        printer.print(runId);

                        for (Double value : tuple.getSecond().values()) {
                            printer.print(value);
                        }
                        printer.println();
                    }
                    printer.flush();
                }
            }
        }
    }

    private String extractFilename(String filePath) {

        var index = filePath.lastIndexOf("/");
        if (index < 0) {
            index = filePath.lastIndexOf("\\");
        }
        return index >= 0 ? filePath.substring(index + 1) : filePath;
    }


    private static class Handler implements ColdEmissionEventHandler, WarmEmissionEventHandler {

        private final Map<Pollutant, Double> pollution;
        private final Set<Id> agentIds;

        private Handler(Map<Pollutant, Double> pollution, Set<Id> agentIds) {
            this.pollution = pollution;
            this.agentIds = agentIds;
        }

        @Override
        public void handleEvent(ColdEmissionEvent event) {

            if (!agentIds.contains(event.getVehicleId())) {
                return;
            }
            for (Map.Entry<Pollutant, Double> pollutant : event.getColdEmissions().entrySet()) {
                pollution.merge(pollutant.getKey(), pollutant.getValue(), Double::sum);
            }
        }

        @Override
        public void handleEvent(WarmEmissionEvent event) {

            if (!agentIds.contains(event.getVehicleId())) {
                return;
            }
            for (Map.Entry<Pollutant, Double> pollutant : event.getWarmEmissions().entrySet()) {
                pollution.merge(pollutant.getKey(), pollutant.getValue(), Double::sum);
            }
        }
    }

    private static Set<Id> readAgentIds(String filePath) throws IOException {
        // Implement reading of agent IDs from a .txt file and return as a set
        Set<Id> agentIds = new HashSet<>();
        Scanner scanner = new Scanner(new File(filePath));
        while (scanner.hasNextLine()){
            String id = scanner.nextLine();
            agentIds.add(Id.createPersonId(id));
        }
        scanner.close();
        return agentIds;
    }
}
