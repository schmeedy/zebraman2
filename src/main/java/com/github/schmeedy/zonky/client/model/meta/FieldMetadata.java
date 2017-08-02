package com.github.schmeedy.zonky.client.model.meta;

import com.github.schmeedy.zonky.client.filter.FieldFilter;
import com.github.schmeedy.zonky.client.filter.FilterOp;
import com.github.schmeedy.zonky.client.filter.ResourceFilter;
import com.github.schmeedy.zonky.client.order.FieldOrderSpec;
import com.github.schmeedy.zonky.client.order.Order;
import com.github.schmeedy.zonky.client.order.OrderSpec;
import org.apache.commons.lang3.Validate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;

import static com.github.schmeedy.zonky.client.filter.FilterOp.*;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * Metadata about an individual field of an API record
 * @param <T> Java type of the field
 */
public class FieldMetadata<T> {

    public static <E> FieldMetadata<E> filterableSortableField(String fieldName) {
        return new FieldMetadata<>(fieldName, true, false);
    }

    private String fieldName;
    private boolean filterable;
    private boolean sortable;

    /**
     * @param fieldName Name of the field as expected by the API
     * @param filterable Can this field be used in a filter?
     * @param sortable Can you sort according to the field?
     */
    FieldMetadata(String fieldName, boolean filterable, boolean sortable) {
        this.fieldName = notNull(fieldName);
        this.filterable = filterable;
        this.sortable = sortable;
    }

    /**
     * Creates field filter for: X == value
     */
    public ResourceFilter eq(T value) {
        return fieldFilter(EQ, value);
    }

    /**
     * Creates field filter for: X > value
     */
    public ResourceFilter gt(T value) {
        return fieldFilter(GT, value);
    }

    /**
     * Creates field filter for: X >= value
     */
    public ResourceFilter gte(T value) {
        return fieldFilter(GTE, value);
    }

    /**
     * Creates field filter for: X < value
     */
    public ResourceFilter lt(T value) {
        return fieldFilter(LT, value);
    }

    /**
     * Creates field filter for: X <= value
     */
    public ResourceFilter lte(T value) {
        return fieldFilter(LTE, value);
    }

    /**
     * Returns {@link OrderSpec} for this field in ascending order
     */
    public OrderSpec asc() {
        return fieldOrderSpec(Order.ASCENDING);
    }

    /**
     * Returns {@link OrderSpec} for this field in descending order
     */
    public OrderSpec desc() {
        return fieldOrderSpec(Order.DESCENDING);
    }

    public String getFieldName() {
        return fieldName;
    }

    public boolean isFilterable() {
        return filterable;
    }

    public boolean isSortable() {
        return sortable;
    }

    /**
     * Creates new {@link ResourceFilter} for this field
     * @param op filter operation
     * @param value filter value
     */
    private ResourceFilter fieldFilter(FilterOp op, T value) {
        isTrue(filterable, "field " + fieldName + " is not filterable");
        FieldFilter fieldFilter = new FieldFilter(fieldName, op, toQueryStringRepr(value));
        return new ResourceFilter(Collections.singleton(fieldFilter));
    }

    private OrderSpec fieldOrderSpec(Order order) {
        isTrue(filterable, "field " + sortable + " is not sortable");
        return new OrderSpec(Collections.singletonList(new FieldOrderSpec(this, order)));
    }

    /**
     * "Serializes" value being used to a representation understood by the server
     */
    private String toQueryStringRepr(T value) {
        if (value instanceof ZonedDateTime) {
            /*
             * There's a bug in the API where sending ZonedDateTime including timezone as filter value
             * leads to a HTTP 500 response (even though loans are actually listed with timezones)
             */
            return ((ZonedDateTime) value).withZoneSameInstant(ZoneId.of("+02:00")).toLocalDateTime().toString();
        } else {
            return String.valueOf(value); // a little simplification
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldMetadata that = (FieldMetadata) o;

        if (filterable != that.filterable) return false;
        if (sortable != that.sortable) return false;
        return fieldName != null ? fieldName.equals(that.fieldName) : that.fieldName == null;
    }

    @Override
    public int hashCode() {
        int result = fieldName != null ? fieldName.hashCode() : 0;
        result = 31 * result + (filterable ? 1 : 0);
        result = 31 * result + (sortable ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FieldMetadata{" +
                "fieldName='" + fieldName + '\'' +
                ", filterable=" + filterable +
                ", sortable=" + sortable +
                '}';
    }
}
