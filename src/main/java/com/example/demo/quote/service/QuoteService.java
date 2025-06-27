package com.example.demo.quote.service;

import com.example.demo.quote.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.quote.repository.QuoteRepository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

/**
 * 名言管理システムサービスクラス（ビジネスロジックをまとめるクラス）
 * 
 * @author 横井
 * @since 2025/05/09
 */
@Service
public class QuoteService {

    @Autowired
    private QuoteRepository quoteRepository;

    public QuoteService(QuoteRepository q) {
        this.quoteRepository = q;
    }

    /**
     * 名言一覧取得メソッド
     * 
     * @author 横井
     * @since 2025/05/09
     * 
     * @return 全ての名言オブジェクトのリスト
     */
    public List<Quote> getAllQuotes() {
        try {
            List<Quote> allQuotes = quoteRepository.findAll();
            return allQuotes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 外部ファイルパス取得メソッド
     * 
     * @author 鈴木
     * @since 2025/06/03
     * 
     * @return 外部ファイルの相対パス
     */
    public String getFilePath() {
        return "src\\main\\java\\com\\example\\demo\\Quote.csv";
    }

    /**
     * 名言登録メソッド
     * 
     * @author 鈴木
     * @since 2025/05/26
     * 
     * @param quote 登録する名言オブジェクト
     * @return 実際に登録した名言オブジェクト
     * @throw Exception 外部ファイルが開かない場合 外部ファイルに書き込みできない場合
     */
    public Quote addQuote(Quote quote) {
        try {
            Quote savedQuote = quoteRepository.save(quote);
            // 課題2 登録時の外部ファイル書き込み

            String text = savedQuote.getText();
            String author = savedQuote.getAuthor();
            String filePath = getFilePath();

            // text：名言オブジェクトに格納されている名言
            // author：名言オブジェクトに格納されている著者
            // filePath：外部ファイルの相対パス

            FileWriter file = new FileWriter(filePath, true);
            BufferedWriter writer = new BufferedWriter(file);

            try {
                // 外部ファイルへの書き込み
                writer.write(text);
                writer.write(",");
                writer.write(author);
                writer.newLine();
                return savedQuote;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                writer.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * ランダム名言メソッド
     * 
     * @author 横井
     * @since 2025/05/09
     * 
     * @return 取得した名言オブジェクト
     */
    public Quote getRandomQuote() {
        try {
            List<Quote> quotes = quoteRepository.findAll();
            if (quotes.isEmpty()) {
                return null;
            }

            Random random = new Random();
            Quote randomQuote = quotes.get(random.nextInt(quotes.size()));
            return randomQuote;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 統計情報を収集するメソッド
     * 
     * @author 横山
     * @since 2025/05/26
     * 
     * @return 名言の統計情報（平均・最長・最短）を返す
     * 
     */
    public Map<String, Object> getQuoteStatistics() {
        try {

            Map<String, Object> statistics = new HashMap<>();

            List<Quote> allQuotes = quoteRepository.findAll();

            // 全てのquotesを取得

            if (allQuotes.isEmpty()) {
                // もし名言オブジェクト一覧が空ならば、
                return statistics;
                // statisticsを返却（追加）
            }

            int maxLength = Integer.MIN_VALUE;
            // 最長を求めるため、最も小さい値で初期化

            int minLength = Integer.MAX_VALUE;
            // 最短を求めるため、最も大きい値で初期化

            int totalLength = 0;
            // 全ての文字数の合計の初期値設定

            Quote maxBox = new Quote();
            // 最長を格納する箱
            Quote minBox = new Quote();
            // 最短を格納する箱

            for (Quote quotes : allQuotes) {
                // 各要素を順番に取り出して最後の名言まで調べる

                String quote = quotes.getText();
                // 名言を得る処理

                int length = quote.length();
                // 名言の文字数を数える

                totalLength += length;
                // 名言文字数を足していく

                if (length > maxLength) {

                    maxLength = length;
                    // より大きな値を更新

                    maxBox = quotes;
                    // 最長の名言オブジェクトを格納する
                }

                if (length < minLength) {

                    minLength = length;
                    // より小さな値を更新

                    minBox = quotes;
                    // 最短の名言オブジェクトを格納する
                }
            }

            int averageLength = totalLength / allQuotes.size();
            // 名言文字数の平均を調べる処理

            statistics.put("averageLength", averageLength);
            // Maxboxとmaxlengthをひもずけたobject maxboxのところ
            statistics.put("longestQuote", maxBox);
            statistics.put("shortestQuote", minBox);

            return statistics;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 名言の全数をカウントするメソッド
     * 
     * @author 平野
     * @since 2025/05/23
     * 
     * @return 取得したQuoteオブジェクト
     */

    public int countQuotes() {
        try {
            int count = 0;
            List<Quote> countList = quoteRepository.findAll();
            count = countList.size();
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    /**
     * 文字数検索メソッド
     * 
     * @author 平野
     * @since 2025/05/29
     * 
     * @param length    検索欄に入力された検索文字数
     * @param condition 未満、同じ、より大きいの文字数比較条件
     * @return 取得した名言オブジェクトを格納したリスト
     */
    public List<Quote> searchByLength(String length, String condition) {
        try {
            if (length == null) {// 検索欄に検索文字数が入力されずに検索されたとき
                return null;
            }

            // DBに登録された全ての名言オブジェクトを取得
            List<Quote> searchList = quoteRepository.findAll();

            // 条件を満たしたQuoteオブジェクトを格納するリストを定義
            List<Quote> list = new ArrayList<>();

            int ilength = Integer.parseInt(length);// 検索文字数をint型に変換

            if (condition.equals("less")) {
                for (Quote text : searchList) {// searchListからQuoteオブジェクトを一行ずつ取り出す
                    String textQuotes = text.getText();// getText()で名言オブジェクトのみ取り出す
                    int quoteLength = textQuotes.length();// 名言オブジェクトを文字数に変換

                    if (ilength > quoteLength) {
                        list.add(text);
                    }
                }
            } else if (condition.equals("equal")) {
                for (Quote text : searchList) {
                    String textQuotes = text.getText();
                    int quoteLength = textQuotes.length();

                    if (ilength == quoteLength) {
                        list.add(text);
                    }
                }
            } else if (condition.equals("greater")) {
                for (Quote text : searchList) {
                    String textQuotes = text.getText();
                    int quoteLength = textQuotes.length();

                    if (ilength < quoteLength) {
                        list.add(text);
                    }
                }
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Quote> searchQuotes(String query, String option) {

        try {
            if (query == null) {// 検索窓に文字列がない場合、nullを返す
                return null;
            }
            // DBに登録された全ての名言オブジェクトを取得
            List<Quote> searchQuotesList = quoteRepository.findAll();
            // 条件を満たしたQuoteオブジェクトを格納するリストを定義
            List<Quote> list = new ArrayList<>();

            switch (option) {
                case "text":
                    for (Quote resultQuote : searchQuotesList) {
                        String textQuotes = resultQuote.getText();
                        if (textQuotes.contains(query)) {
                            list.add(resultQuote);
                        }
                    }
                    break;
                case "author":
                    for (Quote resultQuote : searchQuotesList) {
                        String authorQuotes = resultQuote.getAuthor();
                        if (authorQuotes.contains(query)) {
                            list.add(resultQuote);
                        }
                    }
                    break;
                case "both":
                    for (Quote resultQuote : searchQuotesList) {
                        String authorQuotes = resultQuote.getAuthor();
                        String textQuotes = resultQuote.getText();
                        if (authorQuotes.contains(query) || textQuotes.contains(query)) {
                            list.add(resultQuote);
                        }
                    }
                    break;
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
