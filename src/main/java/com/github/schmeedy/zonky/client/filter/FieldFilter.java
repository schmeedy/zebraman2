package com.github.schmeedy.zonky.client.filter;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * A filter to be applied to an individual object / record field
 */
public class FieldFilter {
    private String fieldName;
    private FilterOp filterOp;
    private String filterValue;

    /**
     * @param fieldName name of a filterable field
     * @param filterOp filter operation to applyTo
     * @param filterValue value of the filter (argument to filter operation)
     */
    public FieldFilter(String fieldName, FilterOp filterOp, String filterValue) {
        this.fieldName = notEmpty(fieldName);
        this.filterOp = notNull(filterOp);
        this.filterValue = notEmpty(filterValue);
    }

    /**
     * Returns name of the the query param corresponding to this filter
     * (query params are concatenation of field name and operation to execute)
     */
    public String getQueryParamName() {
        return fieldName + "__" + filterOp.getOpSuffix();
    }

    /**
     * Returns name of a filterable field
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Returns filter operation to applyTo
     */
    public FilterOp getFilterOp() {
        return filterOp;
    }

    /**
     * Returns value of the filter (argument to filter operation)
     */
    public String getFilterValue() {
        return filterValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldFilter that = (FieldFilter) o;

        if (fieldName != null ? !fieldName.equals(that.fieldName) : that.fieldName != null) return false;
        if (filterOp != that.filterOp) return false;
        return filterValue != null ? filterValue.equals(that.filterValue) : that.filterValue == null;
    }

    @Override
    public int hashCode() {
        int result = fieldName != null ? fieldName.hashCode() : 0;
        result = 31 * result + (filterOp != null ? filterOp.hashCode() : 0);
        result = 31 * result + (filterValue != null ? filterValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FieldFilter{" +
                "fieldName='" + fieldName + '\'' +
                ", filterOp=" + filterOp +
                ", filterValue='" + filterValue + '\'' +
                '}';
    }
}
