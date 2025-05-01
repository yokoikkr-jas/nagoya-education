package com.example.demo.quote.repository;

import com.example.demo.quote.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Long>{
}