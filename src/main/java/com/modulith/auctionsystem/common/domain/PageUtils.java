package com.modulith.auctionsystem.common.domain;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageUtils {

    private PageUtils() {
    }

    public static Pageable makePageable(Pageable pageable) {
        return makePageable(pageable, "id");
    }

    public static Pageable makePageable(Pageable pageable, String defaultSortProperty) {
        if (pageable.isUnpaged()) {
            return PageRequest.of(0, 10, Sort.by(defaultSortProperty).ascending());
        }
        int pageNo = pageable.getPageNumber();
        int pageSize = pageable.getPageSize() <= 0 ? 10 : pageable.getPageSize();
        Sort sort = pageable.getSort().isUnsorted() ? Sort.by(defaultSortProperty).ascending() : pageable.getSort();
        return PageRequest.of(pageNo, pageSize, sort);
    }
}
