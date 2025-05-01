package com.example.demo.quote.service;

import com.example.demo.quote.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.quote.repository.QuoteRepository;

import java.util.*;

@Service
public class QuoteService {

    @Autowired
    private QuoteRepository quoteRepository;

    public List<Quote> getAllQuotes() {
        return quoteRepository.findAll();
    }

    public Quote addQuote(Quote quote) {
        return quoteRepository.save(quote);
    }

    public Quote getRandomQuote() {
        List<Quote> quotes = quoteRepository.findAll();
        if (quotes.isEmpty()) {
            return null;
        }

        Random random = new Random();
        return quotes.get(random.nextInt(quotes.size()));
    }
}
