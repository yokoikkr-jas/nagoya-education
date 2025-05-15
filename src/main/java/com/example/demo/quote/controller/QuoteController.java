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
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 名言管理システムコントローラークラス （画面（フロント）側からリクエストを受け取り、サービスクラスのメソッドを呼び出す）
 * 
 * @author JAS横井
 * @since 2025/05/09
 */
@RestController
@RequestMapping("/quotes")
public class QuoteController {
    @Autowired
    private QuoteService quoteService;

    @GetMapping
    public List<Quote> getAllQuotes() {
        List<Quote> allQuotes = quoteService.getAllQuotes();
        return allQuotes;
    }

    @PostMapping
    public Quote addQuote(@RequestBody Quote quote) {
        Quote savedQuote = quoteService.addQuote(quote);
        return savedQuote;
    }

    @GetMapping("/random")
    public Quote getRandomQuote() {
        Quote randQuote = quoteService.getRandomQuote();
        return randQuote;
    }

    @GetMapping("/search")
    public List<Quote> searchQuotes(@RequestParam String query, @RequestParam String option) {
        // 課題3 検索機能(部分一致)
        // 引数optionには、名言のみ：text、著者：author、両方：bothがくる
        List<Quote> a = new ArrayList();
        a.add(new Quote("hoge query", "hoge author"));
        return a;
    }

    @GetMapping("/searchByLength")
    public List<Quote> searchByLength(@RequestParam String length, @RequestParam String condition) {
        // 課題4 検索機能（文字数検索）
        // 引数optionには、以下：less、著者：equal、両方：greaterがくる
        List<Quote> a = new ArrayList();
        a.add(new Quote("hoge query", "hoge author"));
        return a;
    }

    @GetMapping("/statistics")
    public Map<String, Object> getQuoteStatistics() {
        // 課題5 名言の統計情報
        Map<String, Object> a = new HashMap<>();
        Quote b = new Quote("hoge quote", "hoge author");
        a.put("averageLength", 3);
        a.put("longestQuote", b);
        a.put("shortestQuote", b);

        return a;
    }

    @GetMapping("/count")
    public int countQuotes() {
        // 課題6 名言の全数カウント
        int count = 0;
        return count;
    }
}
