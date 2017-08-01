package com.github.schmeedy.zonky.client.filter;

/**
 * Filter operations supported by Zonky API
 */
public enum FilterOp {
    EQ("eq"), GT("gt"), GTE("gte"), LT("lt"), LTE("lte"); // there are more :)

    private String opSuffix;

    private FilterOp(String opSuffix) {
        this.opSuffix = opSuffix;
    }

    /**
     * Suffix of the operation to be used in query string
     */
    public String getOpSuffix() {
        return opSuffix;
    }
}
