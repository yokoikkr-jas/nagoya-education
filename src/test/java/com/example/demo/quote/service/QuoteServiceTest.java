package com.example.demo.quote.service;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.quote.model.Quote;
import com.example.demo.quote.repository.QuoteRepository;

@ExtendWith(MockitoExtension.class)
public class QuoteServiceTest {

    @Mock
    private QuoteRepository quoteRepositoryMock;

    @InjectMocks
    private QuoteService quoteService;

    /*
     * 前提条件：なし
     * 手順：キーワードqueryと検索対象optionの条件で検索を行う
     * 入力値：query"地球"、option"text"
     * 期待する結果：null
     * 
     * 内容：DBから名言オブジェクトを取得してない際に
     * nullで戻るかどうか
     */
    @Test
    void testPartialMatchText_noDb() {

        assertNull(quoteService.partialMatch("地球", "text"));

    }

    /*
     * 前提条件：DBから名言オブジェクトを取得
     * 手順：キーワードqueryと検索対象optionの条件で検索を行う
     * 入力値：query"地球"、option"text"
     * 期待する結果：text"地球は青かった", author"ガガーリン"
     * 
     * 内容：検索対象が名言のみの場合、部分一致した名言オブジェクトの
     * リストを作成し、そのリストを取り出せるかどうか
     */
    @Test
    void testPartialMatch_main() {
        List<Quote> allList = new ArrayList<>();
        List<Quote> mainQ = new ArrayList<>();
        List<Quote> sub = new ArrayList<>();
        allList.add(new Quote("地球は青かった", "ガガーリン"));
        allList.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        allList.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        allList.add((new Quote("僕の大冒険", "僕")));
        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        mainQ.addAll(quoteService.partialMatch("地球", "text"));
        sub.add(allList.get(0));
        assertIterableEquals(sub, mainQ);

    }

    /*
     * 前提条件：DBから名言オブジェクトを取得
     * 手順：キーワードqueryと検索対象optionの条件で検索を行う
     * 入力値：query"地球", option"text"
     * 期待する結果：null
     * 
     * 内容：検索対象が名言のみの場合、部分一致した名言オブジェクトの
     * リストが空っぽの際にnull
     */
    @Test
    void testPartialMatchText_quoteEmpty() {
        List<Quote> allList = new ArrayList<>();
        allList.add(new Quote("地球は青かった", "ガガーリン"));
        allList.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        allList.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        allList.add((new Quote("僕の大冒険", "僕")));

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        assertNull(quoteService.partialMatch("テスト", "text"));
    }

    /*
     * 前提条件：DBから名言オブジェクトを取得
     * 手順：キーワードqueryと検索対象optionの条件で検索を行う
     * 入力値：query"テスト", option"text"
     * 期待する結果：null
     * 
     * 内容：DBから受け取ったリストが空っぽの場合はnull
     */
    @Test
    void testPartialMatchText_dbEmpty() {
        List<Quote> allList = new ArrayList<>();

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        assertNull(quoteService.partialMatch("テスト", "text"));

    }

    /*
     * 前提条件：DBから名言オブジェクトを取得
     * 手順：キーワードqueryと検索対象optionの条件で検索を行う
     * 入力値：query"テスト", option"text"
     * 期待する結果："地球は青かった", "ガガーリン"
     * "天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"
     * 
     * 内容：リスト内の要素位置からループの１回目と２回目を確認し、ループの開始地点と
     * 終了条件に誤りがないか
     */
    @Test
    void testPartialMatchText_roop() {
        List<Quote> allList = new ArrayList<>();
        List<Quote> mainQ = new ArrayList<>();
        List<Quote> sub = new ArrayList<>();
        allList.add(new Quote("地球は青かった", "ガガーリン"));
        allList.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        allList.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        allList.add((new Quote("僕の大冒険", "僕")));

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        mainQ.addAll(quoteService.partialMatch("は", "text"));
        sub.add(allList.get(0));
        sub.add(allList.get(1));
        assertIterableEquals(sub, mainQ);
    }

