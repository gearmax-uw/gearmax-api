package com.uw.gearmax.gearmaxapi.repository.es;

import com.uw.gearmax.gearmaxapi.domain.es.EsCar;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface EsCarRepository extends ElasticsearchRepository<EsCar, Long> {

    Optional<EsCar> findById(Long id);
}
