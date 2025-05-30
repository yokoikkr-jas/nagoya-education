package com.example.demo.quote.service;

import org.junit.jupiter.api.Test;// テストメソッドの定義に使う



import static org.junit.jupiter.api.Assertions.assertEquals;// アサーションのインポート：実際の結果と期待する結果を比較
import static org.mockito.Mockito.mock;// Mockito(mock,when)をインポート：QuoteRepositoryのモックを作成し、テスト環境での動作を定義
import static org.mockito.Mockito.when;
// import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;// @Autowired：Springが自動でインスタンスを注入
import org.springframework.boot.test.context.SpringBootTest;// @SpringBootTest：Spring
                                                            // Bootのテスト環境を設定するためのアノテーション

import com.example.demo.quote.model.Quote;
import com.example.demo.quote.repository.QuoteRepository;
// import com.example.demo.quote.service.QuoteService;

import java.util.List;
import java.util.Arrays;

// testCountQuotes2()に必要なimport
import java.util.Collections;


@SpringBootTest
public class QuoteServiceTest {
    @Autowired
    private QuoteService quoteService;// テストしたいメソッドがあるクラスを書く

    @Autowired
    private QuoteRepository quoteRepository;



    @Test
    void testCountQuotes() {

        // Mockオブジェクトの作成
        // QuoteRepository mockRepo = mock(QuoteRepository.class);

        // モックのリストを作成
        // リスト名にmockをつけなければDBを使用することになってしまう
        // List<Quote> mockQuotes = Arrays.asList(new Quote("A"), new Quote("B"), new Quote("C"));

        // モックの動作を定義
        // when(mockRepo.findAll())：mockRepo.findAll()が呼び出されたとき、特定の値を返すように設定
        // .thenReturn(mockQuotes);：実際のデータベースの値ではなく、手動で設定したmockQuotesを返す
        // when(mockRepo.findAll()).thenReturn(mockQuotes);

        // QuoteServiceのインスタンスを作成
        // mockRepoをQuoteServiceに渡して、テスト用のQuoteServiceインスタンスを生成
        // この処理がないとQuoteServiceインスタンスが作成されないため、quoteService.
        // QuoteService quoteService = new QuoteService(mockRepo);

        // メソッドの実行
        int count = quoteService.countQuotes();

        // アサーション（期待値との比較）
        assertEquals(3, count);

    }

    @Test
    void testCountQuotesEmpty() {

        // Mockオブジェクトの作成
        // QuoteRepository mockRepo = mock(QuoteRepository.class);

        // モックの動作を定義
        // when(mockRepo.findAll()).thenReturn(Collections.emptyList());

        // QuoteServiceのインスタンスを作成
        // QuoteService quoteService = new QuoteService(mockRepo);

        quoteRepository.deleteAll();// DBに登録された全てのオブジェクトを削除

        // メソッドの実行
        int count = quoteService.countQuotes();

        // アサーション（期待値との比較）
        assertEquals(0, count);

    }


    @Test
    void testCountQuotesError() {
        Quote result = quoteService.countQuotes();
        assertNull(result);
        // 例外を発生させるリポジトリを使用してQuoteServiceを作成
        // QuoteService testService = new QuoteService(new FailingQuoteRepository());

        // メソッドの実行
        // int count = testService.countQuotes();

        // アサーション（期待値との比較）
        // assertEquals(0, count);
    }



    @Test
    public void testCountQuotes4() {

        // Mockオブジェクトの作成
        QuoteRepository mockRepo = mock(QuoteRepository.class);

        // モックの動作を定義
        // 通常はthenReturnを使い、特定の値List<Quote>などを返す
        // 今回はエラーのシナリオをテストするので例外を発生させるようにthenThrowを使う
        when(mockRepo.findAll()).thenThrow(new RuntimeException("Database error"));

        // QuoteServiceのインスタンスを作成
        // mockRepoをQuoteServiceに渡して、テスト用のQuoteServiceインスタンスを生成
        // この処理がないとQuoteServiceインスタンスが作成されないため、quoteService.
        QuoteService quoteService = new QuoteService(mockRepo);

        // メソッドの実行
        int count = quoteService.countQuotes();

        // アサーション（期待値との比較）
        assertEquals(0, count);

    }

}


