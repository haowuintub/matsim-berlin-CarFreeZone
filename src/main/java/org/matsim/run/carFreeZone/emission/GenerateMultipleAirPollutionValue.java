package org.matsim.run.carFreeZone.emission;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.matsim.contrib.emissions.Pollutant;
import org.matsim.contrib.emissions.events.*;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsManagerImpl;
import org.matsim.core.utils.collections.Tuple;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.scenario.ScenarioUtils;

import org.matsim.core.utils.geometry.geotools.MGC;
import org.matsim.core.utils.gis.ShapeFileReader;
import org.opengis.feature.simple.SimpleFeature;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

public class GenerateMultipleAirPollutionValue {

    @Parameter(names = {"-events", "-e"}, required = true)
    private List<String> eventsFiles = new ArrayList<>();

    @Parameter(names = {"-output", "-o"}, required = true)
    private String outputFile = "";

    @Parameter(names = {"-calculated area", "-c"}, required = true)
    private String calculatedArea = "";

    @Parameter(names = {"-postal-code", "-p"})
    private String plz = "0";

    @Parameter(names = {"-network", "-n"}, required = true)
    private String matsimNetwork = "";

    /**
     * Run the script with command line args
     *
     * @param args e.g. -e /path/to/your/emission/events-file.xml.gz -e /path/to/your/other/emission/events-file.xml.gz -o /path/to/your/output/file.csv
     */
    // --- haowu ---
    // Program Arguments for Berlin:
    //base Case: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-baseCase_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-baseCase_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.basecase.MultipleAirPollutionValue_Berlin.csv -c /home/tumtse/Documents/haowu/DRZ/events_Biao/shapefiles/berlin-shp/berlin.shp -n scenarios/berlin-v5.5-1pct/input/berlin-v5.5-network.xml.gz
    //Plan1: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase1_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase1_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.policyCase1.MultipleAirPollutionValue_Berlin.csv -c /home/tumtse/Documents/haowu/DRZ/events_Biao/shapefiles/berlin-shp/berlin.shp -n scenarios/berlin-v5.5-1pct/DRZ/input/network-modified-carInternal_Plan1.xml.gz
    //Plan3: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.policyCase3.MultipleAirPollutionValue_Berlin.csv -c /home/tumtse/Documents/haowu/DRZ/events_Biao/shapefiles/berlin-shp/berlin.shp -n scenarios/berlin-v5.5-1pct/DRZ/input/network-modified-carInternal_Plan3.xml.gz

    // Program Arguments for DRZ:
    //base Case: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-baseCase_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-baseCase_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.basecase.MultipleAirPollutionValue_DRZ.csv -c scenarios/berlin-v5.5-1pct/DRZ/shapefile_Biao/drz_withoutRA/drz_withoutRA.shp -n scenarios/berlin-v5.5-1pct/input/berlin-v5.5-network.xml.gz
    //Plan1: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase1_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase1_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.policyCase1.MultipleAirPollutionValue_DRZ.csv -c scenarios/berlin-v5.5-1pct/DRZ/shapefile_Biao/drz_withoutRA/drz_withoutRA.shp -n scenarios/berlin-v5.5-1pct/DRZ/input/network-modified-carInternal_Plan1.xml.gz
    //Plan3: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.policyCase3.MultipleAirPollutionValue_DRZ.csv -c scenarios/berlin-v5.5-1pct/DRZ/shapefile_Biao/drz_withoutRA/drz_withoutRA.shp -n scenarios/berlin-v5.5-1pct/DRZ/input/network-modified-carInternal_Plan3.xml.gz

    //Program Arguments for BB:
    //base Case: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-baseCase_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-baseCase_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.basecase.MultipleAirPollutionValue_BB.csv -c /home/tumtse/Documents/haowu/DRZ/events_Biao/shapefiles/boundingbox/2-time-DRZ.shp -n scenarios/berlin-v5.5-1pct/input/berlin-v5.5-network.xml.gz
    //Plan1: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase1_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase1_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.policyCase1.MultipleAirPollutionValue_BB.csv -c /home/tumtse/Documents/haowu/DRZ/events_Biao/shapefiles/boundingbox/2-time-DRZ.shp -n scenarios/berlin-v5.5-1pct/DRZ/input/network-modified-carInternal_Plan1.xml.gz
    //Plan3: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.policyCase3.MultipleAirPollutionValue_BB.csv -c /home/tumtse/Documents/haowu/DRZ/events_Biao/shapefiles/boundingbox/2-time-DRZ.shp -n scenarios/berlin-v5.5-1pct/DRZ/input/network-modified-carInternal_Plan3.xml.gz

