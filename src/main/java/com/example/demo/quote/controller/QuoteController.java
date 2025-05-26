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

    /**
     * 統計情報を収集するメソッド
     * 
     * @author JAS横山
     * @since 2025/05/26
     * @param averageLength  全ての名言の平均文字数
     * @param longestLength  名言の最長文字数
     * @param shortestLength 名言の最短文字数
     * 
     * @return 名言の統計情報（平均・最長・最短）を返す
     * 
     */
    @GetMapping("/statistics")
    public Map<String, Object> getQuoteStatistics() {
        // 課題5 名言の統計情報
        Map<String, Object> a = new HashMap<>();
        Quote b = new Quote("hoge quote", "hoge author");
        a.put("averageLength", 3);
        a.put("longestQuote", b);
        a.put("shortestQuote", b);

        List<Quote> allQuotes = quoteService.getAllQuotes();
        // リストから全てのquotesを取得

        int MAX_LENGTH = 0;
        // 最長文字数の初期値設定
        int MIN_LENGTH = 0;
        // 最短文字数の初期値設定
        double TOTAL_LENGTH = 0;
        // 全ての文字数の合計の初期値設定

        for (Quote q : allQuotes) {
            // 各要素を順番に取り出して最後の名言まで調べる

            List<Quote> getAllQuotes = new ArrayList<>();
            // 名言のみを取り出す

            String quote = "";
            int length = quote.length();
            // 名言の文字数を数える

            TOTAL_LENGTH += quote.length();
            // 名言文字数を足していく

            if (quote.length() > MAX_LENGTH) {
                // 最長文字数に文字数の多いほうを入れる
                MAX_LENGTH = quote.length();
                // 代入した名言オブジェクトを格納する
            }

            if (quote.length() < MIN_LENGTH) {
                // 最短文字数に文字数の短いほうを代入
                MIN_LENGTH = quote.length();
                // 代入した名言オブジェクトを格納する
            }
        }

        double averageLength = TOTAL_LENGTH / allQuotes.size();
        // 名言文字数の平均を調べる

        System.out.println("averageLength:" + TOTAL_LENGTH / allQuotes.size());
        // 名言の文字数の平均を表示

        System.out.println("longestQuote:" + "MAX_LENGTH");
        // 全ての名言文字列の中から最長文字数の名言を表示

        System.out.println("shortestQuote：" + "MIN_LENGTH");
        // 全ての名言文字列の中から最短文字数をの名言を表示

        return a;
    }

    @GetMapping("/count")
    public int countQuotes() {
        // 課題6 名言の全数カウント
        int count = 0;
        return count;
    }
}
