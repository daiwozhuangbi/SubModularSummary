package edu.ecnu.dase.summary;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.ecnu.dase.jgbblda.LDA;
import edu.ecnu.dase.jgbblda.LDACmdOption;



public class DocCluster {

	/**
	 * 文档聚类（LDA主题模型）
	 * 
	 * @param filePath
	 *            语料存放路径
	 * @param resulttemp
	 *            随机抽取的20篇文献行号
	 */
	public void docCluster(String filePath, int[] resulttemp) {

		TFIDFAlgorithm segStr = new TFIDFAlgorithm();
		Map<Integer, Map<String, Integer>> allSegsMap = new HashMap<Integer, Map<String, Integer>>();
		allSegsMap = segStr.allSegsMap(filePath, resulttemp);
		// 清空predictDoc.txt
//		File f1 = new File("models/casestudy-china/predictDoc.txt");
//		FileWriter fw1;
//		try {
//			fw1 = new FileWriter(f1);
//			BufferedWriter bw1 = new BufferedWriter(fw1);
//			bw1.write("");
//
//			bw1.close();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}

		// 将分词后的文本信息存入输出文件
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileOutputStream(new File("models/casestudy-china/predictDoc.txt"), false), true);
			pw.print(allSegsMap.size());
			pw.println();
			// BufferedWriter writer = new BufferedWriter(new
			// FileWriter("models\\casestudy-china\\predictDoc.txt"));
			// writer.write(allSegsMap.size());
			// writer.write("\n");

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
					pw.print(seg + " ");
				}
				pw.println();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LDA lda = new LDA();
		String filename = "predictDoc.txt";
		lda.RunLda(filename);

	}

	/**
	 * 获取文本分类结果
	 * 
	 * @param resulttemp
	 *            随机抽取的20篇文献行号
	 * @return clustertreemap 返回分类结果
	 * @throws Exception 

	 */
	public Map<Integer, List<Integer>> getClusterResult(int[] resulttemp,int kmtopic) throws Exception{
		String file = "models/casestudy-china/predictDoc.txt.model-final.theta";
		Map<Integer, Integer> clustermap = new HashMap<Integer, Integer>();
		Map<Integer, List<Integer>> clustertreemap = new TreeMap<Integer, List<Integer>>();

		LDACmdOption option = new LDACmdOption();
		int ldatopic = option.K;
		
		//修改predictDoc.txt格式
		 FileOperation f = new FileOperation();
		 String result = f.readTxtFile(new File(file));
		 f.writeTxtFile("@RELATION predictDoc.txt.model-final.theta",new File(file));
		 for(int i =1;i<=ldatopic;i++){
			 f.contentToTxt(file, "@ATTRIBUTE "+i+" REAL");
		 }
		 f.contentToTxt(file, "@DATA");
		 f.contentToTxt(file,result);
		
		KMeans k = new KMeans();
		clustermap =k.Kmeans(file, resulttemp);
		
		for (int i = 1; i <= kmtopic; i++) {
			List<Integer> topicnum = new ArrayList<Integer>();
			Set<Integer> fileList = clustermap.keySet();
			for (Integer fileNum : fileList) {
				int tmp = clustermap.get(fileNum);
				if (tmp == i) {
					topicnum.add(fileNum);
				}
			}
			clustertreemap.put(i, topicnum);
		}

		return clustertreemap;
	}

}
