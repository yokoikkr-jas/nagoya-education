package com.example.demo.quote;

import com.example.demo.quote.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.demo.quote.repository.QuoteRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private QuoteRepository quoteRepository;

    @Override
    public void run(String... args) throws Exception {
        quoteRepository.save(new Quote("地球は青かった", "ガガーリン"));
        quoteRepository.save(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        quoteRepository.save(new Quote("憧れるのをやめましょう", "大谷翔平"));
    }
}
