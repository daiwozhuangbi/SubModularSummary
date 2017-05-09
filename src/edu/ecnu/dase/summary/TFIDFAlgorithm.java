package edu.ecnu.dase.summary;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class TFIDFAlgorithm {

	/** 取出excel的GET_COLUM列 **/
	public static final int GET_COLUM = 4;
	/**
	 * 
	 * 文本分词
	 * 用ik进行字符串分词,统计各个词出现的次数
	 * @param content 一篇文本内容
	 * @return words key:单词 value：在该文本中出现次数
	 */
	public  Map<String, Integer> segString(String content) {
		// 分词
				Reader input = new StringReader(content);
				// 智能分词关闭（对分词的精度影响很大）
				IKSegmenter iks = new IKSegmenter(input, true);
				Lexeme lexeme = null;
				Map<String, Integer> words = new HashMap<String, Integer>();
				try {
					while ((lexeme = iks.next()) != null) {
						if (words.containsKey(lexeme.getLexemeText())) {
							words.put(lexeme.getLexemeText(),
									words.get(lexeme.getLexemeText()) + 1);
						} else {
							words.put(lexeme.getLexemeText(), 1);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return words;
		 
	}
	/**
	 * 
	 * 得到所有文件分词结果
	 * @param filePath 语料库路径
	 * @param resulttemp 随机抽取行数结果
	 * @return allSegsMap key:文件名,value:该文件分词结果
	 */
	public Map<Integer, Map<String, Integer>> allSegsMap(String filePath,int[]resulttemp) {
		Map<Integer, Map<String, Integer>> allSegsMap = new HashMap<Integer, Map<String, Integer>>();		
		try {			
			@SuppressWarnings("resource")
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(filePath));
			String content=null;
			for (int l = 0; l < wb.getNumberOfSheets(); l++) {// l张表格
				Row row = null;
				for (int i = 0; i <resulttemp.length; i++) {// i行
					row = wb.getSheetAt(l).getRow(resulttemp[i]);
					Cell cij = row.getCell(GET_COLUM - 1);
					content = cij.getRichStringCellValue().getString();
					Map<String, Integer> segs = segString(content);
					allSegsMap.put(resulttemp[i], segs);
			}
		}
		}catch (FileNotFoundException ffe) {
			ffe.printStackTrace();
		} catch (IOException io) {
			io.printStackTrace();
		}
		return allSegsMap;
	}
	/**
	 * 
	 * 计算tf<br>
	 * 分词结果转化为tf,公式为:tf(w,d) = count(w, d) / size(d)<br>
	 * 即词w在文档d中出现次数count(w, d)和文档d中总词数size(d)的比值
	 * @param segWordsResult key：单词  value：出现次数 
	 * @return tf key:单词 value:tf
	 */
	public HashMap<String, Double> tf(Map<String, Integer> segWordsResult) {
		HashMap<String, Double> tf = new HashMap<String, Double>();// 正规化
		if (segWordsResult == null || segWordsResult.size() == 0) {
			return tf;
		}
		Double size = Double.valueOf(segWordsResult.size());
		Set<String> keys = segWordsResult.keySet();
		for (String key : keys) {
			Integer value = segWordsResult.get(key);
			tf.put(key, Double.valueOf(value) / size);
		}
		return tf;
	}
	/** 
	 * 得到所有文件的tf
	 * @param filePath 语料库路径
	 * @param resulttemp 随机抽取行数结果
	 * @return allTfMap key:文件号,value:该文件tf
	 */
	public Map<Integer, Map<String, Double>> allTf(String filePath,int[]resulttemp) {
		Map<Integer, Map<String, Double>> allTfMap = new HashMap<Integer, Map<String, Double>>();
		try {
    		@SuppressWarnings("resource")
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(filePath));
			String content=null;
			for (int l = 0; l < wb.getNumberOfSheets(); l++) {// l张表格
				Row row = null;
				for (int i = 0; i <resulttemp.length; i++) {// i行
					row = wb.getSheetAt(l).getRow(resulttemp[i]);
					Cell cij = row.getCell(GET_COLUM - 1);
					content = cij.getRichStringCellValue().getString();
					Map<String, Integer> segs = segString(content);				
					allTfMap.put(resulttemp[i], tf(segs));
			}
		}
		}catch (FileNotFoundException ffe) {
			ffe.printStackTrace();
		} catch (IOException io) {
			io.printStackTrace();
		}
		return allTfMap;
	}
	/**
	 * 
	 * @Title: containWordOfAllDocNumber
	 * @Description: 统计包含单词的文档数 key:单词 value:包含该词的文档数
	 * @param  allSegsMap 所有文件的切分单词结果
	 * @return containWordOfAllDocNumberMap key:单词 value:包含该词的文档数
	 */
	private static Map<String, Integer> containWordOfAllDocNumber(
			Map<Integer, Map<String, Integer>> allSegsMap) {
		Map<String, Integer> containWordOfAllDocNumberMap = new HashMap<String, Integer>();
		
		if (allSegsMap == null || allSegsMap.size() == 0) {
			return containWordOfAllDocNumberMap;
		}

		Set<Integer> fileList = allSegsMap.keySet();
		for (Integer fileNum : fileList) {
			Map<String, Integer> fileSegs = allSegsMap.get(fileNum);
			// 获取该文件分词为空或为0,进行下一个文件
			if (fileSegs == null || fileSegs.size() == 0) {
				continue;
			}
			// 统计每个分词的idf
			Set<String> segs = fileSegs.keySet();
			for (String seg : segs) {
				if (containWordOfAllDocNumberMap.containsKey(seg)) {
					containWordOfAllDocNumberMap.put(seg,
							containWordOfAllDocNumberMap.get(seg) + 1);
				} else {
					containWordOfAllDocNumberMap.put(seg, 1);
				}
			}

		}
		return containWordOfAllDocNumberMap;
	}
	/**
	 * 计算idf<br>
	 * idf = log(n / docs(w, D))
	 * @param allSegsMap 所有文件的切分结果
	 * @return idfMap key:词语 value:idf
	 */
	public static Map<String, Double> idf(
			Map<Integer, Map<String, Integer>> allSegsMap) {
		Map<String, Integer> containWordOfAllDocNumberMap = new HashMap<String, Integer>();
		Map<String, Double> idfMap = new HashMap<String, Double>();
		if (allSegsMap == null || allSegsMap.size() == 0) {
			return idfMap;
		}
		containWordOfAllDocNumberMap = containWordOfAllDocNumber(allSegsMap);
		Set<String> words = containWordOfAllDocNumberMap.keySet();
		Double textSize = Double.valueOf(allSegsMap.size());
		for (String word : words) {
			Double number = Double.valueOf(containWordOfAllDocNumberMap.get(word));
			idfMap.put(word, Math.log(textSize / (number + 1.0d)));
		}
		return idfMap;
	}
	/**
	 * 计算tfIdf
	 * @param allTfMap 所有文件的tf
	 * @param idf 所有词语的idf
	 * @return tfIdfMap key:文件号 value:所有词的tfidf
	 */
	public static Map<Integer, Map<String, Double>> tfIdf(
			Map<Integer, Map<String, Double>> allTfMap, Map<String, Double> idf) {
		Map<Integer, Map<String, Double>> tfIdfMap = new HashMap<Integer, Map<String, Double>>();
		Set<Integer> fileList = allTfMap.keySet();
		for (Integer fileNum : fileList) {
			Map<String, Double> tfMap = allTfMap.get(fileNum);
			Map<String, Double> docTfIdf = new HashMap<String, Double>();
			Set<String> words = tfMap.keySet();
			for (String word : words) {
				Double tfValue = Double.valueOf(tfMap.get(word));
				Double idfValue = idf.get(word);
				docTfIdf.put(word, tfValue * idfValue);
			}
			tfIdfMap.put(fileNum, docTfIdf);
		}
		return tfIdfMap;
	}
 
	/**
	 * 本类的调用函数
	 * @param filePath 语料库路径
	 * @param resulttemp 随机生成的行数
	 * @return 返回tfidf值
	 */
	public Map<Integer,Map<String,Double>> countTFIDF(String filePath,int[]resulttemp){
		Map<Integer, Map<String, Integer>> allSegsMap = allSegsMap(filePath, resulttemp);
		Map<Integer, Map<String, Double>> allTfMap = allTf(filePath,resulttemp);
		
		Map<String, Double> idfMap = TFIDFAlgorithm.idf(allSegsMap);		
		Map<Integer, Map<String, Double>> tfIdfMap = TFIDFAlgorithm.tfIdf(allTfMap, idfMap);
	    return tfIdfMap;
	}
	
}

	