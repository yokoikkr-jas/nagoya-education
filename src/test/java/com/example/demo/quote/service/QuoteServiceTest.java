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

@SpringBootTest
public class QuoteServiceTest {

    @Mock
    private QuoteRepository mockquoteRepository;
    @InjectMocks
    private QuoteService mockquoteService;


    // 正常Less than
    @Test
    void testSearchByLength_Less() {
        List<Quote> list1 = new ArrayList<>();
        List<Quote> list2 = new ArrayList<>();
        List<Quote> list3 = new ArrayList<>();
        list1.add(new Quote("地球は青かった", "ガガーリン"));
        list1.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        list1.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        Mockito.when(mockquoteRepository.findAll()).thenReturn(list1);

        list2.addAll(mockquoteService.searchByLength("10", "Less than"));
        list3.add(list1.get(0));
        assertEquals(list3, list2);
    }


    // 正常Equal to
    @Test
    void testSearchByLength_Equal() {
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


    // 正常Greater than
    @Test
    void testSearchByLength_Greater() {
        List<Quote> list1 = new ArrayList<>();
        List<Quote> list2 = new ArrayList<>();
        List<Quote> list3 = new ArrayList<>();
        list1.add(new Quote("地球は青かった", "ガガーリン"));
        list1.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        list1.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        Mockito.when(mockquoteRepository.findAll()).thenReturn(list1);

        list2.addAll(mockquoteService.searchByLength("15", "Greater than"));
        list3.add(list1.get(1));
        assertEquals(list3, list2);
    }


    // 名言オブジェクトを文字数に変換できるか
    @Test
    void testSearchByLength_length() {
        Quote quote = new Quote("地球は青かった", "ガガーリン");
        int length = quote.getText().length();
        assertEquals(7, length);
    }

    // lengthに文字が入力されずに検索
    // nullのとき（空はいらない？）
    @Test
    void testSearchByLength_Noword() {
        List<Quote> result = mockquoteService.searchByLength(null, "Less than");
        assertNull(result);
    }


    // 例外処理
    // DBからQuoteオブジェクトを取得できなかったとき
    @Test
    void testSearchByLength_list() {
        when(mockquoteRepository.findAll()).thenThrow(new RuntimeException("DB接続エラー"));
        List<Quote> result = mockquoteService.searchByLength("10", "Less than");
        assertNull(result);
    }

}
