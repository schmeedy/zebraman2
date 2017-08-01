package com.github.schmeedy.zonky.client.paging;

import java.util.List;

/**
 * Response from a paged API resource
 *
 * @param <T> type of individual items on this page
 */
public class Page<T> {
    private List<T> items;
    private PageRequest request;

    /**
     * @param items items contained in this page
     * @param request original page request
     */
    public Page(List<T> items, PageRequest request) {
        this.items = items;
        this.request = request;
    }

    /**
     * Returns items in this page
     */
    public List<T> getItems() {
        return items;
    }

    /**
     * Returns original page request
     */
    public PageRequest getRequest() {
        return request;
    }

    /**
     * Is this the last page of a paged response?
     */
    public boolean isLastPage() {
        return items.size() < request.getPageSize();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Page<?> page = (Page<?>) o;

        if (items != null ? !items.equals(page.items) : page.items != null) return false;
        return request != null ? request.equals(page.request) : page.request == null;
    }

    @Override
    public int hashCode() {
        int result = items != null ? items.hashCode() : 0;
        result = 31 * result + (request != null ? request.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Page{" +
                "items=" + items +
                ", request=" + request +
                '}';
    }
}
