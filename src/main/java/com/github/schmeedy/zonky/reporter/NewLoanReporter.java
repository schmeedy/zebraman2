package com.github.schmeedy.zonky.reporter;

import com.github.schmeedy.zonky.client.model.Loan;

import java.util.List;

/**
 * Reports new loans discovered on Zonky to the user...somehow :)
 */
public interface NewLoanReporter {
    /**
     * To be called if check for new loans happened and there were none
     */
    void noNewLoans();

    /**
     * To be called if new loans were discovered
     *
     * @param loans discovered loans - must be non-empty!
     */
    void newLoans(List<Loan> loans);
}
