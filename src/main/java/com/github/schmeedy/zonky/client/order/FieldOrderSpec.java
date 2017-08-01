package com.github.schmeedy.zonky.client.order;

import com.github.schmeedy.zonky.client.model.meta.FieldMetadata;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * Specifies sort order on a given record field
 */
public class FieldOrderSpec {
    private FieldMetadata<?> field;
    private Order order;

    public FieldOrderSpec(FieldMetadata<?> field, Order order) {
        this.field = notNull(field);
        this.order = notNull(order);
    }

    /**
     * Returns representation suitable for usage with Zonky API
     */
    public String toQueryParamRepr() {
        return order.applyTo(field);
    }

    public FieldMetadata<?> getField() {
        return field;
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldOrderSpec that = (FieldOrderSpec) o;

        if (field != null ? !field.equals(that.field) : that.field != null) return false;
        return order == that.order;
    }

    @Override
    public int hashCode() {
        int result = field != null ? field.hashCode() : 0;
        result = 31 * result + (order != null ? order.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FieldOrderSpec{" +
                "field=" + field +
                ", order=" + order +
                '}';
    }
}
