package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


// 追加コード
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;



@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
		// Mockオブジェクトの作成
		QuoteRepository mockRepo = mock(QuoteRepository.class);

		// モックのリストを作成
		List<Quote> mockQuotes = Arrays.asList(new Quote("A"), new Quote("B"), new Quote("C"));

		// モックの動作を定義
		when(mockRepo.findAll()).thenReturn(mockQuotes);

		// QuoteServiceのインスタンスを作成
		QuoteService quoteService = new QuoteService(mockRepo);

		// メソッドの実行
		int count = quoteService.countQuotes();

		// アサーション（期待値との比較）
		assertEquals(3, count);
	}

}
