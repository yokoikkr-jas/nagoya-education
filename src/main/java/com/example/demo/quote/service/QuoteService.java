package com.example.demo.quote.service;

import com.example.demo.quote.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.quote.repository.QuoteRepository;

import io.contek.zeus.exchange.GetAllQuotesRequest;

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

    public QuoteService(QuoteRepository q) {
        this.quoteRepository = q;
    }

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
     * 統計情報を収集するメソッド
     * 
     * @author 横山
     * @since 2025/05/26
     * 
     * @return 名言の統計情報（平均・最長・最短）を返す
     * 
     */

    public Map<String, Object> getQuoteStatistics() {

        Map<String, Object> statistics = new HashMap<>();

        List<Quote> allQuotes = quoteRepository.findAll();

        // 全てのquotesを取得

        if (allQuotes.isEmpty()) {
            // もし名言オブジェクト一覧が空ならば、
            return statistics;
            // statisticsを返却（追加）
        }

        int maxLength = Integer.MIN_VALUE;
        // 最長を求めるため、最も小さい値で初期化

        int minLength = Integer.MAX_VALUE;
        // 最短を求めるため、最も大きい値で初期化

        int totalLength = 0;
        // 全ての文字数の合計の初期値設定

        Quote maxBox = new Quote();
        // 最長を格納する箱
        Quote minBox = new Quote();
        // 最短を格納する箱

        for (Quote quotes : allQuotes) {
            // 各要素を順番に取り出して最後の名言まで調べる

            String quote = quotes.getText();
            // 名言を得る処理

            int length = quote.length();
            // 名言の文字数を数える

            totalLength += length;
            // 名言文字数を足していく

            if (length > maxLength) {

                maxLength = length;
                // より大きな値を更新

                maxBox = quotes;
                // 最長の名言オブジェクトを格納する

            }

            if (length < minLength) {

                minLength = length;
                // より小さな値を更新

                minBox = quotes;
                // 最短の名言オブジェクトを格納する

            }
        }

        int averageLength = totalLength / allQuotes.size();
        // 名言文字数の平均を調べる処理

        statistics.put("averageLength", averageLength);
        statistics.put("longestQuote", maxBox);
        statistics.put("shortestQuote", minBox);

        return statistics;
    }
}
