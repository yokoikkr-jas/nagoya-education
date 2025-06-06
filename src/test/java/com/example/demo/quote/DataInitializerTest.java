package com.example.demo.quote;

import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import com.example.demo.quote.repository.QuoteRepository;

import com.example.demo.quote.model.Quote;

@SpringBootTest
public class DataInitializerTest {

    @MockitoSpyBean
    private DataInitializer dataInitializer;// モックインスタンスの宣言
    @Autowired
    private QuoteRepository quoteRepository;
    @Mock
    private BufferedReader bufferedReader;

    @BeforeEach
    void delateAll() {
        quoteRepository.deleteAll();
    }
    /*
     * テスト概要：正常系
     * 初期化メソッドを呼び出して、csvファイルを読み込むことができる
     * 初期化メソッドを呼び出して、DBの内容とcsvの内容が等しくなる
     */

    @Test
    void testRun1() throws Exception {
        doReturn("C:/a/n" + //
                "agoya-education/src/test/java/com/example/demo/quote/test.csv").when(dataInitializer)
                .getFilePath();

        dataInitializer.run();
        String expectedQuote = "殺してしま";
        String expectedAuthor = "織田信長";
        String actualQuote = quoteRepository.findAll().get(0).getText();
        String actualAuthor = quoteRepository.findAll().get(0).getAuthor();
        assertEquals(expectedQuote, actualQuote);
        assertEquals(expectedAuthor, actualAuthor);
    }

    /*
     * テスト概要：正常系
     * 初期化メソッドを呼び出して、csvファイルを複数行、読み込むことができる
     */
    @Test
    void testRun2() throws Exception {

        doReturn("C:/a/nagoya-education/src/test/java/com/example/demo/quote/test2.csv")
                .when(dataInitializer).getFilePath();

        dataInitializer.run();
        String expectedQuote1 = "急がず休まず";
        String expectedAuthor1 = "ゲーテ";
        String actualQuote1 = quoteRepository.findAll().get(0).getText();
        String actualAuthor1 = quoteRepository.findAll().get(0).getAuthor();
        String expectedQuote2 = "少年よ大志を抱け";
        String expectedAuthor2 = "クラーク";
        String actualQuote2 = quoteRepository.findAll().get(1).getText();
        String actualAuthor2 = quoteRepository.findAll().get(1).getAuthor();
        String expectedQuote3 = "想像力は知識よりも重要である";
        String expectedAuthor3 = "アインシュタイン";
        String actualQuote3 = quoteRepository.findAll().get(2).getText();
        String actualAuthor3 = quoteRepository.findAll().get(2).getAuthor();

        assertEquals(expectedQuote1, actualQuote1);
        assertEquals(expectedAuthor1, actualAuthor1);
        assertEquals(expectedQuote2, actualQuote2);
        assertEquals(expectedAuthor2, actualAuthor2);
        assertEquals(expectedQuote3, actualQuote3);
        assertEquals(expectedAuthor3, actualAuthor3);

    }
    /*
     * 異常系
     * CSVファイルが存在しない
     * 
     * @throws Exception
     */

    // @Test
    // void testRun3() throws Exception {
    // doReturn("C:/a/nagoya-education/src/test/java/com/example/demo/quote/test9.csv")
    // .when(dataInitializer).getFilePath();

    // dataInitializer.run();
    // List<Quote> expectedQuotes = new ArrayList<>();
    // List<Quote> actualQuotes = quoteRepository.findAll();
    // assertEquals(expectedQuotes, actualQuotes);

    // }

    /*
     * 正常系
     * 空のファイルを読み込んだ場合
     */

    @Test
    void testRun3() throws Exception {
        doReturn("C:/a/nagoya-education/src/test/java/com/example/demo/quote/empty.csv")
                .when(dataInitializer).getFilePath();
        dataInitializer.run();
        List<Quote> expectedQuotes = new ArrayList<>();
        List<Quote> actualQuotes = quoteRepository.findAll();
        assertEquals(expectedQuotes, actualQuotes);
    }
    /*
     * 異常系
     * CSVファイルを開くことができない
     */

    // @Test
    // void testRun5() throws Exception {
    // when(bufferedReader.readLine()).thenThrow(new IOException("ファイル読み込みエラー"));
    // dataInitializer.run();
    // assertThrows(IOException.class, () -> dataInitializer.run());

    // }

    /*
     * 正常系
     * csvファイルにヘッダーしかない
     */

    @Test
    void testRun4() throws Exception {
        doReturn("C:/a/nagoya-education/src/test/java/com/example/demo/quote/test4.csv")
                .when(dataInitializer).getFilePath();
        dataInitializer.run();
        List<Quote> expectedQuotes = new ArrayList<>();
        List<Quote> actualQuotes = quoteRepository.findAll();
        assertEquals(expectedQuotes, actualQuotes);
    }
}
