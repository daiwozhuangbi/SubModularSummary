package edu.ecnu.dase.zzj;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
    	
//    	Scanner scanner = new Scanner(System.in);
//		System.out.println("输入文档名称：");
//		String filename = scanner.nextLine();
		String path="D:\\test\\餐饮\\canyin.txt";
//		File writename = new File(path);
//		writename.createNewFile();
//		BufferedWriter out = new BufferedWriter(new FileWriter(writename));  
//		System.out.println("输入文档内容：");
//		String text = scanner.nextLine();
//        out.write(text);   
//        out.flush();   
//        out.close(); 
      
        GetSentences.Summary_by_words(path,1000);  
//		GetSentences.Summary_by_sentence(path, 15);
//      GetSentences.SummaryBySenetence(path, 15);

    }
}