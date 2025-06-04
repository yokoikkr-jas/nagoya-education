package com.example.demo.quote.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.quote.model.Quote;
import com.example.demo.quote.repository.QuoteRepository;

import java.util.*;

@ExtendWith(MockitoExtension.class)

public class QuoteServiceTest {

    @Mock
    private QuoteRepository quoteRepositoryMock;

    @InjectMocks
    private QuoteService quoteService;

    @Test // 通常の動きができるか確認 : 正常系
    void testGetQuoteStatistics() { // mockオブジェクトの作成

        List<Quote> mockQuotes = new ArrayList<>();
        Map<String, Object> pridiction = new HashMap<>();

        // mockのリストを作成
        Quote quote1 = new Quote("quote1", "human1");
        Quote quote2 = new Quote("quotee2", "human2");
        Quote quote3 = new Quote("quoteee3", "human3");

        // quoteを追加していく
        mockQuotes.add(quote1);
        mockQuotes.add(quote2);
        mockQuotes.add(quote3);

        pridiction.put("averageLength", 7);
        pridiction.put("longestQuote", mockQuotes.get(2));
        pridiction.put("shortestQuote", mockQuotes.get(0));

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(mockQuotes);

        // QuoteServiceを呼び出す
        Map<String, Object> statisticsResult = quoteService.getQuoteStatistics();

        // 結果を出力
        assertEquals(pridiction, statisticsResult);
    }

    @Test // 名言オブジェクトが空のとき
    void testGetQuoteStatistics_WhenEmpty() {

        // Repositoryからデータを削除
        quoteRepositoryMock.deleteAll();

        // QuoteServiceを呼び出す
        Map<String, Object> statisticsResult = quoteService.getQuoteStatistics();

        // 結果を出力 //空のMapを返す
        assertEquals(Collections.emptyMap(), statisticsResult);
    }

    @Test // 名言オブジェクトが一つのとき
    void testGetQuoteStatistics_WhenSingle() {

        Quote quote1 = new Quote("夢はでっかく、根はふかく", "みつを");

        List<Quote> mockQuotes = new ArrayList<>();
        Map<String, Object> pridiction = new HashMap<>();

        mockQuotes.add(quote1);
        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(mockQuotes);

        // QuoteServiceを呼び出す
        Map<String, Object> statisticsResult = quoteService.getQuoteStatistics();

        pridiction.put("averageLength", 12);
        pridiction.put("longestQuote", mockQuotes.get(0));
        pridiction.put("shortestQuote", mockQuotes.get(0));

        // 結果を出力
        assertEquals(pridiction, statisticsResult);
    }

    @Test // 名言オブジェクトが二つ以上のとき
    void testGetQuoteStatistics_WhenNotEmpty() {

        Quote quote1 = new Quote("夢はでっかく、根はふかく", "みつを");
        Quote quote2 = new Quote("日本を今一度、せんたくいたし申候", "坂本龍馬");
        Quote quote3 = new Quote("敵は多ければ多いほど面白い", "勝海舟");

        List<Quote> mockQuotes = new ArrayList<>();
        Map<String, Object> pridiction = new HashMap<>();

        // quoteを追加していく
        mockQuotes.add(quote1);
        mockQuotes.add(quote2);
        mockQuotes.add(quote3);

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(mockQuotes);

        // QuoteServiceを呼び出す
        Map<String, Object> statisticsResult = quoteService.getQuoteStatistics();

        pridiction.put("averageLength", 13);
        pridiction.put("longestQuote", mockQuotes.get(1));
        pridiction.put("shortestQuote", mockQuotes.get(0));

        // 結果を出力
        assertEquals(pridiction, statisticsResult);
    }

    @Test // 最短文字数・最長文字数が等しいとき
    void testGetQuoteStatistics_WhenMinEqualsMaxLength() {

        Quote quote1 = new Quote("夢はでっかく、根はふかく。", "みつを");
        Quote quote3 = new Quote("敵は多ければ多いほど面白い", "勝海舟");

        List<Quote> mockQuotes = new ArrayList<>();
        Map<String, Object> pridiction = new HashMap<>();

        // quoteを追加していく
        mockQuotes.add(quote1);
        mockQuotes.add(quote3);

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(mockQuotes);

        // QuoteServiceを呼び出す
        Map<String, Object> statisticsResult = quoteService.getQuoteStatistics();

        pridiction.put("averageLength", 13);
        pridiction.put("longestQuote", mockQuotes.get(0));
        pridiction.put("shortestQuote", mockQuotes.get(0));

        // それぞれの結果を期待値とする
        assertEquals(pridiction, statisticsResult);
    }

    @Test // 最短文字数・最長文字数・平均文字数が等しいとき
    void testGetQuoteStatistics_WhenMinEqualsMaxEqualsAverageLength() {

        Quote quote1 = new Quote("夢はでっかく、根はふかく", "みつを");
        Quote quote2 = new Quote("おはようございます。。。", "坂本龍馬");
        Quote quote3 = new Quote("敵は多ければ多いほど面白", "勝海舟");

        List<Quote> mockQuotes = new ArrayList<>();
        Map<String, Object> pridiction = new HashMap<>();

        // quoteを追加していく
        mockQuotes.add(quote1);
        mockQuotes.add(quote2);
        mockQuotes.add(quote3);

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(mockQuotes);

        // QuoteServiceを呼び出す
        Map<String, Object> statisticsResult = quoteService.getQuoteStatistics();

        pridiction.put("averageLength", 12);
        pridiction.put("longestQuote", mockQuotes.get(0));
        pridiction.put("shortestQuote", mockQuotes.get(0));

        // それぞれの結果を期待値とする
        assertEquals(pridiction, statisticsResult);
    }
}
