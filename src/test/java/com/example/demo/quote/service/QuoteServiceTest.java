package com.example.demo.quote.service;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import com.example.demo.quote.model.Quote;
import com.example.demo.quote.repository.QuoteRepository;

import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;

import java.util.ArrayList;
import java.util.List;

import java.util.Collections;



@SpringBootTest
public class QuoteServiceTest {

    @Mock
    private QuoteRepository mockquoteRepository;
    @InjectMocks
    private QuoteService mockquoteService;


    // 検索文字数が入力されずに検索(Less thanのとき)
    @Test
    void testSearchByLengthNullLess() {
        List<Quote> result = mockquoteService.searchByLength(null, "less");
        assertNull(result);
    }


    // 検索文字数が入力されずに検索(Equal toのとき)
    @Test
    void testSearchByLengthNullEqual() {
        List<Quote> result = mockquoteService.searchByLength(null, "equal");
        assertNull(result);
    }


    // 検索文字数が入力されずに検索(Greater thanのとき)
    @Test
    void testSearchByLengthNullGreater() {
        List<Quote> result = mockquoteService.searchByLength(null, "greater");
        assertNull(result);
    }



    // 0文字未満の名言を検索
    @Test
    void testSearchByLengthLessZero() {
        List<Quote> quotes = new ArrayList<>();
        quotes.add(new Quote("A", "アルファベット"));
        quotes.add(new Quote("地球は青かった", "ガガーリン"));
        quotes.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        quotes.add(new Quote("憧れるのをやめましょう", "大谷翔平"));

        Mockito.when(mockquoteRepository.findAll()).thenReturn(quotes);
        List<Quote> result = mockquoteService.searchByLength("-1", "equal");
        assertTrue(result.isEmpty());
    }


    // 0文字の名言を検索
    @Test
    void testSearchByLengthEqualZero() {
        List<Quote> quotes = new ArrayList<>();
        quotes.add(new Quote("A", "アルファベット"));
        quotes.add(new Quote("地球は青かった", "ガガーリン"));
        quotes.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        quotes.add(new Quote("憧れるのをやめましょう", "大谷翔平"));

        Mockito.when(mockquoteRepository.findAll()).thenReturn(quotes);
        List<Quote> result = mockquoteService.searchByLength("0", "equal");
        assertTrue(result.isEmpty());
    }


    // 0文字より大きい名言を検索
    @Test
    void testSearchByLengthGreaterZero() {
        List<Quote> quotes = new ArrayList<>();
        List<Quote> result = new ArrayList<>();
        List<Quote> value = new ArrayList<>();
        quotes.add(new Quote("A", "アルファベット"));
        quotes.add(new Quote("地球は青かった", "ガガーリン"));
        quotes.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        quotes.add(new Quote("憧れるのをやめましょう", "大谷翔平"));

        Mockito.when(mockquoteRepository.findAll()).thenReturn(quotes);
        result = mockquoteService.searchByLength("1", "equal");
        value.add(quotes.get(0));
        assertEquals(value, result);
    }



    // 正常Less than
    @Test // 一致する名言オブジェクトが一つのとき
    void testSearchByLengthLessOne() {
        List<Quote> quotes = new ArrayList<>();// モックデータを入れるためのリスト
        List<Quote> result = new ArrayList<>();// 10字未満の検索結果を格納するリスト
        List<Quote> value = new ArrayList<>();// 期待値を格納するリスト
        quotes.add(new Quote("地球は青かった", "ガガーリン"));
        quotes.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        quotes.add(new Quote("憧れるのをやめましょう", "大谷翔平"));

        // mockquoteRepository.findAll()が呼ばれたとき、quotesを返す
        Mockito.when(mockquoteRepository.findAll()).thenReturn(quotes);

        result.addAll(mockquoteService.searchByLength("10", "less"));

        // quotesから条件に一致するQuoteを取得し、valueに追加
        value.add(quotes.get(0));
        assertEquals(value, result);
    }


    @Test // 一致するQuoteオブジェクトが複数のとき
    void testSearchByLengthLessMultiple() {
        List<Quote> quotes = new ArrayList<>();
        List<Quote> result = new ArrayList<>();
        List<Quote> value = new ArrayList<>();
        quotes.add(new Quote("地球は青かった", "ガガーリン"));
        quotes.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        quotes.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        Mockito.when(mockquoteRepository.findAll()).thenReturn(quotes);

        result.addAll(mockquoteService.searchByLength("15", "less"));
        value.add(quotes.get(0));
        value.add(quotes.get(2));
        assertEquals(value, result);
    }


