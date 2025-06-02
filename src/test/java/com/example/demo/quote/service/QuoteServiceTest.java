package com.example.demo.quote.service;

// テストメソッドの定義に使う
import org.junit.jupiter.api.Test;

// アサーションのインポート：実際の結果と期待する結果を比較
import static org.junit.jupiter.api.Assertions.assertEquals;

// Mockito(mock,when)をインポート：QuoteRepositoryのモックを作成し、テスト環境での動作を定義
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// @Autowired：Springが自動でインスタンスを注入
import org.springframework.beans.factory.annotation.Autowired;

// @SpringBootTest：SpringBootのテスト環境を設定するためのアノテーション
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.quote.repository.QuoteRepository;



@SpringBootTest
public class QuoteServiceTest {
    @Autowired
    private QuoteService quoteService;// テストしたいメソッドがあるクラスを書く

    @Autowired
    private QuoteRepository quoteRepository;



    @Test // DBにQuoteオブジェクトが存在するとき
    void testCountQuotes() {
        // メソッドの実行
        int count = quoteService.countQuotes();

        // アサーション（期待値との比較）
        assertEquals(3, count);
    }


    @Test // DBが空のとき
    void testCountQuotesEmpty() {
        // DBに登録された全てのオブジェクトを削除
        quoteRepository.deleteAll();

        // メソッドの実行
        int count = quoteService.countQuotes();

        // アサーション（期待値との比較）
        assertEquals(0, count);
    }


    @Test // 例外処理（DBからQuoteオブジェクトを取得できなかったとき）
    public void testCountQuotesError() {
        QuoteRepository mockRepo = mock(QuoteRepository.class);
        when(mockRepo.findAll()).thenThrow(new RuntimeException("DB接続エラー"));
        QuoteService quoteService = new QuoteService(mockRepo);

        int result = quoteService.countQuotes();
        assertEquals(0, result);
    }
}
