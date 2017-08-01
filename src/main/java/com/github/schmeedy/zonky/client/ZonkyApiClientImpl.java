package com.github.schmeedy.zonky.client;

import com.github.schmeedy.zonky.client.filter.ResourceFilter;
import com.github.schmeedy.zonky.client.model.Loan;
import com.github.schmeedy.zonky.client.order.OrderSpec;
import com.github.schmeedy.zonky.client.paging.Page;
import com.github.schmeedy.zonky.client.paging.PageRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Arrays;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;
import static org.springframework.http.HttpMethod.GET;

/**
 * {@link RestTemplate} based implementation of {@link ZonkyApiClient}
 */
@Component
public class ZonkyApiClientImpl implements ZonkyApiClient {

    private URI apiBaseUri;
    private RestTemplate restTemplate;

    public ZonkyApiClientImpl(@Value("${http.api-endpoint}") URI apiBaseUri, RestTemplateBuilder restTemplateBuilder) {
        this.apiBaseUri = notNull(apiBaseUri);
        this.restTemplate = notNull(restTemplateBuilder).build();
    }

    @Override
    public Page<Loan> getNewLoansSince(ZonedDateTime since, PageRequest pr) {
        notNull(since);
        notNull(pr);

        return getLoans(Loan.DATE_PUBLISHED.gt(since), Loan.DATE_PUBLISHED.desc(), pr);
    }


    @Override
    public Page<Loan> getLoans(ResourceFilter filter, OrderSpec order, PageRequest pr) {
        return getForPage("/loans/marketplace", filter, order, pr, Loan[].class);
    }

    /**
     * Executes a GET request against a paged resource and returns items on the specified page.
     *
     * @param resourcePath path of the resource to request
     * @param filter filter to apply to this request
     * @param order order to apply to this request
     * @param pageRequest paging params
     * @param resultType array type the response can be mapped to
     * @param <E> type of individual item on the page
     * @return page with items
     */
    private <E> Page<E> getForPage(String resourcePath, ResourceFilter filter, OrderSpec order, PageRequest pageRequest, Class<E[]> resultType) {
        notEmpty(resourcePath);
        notNull(filter);
        notNull(order);
        notNull(pageRequest);
        notNull(resultType);

        HttpHeaders headers = order.applyTo(pageRequest.applyTo(new HttpHeaders()));

        ResponseEntity<E[]> pageData = restTemplate.exchange(
            getFullUri(resourcePath, filter),
            GET,
            new HttpEntity<>(headers),
            resultType);

        return new Page<>(Arrays.asList(pageData.getBody()), pageRequest);
    }

    /**
     * Constructs absolute URI for the specified resource
     *
     * @param resourcePath path of the resource to request
     * @param filter filter to applyTo to a list-style resource
     */
    private URI getFullUri(String resourcePath, ResourceFilter filter) {
        notEmpty(resourcePath);
        notNull(filter);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUri(apiBaseUri).path(resourcePath);
        return filter.applyTo(uriBuilder).build().toUri();
    }
}
