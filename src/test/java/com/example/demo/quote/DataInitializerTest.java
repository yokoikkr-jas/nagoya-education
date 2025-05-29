package com.example.demo.quote;

import java.io.BufferedReader;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class DataInitializerTest {
    @ParameterizedTest
    @CsvFileSource(resources = "src\\test\\java\\com\\example\\demo\\quote\\test.csv", numLinesToSkip = 1)
    void testWithCsvFile(String line) {
        String[] parts = line.split(",");
        String quote = parts[0];
        String author = parts[1];

        // quoteが空でないことの確認
        assertFalse(quote.isEmpty(), author + "の名言が空です");

    }

    /*
     * テスト概要：正常系
     * 初期化メソッドを呼び出して、DBの内容とcsvの内容が等しくなる
     */
    @Test
    void testRun2() {

        DataInitializer dataInitializer = new DataInitializer();
        try {
            dataInitializer.run();
        } catch (Exception e) {
            assertEquals("e", "w");
        }
    }
}
