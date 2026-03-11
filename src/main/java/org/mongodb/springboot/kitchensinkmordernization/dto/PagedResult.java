package org.mongodb.springboot.kitchensinkmordernization.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Custom result class
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedResult<T> {
    private List<T> data;
    private long totalCount;
    private int page;
    private int size;
    private int totalPages;

    public static <T> PagedResult<T> of(List<T> data, long totalCount, int page, int size) {
        int totalPages = (int) Math.ceil((double) totalCount / size);
        return new PagedResult<>(data, totalCount, page, size, totalPages);
    }
}
