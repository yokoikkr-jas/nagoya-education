package com.example.demo.quote.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.example.demo.quote.model.Quote;
import com.example.demo.quote.repository.QuoteRepository;

@SpringBootTest
public class QuoteServiceTest {

    @Mock
    private QuoteRepository quoteRepositoryMock;

    @InjectMocks
    private QuoteService injectQuoteService;

    @MockitoSpyBean
    private QuoteService quoteService;

    /*
     * ヘッダーのみが入力されている外部ファイルにデータが出力される
     */
    @Test
    void testAddQuote1(@TempDir Path tempDir) throws Exception {
        List<String> initialLines = List.of("quote,author");
        Path csvFile = Files.write //
        (Files.createTempFile(tempDir, "sample1-", ".csv"), initialLines);

        List<String> expectedList = List.of("quote,author", "text1,author1");

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
        List<String> initialLines = List.of("quote,author", "text1,author1", "text2,author2");

        Path csvFile = Files.write //
        (Files.createTempFile(tempDir, "sample2-", ".csv"), initialLines);

        List<String> expectedList =
                List.of("quote,author", "text1,author1", "text2,author2", "text3,author3");

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
        List<String> initialLines = List.of("quote,author", "text1,author1");

        Path csvFile = Files.write //
        (Files.createTempFile(tempDir, "sample3-", ".csv"), initialLines);

        List<String> expectedList =
                List.of("quote,author", "text1,author1", "text2,author2", "text3,author3");

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
        List<String> initialLines = List.of("quote,author", "text1,author1");

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
     * 出力する外部ファイルが存在しない場合、ファイルを自動的に作成しデータを出力する FileWriterの仕様上、出力先のファイルが自動生成される
     */
    @Test
    void testAddQuote5(@TempDir Path tempDir) throws Exception {
        List<String> initialLines = List.of("quote,author", "text1,author1");

        Path csvFile = Files.write //
        (Files.createTempFile(tempDir, "sample5-", ".csv"), initialLines);

        List<String> expectedList = List.of("text2,author2");

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
     * 出力する外部ファイルが存在しない、かつ、ファイルを作成できない(ディレクトリを削除して状況を再現)場合、nullを返却する 外側のtry-catch、ファイル読み込み時の例外
     */
    @Test
    void testAddQuote6(@TempDir Path tempDir) throws Exception {
        List<String> initialLines = List.of("quote,author", "text1,author1");
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
     * 出力するファイルが通常ファイルとして存在していない(ディレクトリとして存在している)場合、nullを返却する 外側のtry-catch、ファイル読み込み時の例外
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
     * 出力する外部ファイルに出力できない場合、nullを返却する 内側のtry-catch、ファイル書き込み時の例外
     */
    @Test
    void testAddQuote8(@TempDir Path tempDir) throws Exception {
        List<String> initialLines = List.of("quote,author", "text1,author1");
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

    /*
     * 前提条件：DBから名言オブジェクトを取得 手順：キーワードqueryと検索対象optionの条件で検索を行う。 入力値：query"地球"、option"text"
     * 期待する結果：text"地球は青かった", author"ガガーリン"
     * 
     * 内容：検索対象が名言のみの場合、部分一致した名言オブジェクトの リストを作成し、そのリストを取り出せるかどうか。
     */
    @Test
    void testPartialMatch_main() {
        List<Quote> allList = new ArrayList<>();
        List<Quote> actualList = new ArrayList<>();
        List<Quote> expectedList = new ArrayList<>();
        allList.add(new Quote("地球は青かった", "ガガーリン"));
        allList.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        allList.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        allList.add((new Quote("僕の大冒険", "僕")));
        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        actualList.addAll(injectQuoteService.partialMatch("地球", "text"));
        expectedList.add(allList.get(0));
        assertIterableEquals(expectedList, actualList);

    }

    /*
     * 前提条件：DBから名言オブジェクトを取得 手順：キーワードqueryと検索対象optionの条件で検索を行う。 入力値：query"テスト", option"text"
     * 期待する結果：null
     * 
     * 内容：検索対象が名言のみの場合、部分一致した名言オブジェクトの リストが空っぽの際にnull。
     */
    @Test
    void testPartialMatchText_quoteEmpty() {
        List<Quote> allList = new ArrayList<>();
        allList.add(new Quote("地球は青かった", "ガガーリン"));
        allList.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        allList.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        allList.add((new Quote("僕の大冒険", "僕")));

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        assertNull(injectQuoteService.partialMatch("テスト", "text"));
    }

    /*
     * 前提条件：DBから名言オブジェクトを取得 手順：キーワードqueryと検索対象optionの条件で検索を行う。 入力値：query"テスト", option"text"
     * 期待する結果：null
     * 
     * 内容：DBから受け取ったリストが空っぽの場合はnull。
     */
    @Test
    void testPartialMatchText_dbEmpty() {
        List<Quote> allList = new ArrayList<>();

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        assertNull(injectQuoteService.partialMatch("テスト", "text"));

    }

    /*
     * 前提条件：DBから名言オブジェクトを取得 手順：キーワードqueryと検索対象optionの条件で検索を行う。 入力値：query"は", option"text"
     * 期待する結果："地球は青かった", "ガガーリン" "天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"
     * 
     * 内容：リスト内の要素位置からループの１回目と２回目を確認し、ループの開始地点と 終了条件に誤りがないか。
     */
    @Test
    void testPartialMatchText_roop() {
        List<Quote> allList = new ArrayList<>();
        List<Quote> actuaList = new ArrayList<>();
        List<Quote> expectedList = new ArrayList<>();
        allList.add(new Quote("地球は青かった", "ガガーリン"));
        allList.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        allList.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        allList.add((new Quote("僕の大冒険", "僕")));

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        actuaList.addAll(injectQuoteService.partialMatch("は", "text"));
        expectedList.add(allList.get(0));
        expectedList.add(allList.get(1));
        assertIterableEquals(expectedList, actuaList);
    }

    /*
     * 前提条件：DBから名言オブジェクトを取得 手順：キーワードqueryと検索対象optionの条件で検索を行う。 入力値：query"大"、option"author"
     * 期待する結果：text"憧れるのをやめましょう", author"大谷翔平"
     * 
     * 内容：検索対象が著者の場合、部分一致した名言オブジェクトの リストを作成し、そのリストを取り出せるかどうか。
     */
    @Test
    void testPartialMatchAuthor_main() {
        List<Quote> allList = new ArrayList<>();
        List<Quote> actuaList = new ArrayList<>();
        List<Quote> expectedList = new ArrayList<>();
        allList.add(new Quote("地球は青かった", "ガガーリン"));
        allList.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        allList.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        allList.add((new Quote("僕の大冒険", "僕")));

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        actuaList.addAll(injectQuoteService.partialMatch("大", "author"));
        expectedList.add(allList.get(2));
        assertIterableEquals(expectedList, actuaList);

    }

    /*
     * 前提条件：DBから名言オブジェクトを取得 手順：キーワードqueryと検索対象optionの条件で検索を行う。 入力値：query"ノーネイム", option"author"
     * 期待する結果：null
     * 
     * 内容：検索対象が著者の場合、部分一致した名言オブジェクトの リストが空っぽの際にnull。
     */
    @Test
    void testPartialMatchAuthor_quoteEmpty() {
        List<Quote> allList = new ArrayList<>();
        allList.add(new Quote("地球は青かった", "ガガーリン"));
        allList.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        allList.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        allList.add((new Quote("僕の大冒険", "僕")));

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        assertNull(injectQuoteService.partialMatch("ノーネイム", "author"));

    }

    /*
     * 前提条件：DBから名言オブジェクトを取得 手順：キーワードqueryと検索対象optionの条件で検索を行う。 入力値：query"ノーネイム", option"author"
     * 期待する結果：null
     * 
     * 内容：DBから受け取ったリストが空っぽの場合はnull。
     */
    @Test
    void testPartialMatchAuthor_dbEmpty() {
        List<Quote> allList = new ArrayList<>();

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        assertNull(injectQuoteService.partialMatch("ノーネイム", "author"));

    }

    /*
     * 前提条件：DBから名言オブジェクトを取得 手順：キーワードqueryと検索対象optionの条件で検索を行う。 入力値：query"ン", option"author"
     * 期待する結果："地球は青かった", "ガガーリン" "天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"
     * 
     * 内容：ループの１回目と２回目のリスト内の要素位置を確認し、ループの開始地点と 終了条件に誤りがないか。
     */
    @Test
    void testPartialMatchAuthor_roop() {
        List<Quote> allList = new ArrayList<>();
        List<Quote> actuaList = new ArrayList<>();
        List<Quote> expectedList = new ArrayList<>();
        allList.add(new Quote("地球は青かった", "ガガーリン"));
        allList.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        allList.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        allList.add((new Quote("僕の大冒険", "僕")));

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        actuaList.addAll(injectQuoteService.partialMatch("ン", "author"));
        expectedList.add(allList.get(0));
        expectedList.add(allList.get(1));
        assertIterableEquals(expectedList, actuaList);
    }

    /*
     * 前提条件：DBから名言オブジェクトを取得 手順：キーワードqueryと検索対象optionの条件で検索を行う。 入力値：query"僕"、option"both"
     * 期待する結果：text"僕の大冒険", author"僕"
     * 
     * 内容：検索対象が両方の場合、部分一致した名言オブジェクトの リストを作成し、そのリストを取り出せるかどうか。名言と著者に同じ
     * キーワードがあった際に、リスト内に同じ名言オブジェクトないか。
     */
    @Test
    void testPartialMatchBoth_main() {
        List<Quote> allList = new ArrayList<>();
        List<Quote> actuaList = new ArrayList<>();
        List<Quote> expectedList = new ArrayList<>();
        allList.add(new Quote("地球は青かった", "ガガーリン"));
        allList.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        allList.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        allList.add((new Quote("僕の大冒険", "僕")));

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        actuaList.addAll(injectQuoteService.partialMatch("僕", "both"));
        expectedList.add(allList.get(3));
        assertIterableEquals(expectedList, actuaList);

    }

    /*
     * 前提条件：DBから名言オブジェクトを取得 手順：キーワードqueryと検索対象optionの条件で検索を行う。 入力値：query"両方", option"both"
     * 期待する結果：null
     * 
     * 内容：検索対象が両方の場合、部分一致した名言オブジェクトの リストが空っぽの際にnull。
     */
    @Test
    void testPartialMatchBoth_quoteEmpty() {
        List<Quote> allList = new ArrayList<>();
        allList.add(new Quote("地球は青かった", "ガガーリン"));
        allList.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        allList.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        allList.add((new Quote("僕の大冒険", "僕")));

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        assertNull(injectQuoteService.partialMatch("両方", "both"));

    }

    /*
     * 前提条件：DBから名言オブジェクトを取得 手順：キーワードqueryと検索対象optionの条件で検索を行う。 入力値：query"両方", option"both"
     * 期待する結果：null
     * 
     * 内容：DBから受け取ったリストが空っぽの場合はnull。
     */
    @Test
    void testPartialMatchBoth_dbEmpty() {
        List<Quote> allList = new ArrayList<>();

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        assertNull(injectQuoteService.partialMatch("両方", "both"));

    }

    /*
     * 前提条件：DBから名言オブジェクトを取得 手順：キーワードqueryと検索対象optionの条件で検索を行う。 入力値：query"僕", option"both"
     * 期待する結果："僕のお家", "僕家" "僕の大冒険", "僕"
     * 
     * 内容：ループの１個目と２個目のリスト内の要素位置を確認し、ループの開始地点と 終了条件に誤りがないか。
     */
    @Test
    void testPartialMatchBoth_roop() {
        List<Quote> allList = new ArrayList<>();
        List<Quote> actuaList = new ArrayList<>();
        List<Quote> expectedList = new ArrayList<>();
        allList.add(new Quote("僕のお家", "僕家"));
        allList.add(new Quote("地球は青かった", "ガガーリン"));
        allList.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        allList.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        allList.add((new Quote("僕の大冒険", "僕")));

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        actuaList.addAll(injectQuoteService.partialMatch("僕", "both"));
        expectedList.add(allList.get(0));
        expectedList.add(allList.get(4));
        assertIterableEquals(expectedList, actuaList);
    }

    /*
     * 前提条件：DBから名言オブジェクトを取得 手順：キーワードqueryと検索対象optionの条件で検索を行う。 入力値：query"我", option"both"
     * 期待する結果："僕のお家", "我家"
     * 
     * 内容：著者分岐の場合にループ１個目の処理が適切に行われるか。
     */
    @Test
    void testPartialMatchBoth_roopAuthorOne() {
        List<Quote> allList = new ArrayList<>();
        List<Quote> actuaList = new ArrayList<>();
        List<Quote> expectedList = new ArrayList<>();
        allList.add(new Quote("僕のお家", "我家"));
        allList.add(new Quote("地球は青かった", "ガガーリン"));
        allList.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        allList.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        allList.add((new Quote("僕の大冒険", "僕")));

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        actuaList.addAll(injectQuoteService.partialMatch("我", "both"));
        expectedList.add(allList.get(0));
        assertIterableEquals(expectedList, actuaList);
    }

    /*
     * 前提条件：DBから名言オブジェクトを取得 手順：キーワードqueryと検索対象optionの条件で検索を行う。 入力値：query"ン", option"both"
     * 期待する結果："地球は青かった", "ガガーリン" "天才は1%のひらめきと99%の努力でつくられる","トーマス・エジソン"
     * 
     * 内容：著者分岐の場合にループ2個目以降の処理が適切に行われるか。
     */
    @Test
    void testPartialMatchBoth_roopAuthorTwo() {
        List<Quote> allList = new ArrayList<>();
        List<Quote> actuaList = new ArrayList<>();
        List<Quote> expectedList = new ArrayList<>();
        allList.add(new Quote("僕のお家", "僕家"));
        allList.add(new Quote("地球は青かった", "ガガーリン"));
        allList.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        allList.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        allList.add((new Quote("僕の大冒険", "僕")));

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        actuaList.addAll(injectQuoteService.partialMatch("ン", "both"));
        expectedList.add(allList.get(1));
        expectedList.add(allList.get(2));
        assertIterableEquals(expectedList, actuaList);
    }

    /*
     * 例外発生
     */
    @Test
    void testStackTrace() {
        Mockito.when(quoteRepositoryMock.findAll()).thenThrow(new RuntimeException());
        List<Quote> method = injectQuoteService.partialMatch("僕", "text");
        assertNull(method);

    }
}
