package edu.ecnu.dase.summary;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeMap;

public class CountKeyWords {
	
	/** 自定义关键词的个数 **/
//	public static final int KeyWordCount=30;
	/**
	 * 返回map中value最大的value值
	 * @param map 目标map
	 * @return max
	 */
	public double getMax(Map<String,Double>map){
		double max=0.0;
		for(double vlaue:map.values()){
			if(vlaue>max)
				max=vlaue;
		}
		return max;	
	}
	/**
	 * 返回map中value最小的value值
	 * @param map 目标map
	 * @return min
	 */
	public double getMin(Map<String,Double>map){
		double min=0.0;
		for(double value:map.values()){
			if(value<min)
				min=value;
		}
		return min;	
	}
	/**
	 * 归一化map中的某个value
	 * @param x 需要归一化的值
	 * @param map 目标map
	 * @return result
	 */
	public Double normalization(Double x,Map<String,Double>map){
		double result=(x-getMin(map))/(getMax(map)-getMin(map));
		return result;
	}
 
   /**
    * 归一化map中所有的value
    * @param map 目标map
    * @return normalizationmap
    */
	public Map<Integer,Map<String, Double>>normalMap(Map<Integer,Map<String, Double>>map){
		
		   
		   Map<Integer,Map<String, Double>> normalizationmap= new HashMap<Integer,Map<String, Double>>();
		   
	       for(Iterator<Entry<Integer, Map<String, Double>>> iterator = map.entrySet().iterator(); iterator.hasNext();){
	    	   Map<String, Double> valuetempmap= new HashMap<String, Double>();//存储value中的Map
	    	   Map.Entry<Integer, Map<String, Double>> entry1=(Map.Entry<Integer, Map<String, Double>>)iterator.next();
	    	   Map<String, Double> valuemap=(Map<String, Double>)entry1.getValue();	    	   
	    	   for(Iterator<Entry<String, Double>> it=valuemap.entrySet().iterator();it.hasNext();){
	  		     Map.Entry<String, Double> entry2=(Map.Entry<String, Double>)it.next();
	  		     valuetempmap.put((String)entry2.getKey(), (Double)normalization((Double) entry2.getValue(),valuemap));
	    	   }
	    	   normalizationmap.put((Integer) entry1.getKey(), valuetempmap);
	       }
		return normalizationmap;
	}
	/**
	 * 合并map中的value到normalizationmap<br>
	 * Key相同时保存value更大的那个值
	 * @param map 目标map
	 * @return resultmap
	 */
	public Map<String,Double> mergeMap(Map<Integer,Map<String, Double>>map){
		Map<Integer,Map<String, Double>> normalizationmap=normalMap(map);
		
		Map<String,Double> resultmap=new HashMap<String,Double>();
		for(Iterator<Entry<Integer, Map<String, Double>>> iterator = normalizationmap.entrySet().iterator();iterator.hasNext();){
			Map.Entry<Integer,Map<String, Double>> entry1=(Map.Entry<Integer, Map<String, Double>>)iterator.next();
			Map<String, Double> valuemap=(Map<String, Double>)entry1.getValue();
			 for(Iterator<Entry<String, Double>> it=valuemap.entrySet().iterator();it.hasNext();){
	  		     Map.Entry<String, Double> entry2=(Map.Entry<String, Double>)it.next();
	  		   if(!(resultmap.containsKey(entry2.getKey()))){
	  			 resultmap.put(entry2.getKey(), entry2.getValue());
	  		   }
	  		   else if ((resultmap.containsKey(entry2.getKey()))&&(resultmap.get(entry2.getKey())<entry2.getValue())){
			    	 resultmap.put(entry2.getKey(), entry2.getValue());
			   }
			 }			
		}		
		return resultmap;
	}
	/**
	 * 获取关键词
	 * @param map key-词 value-tfidf
	 * @param KeyWordCount 关键词个数
	 * @return keyword 关键词
	 */
	@SuppressWarnings("rawtypes")
	public List<String> setToTreeMap(Map<String, Double>resultmap,int KeyWordCount){
		
		   BufferedReader reader = null;
			//去停用词
			try {
			    reader = new BufferedReader(new InputStreamReader(new FileInputStream("stop_words_ch.txt")));
			} catch (FileNotFoundException e) {
				System.out.println("没有找到文件！");
				e.printStackTrace();
			}
			String stopword;
			Set<String> stopwordset =  new HashSet<String>();
			try {
				while((stopword = reader.readLine())!= null){
					stopwordset.add(stopword);			
				}
			} catch (IOException e) {
				System.out.println("读取文件stop_words_ch.txt内容时候出错！");
				e.printStackTrace();
			}
			 Iterator<Map.Entry<String, Double>> it = resultmap.entrySet().iterator();  
		        while(it.hasNext()){ 
		        	 Map.Entry<String, Double> entry=it.next();  
		             if(stopwordset.contains(entry.getKey())){   
		                 it.remove();       
		             }  
		        }
		        
		        
		   Map<Double, List> tmap = new TreeMap<Double, List>(Collections.reverseOrder());
	       //把tf-idf存到treemap中
	      	       
	       Iterator<Entry<String, Double>> iter = resultmap.entrySet().iterator();
		   while (iter.hasNext()) {
		       Entry<String, Double> entry = iter.next();	    
	           tmap.put((Double) entry.getValue(), null);
	       }		    	       
	       Iterator<Entry<Double, List>> iter2 = tmap.entrySet().iterator();//tmap
		   while (iter2.hasNext()) {
			    List<String> words=new ArrayList<String>(); 
				Map.Entry<Double, List> entry2= (Map.Entry<Double, List>) iter2.next();
				Iterator<Entry<String, Double>> iter1 = resultmap.entrySet().iterator();//map
				while(iter1.hasNext()){
					Map.Entry<String, Double> entry1 = (Map.Entry<String, Double>) iter1.next();
					Double obj1 = new Double((double) entry1.getValue());
					Double obj2 = new Double((double) entry2.getKey());
					if(obj2.compareTo(obj1)==0){
						 words.add((String) entry1.getKey());						 
					}
				}
				tmap.put((Double) entry2.getKey(), words);				
		    }		   		   
			Iterator iter3 = tmap.entrySet().iterator();
			int count =0;
			String[] input=null; 
			List<String> keyword = new ArrayList<String>();
			while (iter3.hasNext()&&count<KeyWordCount) {				
				Map.Entry entry = (Map.Entry) iter3.next();
				input = entry.getValue().toString().substring(1,entry.getValue().toString().length()-1).split(",");
				for(int i =0; i<input.length&&count<KeyWordCount;i++){
					keyword.add(input[i].trim());
					count++;
				}
			}		
			return keyword;
	}
	
	public Map<Integer, Map<String, Double>> tfidf(String filePath,int[]resulttemp,int keywordnumber){
		Map<Integer, Map<String, Double>> tfIdfMap = new HashMap<Integer, Map<String, Double>>();
		TFIDFAlgorithm tfidf=new TFIDFAlgorithm();
		tfIdfMap=tfidf.countTFIDF(filePath, resulttemp);
		return tfIdfMap;
	}
	
    /**
     * 计算关键词
     * @param filePath 语料库路径
     * @param resulttemp 随机生成的行数
     * @param keywordnumber 关键词个数
     * @return keyword 关键词
     */
	public List<String> countKeyWords(String filePath, int[] sameclusterrows, int keywordnumber,
			Map<String, Double> resultmap) {
		//求keyword		 
		 List<String> keyword=new ArrayList<String>();	 		 	 
		 keyword=setToTreeMap(resultmap,keywordnumber);
		 return keyword;
	}
}
