package com.example.demo.quote;

import com.example.demo.quote.model.Quote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.demo.quote.repository.QuoteRepository;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private QuoteRepository quoteRepository;

    /**
     * データベースを初期化するメソッド
     * 
     * @author 二階
     * @since 2025/05/23
     * 
     * @param args コマンドライン引数
     */
    // ファイルパスを指定するメソッド
    public String getFilePath() {
        return "src\\main\\java\\com\\example\\demo\\Quote.csv";// ファイルパスを指定
    }

    @Override
    public void run(String... args) throws Exception {

        try {
            String csvFile = getFilePath();// ファイルパスの指定
            String line;

            BufferedReader br = new BufferedReader(new FileReader(csvFile)); // ファイルを読み込む
            br.readLine();

            try {
                while ((line = br.readLine()) != null) { // ファイルの次の行に読み込むものがあるまで、処理を続ける
                    List<String> value = Arrays.asList(line.split(","));

                    // 読み込んだデータをDBに登録する

                    quoteRepository.save(new Quote(value.getFirst(), value.getLast()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                br.close();// ファイルを閉じる
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
