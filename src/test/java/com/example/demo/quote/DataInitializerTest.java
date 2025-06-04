package com.example.demo.quote;

import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import com.example.demo.quote.repository.QuoteRepository;
import com.example.demo.quote.service.QuoteService;

import net.bytebuddy.implementation.bytecode.Throw;
import com.example.demo.quote.model.Quote;

@SpringBootTest
public class DataInitializerTest {

    @Autowired
    private QuoteService quoteService;

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
        assertEquals("殺してしま", quoteService.getAllQuotes().get(0).getText());
        assertEquals("織田信長", quoteService.getAllQuotes().get(0).getAuthor());
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
        assertEquals("急がず休まず", quoteRepository.findAll().get(0).getText());
        assertEquals("少年よ大志を抱け", quoteRepository.findAll().get(1).getText());
        assertEquals("想像力は知識よりも重要である", quoteRepository.findAll().get(2).getText());

        assertEquals("ゲーテ", quoteRepository.findAll().get(0).getAuthor());
        assertEquals("クラーク", quoteRepository.findAll().get(1).getAuthor());
        assertEquals("アインシュタイン", quoteRepository.findAll().get(2).getAuthor());

    }
    /*
     * 異常系
     * CSVファイルが存在しない
     * 
     * @throws Exception
     */

    @Test
    void testRun3() throws Exception {
        doReturn("C://a//nagoya-education//src//test//java//com//example//demo//quote/test3.csv")
                .when(dataInitializer).getFilePath();
        assertThrows(FileNotFoundException.class, () -> {
            dataInitializer.run();

        });
    }

    /*
     * 正常系
     * 空のファイルを読み込んだ場合
     */

    @Test
    void testRun4() throws Exception {
        doReturn("C:/a/nagoya-education/src//test//java//com//example//demo//quote//empty.csv")
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

    @Test
    void testRun5() throws Exception {
        when(bufferedReader.readLine()).thenThrow(new IOException("ファイル読み込みエラー"));
        dataInitializer.run();
        assertThrows(IOException.class, () -> dataInitializer.run());

    }
}
// ①run呼び出し

// ②findAlii()でList<Quote>型の値をDBから取り出す

// 3Assertで期待値比較
