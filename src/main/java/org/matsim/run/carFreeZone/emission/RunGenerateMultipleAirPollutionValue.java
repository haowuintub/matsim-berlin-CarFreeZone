package org.matsim.run.carFreeZone.emission;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunGenerateMultipleAirPollutionValue {

    public static void main(String[] args) throws IOException {
        // Define the different argument sets for each case
        String[][] arguments = {
                {
                        "-e", "/home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-baseCase_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz",
                        "-o", "/home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-baseCase_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.basecase.MultipleAirPollutionValue_Berlin.csv",
                        "-c", "/home/tumtse/Documents/haowu/DRZ/events_Biao/shapefiles/berlin-shp/berlin.shp",
                        "-n", "scenarios/berlin-v5.5-1pct/input/berlin-v5.5-network.xml.gz"
                },
                {
                        "-e", "/home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase1_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz",
                        "-o", "/home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase1_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.policyCase1.MultipleAirPollutionValue_Berlin.csv",
                        "-c", "/home/tumtse/Documents/haowu/DRZ/events_Biao/shapefiles/berlin-shp/berlin.shp",
                        "-n", "scenarios/berlin-v5.5-1pct/DRZ/input/network-modified-carInternal_Plan1.xml.gz"
                },
                {
                        "-e", "/home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz",
                        "-o", "/home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.policyCase3.MultipleAirPollutionValue_Berlin.csv",
                        "-c", "/home/tumtse/Documents/haowu/DRZ/events_Biao/shapefiles/berlin-shp/berlin.shp",
                        "-n", "scenarios/berlin-v5.5-1pct/DRZ/input/network-modified-carInternal_Plan3.xml.gz"
                },

                //Rectangle
                {
                        "-e", "/home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-baseCase_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz",
                        "-o", "/home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-baseCase_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.basecase.MultipleAirPollutionValue_Rectangle.csv",
                        "-c", "/home/tumtse/Documents/haowu/DRZ/events_Biao/shapefiles/rectangle/rectangle.shp",
                        "-n", "scenarios/berlin-v5.5-1pct/input/berlin-v5.5-network.xml.gz"
                },
                {
                        "-e", "/home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase1_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz",
                        "-o", "/home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase1_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.policyCase1.MultipleAirPollutionValue_Rectangle.csv",
                        "-c", "/home/tumtse/Documents/haowu/DRZ/events_Biao/shapefiles/rectangle/rectangle.shp",
                        "-n", "scenarios/berlin-v5.5-1pct/DRZ/input/network-modified-carInternal_Plan1.xml.gz"
                },
                {
                        "-e", "/home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz",
                        "-o", "/home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.policyCase3.MultipleAirPollutionValue_Rectangle.csv",
                        "-c", "/home/tumtse/Documents/haowu/DRZ/events_Biao/shapefiles/rectangle/rectangle.shp",
                        "-n", "scenarios/berlin-v5.5-1pct/DRZ/input/network-modified-carInternal_Plan3.xml.gz"
                },

                //DRZ
                {
                        "-e", "/home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-baseCase_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz",
                        "-o", "/home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-baseCase_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.basecase.MultipleAirPollutionValue_DRZ.csv",
                        "-c", "scenarios/berlin-v5.5-1pct/DRZ/shapefile_Biao/drz_withoutRA/drz_withoutRA.shp",
                        "-n", "scenarios/berlin-v5.5-1pct/input/berlin-v5.5-network.xml.gz"
                },
                {
                        "-e", "/home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase1_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz",
                        "-o", "/home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase1_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.policyCase1.MultipleAirPollutionValue_DRZ.csv",
                        "-c", "scenarios/berlin-v5.5-1pct/DRZ/shapefile_Biao/drz_withoutRA/drz_withoutRA.shp",
                        "-n", "scenarios/berlin-v5.5-1pct/DRZ/input/network-modified-carInternal_Plan1.xml.gz"
                },
                {
                        "-e", "/home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/berlin-v5.5-1pct.emission.events.offline.xml.gz",
                        "-o", "/home/tumtse/Documents/haowu/DRZ/events_Biao/output-berlin-v5.5-1pct-policyCase3_200_simple/emission/berlin-v5.5-1pct.emission.events.offline.policyCase3.MultipleAirPollutionValue_DRZ.csv",
                        "-c", "scenarios/berlin-v5.5-1pct/DRZ/shapefile_Biao/drz_withoutRA/drz_withoutRA.shp",
                        "-n", "scenarios/berlin-v5.5-1pct/DRZ/input/network-modified-carInternal_Plan3.xml.gz"
                }
        };

        // Create a thread pool with 9 threads
        ExecutorService executor = Executors.newFixedThreadPool(9);

        for (String[] argSet : arguments) {
            // Submit a new task to the executor for each argument set
            executor.submit(() -> {
                try {
                    //runCommand(argSet);
                    GenerateMultipleAirPollutionValue.main(argSet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        // Initiates an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be accepted
        executor.shutdown();

    }

    private static void runCommand(String[] args) {
        try {
            // Constructing the command
            String javaCommand = "java GenerateMultipleAirPollutionValue";
            for (String arg : args) {
                javaCommand += " " + arg;
            }

            // Executing the command
            Process process = Runtime.getRuntime().exec(javaCommand);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // Read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            String s;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // Read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

            process.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}


