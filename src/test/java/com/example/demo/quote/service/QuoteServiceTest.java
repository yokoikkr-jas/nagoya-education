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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;



@SpringBootTest
public class QuoteServiceTest {

    @Mock
    private QuoteRepository mockquoteRepository;
    @InjectMocks
    private QuoteService mockquoteService;



    // lengthに文字が入力されずに検索
    // nullのとき（空はいらない？）
    @Test
    void testSearchByLengthNull() {
        List<Quote> result = mockquoteService.searchByLength(null, "Less than");
        assertNull(result);
    }



    // lengthの値：境界値 1のときの書き方
    @ParameterizedTest
    @ValueSource(strings = {"1", "0", "-1"})
    void testSearchByLengthValue(String length) {
        List<Quote> quotes = new ArrayList<>();
        quotes.add(new Quote("A"));

        when(mockquoteRepository.findAll()).thenReturn(quotes);
        List<Quote> result = mockquoteService.searchByLength(length, "Equal to");
        // searchByLengthはnullではなく空のリストやデータを返す
        assertNotNull(result);

        if (length.equals("1")) {
            assertFalse(result.isEmpty());
        } else {
            assertTrue(result, isEmpty());
        }
    }


    // 正常Less than
    @Test // 一致する名言オブジェクトが一つのとき
    void testSearchByLengthLessOne() {
        List<Quote> list1 = new ArrayList<>();// モックデータを入れるためのデータ
        List<Quote> list2 = new ArrayList<>();// 10字未満の検索結果を格納するリスト
        List<Quote> list3 = new ArrayList<>();// 比較用
        list1.add(new Quote("地球は青かった", "ガガーリン"));
        list1.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        list1.add(new Quote("憧れるのをやめましょう", "大谷翔平"));

        // mockquoteRepository.findAll()が呼ばれたとき、list1を返す
        Mockito.when(mockquoteRepository.findAll()).thenReturn(list1);

        list2.addAll(mockquoteService.searchByLength("10", "Less than"));

        // list1の最初のQuoteを取得し、list3に追加
        list3.add(list1.get(0));
        assertEquals(list3, list2);
    }


    @Test // 一致するQuoteオブジェクトが複数のとき
    void testSearchByLengthLessMultiple() {
        List<Quote> list1 = new ArrayList<>();
        List<Quote> list2 = new ArrayList<>();
        List<Quote> list3 = new ArrayList<>();
        list1.add(new Quote("地球は青かった", "ガガーリン"));
        list1.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        list1.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        Mockito.when(mockquoteRepository.findAll()).thenReturn(list1);

        list2.addAll(mockquoteService.searchByLength("15", "Less than"));
        list3.add(list1.get(0));
        list3.add(list1.get(2));
        assertEquals(list3, list2);
    }


    @Test // 一致するQuoteオブジェクトがなかったとき→空のリストを返す
    void testSearchByLengthLessEmpty() {
        List<Quote> list1 = new ArrayList<>();
        List<Quote> list2 = new ArrayList<>();

        list1.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        list1.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        Mockito.when(mockquoteRepository.findAll()).thenReturn(list1);

        list2.addAll(mockquoteService.searchByLength("5", "Less than"));
        assertEquals(Collections.emptyList(), list2);
    } 



    @Test// 一致する名言オブジェクトが一つのとき
    void testSearchByLengthEqualOne() {
        List<Quote> list1 = new ArrayList<>();
        List<Quote> list2 = new ArrayList<>();
        List<Quote> list3 = new ArrayList<>();
        list1.add(new Quote("地球は青かった", "ガガーリン"));
        list1.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        list1.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        Mockito.when(mockquoteRepository.findAll()).thenReturn(list1);

        list2.addAll(mockquoteService.searchByLength("11", "Equal to"));
        list3.add(list1.get(2));
        assertEquals(list3, list2);
    }


    @Test // 一致するQuoteオブジェクトが複数のとき
    void testSearchByLengthEqualMultiple() {
        List<Quote> list1 = new ArrayList<>();
        List<Quote> list2 = new ArrayList<>();
        List<Quote> list3 = new ArrayList<>();
        list1.add(new Quote("地球は青い", "ガガーリン"));
        list1.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        list1.add(new Quote("憧れをもつ", "大谷翔平"));
        Mockito.when(mockquoteRepository.findAll()).thenReturn(list1);

        list2.addAll(mockquoteService.searchByLength("5", "Equal to"));
        list3.add(list1.get(0));
        list3.add(list1.get(2));
        assertEquals(list3, list2);
    }


    @Test // 一致するQuoteオブジェクトがなかったとき→空のリストを返す
    void testSearchByLengthEqualEmpty() {
        List<Quote> list1 = new ArrayList<>();
        List<Quote> list2 = new ArrayList<>();

        list1.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        list1.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        Mockito.when(mockquoteRepository.findAll()).thenReturn(list1);

        list2.addAll(mockquoteService.searchByLength("3", "Equal to"));
        assertEquals(Collections.emptyList(), list2);
    } 



    @Test// 一致する名言オブジェクトが一つのとき
    void testSearchByLengthGreaterOne() {
        List<Quote> list1 = new ArrayList<>();
        List<Quote> list2 = new ArrayList<>();
        List<Quote> list3 = new ArrayList<>();
        list1.add(new Quote("地球は青かった", "ガガーリン"));
        list1.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        list1.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        Mockito.when(mockquoteRepository.findAll()).thenReturn(list1);

        list2.addAll(mockquoteService.searchByLength("15", "Greater than"));
        list3.add(list1.get(1));
    }


    @Test // 一致する名言オブジェクトが複数のとき
    void testSearchByLengthGreaterMultiple() {
        List<Quote> list1 = new ArrayList<>();
        List<Quote> list2 = new ArrayList<>();
        List<Quote> list3 = new ArrayList<>();
        list1.add(new Quote("地球は青かった", "ガガーリン"));
        list1.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        list1.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        Mockito.when(mockquoteRepository.findAll()).thenReturn(list1);

        list2.addAll(mockquoteService.searchByLength("10", "Greater than"));
        list3.add(list1.get(1));
        list3.add(list1.get(2));
        assertEquals(list3, list2);
    }


    @Test // 一致するQuoteオブジェクトがなかったとき→空のリストを返す
    void testSearchByLengthGreaterEmpty() {
        List<Quote> list1 = new ArrayList<>();
        List<Quote> list2 = new ArrayList<>();
        list1.add(new Quote("地球は青かった", "ガガーリン"));
        list1.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        list1.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        Mockito.when(mockquoteRepository.findAll()).thenReturn(list1);

        list2.addAll(mockquoteService.searchByLength("30", "Greater than"));
        assertEquals(Collections.emptyList(), list2);
    }



    // 例外処理
    // DBからQuoteオブジェクトを取得できなかったとき
    @Test
    void testSearchByLengthError() {
        when(mockquoteRepository.findAll()).thenThrow(new RuntimeException("DB接続エラー"));
        List<Quote> result = mockquoteService.searchByLength("10", "Less than");
        assertNull(result);
    }

}
