package com.github.schmeedy.zonky.client;

import com.github.schmeedy.zonky.client.filter.ResourceFilter;
import com.github.schmeedy.zonky.client.model.Loan;
import com.github.schmeedy.zonky.client.order.OrderSpec;
import com.github.schmeedy.zonky.client.paging.Page;
import com.github.schmeedy.zonky.client.paging.PageRequest;

import java.time.ZonedDateTime;

/**
 * Synchronous client for Zonky API
 */
public interface ZonkyApiClient {

    /**
     * Returns selected page of {@link Loan}s that appeared in the marketplace since the given time.
     *
     * @param since lower bound of loans publish date (exclusive)
     * @param pageRequest paging parameters
     */
    Page<Loan> getNewLoansSince(ZonedDateTime since, PageRequest pageRequest);


    /**
     * Returns loans from the marketplace
     *
     * @param filter filter to be applied
     * @param order order in which to return loans
     * @param pageRequest paging parameters
     */
    Page<Loan> getLoans(ResourceFilter filter, OrderSpec order, PageRequest pageRequest);
}
