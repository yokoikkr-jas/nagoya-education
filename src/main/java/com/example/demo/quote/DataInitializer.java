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

    /**
     * データベースを初期化するメソッド
     * 
     * @author 横井
     * @since 2025/05/09
     * 
     * @param args コマンドライン引数
     */
    @Override
    public void run(String... args) throws Exception {
        // 課題1 データ初期化の外部化
        quoteRepository.save(new Quote("地球は青かった", "ガガーリン"));
        quoteRepository.save(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        quoteRepository.save(new Quote("憧れるのをやめましょう", "大谷翔平"));
    }
}
