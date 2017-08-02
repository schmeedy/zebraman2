package com.github.schmeedy.zonky;

import com.github.schmeedy.zonky.client.ZonkyApiClient;
import com.github.schmeedy.zonky.client.model.Loan;
import com.github.schmeedy.zonky.client.paging.Page;
import com.github.schmeedy.zonky.client.paging.PageRequest;
import com.github.schmeedy.zonky.reporter.NewLoanReporter;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;

import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class NewLoanCheckerTest {

    @Mock private ZonkyApiClient apiClient;
    @Mock private NewLoanReporter newLoanReporter;

    private NewLoanChecker checker;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        checker = new NewLoanChecker(apiClient, newLoanReporter);
    }

    @Test
    public void shouldReportNoNewLoansIfNoneIsFound() {
        setReturnedLoans(Lists.emptyList());
        checker.checkNewLoans();
        verify(newLoanReporter).noNewLoans();
    }

    @Test
    public void shouldReportNewLoansIfSomeAreFound() {
        List<Loan> loans = Lists.newArrayList(mock(Loan.class), mock(Loan.class));
        setReturnedLoans(loans);
        checker.checkNewLoans();
        verify(newLoanReporter).newLoans(loans);
    }

    @Test
    public void shouldAskForNewLoansSinceTheLastOneItHasSeen() {
        Loan lastLoan = new Loan(123, "test", "/loan/test", ZonedDateTime.now().minusMinutes(2));
        List<Loan> loans = Lists.newArrayList(lastLoan, mock(Loan.class)); // loans in the response are sorted by -datePublished
        setReturnedLoans(loans);

        checker.checkNewLoans();
        verify(newLoanReporter).newLoans(loans);

        reset(apiClient, newLoanReporter);

        setReturnedLoans(Lists.emptyList());
        checker.checkNewLoans();
        verify(apiClient).getNewLoansSince(eq(lastLoan.getDatePublished()), any(PageRequest.class));
    }

    private void setReturnedLoans(List<Loan> loans) {
        when(apiClient.getNewLoansSince(any(ZonedDateTime.class), any(PageRequest.class))).thenAnswer((InvocationOnMock invocation) -> {
            PageRequest pageRequest = invocation.getArgumentAt(1, PageRequest.class);
            return new Page<>(loans, pageRequest);
        });
    }

}