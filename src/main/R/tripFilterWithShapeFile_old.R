library(tidyverse)
library(dplyr)
library(networkD3)
library(sf) #=> geography

#set working directory
setwd("/Users/haowu/workspace/playground/matsim-berlin-CarFreeZone/")

# shapeFile for filtering
shpFile <- "scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/berlin-shp/berlin.shp"

# run-identifiers
#base_runID <- "p-baseDRT100"

#policy_scenario <- "S2"
#policy_runID <- "p2-26"

#runID = "output-berlin-v5.5-1pct-baseCase_200"
#runID = "output-berlin-v5.5-1pct-policyCase1_200"
#runID = "output-berlin-v5.5-1pct-policyCase2_200"
runID = "output-berlin-v5.5-1pct-policyCase3_200"
base_rawData <- read.csv2(paste("scenarios/berlin-v5.5-1pct/output/carFreeZone", runID, "berlin-v5.5-1pct.output_trips.csv", sep = "/"))



#read raw data
#filterBase = FALSE
#if(filterBase){
  #base_rawData <- read.csv2(paste("output-", base_runID, "/", base_runID, ".output_trips.csv", sep = ""))  
#} else {
#  base_rawData <- read.csv2(paste("output-", base_runID, "/", base_runID, ".output_trips-berlin.csv", sep = ""), sep = ";", dec = ",")  
#}

#policy_rawData <- read.csv2(paste(policy_scenario, "/output-", policy_runID, "/", policy_runID, ".output_trips.csv", sep = ""))
berlinShp <- st_read(shpFile)

#if(filterBase){
  ##########################################################################################################################################
  ## this is how to filter the base case data for trips within berlin. You can instead read in the filtered data, once generated for the first time
  # filter for trips with origin / destination within shape. Select columns that are necessary
    (base <- base_rawData
    %>% mutate(wkt = paste("MULTIPOINT((", start_x, " ", start_y, "),(", end_x, " ", end_y, "))", sep =""))
    %>% st_as_sf(wkt = "wkt", crs= st_crs(berlinShp))
    %>% filter(st_intersects(., berlinShp, sparse=FALSE))
    )
    #base <- as_tibble(base) %>% select(trip_id, main_mode)
    write.csv2(base, file=paste("scenarios/berlin-v5.5-1pct/output/carFreeZone", runID, "berlin-v5.5-1pct.output_trips-berlin.csv", sep = "/"), row.names = FALSE)
  ##########################################################################################################################################
#} else {
#    base <- base_rawData %>% select(trip_id, main_mode)
#}
