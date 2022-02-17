package com.uw.gearmax.gearmaxapi.service.impl;

import com.uw.gearmax.gearmaxapi.domain.es.EsCar;
import com.uw.gearmax.gearmaxapi.error.BusinessException;
import com.uw.gearmax.gearmaxapi.error.EmBusinessError;
import com.uw.gearmax.gearmaxapi.repository.es.EsCarRepository;
import com.uw.gearmax.gearmaxapi.service.EsCarService;
import com.uw.gearmax.gearmaxapi.util.CommonSymbol;
import com.uw.gearmax.gearmaxapi.util.CommonUtility;
import com.uw.gearmax.gearmaxapi.util.EsSearchKey;
import com.uw.gearmax.gearmaxapi.util.UrlParameter;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EsCarServiceImpl extends CommonServiceImpl implements EsCarService {

    @Autowired
    private EsCarRepository esCarRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Override
    public EsCar getCarById(Long id) throws BusinessException {
        Optional<EsCar> optionalEsCar = esCarRepository.findById(id);
        if (!optionalEsCar.isPresent()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,
                    "Car to be obtained does not exist");
        }
        return optionalEsCar.get();
    }

    @Override
    public List<EsCar> listCarsWithQuery(Query searchQuery) {
        SearchHits<EsCar> esCarSearchHits = elasticsearchOperations.search(searchQuery, EsCar.class);
        List<EsCar> esCars = esCarSearchHits.stream().map(hit -> hit.getContent()).collect(Collectors.toList());
        return esCars;
    }

    @Override
    public SearchHits<EsCar> listSearchHitsOfCarsWithDynamicQuery(Map<String, String> queryMap) {
        Pageable pageable = getPageable(queryMap);
        QueryBuilder queryBuilder = null;
        if (!queryMap.containsKey(UrlParameter.SEARCH.val())) {
            queryBuilder = buildBoolSearchQuery(queryMap);
        } else {
            queryBuilder = buildMultiMatchQuery(queryMap);
        }
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(pageable)
                .build();
        return elasticsearchOperations.search(searchQuery, EsCar.class);
    }

    @Override
    public List<EsCar> listCarsWithDynamicQuery(Map<String, String> queryMap) {
        Pageable pageable = getPageable(queryMap);
        QueryBuilder queryBuilder = null;
        if (!queryMap.containsKey(UrlParameter.SEARCH.val())) {
            queryBuilder = buildBoolSearchQuery(queryMap);
        } else {
            queryBuilder = buildMultiMatchQuery(queryMap);
        }
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(pageable)
                .build();
        return listCarsWithQuery(searchQuery);
    }

    private MultiMatchQueryBuilder buildMultiMatchQuery(Map<String, String> queryMap) {
        String keywords = queryMap.getOrDefault(UrlParameter.SEARCH.val(), "");
        MultiMatchQueryBuilder multiMatchQueryBuilder = null;
        if (StringUtils.isNotEmpty(keywords)) {
            String[] keywordArr = keywords.split(CommonSymbol.SPACE.val());
            StringBuilder sb = new StringBuilder();
            for (String keyword : keywordArr) {
                String convertedKeyword = CommonUtility.convertUrlParamValue(keyword);
                sb.append(convertedKeyword).append(" ");
            }
            if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
            multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(sb.toString(), EsSearchKey.BODY_TYPE.val(),
                    EsSearchKey.MAKE_NAME.val(), EsSearchKey.LISTING_COLOR.val(), EsSearchKey.TRANSMISSION_DISPLAY.val(),
                    EsSearchKey.CITY.val(), EsSearchKey.MAJOR_OPTIONS.val(), EsSearchKey.MODEL_NAME.val(),
                    EsSearchKey.WHEEL_SYSTEM.val(), EsSearchKey.TRIM_NAME.val());
//            multiMatchQueryBuilder.operator(Operator.AND);
            multiMatchQueryBuilder.type(MultiMatchQueryBuilder.Type.BEST_FIELDS);
        }
        return multiMatchQueryBuilder;
    }

    private BoolQueryBuilder buildBoolSearchQuery(Map<String, String> queryMap) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        String priceRange = queryMap.getOrDefault(UrlParameter.PRICE.val(), "");
        String bodyType = queryMap.getOrDefault(UrlParameter.BODY_TYPE.val(), "");
        String makeName = queryMap.getOrDefault(UrlParameter.MAKE_NAME.val(), "");
        String listingColor = queryMap.getOrDefault(UrlParameter.LISTING_COLOR.val(), "");
        String yearRange = queryMap.getOrDefault(UrlParameter.YEAR.val(), "");
        String city = queryMap.getOrDefault(UrlParameter.CITY.val(), "");
        Integer mileage = null;
        if (queryMap.containsKey(UrlParameter.MILEAGE.val())) {
            mileage = Integer.parseInt(queryMap.get(UrlParameter.MILEAGE.val()));
        }
        Integer maximumSeating = null;
        if (queryMap.containsKey(UrlParameter.MAXIMUM_SEATING.val())) {
            maximumSeating = Integer.parseInt(queryMap.get(UrlParameter.MAXIMUM_SEATING.val()));
        }
        String transmission = queryMap.getOrDefault(UrlParameter.TRANSMISSION.val(), "");
        String transmissionDisplay = queryMap.getOrDefault(UrlParameter.TRANSMISSION_DISPLAY.val(), "");
        String fuelType = queryMap.getOrDefault(UrlParameter.FUEL_TYPE.val(), "");
        String features = queryMap.getOrDefault(UrlParameter.FEATURES.val(), "");

        if (StringUtils.isNotEmpty(priceRange)) {
            // the given parameter in url will be year = xxxx-yyyy, then the sql condition will be year >= xxxx and year <= yyyy
            int minPrice = Integer.parseInt(priceRange.substring(0, priceRange.indexOf(CommonSymbol.DASH.val())));
            int maxPrice = Integer.parseInt(priceRange.substring(priceRange.indexOf(CommonSymbol.DASH.val()) + 1));
            boolQueryBuilder.must(QueryBuilders.rangeQuery(EsSearchKey.PRICE.val()).gte(minPrice).lte(maxPrice));
        }

        if (StringUtils.isNotEmpty(bodyType)) {
            // if bodyType = "SUV", then the es condition will be bodyType = "SUV"
            // in url, the body type will be xxx-yyy or just xxx, we need to convert the format to match the ones stored in ES server
            String convertedBodyType = CommonUtility.convertUrlParamValue(bodyType);
            boolQueryBuilder.must(QueryBuilders.termQuery(EsSearchKey.BODY_TYPE.val(), convertedBodyType));
        }

        if (StringUtils.isNotEmpty(makeName)) {
            String convertedMakeName = CommonUtility.convertUrlParamValue(makeName);
            boolQueryBuilder.must(QueryBuilders.termQuery(EsSearchKey.MAKE_NAME.val(), convertedMakeName));
        }

        if (StringUtils.isNotEmpty(listingColor)) {
            String convertedListingColor = CommonUtility.convertUrlParamValue(listingColor);
            boolQueryBuilder.must(QueryBuilders.termQuery(EsSearchKey.LISTING_COLOR.val(), convertedListingColor));
        }

        if (StringUtils.isNotEmpty(yearRange)) {
            // the given parameter in url will be year = xxxx-yyyy, then the es condition will be year >= xxxx and year <= yyyy
            int minYear = Integer.parseInt(yearRange.substring(0, yearRange.indexOf(CommonSymbol.DASH.val())));
            int maxYear = Integer.parseInt(yearRange.substring(yearRange.indexOf(CommonSymbol.DASH.val()) + 1));
            boolQueryBuilder.must(QueryBuilders.rangeQuery(EsSearchKey.YEAR.val()).gte(minYear).lte(maxYear));
        }

        if (mileage != null && mileage > 0) {
            // if mileage = 10000, then the es condition will be mileage <= 10000
            boolQueryBuilder.must(QueryBuilders.rangeQuery(EsSearchKey.MILEAGE.val()).lte(mileage));
        }

        if (maximumSeating != null && maximumSeating > 0) {
            boolQueryBuilder.must(QueryBuilders.termQuery(EsSearchKey.MAXIMUM_SEATING.val(), maximumSeating));
        }

        if (StringUtils.isNotEmpty(transmission)) {
            String convertedTransmission = CommonUtility.convertUrlParamValue(transmission);
            boolQueryBuilder.must(QueryBuilders.termQuery(EsSearchKey.TRANSMISSION.val(), convertedTransmission));
        }

        if (StringUtils.isNotEmpty(transmissionDisplay)) {
            String convertedTransmissionDisplay = CommonUtility.convertUrlParamValue(transmissionDisplay);
            boolQueryBuilder.must(QueryBuilders.termQuery(EsSearchKey.TRANSMISSION_DISPLAY.val(), convertedTransmissionDisplay));
        }

        if (StringUtils.isNotEmpty(city)) {
            String convertedCity = CommonUtility.convertUrlParamValue(city);
            // boolQueryBuilder.should(QueryBuilders.termQuery(EsSearchKey.CITY.val(), convertedCity));
            // boolQueryBuilder.must(QueryBuilders.fuzzyQuery(EsSearchKey.CITY.val(), convertedCity));
            boolQueryBuilder.must(QueryBuilders.matchQuery(EsSearchKey.CITY.val(), convertedCity).fuzziness(Fuzziness.AUTO));
        }

        if (StringUtils.isNotEmpty(fuelType)) {
            String convertedFuelType = CommonUtility.convertUrlParamValue(fuelType);
            boolQueryBuilder.must(QueryBuilders.matchQuery(EsSearchKey.FUEL_TYPE.val(), convertedFuelType));
        }

        if (StringUtils.isNotEmpty(features)) {
            String[] featureArr = features.split(CommonSymbol.SPACE.val());
            for (String feature : featureArr) {
                String convertedFeature = CommonUtility.convertUrlParamValue(feature);
                boolQueryBuilder.filter(QueryBuilders.termQuery(EsSearchKey.MAJOR_OPTIONS.val(), convertedFeature));
            }
        }
        return boolQueryBuilder;
    }
}
