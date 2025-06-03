package com.example.demo.quote.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;

import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
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
     * 入力されたデータが外部ファイルに出力される
     */
    @Test
    void testAddQuote1(@TempDir Path tempDir) throws Exception {
        Path csvFile1 = Files.createTempFile(tempDir, "sample1-", ".csv");

        List<String> list1 = List.of(
                "text1,author1");

        String csvFilePath = csvFile1.toString();

        doReturn(csvFilePath).when(quoteService).getFilePath();
        Quote quote = new Quote("text1", "author1");
        quoteService.addQuote(quote);

        List<String> list2 = Files.readAllLines(csvFile1);

        assertEquals(list1, list2);
    }

    /*
     * 入力された複数のデータ(2個以上)が外部ファイルに出力される
     */
    @Test
    void testAddQuote2(@TempDir Path tempDir) throws Exception {
        Path csvFile1 = Files.createTempFile(tempDir, "sample2-", ".csv");

        List<String> list1 = List.of(
                "text2,author2",
                "text3,author3");

        String csvFilePath = csvFile1.toString();

        doReturn(csvFilePath).when(quoteService).getFilePath();
        Quote quote1 = new Quote("text2", "author2");
        Quote quote2 = new Quote("text3", "author3");
        quoteService.addQuote(quote1);
        quoteService.addQuote(quote2);

        List<String> list2 = Files.readAllLines(csvFile1);

        assertEquals(list1, list2);
    }

    /*
     * 入力されたデータがDBに登録される
     */
    @Test
    void testAddQuote3(@TempDir Path tempDir) throws Exception {
        Path csvFile1 = Files.createTempFile(tempDir, "sample3-", ".csv");

        List<Quote> list1 = new ArrayList<>();
        String csvFilePath = csvFile1.toString();

        doReturn(csvFilePath).when(quoteService).getFilePath();
        Quote quote1 = new Quote("text4", "author4");
        quoteService.addQuote(quote1);
        list1.add(quote1);
        Quote q1 = quoteService.getAllQuotes().getLast();

        assertEquals(quote1.getText(), q1.getText());
        assertEquals(quote1.getAuthor(), q1.getAuthor());
    }

    /*
     * 出力する外部ファイルを開けない場合、nullを返却する
     * setReadableでは読み込めない状態にならない
     * ロックすれば読み込めずnullを返却はするが、一時ディレクトリを削除できずにエラーになる
     */
    @Test
    void testAddQuote4(@TempDir Path tempDir) throws Exception {
        Path csvFile1 = Files.createTempFile(tempDir, "sample4-", ".csv");

        String csvFilePath = csvFile1.toString();

        doReturn(csvFilePath).when(quoteService).getFilePath();
        Quote quote = new Quote("text5", "author5");

        csvFile1.toFile().setReadable(false, false);
        Quote q = quoteService.addQuote(quote);
        assertNull(q);

        // FileChannel channel = FileChannel.open(csvFile1, StandardOpenOption.WRITE);
        // FileLock lock = channel.lock();
        // Quote q = quoteService.addQuote(quote);
        // assertNull(q);
        // lock.release();
        // channel.close();
    }

    /*
     * 出力する外部ファイルが存在しない場合、ファイルを自動的に作成しデータを出力する
     */
    @Test
    void testAddQuote5(@TempDir Path tempDir) throws Exception {
        Path csvFile1 = Files.createTempFile(tempDir, "sample5-", ".csv");

        List<String> list1 = List.of(
                "text6,author6");

        String csvFilePath = csvFile1.toString();
        Files.deleteIfExists(csvFile1);
        assertFalse(Files.exists(csvFile1));

        doReturn(csvFilePath).when(quoteService).getFilePath();
        Quote quote = new Quote("text6", "author6");
        quoteService.addQuote(quote);

        List<String> list2 = Files.readAllLines(csvFile1);

        assertEquals(list1, list2);
    }

    /*
     * 出力する外部ファイルが存在しない、かつ、ファイルを作成できない(ディレクトリを削除して状況を再現)場合、nullを返却する
     */
    @Test
    void testAddQuote6(@TempDir Path tempDir) throws Exception {
        Path csvFile1 = Files.createTempFile(tempDir, "sample6-", ".csv");

        String csvFilePath = csvFile1.toString();
        Files.deleteIfExists(csvFile1);
        Files.delete(tempDir);
        assertFalse(Files.exists(csvFile1));

        doReturn(csvFilePath).when(quoteService).getFilePath();
        Quote quote = new Quote("text7", "author7");
        Quote q = quoteService.addQuote(quote);

        assertNull(q);

    }

    /*
     * 出力するファイルが通常ファイルとして存在していない(ディレクトリとして存在している)場合、nullを返却する
     */
    @Test
    void testAddQuote7(@TempDir Path tempDir) throws Exception {
        Path newDir = Files.createDirectory(tempDir.resolve("sample7.csv"));

        String newDirPath = newDir.toString();
        doReturn(newDirPath).when(quoteService).getFilePath();
        Quote quote = new Quote("text8", "author8");
        Quote q = quoteService.addQuote(quote);

        assertNull(q);
    }

    /*
     * 出力する外部ファイルに出力できない場合、nullを返却する
     */
    @Test
    void testAddQuote8(@TempDir Path tempDir) throws Exception {
        Path csvFile1 = Files.createTempFile(tempDir, "sample8-", ".csv");

        String csvFilePath = csvFile1.toString();
        csvFile1.toFile().setReadOnly();

        doReturn(csvFilePath).when(quoteService).getFilePath();
        Quote quote = new Quote("text9", "author9");
        Quote q = quoteService.addQuote(quote);

        assertNull(q);
    }

}
