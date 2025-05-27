package com.example.demo.quote.service;

import com.example.demo.quote.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.quote.repository.QuoteRepository;

import java.util.*;

/**
 * 名言管理システムサービスクラス（ビジネスロジックをまとめるクラス）
 * 
 * @author 横井
 * @since 2025/05/09
 */
@Service
public class QuoteService {

    @Autowired
    private QuoteRepository quoteRepository;

    /**
     * 名言一覧取得メソッド
     * 
     * @author 横井
     * @since 2025/05/09
     * 
     * @return 全ての名言オブジェクトのリスト
     */
    public List<Quote> getAllQuotes() {
        try {
            List<Quote> allQuotes = quoteRepository.findAll();
            return allQuotes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 名言登録メソッド
     * 
     * @author 横井
     * @since 2025/05/09
     * 
     * @param quote 登録する名言オブジェクト
     * @return 実際に登録した名言オブジェクト
     */
    public Quote addQuote(Quote quote) {
        try {
            Quote savedQuote = quoteRepository.save(quote);
            // 課題2 登録時の外部ファイル書き込み
            return savedQuote;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ランダム名言メソッド
     * 
     * @author 横井
     * @since 2025/05/09
     * 
     * @return 取得した名言オブジェクト
     */
    public Quote getRandomQuote() {
        try {
            List<Quote> quotes = quoteRepository.findAll();
            if (quotes.isEmpty()) {
                return null;
            }

            Random random = new Random();
            Quote randomQuote = quotes.get(random.nextInt(quotes.size()));
            return randomQuote;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 部分一致検索を行うメソッド
     * 
     * @auhor 太田
     * @since 2025/05/26
     * 
     * @param searchString
     * @param subject
     * @return 指定された文字列と部分一致する名言オブジェクトのリスト
     */
    public List<Quote> partialMatch(String searchString, String subject) {
        try {
            List<Quote> b = new ArrayList<>(); // 部分一致する名言オブジェクトの格納先
            switch (subject) { // optionによる分岐（名言のみ、著者、両方）
                case "text":
                    for (Quote q : getAllQuotes()) { // DB上リストのサイズまで繰り返す
                        if (q.getText().contains(searchString)) { // 部分一致:true
                            b.add(q);
                        }
                    }
                    break;
                case "author":
                    for (Quote q : getAllQuotes()) {
                        if (q.getAuthor().contains(searchString)) {
                            b.add(q);
                        }
                    }
                    break;
                case "both":
                    for (Quote q : getAllQuotes()) {
                        if (q.getText().contains(searchString)) {
                            b.add(q);
                        } else if (q.getAuthor().contains(searchString)) {
                            b.add(q);
                        }
                    }
                    break;
            }

            if (b == null) { // 部分一致する文字列のリストbが空の場合、nullを返却
                return null;
            }

            return b;

        } catch (Exception e) {
            e.printStackTrace(); // スタックトレースを出力する
            return null;
        }
    }

}
