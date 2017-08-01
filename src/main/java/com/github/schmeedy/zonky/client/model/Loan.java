package com.github.schmeedy.zonky.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.schmeedy.zonky.client.model.meta.FieldMetadata;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static com.github.schmeedy.zonky.client.model.meta.FieldMetadata.filterableSortableField;

/**
 * API representation of a loan
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Loan {
    /**
     * Unique loan identifier
     */
    public static final FieldMetadata<Integer> ID = filterableSortableField("id");

    /**
     * Name of the loan (presented as title in the UI)
     */
    public static final FieldMetadata<String> NAME = filterableSortableField("name");

    /**
     * URL the associated {@link Loan} resource
     */
    public static final FieldMetadata<String> URL = filterableSortableField("url");

    /**
     * Date and time this loan was published
     */
    public static final FieldMetadata<ZonedDateTime> DATE_PUBLISHED = filterableSortableField("datePublished");

    private int id;
    private String name;
    private String url;
    private ZonedDateTime datePublished;

    @JsonCreator
    public Loan(
            @JsonProperty("id") int id,
            @JsonProperty("name") String name,
            @JsonProperty("url") String url,
            @JsonProperty("datePublished") ZonedDateTime datePublished) {

        this.id = id;
        this.name = name;
        this.url = url;
        this.datePublished = datePublished;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public ZonedDateTime getDatePublished() {
        return datePublished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Loan loan = (Loan) o;

        if (id != loan.id) return false;
        if (name != null ? !name.equals(loan.name) : loan.name != null) return false;
        if (url != null ? !url.equals(loan.url) : loan.url != null) return false;
        return datePublished != null ? datePublished.equals(loan.datePublished) : loan.datePublished == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (datePublished != null ? datePublished.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", datePublished=" + datePublished +
                '}';
    }
}
