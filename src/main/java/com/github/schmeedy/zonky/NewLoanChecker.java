package com.github.schmeedy.zonky;

import com.github.schmeedy.zonky.client.ZonkyApiClient;
import com.github.schmeedy.zonky.client.model.Loan;
import com.github.schmeedy.zonky.client.paging.PageRequest;
import com.github.schmeedy.zonky.client.paging.PagingUtils;
import com.github.schmeedy.zonky.reporter.NewLoanReporter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;

import static com.github.schmeedy.zonky.client.paging.PageRequest.FIRST_20_ITEMS;

/**
 * Periodically checks for new loans appearing in the marketplace and reports them
 */
@Component
public class NewLoanChecker {

    private final ZonkyApiClient apiClient;
    private final NewLoanReporter reporter;

    /**
     * We could be fetching just loans published since `ZonedDateTime.now() - ${check.interval.ms}`,
     * but that way we could miss some since scheduling is not really exact. Instead, we just
     * remember publishDate of the last {@link Loan} we've seen and fetch all loans published after
     * that one.
     */
    private ZonedDateTime lastLoanPublishDate = ZonedDateTime.now();

    public NewLoanChecker(ZonkyApiClient apiClient, NewLoanReporter reporter) {
        this.apiClient = apiClient;
        this.reporter = reporter;
    }

    /**
     * Checks for new loans in the marketplace since the last time (actually since
     * publishDate of the last published loan) and reports them.
     */
    @Scheduled(fixedDelayString = "${check.interval.ms}", initialDelayString = "${check.interval.ms}")
    public void checkNewLoans() {
        List<Loan> allNewLoans = PagingUtils.getAllRemainingItems(
                FIRST_20_ITEMS,
                (PageRequest pr) -> apiClient.getNewLoansSince(lastLoanPublishDate, pr));

        if (allNewLoans.isEmpty()) {
            reporter.noNewLoans();
        } else {
            lastLoanPublishDate = allNewLoans.get(0).getDatePublished();
            reporter.newLoans(allNewLoans);
        }
    }


}
