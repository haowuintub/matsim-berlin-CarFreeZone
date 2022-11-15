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
#runDirectory = paste("scenarios/berlin-v5.5-1pct/output/carFreeZone", runID, "berlin-v5.5-1pct.output_trips.csv", sep = "/")
runDirectory = paste("scenarios/berlin-v5.5-1pct/output/carFreeZone", runID, "berlin-v5.5-1pct.output_trips-berlin.csv", sep = "/")


trips <-read_delim(runDirectory,
                            delim=";",
                            # sep = "\t",
                            locale=locale(decimal_mark = "."),
                            col_types = cols(
                              #x = col_double(),
                              #y = col_double()
                              person = col_character(),
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

#view(trips)
#trips


residents <-read_delim("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/personInternalIDsList.txt", 
                          delim="\t",
                          # sep = "\t",
                          locale=locale(decimal_mark = "."),
                          col_names = c("person"),
                          col_types = cols(person=col_character())
                          )
#view(residents)
#residents

workers <-read_delim("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/workerIDsList.txt", 
                       delim="\t",
                       # sep = "\t",
                       locale=locale(decimal_mark = "."),
                       col_names = c("person"),
                       col_types = cols(person=col_character())
                       )
#view(workers)
#workers

visitors <-read_delim("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/agentsDoingOtherActivitiesIDsList.txt", 
                     delim="\t",
                     # sep = "\t",
                     locale=locale(decimal_mark = "."),
                     col_names = c("person"),
                     col_types = cols(person=col_character())
                     )
#view(visitors)
#visitors

passers <-read_delim("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/agentsWithoutActivitiesIDsList.txt", 
                      delim="\t",
                      # sep = "\t",
                      locale=locale(decimal_mark = "."),
                      col_names = c("person"),
                      col_types = cols(person=col_character())
                      )
#view(passers)
#passers

nonAffectedAgents <-read_delim("scenarios/berlin-v5.5-1pct/input/carFreeZone/PlanA/IDLists/nonAffectedAgentsIDsList.txt", 
                     delim="\t",
                     # sep = "\t",
                     locale=locale(decimal_mark = "."),
                     col_names = c("person"),
                     col_types = cols(person=col_character())
                     )
#view(nonAffectedAgents)
#nonAffectedAgents




# ----
#dfList <- list(residents,workers,visitors,passers,nonAffectedAgents,trips)

allAgents <- trips %>%
  select(person)

dfList <- list()
dfList[["residents"]] <-residents
dfList[["workers"]] <-workers
dfList[["visitors"]] <-visitors
dfList[["passers"]] <-passers
dfList[["nonAffectedAgents"]] <-nonAffectedAgents
dfList[["allAgents"]] <-trips
names(dfList)

# for(i in 1:length(dfList))
# {
#   thisDataFrame <- semi_join(trips, dfList[[i]])
#   thisDataFrame_new <- thisDataFrame %>%
#     count(longest_distance_mode, name="modeshare_count") %>%
#     mutate(modeshare=modeshare_count/as.numeric(unlist(count(thisDataFrame)[1,1])))
#   
#   name <- paste("joined_trips", names(dfList)[i], sep = "_")
#   assign(name, thisDataFrame_new)
# }


dfList_modalSplit <- list()

for(i in 1:length(dfList))
{
  thisDataFrame <- semi_join(trips, dfList[[i]])
  thisDataFrame_new <- thisDataFrame %>%
    count(longest_distance_mode, name="modeshare_count") %>%
    mutate(modeshare=modeshare_count/as.numeric(unlist(count(thisDataFrame)[1,1])))
  
  #colnames(thisDataFrame_new)[2] <- paste("number of trips", names(dfList)[i], sep = "_")
  colnames(thisDataFrame_new)[2] <- "number of trips"
  #colnames(thisDataFrame_new)[3] <- paste("trip share", names(dfList)[i], sep = "_")
  colnames(thisDataFrame_new)[3] <- "trip share"
  colnames(thisDataFrame_new)[1] <- "mode"
  
  name <- paste("ModalSplit", names(dfList)[i], sep = "_")
  #assign(name, thisDataFrame_new)
  
  dfList_modalSplit[[name]] <-thisDataFrame_new
}

names(dfList_modalSplit)

for(i in 1:length(dfList_modalSplit))
{
  postfix <- paste(names(dfList_modalSplit)[i], ".csv", sep = "")
  outputDirectory = paste("scenarios/berlin-v5.5-1pct/output/carFreeZone", runID, "modalSplit", postfix, sep = "/")
  write.csv(dfList_modalSplit[[i]], outputDirectory, row.names = FALSE)
}

# # ----
# # residents
# joined_trips_residents <- semi_join(trips, residents)
# #view(joined_trips_residents)
# 
# 
# joined_trips_residents_1 <- joined_trips_residents %>% 
#   #group_by(longest_distance_mode) %>%
#   count(longest_distance_mode, name="modeshare_count") %>%
#   mutate(modeshare=modeshare_count/as.numeric(unlist(count(joined_trips_residents)[1,1])))
