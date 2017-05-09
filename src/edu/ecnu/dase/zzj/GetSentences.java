package edu.ecnu.dase.zzj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetSentences { 
	static String []Feature_Word={"总而言之","综上所述","总之","一言以蔽之","最终"};
	
	public static String[] getContent(String s)
	{
		String text=s;
		text=text.trim();
		text=text.replaceAll(" ","");
		text=text.replaceAll("。", "。|");
		text=text.replaceAll("！", "！|");
		text=text.replaceAll("？", "？|");
		String[] content=text.split("\\|");
		return content;
	}
		
//计算句子中有指定词的数量
	 public static int countStr(String str1, String str2) {  
		    int counter=0;
	        if (str1.indexOf(str2) == -1) {  
	            return 0;  
	        } else if (str1.indexOf(str2) != -1) {  
	            counter++;  
	            countStr(str1.substring(str1.indexOf(str2) +  
	                   str2.length()), str2);  
	               return counter;  
	        }  
	            return 0;  
	    } 
	 
	//按句子数要求输出 
	 public static String Summary_by_sentence(String Filename,int SentenceNum)throws IOException
	 {
		 String result_normal=new String();
		 String []keywords=ReadFiles.cutWord(Filename);
		 HashMap<String, Float> result_tf=ReadFiles.tf(keywords);
		 HashMap<String, Float> result_tfidf=ReadFiles.tfidf_normal(result_tf);
         String s=ReadFiles.readFiles(Filename);
         String[] sentence=GetSentences.getContent(s);
         String[] result_sentence=GetSentences.result_sentence(sentence, result_tfidf);
//       KeySentence=GetSentences.getsentence(sentence, keyWords);	            
         result_normal=GetSentences.summary_by_num(SentenceNum, result_sentence,s);            
//       result_normal=GetSentences.Summary(SentenceNum, KeySentence,s);
         String[] str=getContent(result_normal);
         for(int i=0;i<str.length;i++)
         {
         	System.out.println(str[i]);
         }
		 return result_normal;
	 }
	 
	 //按字数要求输出
	 public static String Summary_by_words(String Filename,int WordsNum)throws IOException
	 {
		 String result_normal=new String();
		 String []keywords=ReadFiles.cutWord(Filename);
		 HashMap<String, Float> result_tf=ReadFiles.tf(keywords);
		 HashMap<String, Float> result_tfidf=ReadFiles.tfidf_normal(result_tf);
         String s=ReadFiles.readFiles(Filename);
         String[] sentence=GetSentences.getContent(s);
         
         String[] result_sentence=GetSentences.result_sentence(sentence, result_tfidf);
//         KeySentence=GetSentences.getsentence(sentence, keyWords);	            
         result_normal=GetSentences.summary_by_word(WordsNum, result_sentence,s);
         String[] str=getContent(result_normal);
         for(int i=0;i<str.length;i++)
         {
         	System.out.println(str[i]);
         }
		 return result_normal;
	 }
	//计算句子权重返回排名第一的句子
	 public static String getRankSentence(String[] s,HashMap<String, Float> keyWords) throws IOException
	 {	
	 	HashMap<String,Float> tempSentence= new HashMap<String, Float>();
	 	String rank_sentence=null;	
	 	for(int i=0;i<s.length;i++)
	 	{
	 		float count=0;
	 		float k=1;
	 		if(s[i].length()<1000000)
	 		{
	 			for(int j=0;j<Feature_Word.length;j++)
	 			{
	 			   if(s[i].indexOf(Feature_Word[j])>=0)
	 				   k=2;					   
	 			}
	 		    for (Map.Entry<String, Float> entry : keyWords.entrySet())  {  
	 			   count=count+entry.getValue()*countStr(s[i], entry.getKey().toString());    
	 			}
	 		    count=count*k;
	             String[] temp=ReadFiles.cut(s[i]);
	 		    count=count/temp.length;
	 		}
	 		tempSentence.put(s[i], count);			
	 	}
	 	List<Map.Entry<String, Float>> infoId =new ArrayList<Map.Entry<String, Float>>(tempSentence.entrySet());
	 	 Collections.sort(infoId, new Comparator<Map.Entry<String, Float>>() {   
	 		    public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {      
	 		        return (o2.getValue().compareTo(o1.getValue())); 
	 		    }
	 	}); 
	 	rank_sentence=infoId.get(0).getKey().toString();		
	 	return rank_sentence;
	 }
	 
	 static List<String>result_list=new ArrayList<String>();
	 //迭代每次取权值排名最高的加入摘要集合
	 public static String[] result_sentence(String[]s,HashMap<String,Float>keyWords)throws IOException
	 {
		 List<String>temp_sentence=Arrays.asList(s);
		 for(int i=0;i<=s.length;i++)
		 {
			 String tempstr=getRankSentence(s,keyWords);
			 result_list.add(tempstr);
			 for(Map.Entry<String, Float>entry:keyWords.entrySet())
			 {
				 if(tempstr.indexOf(entry.getKey())>=0)
				 {
					 keyWords.remove(entry.getKey());
				 }
			 }
			 temp_sentence.remove(tempstr);
		 }
		 String[] result_sentence=result_list.toArray(new String[result_list.size()]);
		 return result_sentence;
	 }
	 
	//根据句子数量要求摘取文摘并且排序	 
		 public static  String summary_by_num(int sentenceNum,String[]result_sentence,String s)
		 {
			 HashMap<String,Integer> SentenceSite= new HashMap<String, Integer>();
			 String summary=new String();
			for(int i=0;i<sentenceNum;i++)
			{
				int loc=s.indexOf(result_sentence[i]);
	            SentenceSite.put(result_sentence[i], loc);	
			}
			List<Map.Entry<String, Integer>> infoIds1 =new ArrayList<Map.Entry<String, Integer>>(SentenceSite.entrySet());
			Collections.sort(infoIds1, new Comparator<Map.Entry<String, Integer>>() {   
			    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {      
			    	return (o1.getValue().compareTo(o2.getValue())); 
			    }
		    }); 
			for( int i=0;i<infoIds1.size();i++)
			{
				summary=summary+infoIds1.get(i).getKey().toString();
			}
			return summary;		 
		 }
	//根据字数限制摘取文摘并且排序	
		 public static  String summary_by_word(int WordNum,String[]result_sentence,String s)
		 {
			 HashMap<String,Integer> SentenceSite= new HashMap<String, Integer>();
			 String summary=new String();
			 String temp=new String();
			for(int i=0;i<=result_sentence.length;i++)
			{
				if(temp.length()<=WordNum)
				{
				int loc=s.indexOf(result_sentence[i]);
	            SentenceSite.put(result_sentence[i], loc);	
	            temp=temp+result_sentence[i];
				}
			}
			List<Map.Entry<String, Integer>> infoIds1 =new ArrayList<Map.Entry<String, Integer>>(SentenceSite.entrySet());
			Collections.sort(infoIds1, new Comparator<Map.Entry<String, Integer>>() {   
			    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {      
			    	return (o1.getValue().compareTo(o2.getValue())); 
			    }
		    }); 
			for( int i=0;i<infoIds1.size();i++)
			{
				summary=summary+infoIds1.get(i).getKey().toString();
			}
			return summary;		 
		 }
}



