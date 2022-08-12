package com.masterteknoloji.printarchiver.service;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.sourceforge.tess4j.Tesseract;

@Service
@Transactional
public class OcrService {

	Tesseract tesseract =null;
			
	public OcrService() {
		super();
		this.tesseract = new Tesseract();
		this.tesseract.setOcrEngineMode(1);
		this.tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
		this.tesseract.setLanguage("tur");
	}
	
	public String analyze(File file) throws Exception {
		BufferedImage image = ImageIO.read(file);
		String result = tesseract.doOCR(image);
		return result;
	}
	
}
