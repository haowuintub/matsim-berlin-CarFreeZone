package org.matsim.run.carFreeZone.emission.BYIN;


import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.emissions.EmissionModule;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Injector;
import org.matsim.core.events.EventsManagerImpl;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.api.core.v01.Scenario;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class trafficVolumePerLink {


    public static void main(String[] args) throws IOException {

        String rootDirectory = "scenarios/berlin-v5.5-1pct/output/carFreeZone/";
        String runDirectory = "output-berlin-v5.5-1pct-baseCase_200/";
        String runId = "berlin-v5.5-1pct";
        String eventsFile = rootDirectory + runDirectory + runId + ".output_events.xml.gz";
        String outputFile = rootDirectory + runDirectory + "carPerLink.csv";


        Config config = ConfigUtils.loadConfig(rootDirectory + runDirectory + runId + ".output_config.xml");
        config.plans().setInputFile(null);
        config.transit().setTransitScheduleFile(null);
        config.facilities().setInputFile(null);
        config.households().setInputFile(null);
        config.transit().setVehiclesFile(null);
        config.vehicles().setVehiclesFile(null);
        config.network().setInputFile(runId + ".output_network.xml.gz");
        Scenario scenario = ScenarioUtils.loadScenario(config);

        EventsManager eventsManager = new EventsManagerImpl();
        Network network = scenario.getNetwork();

        AbstractModule module = new AbstractModule(){
            @Override
            public void install(){
                bind( Scenario.class ).toInstance( scenario );
                bind( EventsManager.class ).toInstance( eventsManager );
                bind (Network.class).toInstance(network);
                bind( VolumesAnalyzer.class ) ;
            }
        };

        eventsManager.initProcessing();
        MatsimEventsReader matsimEventsReader = new MatsimEventsReader(eventsManager);
        matsimEventsReader.readFile(eventsFile);
        eventsManager.finishProcessing();


        com.google.inject.Injector injector = Injector.createInjector(config, module);

        VolumesAnalyzer analyzer = injector.getInstance(VolumesAnalyzer.class);



        ArrayList<int[]> volumePerHour = new ArrayList<>();
        List<Id> Links = new ArrayList<Id>();

        for (Link link : network.getLinks().values()) {
            Id<Link> linkId = link.getId();
            Links.add(linkId);
            System.out.println(analyzer.getVolumesForLink(linkId));

            volumePerHour.add(analyzer.getVolumesForLink(linkId));
        }

        final Path path = Paths.get(outputFile);
        try (final PrintWriter writer = new PrintWriter(Files.newBufferedWriter(path, StandardOpenOption.CREATE_NEW))) {
            for (int i = 0; i < Links.size(); ++i) {
                writer.println(Links.get(i) + "," + volumePerHour.get(i));
            }
        }








    }



}
