# ----
library(tidyverse)
library(sf)
library(lubridate)

setwd("/Users/haowu/workspace/playground/matsim-berlin-CarFreeZone/")
#runID="output-berlin-v5.5-1pct-baseCase_200"
#runID="output-berlin-v5.5-1pct-policyCase1_200"
#runID="output-berlin-v5.5-1pct-policyCase2_200"
runID="output-berlin-v5.5-1pct-policyCase3_200"




# ----
runDirectory = paste("scenarios/berlin-v5.5-1pct/output/carFreeZone", runID, "berlin-v5.5-1pct.emissionsgrid_2boundingbox_hour_20.csv", sep = "/")


emissionsgrid <-read_delim(runDirectory,
                            delim="\t",
                            # sep = "\t",
                            locale=locale(decimal_mark = "."),
                            col_types = cols(
                              #x = col_double(),
                              #y = col_double()
                              #person = col_character(),
                              #vehicleId = col_character(),
                              #fromLinkId = col_double(),
                              #start_x = col_double(),
                              #start_y = col_double()
                              #toLinkId = col_double(),
                              #toX = col_double(),
                              #toY = col_double(),
                              #waitTime = col_double(),
                              #arrivalTime = col_double(),
                              #travelTime = col_double(),
                              #traveled_distance = col_double(),
                              #direcTravelDistance_m = col_double()
                            ))

#view(emissionsgrid)
#emissionsgrid


# ----
emissionsgrid_am_reduced <- emissionsgrid %>%
  mutate(id = row_number()) %>%
  filter(timeBinStartTime==28800|timeBinStartTime==32400) %>%
  group_by(x, y) %>%
  summarise(CO2_TOTAL_AM=sum(CO2_TOTAL))

emissionsgrid_pm_reduced <- emissionsgrid %>%
  mutate(id = row_number()) %>%
  filter(timeBinStartTime==61200|timeBinStartTime==64800) %>%
  group_by(x, y) %>%
  summarise(CO2_TOTAL_PM=sum(CO2_TOTAL))

emissionsgrid_output <- inner_join(emissionsgrid_am_reduced, emissionsgrid_pm_reduced)

outputDirectory = paste("scenarios/berlin-v5.5-1pct/output/carFreeZone", runID, "berlin-v5.5-1pct.emissionsgrid_2boundingbox_hour_20_peakHours.csv", sep = "/")
write.csv(emissionsgrid_output, outputDirectory, row.names = FALSE)