# ----
library(tidyverse)
library(sf)
library(tmap)
tmap_mode("view")

berlin_bezirke <- st_read("/Users/haowu/Workspace/R/DataScience_HA2/shp-bezirke/bezirke_berlin.shp")

berlin_emissions <-read_delim("/Users/haowu/workspace/playground/matsim-berlin-CarFreeZone/scenarios/berlin-v5.5-1pct/output/carFreeZone/output-berlin-v5.5-1pct-baseCase_200/berlin-v5.5-1pct.emissionsgrid_berlin.csv", 
                            delim="\t",
                            # sep = "\t",
                            locale=locale(decimal_mark = "."),
                            col_types = cols(
                              x = col_double(),
                              y = col_double()
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
                              #travelDistance_m = col_double(),
                              #direcTravelDistance_m = col_double()
                            ))
# berlin_bezirke <- read.table("/Users/haowu/Workspace/R/DataScience_HA2/Output_MATSim_Emission/PlanA/berlin-v5.5-1pct.emissionsgrid_PlanA.csv",sep = "\t", dec=".", header = TRUE, encoding="UTF-8", stringsAsFactors = FALSE)

view(berlin_emissions)
#berlin_emissions




# ----
map = read_sf("/Users/haowu/Workspace/R/DataScience_HA2/shp-bezirke/bezirke_berlin.shp")
berlin_emissions_sf <- st_as_sf(berlin_emissions, coords = c('x', 'y'), crs = st_crs(map))
view(berlin_emissions_sf)

berlin_emissions_sf <- berlin_emissions_sf %>% 
  mutate("CO2 (g/cell, d=30m)" = CO2_TOTAL)


nocarzone <- st_read("/Users/haowu/Workspace/QGIS/MATSim_HA2/NoCarZone_withRoundabout_fixed/NoCarZone_withRoundabout_fixed.shp")
# Plot2: CO2平替 ----
tmap_mode("plot")  #tmap_mode("view")
tm_shape(berlin_bezirke) +
  tm_borders() +
  # tm_polygons() +
  # tm_polygons(col="col_value") +
  # tm_dots(size="size_value", col="col_value")
  tm_shape(berlin_emissions_sf) +
  tm_dots(size=0.01, col="CO2 (g/cell, d=30m)", border.lwd=NA) +
  tm_shape(nocarzone) +
  tm_borders(col="red") +
  tm_layout(legend.width=0.8,
            legend.height=0.8) +
  tm_layout(frame = FALSE, frame.lwd = NA, panel.label.bg.color = NA)
#tm_text(text = "Driving restriction zone")
#tm_add_legend(text = "Driving restriction zone")
#tm_dots(size=0.001, col="N2O", alpha=0.1)
