package org.matsim.run.carFreeZone.emission;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.emissions.Pollutant;
import org.matsim.contrib.emissions.events.*;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.events.EventsManagerImpl;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.collections.Tuple;

import org.matsim.core.utils.geometry.geotools.MGC;
import org.matsim.core.utils.gis.ShapeFileReader;
import org.opengis.feature.simple.SimpleFeature;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class GenerateSingleAirPollutionValueWithinShape {

    private final Map<Pollutant, Double> pollution = new HashMap<>();

    @Parameter(names = {"-events", "-e"}, required = true)
    private List<String> eventsFiles = new ArrayList<>();

    @Parameter(names = {"-output", "-o"}, required = true)
    private String outputFile = "";

    @Parameter(names = {"-calculated area", "-c"}, required = true)
    private String calculatedArea = "";

    @Parameter(names = {"-network", "-n"}, required = true)
    private String matsimNetwork = "";

    /**
     * Run the script with command line args
     *
     * @param args e.g. -e /path/to/your/emission/events-file.xml.gz -e /path/to/your/other/emission/events-file.xml.gz -o /path/to/your/output/file.csv
     */
    // --- haowu ---
    // Program Arguments for Berlin:
    //base Case: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-baseCase_200_simple/berlin-v5.5-1pct.emission.events.offline.basecase.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-baseCase_200_simple/berlin-v5.5-1pct.emission.events.offline.basecase.SingleAirPollutionValue_Berlin.csv -c /home/tumtse/Documents/haowu/DRZ/events_Biao/shapefiles/berlin-shp/berlin.shp -n scenarios/berlin-v5.5-1pct/input/berlin-v5.5-network.xml.gz
    //Plan1: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase1_200_simple/berlin-v5.5-1pct.emission.events.offline.policyCase1.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase1_200_simple/berlin-v5.5-1pct.emission.events.offline.policyCase1.SingleAirPollutionValue_Berlin.csv -c /home/tumtse/Documents/haowu/DRZ/events_Biao/shapefiles/berlin-shp/berlin.shp -n scenarios/berlin-v5.5-1pct/DRZ/input/network-modified-carInternal_Plan1.xml.gz
    //Plan3: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/berlin-v5.5-1pct.emission.events.offline.policyCase3.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/berlin-v5.5-1pct.emission.events.offline.policyCase3.SingleAirPollutionValue_Berlin.csv -c /home/tumtse/Documents/haowu/DRZ/events_Biao/shapefiles/berlin-shp/berlin.shp -n scenarios/berlin-v5.5-1pct/DRZ/input/network-modified-carInternal_Plan3.xml.gz

    // Program Arguments for DRZ:
    //base Case: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-baseCase_200_simple/berlin-v5.5-1pct.emission.events.offline.basecase.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-baseCase_200_simple/berlin-v5.5-1pct.emission.events.offline.basecase.SingleAirPollutionValue_DRZ.csv -c scenarios/berlin-v5.5-1pct/DRZ/shapefile_Biao/drz_withoutRA/drz_withoutRA.shp -n scenarios/berlin-v5.5-1pct/input/berlin-v5.5-network.xml.gz
    //Plan1: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase1_200_simple/berlin-v5.5-1pct.emission.events.offline.policyCase1.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase1_200_simple/berlin-v5.5-1pct.emission.events.offline.policyCase1.SingleAirPollutionValue_DRZ.csv -c scenarios/berlin-v5.5-1pct/DRZ/shapefile_Biao/drz_withoutRA/drz_withoutRA.shp -n scenarios/berlin-v5.5-1pct/DRZ/input/network-modified-carInternal_Plan1.xml.gz
    //Plan3: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/berlin-v5.5-1pct.emission.events.offline.policyCase3.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/berlin-v5.5-1pct.emission.events.offline.policyCase3.SingleAirPollutionValue_DRZ.csv -c scenarios/berlin-v5.5-1pct/DRZ/shapefile_Biao/drz_withoutRA/drz_withoutRA.shp -n scenarios/berlin-v5.5-1pct/DRZ/input/network-modified-carInternal_Plan3.xml.gz

    //Program Arguments for BB:
    //base Case: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-baseCase_200_simple/berlin-v5.5-1pct.emission.events.offline.basecase.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-baseCase_200_simple/berlin-v5.5-1pct.emission.events.offline.basecase.SingleAirPollutionValue_BB.csv -c /home/tumtse/Documents/haowu/DRZ/events_Biao/shapefiles/boundingbox/2-time-DRZ.shp -n scenarios/berlin-v5.5-1pct/input/berlin-v5.5-network.xml.gz
    //Plan1: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase1_200_simple/berlin-v5.5-1pct.emission.events.offline.policyCase1.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase1_200_simple/berlin-v5.5-1pct.emission.events.offline.policyCase1.SingleAirPollutionValue_BB.csv -c /home/tumtse/Documents/haowu/DRZ/events_Biao/shapefiles/boundingbox/2-time-DRZ.shp -n scenarios/berlin-v5.5-1pct/DRZ/input/network-modified-carInternal_Plan1.xml.gz
    //Plan3: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/berlin-v5.5-1pct.emission.events.offline.policyCase3.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/berlin-v5.5-1pct.emission.events.offline.policyCase3.SingleAirPollutionValue_BB.csv -c /home/tumtse/Documents/haowu/DRZ/events_Biao/shapefiles/boundingbox/2-time-DRZ.shp -n scenarios/berlin-v5.5-1pct/DRZ/input/network-modified-carInternal_Plan3.xml.gz
    // --- haowu ---
    public static void main(String[] args) throws IOException {

        var analysis = new GenerateSingleAirPollutionValueWithinShape();
        JCommander.newBuilder().addObject(analysis).build().parse(args);
        analysis.run();
    }

    public void run() throws IOException {

        var pollutionPerRun = eventsFiles.parallelStream()
                .map(file -> {
                    Map<Pollutant, Double> pollution = new TreeMap<>();

                    // haowu ***
                    //var manager = EventsUtils.createEventsManager();
                    EventsManager manager = new EventsManagerImpl();
                    // haowu ***

                    // read calculated area
                    Collection<SimpleFeature> features = (new ShapeFileReader()).readFileAndInitialize(calculatedArea);
                    Map<String, Geometry> zoneGeometries = new HashMap<>();
                    for (SimpleFeature feature : features) {
                        zoneGeometries.put((String) feature.getAttribute("Name"), (Geometry) feature.getDefaultGeometry());
                    }
                    Geometry areaGeometry = zoneGeometries.get("drz");

                    // Get matsim network
                    Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
                    MatsimNetworkReader networkReader = new MatsimNetworkReader(scenario.getNetwork());
                    networkReader.readFile(matsimNetwork);
                    List<String>linkList = new ArrayList<>();
                    for (Link link : scenario.getNetwork().getLinks().values()) {
                        //check if the center point of the link is in the area
                        Point linkCenter = MGC.xy2Point(link.getCoord().getX(), link.getCoord().getY());
                        if (areaGeometry.intersects(linkCenter)) {
                            linkList.add(link.getId().toString());
                        }
                    }

                    manager.addHandler(new Handler(pollution, linkList));
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


        try (Writer writer = Files.newBufferedWriter(Paths.get(outputFile))) {
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

    private String extractFilename(String filePath) {

        var index = filePath.lastIndexOf("/");
        if (index < 0) {
            index = filePath.lastIndexOf("\\");
        }
        return index >= 0 ? filePath.substring(index + 1) : filePath;
    }


    private static class Handler implements ColdEmissionEventHandler, WarmEmissionEventHandler {

        private final Map<Pollutant, Double> pollution;
        private final List<String> linkList;

        private Handler(Map<Pollutant, Double> pollution, List<String> linkList) {
            this.pollution = pollution;
            this.linkList = linkList;
        }

        @Override
        public void handleEvent(ColdEmissionEvent event) {

            if (!linkList.contains(event.getLinkId().toString())) {
                return; // skip if the link is not in the area
            }
            for (Map.Entry<Pollutant, Double> pollutant : event.getColdEmissions().entrySet()) {
                pollution.merge(pollutant.getKey(), pollutant.getValue(), Double::sum);
            }
        }

        @Override
        public void handleEvent(WarmEmissionEvent event) {

            if (!linkList.contains(event.getLinkId().toString())) {
                return; // skip if the link is not in the area
            }
            for (Map.Entry<Pollutant, Double> pollutant : event.getWarmEmissions().entrySet()) {
                pollution.merge(pollutant.getKey(), pollutant.getValue(), Double::sum);
            }
        }
    }
}