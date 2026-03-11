package org.mongodb.springboot.kitchensinkmordernization.repositories.custom;

import org.mongodb.springboot.kitchensinkmordernization.entites.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface MemberRepositoryCustom {
    Page<Member> findAll(
            String sortField,
            Sort.Direction direction,
            Object lastSortValue,
            Long id,
            int pageSize,
            int pageNumber,
            String searchQuery
    );
}