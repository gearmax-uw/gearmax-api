package com.uw.gearmax.gearmaxapi.service;

import com.uw.gearmax.gearmaxapi.service.impl.EsCarServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@ExtendWith(MockitoExtension.class)
public class EsCarServiceImplIntegrationTest {

    @Autowired
    @InjectMocks
    private EsCarServiceImpl esCarService;

    @Mock
    private ElasticsearchOperations elasticsearchOperations;

//    @Test
//    void listCarsWithDynamicQueryShouldReturnSatisfiedCars() {
//        Map<String, String> queryMap = new HashMap<>();
//        queryMap.put(UrlParameter.PAGE_SIZE.val(), "1");
//        queryMap.put(UrlParameter.PRICE.val(), "10000-100000");
//        queryMap.put(UrlParameter.BODY_TYPE.val(), "coupe");
//        queryMap.put(UrlParameter.MAKE_NAME.val(), "land-rover");
//        queryMap.put(UrlParameter.LISTING_COLOR.val(), "black");
//        queryMap.put(UrlParameter.YEAR.val(), "2010-2020");
//        queryMap.put(UrlParameter.MILEAGE.val(), "1500");
//        queryMap.put(UrlParameter.MAXIMUM_SEATING.val(), "10");
//        queryMap.put(UrlParameter.FEATURES.val(), "bluetooth+backup-camera");
//
//        EsCar expectedEsCar = new EsCar();
//        expectedEsCar.setPrice(10000);
//        expectedEsCar.setBodyType("Coupe");
//        expectedEsCar.setMakeName("Land Rover");
//        expectedEsCar.setListingColor("Black");
//        expectedEsCar.setYear(2012);
//        expectedEsCar.setMileage(1000);
//        expectedEsCar.setMaximumSeating(5);
//        List<String> options = Arrays.asList("bluetooth", "backup-camera");
//        expectedEsCar.setMajorOptions(options);
//
//        // ElasticsearchOperations elasticsearchOperations = mock(ElasticsearchOperations.class);
//        SearchHits<EsCar> esCarSearchHits = mock(SearchHits.class);
//        when(elasticsearchOperations.search(any(Query.class), eq(EsCar.class))).thenReturn(esCarSearchHits);
//        when(esCarSearchHits.stream()).thenAnswer(i -> Stream.of(esCarSearchHits.getSearchHit(0)));
//
//        List<EsCar> returnedEsCars = esCarService.listCarsWithDynamicQuery(queryMap);
//
//        assertThat(returnedEsCars.get(0)).isEqualTo(expectedEsCar);
//    }
}
