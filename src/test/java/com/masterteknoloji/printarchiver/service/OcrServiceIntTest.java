package com.masterteknoloji.printarchiver.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.masterteknoloji.printarchiver.PrintarchiverApp;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrintarchiverApp.class)
public class OcrServiceIntTest {

	@Autowired
	OcrService ocrService;
	
	@Test
	public void checkTurkissCharacter() throws Exception {
		String path = "src/test/resources/ocrtestdata/standart/2022/08/12/09/897b2602636453f5cff4977a746dc587cee94af7/page0001-bw900-bh3000.png";
		String result = ocrService.analyze(new File(path));

		assertThat(result).contains("çarşamba");
		assertThat(result).contains("Çeliç");
		assertThat(result).contains("İstanbul");
		assertThat(result).contains("Isparta");
		assertThat(result).contains("dağ");
		assertThat(result).contains("Şirket");
		assertThat(result).contains("şırdan");
		assertThat(result).contains("Ümran");
		assertThat(result).contains("ümraniye");
		
		result = result.toLowerCase();
		assertThat(result).contains("çarşamba");
		assertThat(result).contains("çeliç");
		assertThat(result).contains("istanbul");
		assertThat(result).contains("ısparta");
		assertThat(result).contains("dağ");
		assertThat(result).contains("şirket");
		assertThat(result).contains("şırdan");
		assertThat(result).contains("ümran");
		assertThat(result).contains("ümraniye");
		
		System.out.println(result);
	}
}
