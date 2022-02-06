package com.uw.gearmax.gearmaxapi.service;

import com.uw.gearmax.gearmaxapi.domain.es.EsCar;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.List;
import java.util.Map;

public interface EsCarService {

    List<EsCar> listCarsWithQuery(Query query);

    List<EsCar> listCarsWithDynamicQuery(Map<String, String> queryMap);
}