    @Test // 一致するQuoteオブジェクトがなかったとき→空のリストを返す
    void testSearchByLengthLessEmpty() {
        List<Quote> quotes = new ArrayList<>();
        List<Quote> result = new ArrayList<>();

        quotes.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        quotes.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        Mockito.when(mockquoteRepository.findAll()).thenReturn(quotes);

        result.addAll(mockquoteService.searchByLength("5", "less"));
        assertEquals(Collections.emptyList(), result);
    }



    // 正常Equal to
    @Test // 一致する名言オブジェクトが一つのとき
    void testSearchByLengthEqualOne() {
        List<Quote> quotes = new ArrayList<>();
        List<Quote> result = new ArrayList<>();
        List<Quote> value = new ArrayList<>();
        quotes.add(new Quote("地球は青かった", "ガガーリン"));
        quotes.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        quotes.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        Mockito.when(mockquoteRepository.findAll()).thenReturn(quotes);

        result.addAll(mockquoteService.searchByLength("11", "equal"));
        value.add(quotes.get(2));
        assertEquals(value, result);
    }


    @Test // 一致するQuoteオブジェクトが複数のとき
    void testSearchByLengthEqualMultiple() {
        List<Quote> quotes = new ArrayList<>();
        List<Quote> result = new ArrayList<>();
        List<Quote> value = new ArrayList<>();
        quotes.add(new Quote("地球は青い", "ガガーリン"));
        quotes.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        quotes.add(new Quote("憧れをもつ", "大谷翔平"));
        Mockito.when(mockquoteRepository.findAll()).thenReturn(quotes);

        result.addAll(mockquoteService.searchByLength("5", "equal"));
        value.add(quotes.get(0));
        value.add(quotes.get(2));
        assertEquals(value, result);
    }


    @Test // 一致するQuoteオブジェクトがなかったとき→空のリストを返す
    void testSearchByLengthEqualEmpty() {
        List<Quote> quotes = new ArrayList<>();
        List<Quote> result = new ArrayList<>();

        quotes.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        quotes.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        Mockito.when(mockquoteRepository.findAll()).thenReturn(quotes);

        result.addAll(mockquoteService.searchByLength("3", "equal"));
        assertEquals(Collections.emptyList(), result);
    }



    // 正常Greater than
    @Test // 一致する名言オブジェクトが一つのとき
    void testSearchByLengthGreaterOne() {
        List<Quote> quotes = new ArrayList<>();
        List<Quote> result = new ArrayList<>();
        List<Quote> value = new ArrayList<>();
        quotes.add(new Quote("地球は青かった", "ガガーリン"));
        quotes.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        quotes.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        Mockito.when(mockquoteRepository.findAll()).thenReturn(quotes);

        result.addAll(mockquoteService.searchByLength("15", "greater"));
        value.add(quotes.get(1));
        assertEquals(value, result);
    }


    @Test // 一致する名言オブジェクトが複数のとき
    void testSearchByLengthGreaterMultiple() {
        List<Quote> quotes = new ArrayList<>();
        List<Quote> result = new ArrayList<>();
        List<Quote> value = new ArrayList<>();
        quotes.add(new Quote("地球は青かった", "ガガーリン"));
        quotes.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        quotes.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        Mockito.when(mockquoteRepository.findAll()).thenReturn(quotes);

        result.addAll(mockquoteService.searchByLength("10", "greater"));
        value.add(quotes.get(1));
        value.add(quotes.get(2));
        assertEquals(value, result);
    }


    @Test // 一致するQuoteオブジェクトがなかったとき→空のリストを返す
    void testSearchByLengthGreaterEmpty() {
        List<Quote> quotes = new ArrayList<>();
        List<Quote> result = new ArrayList<>();
        quotes.add(new Quote("地球は青かった", "ガガーリン"));
        quotes.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        quotes.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        Mockito.when(mockquoteRepository.findAll()).thenReturn(quotes);

        result.addAll(mockquoteService.searchByLength("30", "greater"));
        assertEquals(Collections.emptyList(), result);
    }



    // 例外処理
    // DBからQuoteオブジェクトを取得できなかったとき
    @Test
    void testSearchByLengthError() {
        when(mockquoteRepository.findAll()).thenThrow(new RuntimeException("DB接続エラー"));
        List<Quote> result = mockquoteService.searchByLength("10", "less");
        assertNull(result);
    }

}
