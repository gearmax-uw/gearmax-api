package com.uw.gearmax.gearmaxapi.service;

import com.uw.gearmax.gearmaxapi.domain.es.EsCar;
import com.uw.gearmax.gearmaxapi.error.BusinessException;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.List;
import java.util.Map;

public interface EsCarService {

    EsCar getCarById(Long id) throws BusinessException;

    List<EsCar> listCarsWithQuery(Query query);

    List<EsCar> listCarsWithDynamicQuery(Map<String, String> queryMap);
}
