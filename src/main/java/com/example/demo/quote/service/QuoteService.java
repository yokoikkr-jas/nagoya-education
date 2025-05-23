package com.example.demo.quote.service;

import com.example.demo.quote.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.quote.repository.QuoteRepository;

import java.io.BufferedWriter;
import java.io.FileWriter;
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
     * 
     * @author
     * @since 2025/05/23
     * 
     * @param
     * @return
     * @throw
     */
    public Quote addQuote(Quote quote) {
        try {
            Quote savedQuote = quoteRepository.save(quote);
            // 課題2 登録時の外部ファイル書き込み
            String text = savedQuote.getText();
            String author = savedQuote.getAuthor();
            String fileName = "src\\main\\java\\com\\example\\demo\\quote\\Quote.csv";
            // text：名言オブジェクトに格納されている名言
            // author：名言オブジェクトに格納されている著者
            // fileName：外部ファイルの相対パス
            FileWriter file = new FileWriter(fileName, true);
            BufferedWriter writer = new BufferedWriter(file);

            try {
                // 外部ファイルへの書き込み
                writer.write(text);
                writer.write(",");
                writer.write(author);
                writer.newLine();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                writer.close();
            }

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
}