    /*
     * 前提条件：なし
     * 手順：キーワードqueryと検索対象optionの条件で検索を行う
     * 入力値：query"大"、option"author"
     * 期待する結果：null
     * 
     * 内容：DBから名言オブジェクトを取得してない際に
     * nullで戻るかどうか
     */
    @Test
    void testPartialMatchAuthor_noDb() {

        assertNull(quoteService.partialMatch("大", "author"));

    }

    /*
     * 前提条件：DBから名言オブジェクトを取得
     * 手順：キーワードqueryと検索対象optionの条件で検索を行う
     * 入力値：query"大"、option"author"
     * 期待する結果：text"憧れるのをやめましょう", author"大谷翔平"
     * 
     * 内容：検索対象が著者の場合、部分一致した名言オブジェクトの
     * リストを作成し、そのリストを取り出せるかどうか
     */
    @Test
    void testPartialMatchAuthor_main() {
        List<Quote> allList = new ArrayList<>();
        List<Quote> mainQ = new ArrayList<>();
        List<Quote> sub = new ArrayList<>();
        allList.add(new Quote("地球は青かった", "ガガーリン"));
        allList.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        allList.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        allList.add((new Quote("僕の大冒険", "僕")));

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        mainQ.addAll(quoteService.partialMatch("大", "author"));
        sub.add(allList.get(2));
        assertIterableEquals(sub, mainQ);

    }

    /*
     * 前提条件：DBから名言オブジェクトを取得
     * 手順：キーワードqueryと検索対象optionの条件で検索を行う
     * 入力値：query"ノーネイム", option"author"
     * 期待する結果：null
     * 
     * 内容：検索対象が著者の場合、部分一致した名言オブジェクトの
     * リストが空っぽの際にnull
     */
    @Test
    void testPartialMatchAuthor_quoteEmpty() {
        List<Quote> allList = new ArrayList<>();
        allList.add(new Quote("地球は青かった", "ガガーリン"));
        allList.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        allList.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        allList.add((new Quote("僕の大冒険", "僕")));

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        assertNull(quoteService.partialMatch("ノーネイム", "author"));

    }

    /*
     * 前提条件：DBから名言オブジェクトを取得
     * 手順：キーワードqueryと検索対象optionの条件で検索を行う
     * 入力値：query"ノーネイム", option"author"
     * 期待する結果：null
     * 
     * 内容：DBから受け取ったリストが空っぽの場合はnull
     */
    @Test
    void testPartialMatchAuthor_dbEmpty() {
        List<Quote> allList = new ArrayList<>();

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        assertNull(quoteService.partialMatch("ノーネイム", "author"));

    }

    /*
     * 前提条件：DBから名言オブジェクトを取得
     * 手順：キーワードqueryと検索対象optionの条件で検索を行う
     * 入力値：query"ン", option"author"
     * 期待する結果："地球は青かった", "ガガーリン"
     * "天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"
     * 
     * 内容：ループの１回目と２回目のリスト内の要素位置を確認し、ループの開始地点と
     * 終了条件に誤りがないか
     */
    @Test
    void testPartialMatchAuthor_roop() {
        List<Quote> allList = new ArrayList<>();
        List<Quote> mainQ = new ArrayList<>();
        List<Quote> sub = new ArrayList<>();
        allList.add(new Quote("地球は青かった", "ガガーリン"));
        allList.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        allList.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        allList.add((new Quote("僕の大冒険", "僕")));

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        mainQ.addAll(quoteService.partialMatch("ン", "author"));
        sub.add(allList.get(0));
        sub.add(allList.get(1));
        assertIterableEquals(sub, mainQ);
    }

    /*
     * 前提条件：なし
     * 手順：キーワードqueryと検索対象optionの条件で検索を行う
     * 入力値：query"僕"、option"both"
     * 期待する結果：null
     * 
     * 内容：DBから名言オブジェクトを取得してない際に
     * nullで戻るかどうか
     */
    @Test
    void testPartialMatchBoth_noDb() {

        assertNull(quoteService.partialMatch("僕", "both"));

    }

