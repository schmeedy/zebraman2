package com.github.schmeedy.zonky.client.paging;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.schmeedy.zonky.client.paging.PageRequest.FIRST_20_ITEMS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class PagingUtilsTest {

    @Test
    public void shouldReturnAllItemsWhenTheyFitOnSinglePage() {
        assertEquals(
                20,
                PagingUtils.getAllRemainingItems(FIRST_20_ITEMS, mockPageProvider(20)).size());
    }

    @Test
    public void shouldReturnAllItemsEvenIfTheyDontFitOnSinglePage() {
        assertEquals(
                70,
                PagingUtils.getAllRemainingItems(FIRST_20_ITEMS, mockPageProvider(70)).size());
    }

    @Test
    public void shouldNotCallProviderAgainAfterReceivingLessThanFullPage() {
        PagingUtils.PageProvider<Integer> mock = mockPageProvider(19);
        PagingUtils.getAllRemainingItems(FIRST_20_ITEMS, mock);
        verify(mock, times(1)).getPage(FIRST_20_ITEMS);
        verifyNoMoreInteractions(mock);
    }

    /**
     * Mocks implementation of page provider that returns pages of items up to the predefined limit
     *
     * @param numberOfItems how many items there are in total
     */
    private PagingUtils.PageProvider<Integer> mockPageProvider(int numberOfItems) {
        PagingUtils.PageProvider<Integer> pageProvider = mock(PagingUtils.PageProvider.class);

        when(pageProvider.getPage(any(PageRequest.class))).thenAnswer((InvocationOnMock invocation) -> {
            PageRequest pr = invocation.getArgumentAt(0, PageRequest.class);
            int firstItemIdx = pr.getPageSize() * pr.getPageIndex();
            int lastItemIdx = firstItemIdx + pr.getPageSize();

            List<Integer> items;
            if (firstItemIdx >= numberOfItems) {
                items = Collections.emptyList();
            } else {
                items = intRange(firstItemIdx, Math.min(numberOfItems, lastItemIdx));
            }

            return new Page<>(items, pr);
        });

        return pageProvider;
    }

    private List<Integer> intRange(int startInclusive, int endExclusive) {
        return IntStream.range(startInclusive, endExclusive).boxed().collect(Collectors.toList());
    }
}