    //Program Arguments for BB:
    //base Case: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-baseCase_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-baseCase_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.basecase.MultipleAirPollutionValue_Rectangle.csv -c /home/tumtse/Documents/haowu/DRZ/events_Biao/shapefiles/rectangle/rectangle.shp -n scenarios/berlin-v5.5-1pct/input/berlin-v5.5-network.xml.gz
    //Plan1: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase1_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase1_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.policyCase1.MultipleAirPollutionValue_Rectangle.csv -c /home/tumtse/Documents/haowu/DRZ/events_Biao/shapefiles/rectangle/rectangle.shp -n scenarios/berlin-v5.5-1pct/DRZ/input/network-modified-carInternal_Plan1.xml.gz
    //Plan3: -e /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz -o /home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.policyCase3.MultipleAirPollutionValue_Rectangle.csv -c /home/tumtse/Documents/haowu/DRZ/events_Biao/shapefiles/rectangle/rectangle.shp -n scenarios/berlin-v5.5-1pct/DRZ/input/network-modified-carInternal_Plan3.xml.gz
    // --- haowu ---
    public static void main(String[] args) throws IOException {

        var analysis = new GenerateMultipleAirPollutionValue();
        JCommander.newBuilder().addObject(analysis).build().parse(args);
        analysis.run();
    }

    public void run() throws IOException {

        List<List<Tuple<String, Map<Pollutant, Double>>>> varList = new ArrayList<>();

        List<Double> timeList = new ArrayList<>();
        int i;
        for(i=1; i<37; i++){
            double time = 3600*i;
            timeList.add(time);
        }

        int ii;
        for(ii=1; ii<37; ii++){
            int finalIi = ii;
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
                            zoneGeometries.put((String) feature.getAttribute("plz"), (Geometry) feature.getDefaultGeometry());
                        }
                        Geometry areaGeometry = zoneGeometries.get(plz);

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

                        manager.addHandler(new Handler(pollution, timeList.get(finalIi -1), linkList));
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
            varList.add(pollutionPerRun);
        }

        // create the header out of the pollution map of the first run. Assuming, all runs emitted the same set of pollutants
        var csvHeader = varList.get(0).get(0).getSecond().keySet().stream()
                .map(pollutant -> pollutant.toString())
                .collect(Collectors.toList());

        csvHeader.add(0, "timeBinStartTime");


        try (Writer writer = Files.newBufferedWriter(Paths.get(outputFile))) {
            try (CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
                printer.printRecord(csvHeader);

                int iii = 1;
                for(var pollutionPerRun: varList) {
                    for (var tuple : pollutionPerRun) {


                        double timeBinStartTime = 3600*iii;
                        printer.print(timeBinStartTime);
                        iii=iii+1;

                        for (Double value : tuple.getSecond().values()) {
                            printer.print(value);
                        }
                        printer.println();
                    }
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
        double time;
        private final List<String> linkList;

        private Handler(Map<Pollutant, Double> pollution, double time, List<String> linkList) {
            this.pollution = pollution;
            this.time = time;
            this.linkList = linkList;
        }

        @Override
        public void handleEvent(ColdEmissionEvent event) {

            if (!linkList.contains(event.getLinkId().toString())) {
                return; // skip if the link is not in the area
            }
            for (Map.Entry<Pollutant, Double> pollutant : event.getColdEmissions().entrySet()) {
                if(event.getTime()>(time-3600)&event.getTime()<=time) {
                    pollution.merge(pollutant.getKey(), pollutant.getValue(), Double::sum);
                }else{
                    return;
                }
            }
        }

        @Override
        public void handleEvent(WarmEmissionEvent event) {

            if (!linkList.contains(event.getLinkId().toString())) {
                return; // skip if the link is not in the area
            }
            for (Map.Entry<Pollutant, Double> pollutant : event.getWarmEmissions().entrySet()) {
                if(event.getTime()>(time-3600)&event.getTime()<=time) {
                    pollution.merge(pollutant.getKey(), pollutant.getValue(), Double::sum);
                }else{
                    //break the for loop
                    return;
                }
            }
        }
    }

}