    /*
     * 前提条件：DBから名言オブジェクトを取得
     * 手順：キーワードqueryと検索対象optionの条件で検索を行う
     * 入力値：query"僕"、option"both"
     * 期待する結果：text"僕の大冒険", author"僕"
     * 
     * 内容：検索対象が両方の場合、部分一致した名言オブジェクトの
     * リストを作成し、そのリストを取り出せるかどうか。名言と著者に同じ
     * キーワードがあった際に、リスト内に同じ名言オブジェクトないか。
     */
    @Test
    void testPartialMatchBoth_main() {
        List<Quote> allList = new ArrayList<>();
        List<Quote> mainQ = new ArrayList<>();
        List<Quote> sub = new ArrayList<>();
        allList.add(new Quote("地球は青かった", "ガガーリン"));
        allList.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        allList.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        allList.add((new Quote("僕の大冒険", "僕")));

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        mainQ.addAll(quoteService.partialMatch("僕", "both"));
        sub.add(allList.get(3));
        assertIterableEquals(sub, mainQ);

    }

    /*
     * 前提条件：DBから名言オブジェクトを取得
     * 手順：キーワードqueryと検索対象optionの条件で検索を行う
     * 入力値：query"両方", option"both"
     * 期待する結果：null
     * 
     * 内容：検索対象が両方の場合、部分一致した名言オブジェクトの
     * リストが空っぽの際にnull
     */
    @Test
    void testPartialMatchBoth_quoteEmpty() {
        List<Quote> allList = new ArrayList<>();
        allList.add(new Quote("地球は青かった", "ガガーリン"));
        allList.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        allList.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        allList.add((new Quote("僕の大冒険", "僕")));

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        assertNull(quoteService.partialMatch("両方", "both"));

    }

    /*
     * 前提条件：DBから名言オブジェクトを取得
     * 手順：キーワードqueryと検索対象optionの条件で検索を行う
     * 入力値：query"両方", option"both"
     * 期待する結果：null
     * 
     * 内容：DBから受け取ったリストが空っぽの場合はnull
     */
    @Test
    void testPartialMatchBoth_dbEmpty() {
        List<Quote> allList = new ArrayList<>();

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        assertNull(quoteService.partialMatch("両方", "both"));

    }

    /*
     * 前提条件：DBから名言オブジェクトを取得
     * 手順：キーワードqueryと検索対象optionの条件で検索を行う
     * 入力値：query"僕", option"both"
     * 期待する結果："僕のお家", "僕家"
     * "僕の大冒険", "僕
     * 
     * 内容：ループの１個目と２個目のリスト内の要素位置を確認し、ループの開始地点と
     * 終了条件に誤りがないか
     */
    @Test
    void testPartialMatchBoth_roop() {
        List<Quote> allList = new ArrayList<>();
        List<Quote> mainQ = new ArrayList<>();
        List<Quote> sub = new ArrayList<>();
        allList.add(new Quote("僕のお家", "僕家"));
        allList.add(new Quote("地球は青かった", "ガガーリン"));
        allList.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        allList.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        allList.add((new Quote("僕の大冒険", "僕")));

        Mockito.when(quoteRepositoryMock.findAll()).thenReturn(allList);

        mainQ.addAll(quoteService.partialMatch("僕", "both"));
        sub.add(allList.get(0));
        sub.add(allList.get(4));
        assertIterableEquals(sub, mainQ);
    }

    /*
     * 例外発生
     */
    @Test
    void testStackTrace() {
        List<Quote> allList = new ArrayList<>();
        allList.add(new Quote("僕のお家", "僕家"));
        allList.add(new Quote("地球は青かった", "ガガーリン"));
        allList.add(new Quote("天才は1%のひらめきと99%の努力でつくられる", "トーマス・エジソン"));
        allList.add(new Quote("憧れるのをやめましょう", "大谷翔平"));
        allList.add((new Quote("僕の大冒険", "僕")));
        Mockito.when(quoteRepositoryMock.findAll()).thenThrow(new RuntimeException());
        List<Quote> method = quoteService.partialMatch("僕", "text");
        assertNull(method);

    }
}
