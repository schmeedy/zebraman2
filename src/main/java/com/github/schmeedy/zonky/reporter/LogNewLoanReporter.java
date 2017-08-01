package com.github.schmeedy.zonky.reporter;

import com.github.schmeedy.zonky.client.model.Loan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * {@link NewLoanReporter} that prints new loans to the log
 */
@Component
public class LogNewLoanReporter implements NewLoanReporter {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void noNewLoans() {
        logger.info("no new loans");
    }

    @Override
    public void newLoans(List<Loan> loans) {
        logger.info("{} new loans discovered:", loans.size());
        loans.forEach((Loan l) -> logger.info(l.toString()));
    }
}
