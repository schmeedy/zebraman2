package com.github.schmeedy.zonky.client.order;

import com.github.schmeedy.zonky.client.model.meta.FieldMetadata;

/**
 * Sort order
 */
public enum Order {
    ASCENDING, DESCENDING;

    /**
     * Returns sort specifier for the given record field to be used with API calls
     */
    public String applyTo(FieldMetadata<?> field) {
        if (this == DESCENDING) {
            return "-" + field.getFieldName();
        } else {
            return field.getFieldName();
        }
    }
}
