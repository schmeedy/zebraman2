package com.github.schmeedy.zonky.client.paging;

import org.springframework.http.HttpHeaders;

/**
 * Specifies paging params for Zonky API resources
 */
public class PageRequest {
    public static final PageRequest FIRST_20_ITEMS = new PageRequest(0, 20);

    private int pageIndex;
    private int pageSize;

    /**
     * @param pageIndex page index (0-based)
     * @param pageSize number of items per page
     */
    public PageRequest(int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    /**
     * Appends HTTP headers with this page props to the {@link HttpHeaders} builder
     * and returns it back for chaining.
     */
    public HttpHeaders applyTo(HttpHeaders headers) {
        headers.set("X-Page", String.valueOf(pageIndex));
        headers.set("X-Size", String.valueOf(pageSize));
        return headers;
    }

    /**
     * Constructs {@link PageRequest} for the next page
     */
    public PageRequest nextPage() {
        return new PageRequest(pageIndex + 1, pageSize);
    }

    /**
     * Returns page index (0-based)
     */
    public int getPageIndex() {
        return pageIndex;
    }

    /**
     * Returns number of items per page
     */
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PageRequest that = (PageRequest) o;

        if (pageIndex != that.pageIndex) return false;
        return pageSize == that.pageSize;
    }

    @Override
    public int hashCode() {
        int result = pageIndex;
        result = 31 * result + pageSize;
        return result;
    }

    @Override
    public String toString() {
        return "PageRequest{" +
                "pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                '}';
    }
}
