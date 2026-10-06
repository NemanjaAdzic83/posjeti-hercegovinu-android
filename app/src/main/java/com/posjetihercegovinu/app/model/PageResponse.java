package com.posjetihercegovinu.app.model;

import java.util.List;

// Odgovara backen klasi PageResponse<PlaceDto>
// <T> znaci da klasa radi za bilo koji tip stavki (Place. kasnije Restaurant)
public class PageResponse<T>{

    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private long totalPages;
    private boolean last;

    public List<T> getContent() {
        return content;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public boolean isLast() {
        return last;
    }
}
