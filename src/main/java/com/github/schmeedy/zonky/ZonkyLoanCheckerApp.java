package com.github.schmeedy.zonky;

import com.github.schmeedy.zonky.client.filter.ResourceFilter;
import com.github.schmeedy.zonky.client.model.Loan;
import com.github.schmeedy.zonky.client.paging.Page;
import com.github.schmeedy.zonky.client.paging.PageRequest;
import com.github.schmeedy.zonky.client.ZonkyApiClient;
import com.github.schmeedy.zonky.client.paging.PagingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Periodically checks for new loans appearing on Zonky marketplace and prints them to the console.
 */
@SpringBootApplication
@EnableScheduling
public class ZonkyLoanCheckerApp {
    private static Logger logger = LoggerFactory.getLogger(ZonkyLoanCheckerApp.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext appContext = SpringApplication.run(ZonkyLoanCheckerApp.class, args);
        printLastLoanInTheMarketplace(appContext);
    }

    /*
     * Just for convenience for people watching the log
     */
    private static void printLastLoanInTheMarketplace(ApplicationContext appContext) {
        ZonkyApiClient apiClient = appContext.getBean(ZonkyApiClient.class);

        Page<Loan> page = apiClient.getLoans(
                ResourceFilter.EMPTY,
                Loan.DATE_PUBLISHED.desc(),
                new PageRequest(0, 1));

        page.getItems().forEach((Loan l) -> logger.info("Last loan in the marketplace: {}", l));
    }
}
