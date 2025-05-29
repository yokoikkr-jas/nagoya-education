package com.example.demo.quote.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.example.demo.quote.repository.QuoteRepository;

public class QuoteServiceTest {
    @Test
    void testGetQuoteStatistics() {
    }

    @Mock
    private QuoteRepository quoteRepository;

    @InjectMocks
    private QuoteService quoteService;

}
