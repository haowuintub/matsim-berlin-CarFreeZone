# ----
library(tidyverse)
library(sf)
library(lubridate)

setwd("/Users/haowu/workspace/playground/matsim-berlin-CarFreeZone/")
runID="output-berlin-v5.5-1pct-baseCase_200"
#runID="output-berlin-v5.5-1pct-policyCase1_200"
#runID="output-berlin-v5.5-1pct-policyCase2_200"




# ----
runDirectory = paste("scenarios/berlin-v5.5-1pct/output/carFreeZone", runID, "berlin-v5.5-1pct.output_trips.csv", sep = "/")

trips <-read_delim(runDirectory,
                            delim=";",
                            # sep = "\t",
                            locale=locale(decimal_mark = "."),
                            col_types = cols(
                              #x = col_double(),
                              #y = col_double()
                              #person = col_double(),
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
# residents
joined_trips_residents <- semi_join(trips, residents)
#view(joined_trips_residents)


joined_trips_residents_1 <- joined_trips_residents %>% 
  mutate(travel_time = trav_time+wait_time) %>% 
  group_by(person) %>%
  mutate(traveled_distance_person=sum(traveled_distance)) %>% 
  mutate(travel_time_person=sum(travel_time))

joined_trips_residents_1 <- joined_trips_residents_1 %>% 
  select(person,traveled_distance_person, travel_time_person)
joined_trips_residents_1 <- distinct(joined_trips_residents_1)


joined_trips_residents_travel_time <- joined_trips_residents_1 %>%
  group_by() %>%
  summarise(mean_travel_time_person = mean(travel_time_person, na.rm = TRUE))

joined_trips_residents_travel_distance <- joined_trips_residents_1 %>%
  group_by() %>%
  summarise(mean_traveled_distance_person = mean(traveled_distance_person, na.rm = TRUE))


#view(joined_trips_residents_1)
view(joined_trips_residents_travel_time)
view(joined_trips_residents_travel_distance)
#write.csv(joined_trips_residents_1,"/Users/haowu/Downloads/residents.csv", row.names = FALSE)


# ----
# workers
joined_trips_workers <- semi_join(trips, workers)
#view(joined_trips_workers)


joined_trips_workers_1 <- joined_trips_workers %>% 
  mutate(travel_time = trav_time+wait_time) %>% 
  group_by(person) %>%
  mutate(traveled_distance_person=sum(traveled_distance)) %>% 
  mutate(travel_time_person=sum(travel_time))

joined_trips_workers_1 <- joined_trips_workers_1 %>% 
  select(person,traveled_distance_person, travel_time_person)
joined_trips_workers_1 <- distinct(joined_trips_workers_1)


joined_trips_workers_travel_time <- joined_trips_workers_1 %>%
  group_by() %>%
  summarise(mean_travel_time_person = mean(travel_time_person, na.rm = TRUE))

joined_trips_workers_travel_distance <- joined_trips_workers_1 %>%
  group_by() %>%
  summarise(mean_traveled_distance_person = mean(traveled_distance_person, na.rm = TRUE))


#view(joined_trips_workers_1)
view(joined_trips_workers_travel_time)
view(joined_trips_workers_travel_distance)


# ----
# visitors
joined_trips_visitors <- semi_join(trips, visitors)
#view(joined_trips_visitors)


joined_trips_visitors_1 <- joined_trips_visitors %>% 
  mutate(travel_time = trav_time+wait_time) %>% 
  group_by(person) %>%
  mutate(traveled_distance_person=sum(traveled_distance)) %>% 
  mutate(travel_time_person=sum(travel_time))

joined_trips_visitors_1 <- joined_trips_visitors_1 %>% 
  select(person,traveled_distance_person, travel_time_person)
joined_trips_visitors_1 <- distinct(joined_trips_visitors_1)


joined_trips_visitors_travel_time <- joined_trips_visitors_1 %>%
  group_by() %>%
  summarise(mean_travel_time_person = mean(travel_time_person, na.rm = TRUE))

joined_trips_visitors_travel_distance <- joined_trips_visitors_1 %>%
  group_by() %>%
  summarise(mean_traveled_distance_person = mean(traveled_distance_person, na.rm = TRUE))


#view(joined_trips_visitors_1)
view(joined_trips_visitors_travel_time)
view(joined_trips_visitors_travel_distance)


# ----
# passers
joined_trips_passers <- semi_join(trips, passers)
#view(joined_trips_passers)


joined_trips_passers_1 <- joined_trips_passers %>% 
  mutate(travel_time = trav_time+wait_time) %>% 
  group_by(person) %>%
  mutate(traveled_distance_person=sum(traveled_distance)) %>% 
  mutate(travel_time_person=sum(travel_time))

joined_trips_passers_1 <- joined_trips_passers_1 %>% 
  select(person,traveled_distance_person, travel_time_person)
joined_trips_passers_1 <- distinct(joined_trips_passers_1)


joined_trips_passers_travel_time <- joined_trips_passers_1 %>%
  group_by() %>%
  summarise(mean_travel_time_person = mean(travel_time_person, na.rm = TRUE))

joined_trips_passers_travel_distance <- joined_trips_passers_1 %>%
  group_by() %>%
  summarise(mean_traveled_distance_person = mean(traveled_distance_person, na.rm = TRUE))


#view(joined_trips_passers_1)
view(joined_trips_passers_travel_time)
view(joined_trips_passers_travel_distance)


# ----
# nonAffectedAgents
joined_trips_nonAffectedAgents <- semi_join(trips, nonAffectedAgents)
#view(joined_trips_nonAffectedAgents)


joined_trips_nonAffectedAgents_1 <- joined_trips_nonAffectedAgents %>% 
  mutate(travel_time = trav_time+wait_time) %>% 
  group_by(person) %>%
  mutate(traveled_distance_person=sum(traveled_distance)) %>% 
  mutate(travel_time_person=sum(travel_time))

joined_trips_nonAffectedAgents_1 <- joined_trips_nonAffectedAgents_1 %>% 
  select(person,traveled_distance_person, travel_time_person)
joined_trips_nonAffectedAgents_1 <- distinct(joined_trips_nonAffectedAgents_1)


joined_trips_nonAffectedAgents_travel_time <- joined_trips_nonAffectedAgents_1 %>%
  group_by() %>%
  summarise(mean_travel_time_person = mean(travel_time_person, na.rm = TRUE))

joined_trips_nonAffectedAgents_travel_distance <- joined_trips_nonAffectedAgents_1 %>%
  group_by() %>%
  summarise(mean_traveled_distance_person = mean(traveled_distance_person, na.rm = TRUE))


#view(joined_trips_nonAffectedAgents_1)
view(joined_trips_nonAffectedAgents_travel_time)
view(joined_trips_nonAffectedAgents_travel_distance)




# ----
# allAgents
joined_trips_allAgents <- trips
#view(joined_trips_allAgents)


joined_trips_allAgents_1 <- joined_trips_allAgents %>% 
  mutate(travel_time = trav_time+wait_time) %>% 
  group_by(person) %>%
  mutate(traveled_distance_person=sum(traveled_distance)) %>% 
  mutate(travel_time_person=sum(travel_time))

joined_trips_allAgents_1 <- joined_trips_allAgents_1 %>% 
  select(person,traveled_distance_person, travel_time_person)
joined_trips_allAgents_1 <- distinct(joined_trips_allAgents_1)


joined_trips_allAgents_travel_time <- joined_trips_allAgents_1 %>%
  group_by() %>%
  summarise(mean_travel_time_person = mean(travel_time_person, na.rm = TRUE))

joined_trips_allAgents_travel_distance <- joined_trips_allAgents_1 %>%
  group_by() %>%
  summarise(mean_traveled_distance_person = mean(traveled_distance_person, na.rm = TRUE))


#view(joined_trips_allAgents_1)
view(joined_trips_allAgents_travel_time)
view(joined_trips_allAgents_travel_distance)




# ----
joined_trips_residents_travel_time_value <- as.numeric(unlist(joined_trips_residents_travel_time[1,1]))
joined_trips_residents_travel_distance_value <- as.numeric(unlist(joined_trips_residents_travel_distance[1,1]))

joined_trips_workers_travel_time_value <- as.numeric(unlist(joined_trips_workers_travel_time[1,1]))
joined_trips_workers_travel_distance_value <- as.numeric(unlist(joined_trips_workers_travel_distance[1,1]))

joined_trips_visitors_travel_time_value <- as.numeric(unlist(joined_trips_visitors_travel_time[1,1]))
joined_trips_visitors_travel_distance_value <- as.numeric(unlist(joined_trips_visitors_travel_distance[1,1]))

joined_trips_passers_travel_time_value <- as.numeric(unlist(joined_trips_passers_travel_time[1,1]))
joined_trips_passers_travel_distance_value <- as.numeric(unlist(joined_trips_passers_travel_distance[1,1]))

joined_trips_nonAffectedAgents_travel_time_value <- as.numeric(unlist(joined_trips_nonAffectedAgents_travel_time[1,1]))
joined_trips_nonAffectedAgents_travel_distance_value <- as.numeric(unlist(joined_trips_nonAffectedAgents_travel_distance[1,1]))

joined_trips_allAgents_travel_time_value <- as.numeric(unlist(joined_trips_allAgents_travel_time[1,1]))
joined_trips_allAgents_travel_distance_value <- as.numeric(unlist(joined_trips_allAgents_travel_distance[1,1]))

# ----
number_residents <- count(residents, "person")
number_residents <- as.numeric(unlist(number_residents[1,2]))

number_workers <- count(workers, "person")
number_workers <- as.numeric(unlist(number_workers[1,2]))

number_visitors <- count(visitors, "person")
number_visitors <- as.numeric(unlist(number_visitors[1,2]))

number_passers <- count(passers, "person")
number_passers <- as.numeric(unlist(number_passers[1,2]))

number_nonAffectedAgents <- count(nonAffectedAgents, "person")
number_nonAffectedAgents <- as.numeric(unlist(number_nonAffectedAgents[1,2]))

# ----
agent_group <- c('residents','workers','visitors','passers','nonAffectedAgents','allAgents')
number_agent_group <- c(number_residents, number_workers, number_visitors, number_passers, number_nonAffectedAgents, 49054)
travel_time <- c(joined_trips_residents_travel_time_value, joined_trips_workers_travel_time_value, joined_trips_visitors_travel_time_value, joined_trips_passers_travel_time_value, joined_trips_nonAffectedAgents_travel_time_value, joined_trips_allAgents_travel_time_value)
travel_distance <- c(joined_trips_residents_travel_distance_value, joined_trips_workers_travel_distance_value, joined_trips_visitors_travel_distance_value, joined_trips_passers_travel_distance_value, joined_trips_nonAffectedAgents_travel_distance_value, joined_trips_allAgents_travel_distance_value)

output_value <- data.frame(agent_group, number_agent_group, travel_time, travel_distance)
colnames(output_value) <- c("agent_type", "number_agent_group", "travel_time", "travel_time")
outputDirectory = paste("scenarios/berlin-v5.5-1pct/output/carFreeZone", runID, "MobilityAnalysis_perDay.csv", sep = "/")
write.csv(output_value, outputDirectory, row.names = FALSE)
