package com.github.schmeedy.zonky.client.paging;

import java.util.ArrayList;
import java.util.List;

public class PagingUtils {

    /**
     * Get items on any page of a paged resource
     *
     * @param initialPr specifies initial offset in pages and pages size for a single fetch
     * @param pageProvider fetches page data
     * @param <E> type of items in the page
     * @return all items from the specified offset to the end
     */
    public static <E> List<E> getAllRemainingItems(PageRequest initialPr, PageProvider<E> pageProvider) {
        List<E> result = new ArrayList<>();
        PageRequest pr = initialPr;
        Page<E> page;
        do {
            page = pageProvider.getPage(pr);
            result.addAll(page.getItems());
            pr = pr.nextPage();
        } while (!page.isLastPage());
        return result;
    }

    public interface PageProvider<E> {
       Page<E> getPage(PageRequest pageRequest);
    }

    private PagingUtils() {}
}
