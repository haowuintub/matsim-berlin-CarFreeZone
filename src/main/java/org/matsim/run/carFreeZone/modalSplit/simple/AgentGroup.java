package org.matsim.run.carFreeZone.modalSplit.simple;

public class AgentGroup {

    public String name;

    double carInternalCounter = 0;
    double carCounter = 0;
    //double freightCounter = 0;
    double rideCounter = 0;
    double bicycleCounter = 0;
    double walkCounter = 0;
    double ptCounter = 0;
    double sumCounter = 0;

    double modeShare_carInternal = 0;
    double modeShare_car = 0;
    //double modeShare_freight = 0;
    double modeShare_ride = 0;
    double modeShare_bicycle = 0;
    double modeShare_walk = 0;
    double modeShare_pt = 0;

    public AgentGroup(String name) {
        this.name = name;
    }

    void calculate(){
        sumCounter = carInternalCounter + carCounter /*+ freightCounter*/ + rideCounter + bicycleCounter + walkCounter + ptCounter;
        modeShare_carInternal = carInternalCounter/sumCounter;
        modeShare_car = carCounter/sumCounter;
        //modeShare_freight = freightCounter/sumCounter;
        modeShare_ride = rideCounter/sumCounter;
        modeShare_bicycle = bicycleCounter/sumCounter;
        modeShare_walk = walkCounter/sumCounter;
        modeShare_pt = ptCounter/sumCounter;
    }

}
