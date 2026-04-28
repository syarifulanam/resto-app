package com.ngulik.resto_app.response;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationMeta {
    private int page;          // halaman saat ini (0-index)
    private int size;          // jumlah data per halaman
    private long totalElements; // total semua data
    private int totalPages;     // total halaman
    private boolean first;      // apakah halaman pertama?
    private boolean last;       // apakah halaman terakhir?

    // Factory method dari Spring Data Page
    public static PaginationMeta from(Page<?> page) {
        return PaginationMeta.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}
