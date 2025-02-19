package com.honghung.chatapp.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginationData<T> {
    private int page;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean isSorted;
    private List<T> data;
}
