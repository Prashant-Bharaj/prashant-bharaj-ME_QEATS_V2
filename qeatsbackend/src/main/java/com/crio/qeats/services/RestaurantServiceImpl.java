
/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.services;

import com.crio.qeats.dto.Restaurant;
import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;
import com.crio.qeats.repositoryservices.RestaurantRepositoryService;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RestaurantServiceImpl implements RestaurantService {

  private final Double peakHoursServingRadiusInKms = 3.0;
  private final Double normalHoursServingRadiusInKms = 5.0;
  @Autowired
  private RestaurantRepositoryService restaurantRepositoryService;

  /**
   * Get all the restaurants that are open now within a specific service radius.
   * - For peak hours: 8AM - 10AM, 1PM-2PM, 7PM-9PM
   * - service radius is 3KMs.
   * - All other times, serving radius is 5KMs.
   * - If there are no restaurants, return empty list of restaurants.
   * @param getRestaurantsRequest valid lat/long
   * @param currentTime current time.
   * @return GetRestaurantsResponse object containing a list of open restaurants or an
   *     empty list if none fits the criteria.
   */
  // TODO: CRIO_TASK_MODULE_RESTAURANTSAPI - Implement findAllRestaurantsCloseby.
  // Check RestaurantService.java file for the interface contract.
  @Override
  public GetRestaurantsResponse findAllRestaurantsCloseBy(
    GetRestaurantsRequest getRestaurantsRequest, LocalTime currentTime) {
      Double longitude = getRestaurantsRequest.getLongitude();
      Double latitude = getRestaurantsRequest.getLatitude();
      
      Double servingRadiusInKms;
      
      int hh = currentTime.getHour();
      int mm = currentTime.getMinute();

      if(
         (hh >= 8 && (hh < 10 || (hh == 10 && mm == 0))) 
      || (hh >= 13 && (hh < 14 || (hh == 14 && mm == 0)))
      || (hh >= 19 && (hh < 21 || (hh == 21 && mm == 0)))
      ){
        servingRadiusInKms = peakHoursServingRadiusInKms;
      } else {
        servingRadiusInKms = normalHoursServingRadiusInKms;
      }
      List<Restaurant> restaurants = restaurantRepositoryService.findAllRestaurantsCloseBy(latitude,
          longitude, currentTime, servingRadiusInKms);
      GetRestaurantsResponse response = new GetRestaurantsResponse(restaurants);
      
      return response;
  }
}

