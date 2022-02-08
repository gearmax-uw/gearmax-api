package com.uw.gearmax.gearmaxapi.service.impl;

import com.uw.gearmax.gearmaxapi.domain.Car;
import com.uw.gearmax.gearmaxapi.error.BusinessException;
import com.uw.gearmax.gearmaxapi.error.EmBusinessError;
import com.uw.gearmax.gearmaxapi.query.CarSpecificationBuilder;
import com.uw.gearmax.gearmaxapi.query.SearchOperation;
import com.uw.gearmax.gearmaxapi.repository.CarRepository;
import com.uw.gearmax.gearmaxapi.service.CarService;
import com.uw.gearmax.gearmaxapi.util.CommonSymbol;
import com.uw.gearmax.gearmaxapi.util.CommonUtility;
import com.uw.gearmax.gearmaxapi.util.SpecSearchKey;
import com.uw.gearmax.gearmaxapi.util.UrlParameter;
import com.uw.gearmax.gearmaxapi.validator.ValidationResult;
import com.uw.gearmax.gearmaxapi.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CarServiceImpl extends CommonServiceImpl implements CarService {

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private CarRepository carRepository;

    /**
     * Validate the car properties by constraints. If no constraint is violated, then save car to repo.
     * For defined constraints,
     *
     * @see com.uw.gearmax.gearmaxapi.domain.Car
     */
    @Override
    @Transactional
    public Car saveCar(Car car) throws BusinessException {
        ValidationResult result = validator.validate(car);
        if (result.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }

        return carRepository.save(car);
    }

    @Override
    @Transactional
    public Car removeCar(Long id) throws BusinessException {
        // if remove a car does not exist, throw an exception
        Car car = getCarById(id);
        carRepository.deleteById(id);
        return car;
    }

    @Override
    public Page<Car> listCarsByBodyType(String bodyType, Pageable pageable) {
        return carRepository.findAllByBodyType(bodyType, pageable);
    }

    @Override
    public List<Car> listCarsWithSpecification(Specification<Car> spec) {
        return carRepository.findAll(spec);
    }

    @Override
    public Page<Car> listCarsWithSpecificationAndPagination(Specification spec, Pageable pageable) {
        return carRepository.findAll(spec, pageable);
    }

    @Override
    public Optional<Car> getOptionalCarById(Long id) {
        return carRepository.findById(id);
    }

    @Override
    public Car getCarById(Long id) throws BusinessException {
        Optional<Car> optionalCar = getOptionalCarById(id);
        if (!optionalCar.isPresent()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,
                    "Car to be obtained does not exist");
        }
        return optionalCar.get();
    }

    @Override
    public List<Car> listCarsWithDynamicQuery(Map<String, String> queryMap) {
        Pageable pageable = getPageable(queryMap);
        Specification<Car> spec = getCarSpec(queryMap);
        Page<Car> page = listCarsWithSpecificationAndPagination(spec, pageable);
        return page.getContent();
    }

    private Specification<Car> getCarSpec(Map<String, String> queryMap) {
        CarSpecificationBuilder builder = new CarSpecificationBuilder();

        String priceRange = queryMap.getOrDefault(UrlParameter.PRICE.val(), "");
        String bodyType = queryMap.getOrDefault(UrlParameter.BODY_TYPE.val(), "");
        String makeName = queryMap.getOrDefault(UrlParameter.MAKE_NAME.val(), "");
        String listingColor = queryMap.getOrDefault(UrlParameter.LISTING_COLOR.val(), "");
        String yearRange = queryMap.getOrDefault(UrlParameter.YEAR.val(), "");
        Integer mileage = null;
        if (queryMap.containsKey(UrlParameter.MILEAGE.val())) {
            mileage = Integer.parseInt(queryMap.get(UrlParameter.MILEAGE.val()));
        }
        Integer maximumSeating = null;
        if (queryMap.containsKey(UrlParameter.MAXIMUM_SEATING.val())) {
            maximumSeating = Integer.parseInt(queryMap.get(UrlParameter.MAXIMUM_SEATING.val()));
        }
        String transmissionDisplay = queryMap.getOrDefault(UrlParameter.TRANSMISSION_DISPLAY.val(), "");

        if (StringUtils.isNotEmpty(priceRange)) {
            // the given parameter in url will be year = xxxx-yyyy, then the sql condition will be year >= xxxx and year <= yyyy
            int minPrice = Integer.parseInt(priceRange.substring(0, priceRange.indexOf(CommonSymbol.DASH.val())));
            int maxPrice = Integer.parseInt(priceRange.substring(priceRange.indexOf(CommonSymbol.DASH.val()) + 1));
            builder.with(SpecSearchKey.PRICE.val(), SearchOperation.GREATER_THAN, minPrice);
            builder.with(SpecSearchKey.PRICE.val(), SearchOperation.LESS_THAN, maxPrice);
        }

        if (StringUtils.isNotEmpty(bodyType)) {
            // if bodyType = "SUV", then the sql condition will be bodyType = "SUV"
            String convertedBodyType = CommonUtility.convertUrlParamValue(bodyType);
            builder.with(SpecSearchKey.BODY_TYPE.val(), SearchOperation.EQUALITY, convertedBodyType);
        }

        if (StringUtils.isNotEmpty(makeName)) {
            String convertedMakeName = CommonUtility.convertUrlParamValue(makeName);
            builder.with(SpecSearchKey.MAKE_NAME.val(), SearchOperation.EQUALITY, convertedMakeName);
        }

        if (StringUtils.isNotEmpty(listingColor)) {
            String convertedListingColor = CommonUtility.convertUrlParamValue(listingColor);
            builder.with(SpecSearchKey.LISTING_COLOR.val(), SearchOperation.EQUALITY, convertedListingColor);
        }

        if (StringUtils.isNotEmpty(yearRange)) {
            // the given parameter in url will be year = xxxx-yyyy, then the sql condition will be year >= xxxx and year <= yyyy
            int minYear = Integer.parseInt(yearRange.substring(0, yearRange.indexOf(CommonSymbol.DASH.val())));
            int maxYear = Integer.parseInt(yearRange.substring(yearRange.indexOf(CommonSymbol.DASH.val()) + 1));
            builder.with(SpecSearchKey.YEAR.val(), SearchOperation.GREATER_THAN, minYear);
            builder.with(SpecSearchKey.YEAR.val(), SearchOperation.LESS_THAN, maxYear);
        }

        if (mileage != null && mileage >= 0) {
            // if mileage = 10000, then the sql condition will be mileage <= 10000
            builder.with(SpecSearchKey.MILEAGE.val(), SearchOperation.LESS_THAN, mileage);
        }

        if (maximumSeating != null && maximumSeating > 0) {
            builder.with(SpecSearchKey.MAXIMUM_SEATING.val(), SearchOperation.EQUALITY, maximumSeating);
        }

        if (StringUtils.isNotEmpty(transmissionDisplay)) {
            String convertedTransmissionDisplay = CommonUtility.convertUrlParamValue(transmissionDisplay);
            builder.with(SpecSearchKey.TRANSMISSION_DISPLAY.val(), SearchOperation.EQUALITY, convertedTransmissionDisplay);
        }

        return builder.build();
    }


}
