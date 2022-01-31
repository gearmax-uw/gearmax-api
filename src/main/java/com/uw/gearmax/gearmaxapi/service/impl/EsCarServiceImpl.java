package com.uw.gearmax.gearmaxapi.service.impl;

import com.uw.gearmax.gearmaxapi.domain.es.EsCar;
import com.uw.gearmax.gearmaxapi.repository.es.EsCarRepository;
import com.uw.gearmax.gearmaxapi.service.EsCarService;
import com.uw.gearmax.gearmaxapi.util.CommonSymbol;
import com.uw.gearmax.gearmaxapi.util.CommonUtility;
import com.uw.gearmax.gearmaxapi.util.EsSearchKey;
import com.uw.gearmax.gearmaxapi.util.UrlParameter;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EsCarServiceImpl extends CommonServiceImpl implements EsCarService {

    @Autowired
    private EsCarRepository esCarRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    @Override
    public List<EsCar> listCarsWithQuery(Query searchQuery) {
        SearchHits<EsCar> esCarSearchHits = elasticsearchRestTemplate.search(searchQuery, EsCar.class);
        List<EsCar> esCars = esCarSearchHits.stream().map(hit -> hit.getContent()).collect(Collectors.toList());
        return esCars;
    }

    @Override
    public List<EsCar> listCarsWithDynamicQuery(Map<String, String> queryMap) {
        Pageable pageable = getPageable(queryMap);
        BoolQueryBuilder boolQueryBuilder = buildBoolSearchQuery(queryMap);
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageable)
                .build();
        return listCarsWithQuery(searchQuery);
    }

    private BoolQueryBuilder buildBoolSearchQuery(Map<String, String> queryMap) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

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

        if (StringUtils.isNotEmpty(transmissionDisplay)) {
            String convertedTransmissionDisplay = CommonUtility.convertUrlParamValue(transmissionDisplay);
            boolQueryBuilder.must(QueryBuilders.termQuery(EsSearchKey.TRANSMISSION_DISPLAY.val(), convertedTransmissionDisplay));
        }
        return boolQueryBuilder;
    }
}
