package org.matsim.run.carFreeZone.emission.BYIN;

/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2014 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */
//package org.matsim.ruhrgebiet.analysis;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.analysis.spatial.Grid;
import org.matsim.contrib.analysis.time.TimeBinMap;
import org.matsim.contrib.emissions.Pollutant;
import org.matsim.contrib.emissions.analysis.EmissionGridAnalyzer;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.gis.ShapeFileReader;
import org.opengis.feature.simple.SimpleFeature;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author amit, ihab
 */

public class GenerateAirPollutionSpatialPlots {
    private static final Logger log = Logger.getLogger(GenerateAirPollutionSpatialPlots.class);


    // --- haowu ---
    //BoundingBox:
    /*private static final double xMin = 4588458.659;
    private static final double yMin = 5819286.907;
    private static final double xMax = 4591789.274;
    private static final double yMax = 5820831.663;*/
    //2BoundingBox:
/*	private static final double xMin = 4586793.3515;
	private static final double yMin = 5818514.529;
	private static final double xMax = 4593454.5815;
	private static final double yMax = 5821604.041;*/
    //3BoundingBox:
/*	private static final double xMin = 4583462.7365;
	private static final double yMin = 5816969.773;
	private static final double xMax = 4596785.1965;
	private static final double yMax = 5823148.797;*/
    // --- haowu ---

    // For drz and boundingboxes
    private static final double gridSize = 30.;
    private static final double smoothingRadius = 500.;
    private static final double scaleFactor = 100.;

    // For Berlin
    /*private static final double gridSize = 300.;
    private static final double smoothingRadius = 1500.;
    private static final double scaleFactor = 100.;*/

//    final static String runDir = "scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-baseCase_200/";
//    final static String runDir = "scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-policyCase1_200/";
    final static String runDir = "scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-policyCase3_200/";


    final static String runId = "berlin-v5.5-1pct";
    final static String outDir = runDir;


 /*   @Parameter(names = {"-dir"}, required = true)
    private String runDir = "";

    @Parameter(names = {"-runId"}, required = true)
    private String runId = "";

    @Parameter(names = {"-outDir"})
    private String outDir = "";*/

    // --- haowu ---
    // Program Arguments:
    //baseCase: -dir scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-baseCase_200/ -runId berlin-v5.5-1pct -outDir scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-baseCase_200/
    //Plan1: -dir scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-policyCase1_200/ -runId berlin-v5.5-1pct -outDir scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-policyCase1_200/
    //Plan2: -dir scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-policyCase2_200/ -runId berlin-v5.5-1pct -outDir scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-policyCase2_200/
    //Plan3: -dir scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-policyCase3_200/ -runId berlin-v5.5-1pct -outDir scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-policyCase3_200/
    // --- haowu ---

    private GenerateAirPollutionSpatialPlots() {

    }

    public static void main(String[] args) {

        GenerateAirPollutionSpatialPlots plots = new GenerateAirPollutionSpatialPlots();

        //JCommander.newBuilder().addObject(plots).build().parse(args);

        plots.writeEmissions();
    }

    private void writeEmissions() {

        final String configFile = runDir + runId + ".output_config.xml";
        final String events = runDir + runId + ".emission.events.offline.xml.gz";
        final String outputDir = StringUtils.isBlank(outDir) ? runDir : outDir;
        final String outputFile = outputDir + runId + ".emissionsgrid_boundingbox.csv";

        Config config = ConfigUtils.loadConfig(configFile);
        config.plans().setInputFile(null);
        config.transit().setTransitScheduleFile(null);
        config.facilities().setInputFile(null);
        config.households().setInputFile(null);
        config.transit().setVehiclesFile(null);
        config.vehicles().setVehiclesFile(null);
        config.network().setInputFile(runId + ".output_network.xml.gz");
        config.global().setCoordinateSystem("EPSG:31468");

        Scenario scenario = ScenarioUtils.loadScenario(config);

        double binSize = 3600; // make the bin size bigger than the scenario has seconds
        Network network = scenario.getNetwork();

        EmissionGridAnalyzer analyzer = new EmissionGridAnalyzer.Builder()
                .withGridSize(gridSize)
                .withTimeBinSize(binSize)
                .withNetwork(network)
                .withBounds(createBoundingBox())
                .withSmoothingRadius(smoothingRadius)
                .withCountScaleFactor(scaleFactor)
                .withGridType(EmissionGridAnalyzer.GridType.Hexagonal)
                .build();

        TimeBinMap<Grid<Map<Pollutant, Double>>> timeBins = analyzer.process(events);
        //analyzer.processToJsonFile(events, outputFile + ".json");

        log.info("Writing to csv...");
        writeGridToCSV(timeBins, outputFile);
    }

    private void writeGridToCSV(TimeBinMap<Grid<Map<Pollutant, Double>>> bins, String outputPath) {

        var pollutants = Pollutant.values();

        try (CSVPrinter printer = new CSVPrinter(new FileWriter(outputPath), CSVFormat.TDF)) {

            //print header with all possible pollutants
            printer.print("timeBinStartTime");
            printer.print("x");
            printer.print("y");

            for (var p : pollutants) {
                printer.print(p.toString());
            }
            printer.println();

            //print values if pollutant was not present just print 0 instead
            for (TimeBinMap.TimeBin<Grid<Map<Pollutant, Double>>> bin : bins.getTimeBins()) {
                final double timeBinStartTime = bin.getStartTime();
                for (Grid.Cell<Map<Pollutant, Double>> cell : bin.getValue().getCells()) {

                    printer.print(timeBinStartTime);
                    printer.print(cell.getCoordinate().x);
                    printer.print(cell.getCoordinate().y);

                    for (var p : pollutants) {
                        if (cell.getValue().containsKey(p)) {
                            printer.print(cell.getValue().get(p));
                        } else {
                            printer.print(0);
                        }
                    }
                    printer.println();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Geometry createBoundingBox() {
        // --- haowu shapefile1 ---
    /*    return new GeometryFactory().createPolygon(new Coordinate[]{
                new Coordinate(xMin, yMin), new Coordinate(xMax, yMin),
                new Coordinate(xMax, yMax), new Coordinate(xMin, yMax),
                new Coordinate(xMin, yMin)
        });*/
        // --- haowu shapefile2 : BYIN used in the paper ---
        // same kind of BoundingBox but using my own shapeFile as the BoundingBox
//        String areaShapeFile = "/Users/biao.yin/Documents/MATSIM/Doc/HaoWu_TUBerlin/FreeCarZone/shapeFile/drz_withoutRA/drz_withoutRA.shp"; // not used for analysis
        String areaShapeFile = "/Users/biao.yin/Documents/MATSIM/Doc/HaoWu_TUBerlin/FreeCarZone/shapeFile/analysis_zone/largerAnalysisZone.shp";
//        String areaShapeFile = "/Users/biao.yin/Documents/MATSIM/Doc/HaoWu_TUBerlin/FreeCarZone/shapeFile/berlin-shp/berlin.shp";
        Collection<SimpleFeature> features = (new ShapeFileReader()).readFileAndInitialize(areaShapeFile);

        Map<String, Geometry> zoneGeometries = new HashMap<>();
        for (SimpleFeature feature : features) {
            zoneGeometries.put((String)feature.getAttribute("Name"),(Geometry)feature.getDefaultGeometry());
        }

//        Geometry areaGeometry = zoneGeometries.get(("drz"));
        Geometry areaGeometry = zoneGeometries.get(("LargerAnalysisZone"));
//        Geometry areaGeometry = zoneGeometries.get(("berlin"));
        return areaGeometry;
    }
}
