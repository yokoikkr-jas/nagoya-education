package com.example.demo.quote.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.example.demo.quote.model.Quote;

@SpringBootTest
public class QuoteServiceTest {

    @MockitoSpyBean
    private QuoteService quoteService;

    /*
     * ヘッダーのみが入力されている外部ファイルにデータが出力される
     */
    @Test
    void testAddQuote1(@TempDir Path tempDir) throws Exception {
        List<String> initialLines = List.of(
                "quote,author");
        Path csvFile = Files.write //
        (Files.createTempFile(tempDir, "sample1-", ".csv"), initialLines);

        List<String> expectedList = List.of(
                "quote,author",
                "text1,author1");

        String csvFilePath = csvFile.toString();

        doReturn(csvFilePath).when(quoteService).getFilePath();
        Quote quote = new Quote("text1", "author1");
        quoteService.addQuote(quote);

        List<String> actualList = Files.readAllLines(csvFile);

        assertEquals(expectedList, actualList);
    }

    /*
     * ヘッダーといくつかのデータが入力されている外部ファイルにデータが出力される
     */
    @Test
    void testAddQuote2(@TempDir Path tempDir) throws Exception {
        List<String> initialLines = List.of(
                "quote,author",
                "text1,author1",
                "text2,author2");

        Path csvFile = Files.write //
        (Files.createTempFile(tempDir, "sample2-", ".csv"), initialLines);

        List<String> expectedList = List.of(
                "quote,author",
                "text1,author1",
                "text2,author2",
                "text3,author3");

        String csvFilePath = csvFile.toString();

        doReturn(csvFilePath).when(quoteService).getFilePath();
        Quote quote = new Quote("text3", "author3");
        quoteService.addQuote(quote);

        List<String> actualList = Files.readAllLines(csvFile);

        assertEquals(expectedList, actualList);
    }

    /*
     * 入力された複数のデータ(2個以上)が外部ファイルに出力される
     */
    @Test
    void testAddQuote3(@TempDir Path tempDir) throws Exception {
        List<String> initialLines = List.of(
                "quote,author",
                "text1,author1");

        Path csvFile = Files.write //
        (Files.createTempFile(tempDir, "sample3-", ".csv"), initialLines);

        List<String> expectedList = List.of(
                "quote,author",
                "text1,author1",
                "text2,author2",
                "text3,author3");

        String csvFilePath = csvFile.toString();

        doReturn(csvFilePath).when(quoteService).getFilePath();
        Quote quote1 = new Quote("text2", "author2");
        Quote quote2 = new Quote("text3", "author3");
        quoteService.addQuote(quote1);
        quoteService.addQuote(quote2);

        List<String> actualList = Files.readAllLines(csvFile);

        assertEquals(expectedList, actualList);
    }

    /*
     * 入力されたデータがDBに登録される
     */
    @Test
    void testAddQuote4(@TempDir Path tempDir) throws Exception {
        List<String> initialLines = List.of(
                "quote,author",
                "text1,author1");

        Path csvFile = Files.write //
        (Files.createTempFile(tempDir, "sample4-", ".csv"), initialLines);

        String csvFilePath = csvFile.toString();

        doReturn(csvFilePath).when(quoteService).getFilePath();
        Quote expectedQuote = new Quote("text2", "author2");
        quoteService.addQuote(expectedQuote);
        Quote actualQuote = quoteService.getAllQuotes().getLast();

        assertEquals(expectedQuote.getText(), actualQuote.getText());
        assertEquals(expectedQuote.getAuthor(), actualQuote.getAuthor());
    }

    /*
     * 出力する外部ファイルが存在しない場合、ファイルを自動的に作成しデータを出力する
     * FileWriterの仕様上、出力先のファイルが自動生成される
     */
    @Test
    void testAddQuote5(@TempDir Path tempDir) throws Exception {
        List<String> initialLines = List.of(
                "quote,author",
                "text1,author1");

        Path csvFile = Files.write //
        (Files.createTempFile(tempDir, "sample5-", ".csv"), initialLines);

        List<String> expectedList = List.of(
                "text2,author2");

        String csvFilePath = csvFile.toString();
        Files.delete(csvFile);
        assertFalse(Files.exists(csvFile));

        doReturn(csvFilePath).when(quoteService).getFilePath();
        Quote quote = new Quote("text2", "author2");
        quoteService.addQuote(quote);

        List<String> actualList = Files.readAllLines(csvFile);

        assertEquals(expectedList, actualList);
    }

    /*
     * 出力する外部ファイルが存在しない、かつ、ファイルを作成できない(ディレクトリを削除して状況を再現)場合、nullを返却する
     * 外側のtry-catch、ファイル読み込み時の例外
     */
    @Test
    void testAddQuote6(@TempDir Path tempDir) throws Exception {
        List<String> initialLines = List.of(
                "quote,author",
                "text1,author1");
        Path csvFile = Files.write //
        (Files.createTempFile(tempDir, "sample6-", ".csv"), initialLines);

        String csvFilePath = csvFile.toString();
        Files.delete(csvFile);
        Files.delete(tempDir);
        assertFalse(Files.exists(csvFile));
        assertFalse(Files.exists(tempDir));

        doReturn(csvFilePath).when(quoteService).getFilePath();
        Quote quote = new Quote("text2", "author2");
        Quote actualQuote = quoteService.addQuote(quote);

        assertNull(actualQuote);

    }

    /*
     * 出力するファイルが通常ファイルとして存在していない(ディレクトリとして存在している)場合、nullを返却する
     * 外側のtry-catch、ファイル読み込み時の例外
     */
    @Test
    void testAddQuote7(@TempDir Path tempDir) throws Exception {
        Path newDir = Files.createDirectory(tempDir.resolve("sample7.csv"));

        String newDirPath = newDir.toString();
        doReturn(newDirPath).when(quoteService).getFilePath();
        Quote quote = new Quote("text2", "author2");
        Quote actualQuote = quoteService.addQuote(quote);

        assertNull(actualQuote);
    }

    /*
     * 出力する外部ファイルに出力できない場合、nullを返却する
     * 内側のtry-catch、ファイル書き込み時の例外
     */
    @Test
    void testAddQuote8(@TempDir Path tempDir) throws Exception {
        List<String> initialLines = List.of(
                "quote,author",
                "text1,author1");
        Path csvFile = Files.write //
        (Files.createTempFile(tempDir, "sample8-", ".csv"), initialLines);

        String csvFilePath = csvFile.toString();
        csvFile.toFile().setReadOnly();
        assertFalse(csvFile.toFile().canWrite());

        doReturn(csvFilePath).when(quoteService).getFilePath();
        Quote quote = new Quote("text2", "author2");
        Quote actualQuote = quoteService.addQuote(quote);

        assertNull(actualQuote);
    }

}
