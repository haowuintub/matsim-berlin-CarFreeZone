library(tidyverse)
library(dplyr)
library(networkD3)
library(sf) #=> geography

library(readxl)
library(tmap)
tmap_mode("view")

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
    (base_start_prepare <- base_rawData
    #%>% mutate(wkt = paste("MULTIPOINT((", start_x, " ", start_y, "),(", end_x, " ", end_y, "))", sep =""))
    %>% mutate(residents = ifelse(startsWith(start_activity_type, "home"), "residents", "non-residents"))
    )
    (base_start <- base_start_prepare
    %>% filter(residents=="residents")
    %>% mutate(wkt_start = paste("POINT(", start_x, " ", start_y, ")", sep =""))
    %>% st_as_sf(wkt = "wkt_start", crs= st_crs(berlinShp))
    %>% filter(st_intersects(., berlinShp, sparse=FALSE))
    )
    (base_start_simplify <- base_start
      %>% select(person)
    )
    # (tm_shape(berlinShp) + tm_polygons(col="SCHLUESSEL") +
    #   tm_shape(base_start_simplify) + tm_dots()
    # )
    library(sfheaders)
    base_start_df <- sf_to_df( base_start_simplify, fill=TRUE )
    (base_start_df <- base_start_df
    %>% select(person)
    )
    base_start_df <- distinct(base_start_df)
    
    (base_end_prepare <- base_rawData
      #%>% mutate(wkt = paste("MULTIPOINT((", start_x, " ", start_y, "),(", end_x, " ", end_y, "))", sep =""))
      %>% mutate(residents = ifelse(startsWith(end_activity_type, "home"), "residents", "non-residents"))
    )
    (base_end <- base_end_prepare
      %>% filter(residents=="residents")
      %>% mutate(wkt_end = paste("POINT(", end_x, " ", end_y, ")", sep =""))
      %>% st_as_sf(wkt = "wkt_end", crs= st_crs(berlinShp))
      %>% filter(st_intersects(., berlinShp, sparse=FALSE))
    )
    (base_end_simplify <- base_end
      %>% select(person)
    )
    # (tm_shape(berlinShp) + tm_polygons(col="SCHLUESSEL") +
    #     tm_shape(base_end_simplify) + tm_dots()
    # )
    #library(sfheaders)
    base_end_df <- sf_to_df( base_end_simplify, fill=TRUE )
    (base_end_df <- base_end_df
      %>% select(person)
    )
    base_end_df <- distinct(base_end_df)
    
    base_start_end_df <- union(base_start_df, base_end_df)
    sapply(base_start_end_df, class)
    #base_start_end_df$x2 <- as.numeric(base_start_end_df$x2)
    #base_start_end_df <- distinct(base_start_end_df)
    
    base <- inner_join(base_rawData, base_start_end_df)
    #base <- as_tibble(base) %>% select(trip_id, main_mode)
    write.csv2(as_tibble(base), file=paste("scenarios/berlin-v5.5-1pct/output/carFreeZone", runID, "berlin-v5.5-1pct.output_trips-berlin.csv", sep = "/"), row.names = FALSE)
  ##########################################################################################################################################
#} else {
#    base <- base_rawData %>% select(trip_id, main_mode)
#}
