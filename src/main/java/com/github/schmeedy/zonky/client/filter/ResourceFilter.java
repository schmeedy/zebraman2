package com.github.schmeedy.zonky.client.filter;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * Filter to be applied to a list-type API resource
 */
public class ResourceFilter {
    /**
     * An empty resource filter (if you want to get all unfiltered items back)
     */
    public static final ResourceFilter EMPTY = new ResourceFilter(Collections.emptySet());

    private Set<FieldFilter> fieldFilters;

    /**
     * @param fieldFilters filters to be applied to individual fields
     */
    public ResourceFilter(Set<FieldFilter> fieldFilters) {
        this.fieldFilters = notNull(fieldFilters);
    }

    /**
     * Returns new {@link ResourceFilter} that is a combination of this filter and otherFilter
     */
    public ResourceFilter combine(ResourceFilter otherFilter) {
        HashSet<FieldFilter> newFf = new HashSet<>(fieldFilters);
        newFf.addAll(otherFilter.fieldFilters);
        return new ResourceFilter(newFf);
    }

    /**
     * Applies filters to the given {@link URI}
     */
    public URI applyTo(URI uri) {
        return applyTo(UriComponentsBuilder.fromUri(uri)).build().toUri();
    }

    /**
     * Applies filters to an {@link UriComponentsBuilder} and returns it back for chaining
     */
    public UriComponentsBuilder applyTo(UriComponentsBuilder builder) {
        for (FieldFilter ff : fieldFilters) {
            builder.queryParam(ff.getQueryParamName(), ff.getFilterValue());
        }
        return builder;
    }

    public Set<FieldFilter> getFieldFilters() {
        return Collections.unmodifiableSet(fieldFilters);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceFilter that = (ResourceFilter) o;

        return fieldFilters != null ? fieldFilters.equals(that.fieldFilters) : that.fieldFilters == null;
    }

    @Override
    public int hashCode() {
        return fieldFilters != null ? fieldFilters.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ResourceFilter{" +
                "fieldFilters=" + fieldFilters +
                '}';
    }
}
