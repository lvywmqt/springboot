package com.free.springboot.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Controller
public class FileUploadController {
	
	@RequestMapping(value = "tofileUpload")
	public String toFileUpload() {
		return "fileUpload";
	}
	
	
	@RequestMapping(value = "fileUpload" ,method = RequestMethod.POST ,produces="text/plain;charset=UTF-8")
	public void fileUpload(@RequestParam("file")MultipartFile file) throws IOException{
		  //用来检测程序运行时间
        long  startTime=System.currentTimeMillis();
        System.out.println("fileName："+file.getOriginalFilename());
        try {
        	
            //获取输入流 CommonsMultipartFile 中可以直接得到文件的流
           InputStream is=file.getInputStream();
           readAndWriterTest4(is);
           is.close();
           
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        long  endTime=System.currentTimeMillis();
        System.out.println("方法一的运行时间："+String.valueOf(endTime-startTime)+"ms");

	}

	/**
	 * 读取word文件内容
	 * 
	 * @param path
	 * @return buffer
	 */
 
	public static void readAndWriterTest4(InputStream fis) throws IOException {
        String str = "";
        try {
            XWPFDocument xdoc = new XWPFDocument(fis);
            XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
            String doc1 = extractor.getText();
            for(int i = 0; i < doc1.length(); i++){
            	if(i/11 == i)break;
            	doc1.substring(2*i, (i+11)*2);
            }
            System.out.println(doc1);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static void readAndWriterTest3() throws IOException {
        File file = new File("C:\\Users\\tuzongxun123\\Desktop\\aa.doc");
        String str = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            HWPFDocument doc = new HWPFDocument(fis);
            String doc1 = doc.getDocumentText();
            System.out.println(doc1);
            StringBuilder doc2 = doc.getText();
            System.out.println(doc2);
            Range rang = doc.getRange();
            String doc3 = rang.text();
            System.out.println(doc3);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileUploadController tp = new FileUploadController();
		try {
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
 
}

