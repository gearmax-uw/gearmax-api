package com.uw.gearmax.gearmaxapi.repository.es;

import com.uw.gearmax.gearmaxapi.domain.es.EsCar;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsCarRepository extends ElasticsearchRepository<EsCar, Long> {

}
