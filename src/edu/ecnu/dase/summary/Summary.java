package edu.ecnu.dase.summary;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Summary {
	/** 每次取GET_NUM_DOCS篇文献 **/
	public static final int GET_NUM_DOCS = 40;
	/** 自定义关键词的个数 一篇文本5个关键词 **/
	public static final int KeyWordCount = 10;
    /** 每篇文章平均摘出的句子个数 **/
	public static final float NumSen = 1.5f;
	
	public void getSummary(String filePath,String outfilePath,int n,int kmtopic) throws Exception{
		
	  for (int i = 0; i < n; i++) {
		int[] rowofdocs = new int[GET_NUM_DOCS];
		// RandomNumofRows randomnumofrows=new RandomNumofRows();
		// rowofdocs=randomnumofrows.getRow0fContent(filePath,GET_NUM_DOCS);
		for (int j = 0; j < GET_NUM_DOCS; j++)
			rowofdocs[j] = j + 1;

		DocCluster doccluster = new DocCluster();
		doccluster.docCluster(filePath, rowofdocs);
		Map<Integer, List<Integer>> clusterresult = new TreeMap<Integer, List<Integer>>();
		clusterresult = doccluster.getClusterResult(rowofdocs,kmtopic);//{1=[1, 2, 4, 5, 9, 10], 2=[7, 8], 3=[3], 4=[6]}

		Set<Integer> topics = clusterresult.keySet();
		for (Integer topic : topics) {
			if (clusterresult.get(topic).size() == 0)
				continue;

			int[] sameclusterrows = new int[clusterresult.get(topic).size()];
			for (int l = 0; l < clusterresult.get(topic).size(); l++) {
				sameclusterrows[l] = clusterresult.get(topic).get(l);
			}

			
		// keyword个数
		int keywordnumber = KeyWordCount * (clusterresult.get(topic).size());
		//摘句子个数
		int numsen =(int)(NumSen* (clusterresult.get(topic).size()))+1;
		
		CountKeyWords countkeyword = new CountKeyWords();
		// 计算tfidf
		Map<Integer, Map<String, Double>> tfIdfMap = new HashMap<Integer, Map<String, Double>>();
		tfIdfMap = countkeyword.tfidf(filePath, sameclusterrows, keywordnumber);
		
		//merge 后 每个单词的tfidf ->存在resultmap
		Map<String, Double>resultmap=countkeyword.mergeMap(tfIdfMap);
		
		// 求keywords
		
		List<String> keywords = new ArrayList<String>();
		keywords = countkeyword.countKeyWords(filePath, sameclusterrows, keywordnumber,resultmap);

		// 对用户自定义的keyword列分词,结果存入userKeywords，去重
		List<String> userKeywords = new ArrayList<String>();
		GetUserKeyWords usk = new GetUserKeyWords();
		userKeywords = usk.cutKeywords(filePath, sameclusterrows);

		// 求sentence
		CountParaForLP c = new CountParaForLP();
		List<String> allsentence = new ArrayList<String>();
		allsentence = c.getSentence(filePath, sameclusterrows);	
		if(allsentence.size()==0){
			throw new Exception("请先确认您传入的文本至少含有一句话（由。；！？结尾的作为一句）");
		}
		
		SubModular s = new SubModular();
		List<String> summary= new ArrayList<String>();
		summary = s.subModular(numsen, keywords, userKeywords, allsentence, resultmap);
		inputTxt(outfilePath, summary, i);	
		}
	  }
		
	}
	/**
	 * 将List中的句子写入txt
	 * @param outfilePath 输出路径	
	 * @param minscover 待写入的List
	 * @param i txt名称
	 * @throws IOException IO异常
	 */
		public void inputTxt(String outfilePath, List<String> summary, int i) throws IOException {
			// 存储到i.txt文档中
			File file = new File(outfilePath+"/ouput");
			if(!file.exists() && !file.isDirectory())
				file.mkdir();
				
			String temp = i + ".txt";
			File filename = new File(file + "/" + temp);
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(filename, true), 100);
				bw.write("    ");
				for (String str : summary) {
//					if (str.matches("[0-9、.]+.*"))
//						str = str.substring(2);
//					if(str.matches("第[一二三四五六七八九十]+.*"))
//						str = str.substring(2);
					bw.write(str + "。");
				}
				bw.newLine();
			} catch (IOException e) {
				System.out.println("写入文件出错，输入文件路径不正确！");
				e.printStackTrace();
			} finally {
				if (bw != null) {
					bw.flush();
					bw.close();
				}
			}
		}
}
