package com.github.schmeedy.zonky.client.order;

import com.github.schmeedy.zonky.client.model.meta.FieldMetadata;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * Specifies overall order for sorting (potentially using multiple fields)
 */
public class OrderSpec {

    /**
     * Empty order specs (for default order)
     */
    public static final OrderSpec EMPTY = new OrderSpec(Collections.emptyList());

    private List<FieldOrderSpec> fieldSpecs;

    public OrderSpec(List<FieldOrderSpec> fieldSpecs) {
        this.fieldSpecs = notNull(fieldSpecs);
    }

    /**
     * Returns new {@link OrderSpec} which has all the sort fields of this
     * spec with sort field of otherSpec appended
     */
    public OrderSpec append(OrderSpec otherSpec) {
        ArrayList<FieldOrderSpec> newFieldOrders = new ArrayList<>(fieldSpecs);
        newFieldOrders.addAll(otherSpec.fieldSpecs);
        return new OrderSpec(newFieldOrders);
    }

    /**
     * Appends an HTTP header with this order to the {@link HttpHeaders} builder
     * and returns it back for chaining.
     */
    public HttpHeaders applyTo(HttpHeaders headers) {
        if (fieldSpecs.isEmpty()) {
            return headers;
        }

        String value = fieldSpecs.stream()
            .map(FieldOrderSpec::toQueryParamRepr)
            .collect(Collectors.joining(","));

        headers.add("X-Order", value);
        return headers;
    }

    public List<FieldOrderSpec> getFieldSpecs() {
        return fieldSpecs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderSpec orderSpec = (OrderSpec) o;

        return fieldSpecs != null ? fieldSpecs.equals(orderSpec.fieldSpecs) : orderSpec.fieldSpecs == null;
    }

    @Override
    public int hashCode() {
        return fieldSpecs != null ? fieldSpecs.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "OrderSpec{" +
                "fieldSpecs=" + fieldSpecs +
                '}';
    }
}
