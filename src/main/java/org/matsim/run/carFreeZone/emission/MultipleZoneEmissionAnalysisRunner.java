package org.matsim.run.carFreeZone.emission;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MultipleZoneEmissionAnalysisRunner {

    public static void main(String[] args) throws IOException {
        // Path to the CSV file containing the postal codes (plz)
        String csvFile = "/home/tumtse/Documents/haowu/DRZ/events_Biao/shapefiles/emission-analysis-zones/emission-analysis-zones_plz.csv";

        try (CSVParser parser = new CSVParser(new FileReader(csvFile), CSVFormat.DEFAULT.withHeader())) {
            // Iterate through the rows of the CSV file
            for (var record : parser) {
                // Retrieve the postal code (plz) from the current row
                String plz = record.get("plz");

                // Prepare the arguments for calling GenerateSingleAirPollutionValueWithinShape
                String outputFile = "/home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/emission/zonal-emission-analysis/berlin-v5.5-1pct.emission.events.offline.SingleAirPollutionValue_" + plz + ".csv";
                List<String> analysisArgs = Arrays.asList(
                        "-e", "/home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz",
                        "-o", outputFile,
                        "-c", "/home/tumtse/Documents/haowu/DRZ/events_Biao/shapefiles/emission-analysis-zones/emission-analysis-zones.shp",
                        "-p", plz,
                        "-n", "scenarios/berlin-v5.5-1pct/DRZ/input/network-modified-carInternal_Plan3.xml.gz"
                );

                // Convert the arguments list to an array
                String[] analysisArgsArray = analysisArgs.toArray(new String[0]);

                // Call the GenerateSingleAirPollutionValueWithinShape class with the prepared arguments
                GenerateSingleAirPollutionValueWithinShape.main(analysisArgsArray);
            }
        }
    }
}
