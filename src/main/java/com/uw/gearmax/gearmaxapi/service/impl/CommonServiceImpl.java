package com.uw.gearmax.gearmaxapi.service.impl;

import com.uw.gearmax.gearmaxapi.util.SpecSearchKey;
import com.uw.gearmax.gearmaxapi.util.UrlParameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;

public class CommonServiceImpl {

    public Pageable getPageable(Map<String, String> queryMap) {
        int pageIndex = Integer.parseInt(queryMap.getOrDefault(UrlParameter.PAGE_INDEX.val(), "0"));
        int pageSize = Integer.parseInt(queryMap.getOrDefault(UrlParameter.PAGE_SIZE.val(), "10"));
        String sortField = queryMap.getOrDefault(UrlParameter.SORT_FIELD.val(), "");
        String sortOrder = queryMap.getOrDefault(UrlParameter.SORT_ORDER.val(), "asc");

        // sort by both price and year in descending order
        Sort sort = Sort.by(Sort.Direction.DESC, SpecSearchKey.PRICE.val(), SpecSearchKey.YEAR.val());
        if (isSortFieldAvailable(sortField)) {
            if (StringUtils.equals(sortOrder, Sort.Direction.ASC.name())) {
                // sort in ascending order
                sort = Sort.by(sortField).ascending();
            } else if (StringUtils.equals(sortOrder, Sort.Direction.DESC.name())) {
                // sort in descending order
                sort = Sort.by(sortField).descending();
            }
        }

        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        return pageable;
    }

    /**
     * The results can be sorted by price and year
     */
    private boolean isSortFieldAvailable(String sortField) {
        return StringUtils.equals(sortField, UrlParameter.PRICE.val())
                || StringUtils.equals(sortField, UrlParameter.YEAR.val());
    }
}
