package com.example.demo.quote.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeEach;
// テストメソッドの定義に使う
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

// アサーションのインポート：実際の結果と期待する結果を比較
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.when;

// @Autowired：Springが自動でインスタンスを注入
import org.springframework.beans.factory.annotation.Autowired;

// @SpringBootTest：SpringBootのテスト環境を設定するためのアノテーション
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.quote.repository.QuoteRepository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;



@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class QuoteServiceTest {
    @Autowired
    private QuoteService quoteService;

    @Autowired
    private QuoteRepository quoteRepository;

    @Mock
    private QuoteRepository mockRepository;

    @InjectMocks
    private QuoteService mockService;

    @BeforeEach
    void setUp() {
        mockService = new QuoteService(mockRepository);
    }



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

        int count = quoteService.countQuotes();
        assertEquals(0, count);
    }


    @Test // 例外処理（DBからQuoteオブジェクトを取得できなかったとき）
    public void testCountQuotesError() {
        // mockの動作を定義
        when(mockRepository.findAll()).thenThrow(new RuntimeException("DB接続エラー"));

        int result = mockService.countQuotes();
        assertEquals(0, result);
    }
}
