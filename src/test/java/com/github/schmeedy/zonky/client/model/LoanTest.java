package com.github.schmeedy.zonky.client.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.time.ZonedDateTime;

import static org.junit.Assert.*;

public class LoanTest {

    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    public void shouldDeserializeLoanFromJson() throws IOException {
        Loan loan = objectMapper.readValue(getClass().getResourceAsStream("/loan.json"), Loan.class);
        assertEquals(114916, loan.getId());
        assertEquals("Dokončení rekonstrukce domu", loan.getName());
        assertEquals("https://app.zonky.cz/loan/114916", loan.getUrl());
        assertEquals(ZonedDateTime.parse("2017-07-31T16:24:47.630+02:00").toInstant(), loan.getDatePublished().toInstant());
    }

}