package com.example.demo.quote.service;

import com.example.demo.quote.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.quote.repository.QuoteRepository;

import java.util.*;

/**
 * 名言管理システムサービスクラス（ビジネスロジックをまとめるクラス）
 * 
 * @author JAS横井
 * @since 2025/05/09
 */
@Service
public class QuoteService {

    @Autowired
    private QuoteRepository quoteRepository;

    /**
     * 名言一覧取得メソッド
     * 
     * @author JAS横井
     * @since 2025/05/09
     * 
     * @return 全ての名言オブジェクトのリスト
     */
    public List<Quote> getAllQuotes() {
        List<Quote> allQuotes = quoteRepository.findAll();
        return allQuotes;
    }

    /**
     * 名言登録メソッド
     * 
     * @author JAS横井
     * @since 2025/05/09
     * 
     * @param quote 登録する名言オブジェクト
     * @return 実際に登録した名言オブジェクト
     */
    public Quote addQuote(Quote quote) {
        Quote savedQuote = quoteRepository.save(quote);
        // 課題2 登録時の外部ファイル書き込み
        return savedQuote;
    }

    /**
     * ランダム名言メソッド
     * 
     * @author JAS横井
     * @since 2025/05/09
     * 
     * @return 取得した名言オブジェクト
     */
    public Quote getRandomQuote() {
        List<Quote> quotes = quoteRepository.findAll();
        if (quotes.isEmpty()) {
            return null;
        }

        Random random = new Random();
        Quote randomQuote = quotes.get(random.nextInt(quotes.size()));
        return randomQuote;
    }
}
