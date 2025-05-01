package com.example.demo.quote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.quote.service.QuoteService;
import com.example.demo.quote.model.Quote;

import java.util.*;

@RestController
@RequestMapping("/quotes")
public class QuoteController {
    @Autowired
    private QuoteService quoteService;

    @GetMapping
    public List<Quote> getAllQuotes() {
        return quoteService.getAllQuotes();
    }

    @PostMapping
    public Quote addQuote(@RequestBody Quote quote) {
        return quoteService.addQuote(quote);
    }

    @GetMapping("/random")
    public Quote getRandomQuote() {
        return quoteService.getRandomQuote();
    }
}
