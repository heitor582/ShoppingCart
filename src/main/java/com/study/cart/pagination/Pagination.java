package com.study.cart.pagination;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

public record Pagination<T>(
        @JsonProperty("current_page") int currentPage,
        @JsonProperty("per_page") int perPage,
        @JsonProperty("total") long total,
        @JsonProperty("content") List<T> content
) implements Serializable {
    public <R> Pagination<R> map(final Function<T, R> mapper) {
        final List<R> newList = this.content.stream().map(mapper).toList();

        return new Pagination<R>(this.currentPage(), this.perPage(), this.total(), newList);
    }

    public static <T> Pagination<T> from(final Page<T> page){
        return new Pagination<T>(page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getContent()
        );
    }
}
