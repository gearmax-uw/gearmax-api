package com.uw.gearmax.gearmaxapi.query;

import com.uw.gearmax.gearmaxapi.domain.Car;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CarSpecificationBuilder {

    private final List<SpecSearchCriteria> params;

    public CarSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public CarSpecificationBuilder with(String key, SearchOperation operation, Object value) {
        params.add(new SpecSearchCriteria(key, operation, value));
        return this;
    }

    public Specification<Car> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification> specs = params.stream()
                .map(CarSpecification::new)
                .collect(Collectors.toList());

        Specification result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate() ?
                    Specification.where(result).or(specs.get(i)) : Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}
