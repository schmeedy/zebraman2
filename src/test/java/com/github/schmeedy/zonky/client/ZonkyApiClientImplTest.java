package com.github.schmeedy.zonky.client;

import com.github.schmeedy.zonky.client.filter.ResourceFilter;
import com.github.schmeedy.zonky.client.model.Loan;
import com.github.schmeedy.zonky.client.order.OrderSpec;
import com.github.schmeedy.zonky.client.paging.PageRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static com.github.schmeedy.zonky.client.paging.PageRequest.FIRST_20_ITEMS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class ZonkyApiClientImplTest {

    private static final URI BASE_URI = URI.create("https://api.zonky.cz");
    private static final String LOANS_URI = BASE_URI.toString() + "/loans/marketplace";

    private RestTemplate template;

    private ZonkyApiClient apiClient;

    private MockRestServiceServer server;

    @Before
    public void setUp() {
        template = new RestTemplate();
        server = MockRestServiceServer.bindTo(template).build();

        RestTemplateBuilder builder = mock(RestTemplateBuilder.class);
        when(builder.build()).thenReturn(template);

        apiClient = new ZonkyApiClientImpl(BASE_URI, builder);
    }

    @Test
    public void loanRequestShouldApplyPagingHeaders() {
        PageRequest pr = new PageRequest(0, 5);
        server
            .expect(header("X-Page", String.valueOf(pr.getPageIndex())))
            .andExpect(header("X-Size", String.valueOf(pr.getPageSize())))
            .andRespond(withSuccess(new ClassPathResource("/loans.json"), APPLICATION_JSON));

        apiClient.getLoans(ResourceFilter.EMPTY, OrderSpec.EMPTY, pr);
        server.verify();
    }

    @Test
    public void loanRequestShouldApplyOrderSpecHeaders() {
        OrderSpec order = Loan.DATE_PUBLISHED.desc().append(Loan.NAME.asc());
        server
                .expect(header("X-Order", "-datePublished,name"))
                .andRespond(withSuccess(new ClassPathResource("/loans.json"), APPLICATION_JSON));

        apiClient.getLoans(ResourceFilter.EMPTY, order, FIRST_20_ITEMS);
        server.verify();
    }

    @Test
    public void loanRequestShouldApplyFilters() {
        ResourceFilter filter = Loan.ID.eq(42);
        server
                .expect(requestTo(LOANS_URI + "?id__eq=42"))
                .andRespond(withSuccess(new ClassPathResource("/loans.json"), APPLICATION_JSON));

        apiClient.getLoans(filter, OrderSpec.EMPTY, FIRST_20_ITEMS);
        server.verify();
    }
}