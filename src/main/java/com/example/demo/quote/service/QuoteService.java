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
     * 文字数検索メソッド
     * 
     * @author 平野
     * @since 2025/05/28
     * 
     * @param length    検索欄に入力された検索文字数
     * @param condition 以下、同じ、以上の文字数比較条件
     * @return 取得した名言オブジェクトを格納したリスト
     */
    public List<Quote> searchByLength(String length, String condition) {
        try {
            if (length == null) {// 検索欄に検索文字数が入力されずに検索されたとき
                return null;
            }

            List<Quote> searchList = quoteRepository.findAll();

            // 条件を満たしたQuoteオブジェクトを格納するリストを定義
            List<Quote> list = new ArrayList<>();

            for (Quote text : searchList) {// searchListからQuoteオブジェクトを一行ずつ取り出す
                String textquotes = text.getText();// getText()で名言オブジェクトのみ取り出す

                if (condition.equals("less")) {
                    int quotelength = textquotes.length();// 名言オブジェクトを文字数に変換
                    int ilength = Integer.parseInt(length);// 検索文字数をint型に変換

                    if (ilength > quotelength) {
                        list.add(text);
                    }
                }

                if (condition.equals("equal")) {
                    int quotelength = textquotes.length();
                    int ilength = Integer.parseInt(length);

                    if (ilength == quotelength) {
                        list.add(text);
                    }
                }

                if (condition.equals("greater")) {
                    int quotelength = textquotes.length();
                    int ilength = Integer.parseInt(length);

                    if (ilength < quotelength) {
                        list.add(text);
                    }
                }
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
