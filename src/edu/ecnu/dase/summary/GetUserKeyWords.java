package edu.ecnu.dase.summary;

import java.io.FileInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * 获取用户自定义的关键词
 * 
 * @author Iris
 */
public class GetUserKeyWords {
	/** 取出excel的GET_COLUM列 **/
	public static final int GET_COLUM = 2;

	/**
	 * 切分keyword列词语，获得keywords（用户自定义）
	 * 
	 * @param fileName
	 *            语料库路径
	 * @param resulttemp
	 *            存储抽取的100行的行数
	 * @return 返回用户定义的关键词
	 */

	public List<String> cutKeywords(String fileName, int[] resulttemp) {

		String temp = null;
		HSSFWorkbook wb = null;
		Set<String> set = new HashSet<String>();
		List<String> userKeywords = new ArrayList<String>();

		try {
			wb = new HSSFWorkbook(new FileInputStream(fileName));
		} catch (IOException e) {
			System.out.println("文件路径异常，检查文件路径是否正确！");
			e.printStackTrace();
		}
		for (int l = 0; l < wb.getNumberOfSheets(); l++) {// l张表格
			// 对读入文本进行分词
			Row row = null;
			for (int i = 0; i < resulttemp.length; i++) {// i行
				row = wb.getSheetAt(l).getRow(resulttemp[i]);
				Cell cij = row.getCell(GET_COLUM - 1);
				temp = cij.getRichStringCellValue().getString();
				if (temp.length() > 0) {
					Result result = ToAnalysis.parse(temp);
					for (Term term : result) {
						String item = term.getName().trim();
						if (item.length() > 0) {
							set.add(item);
						}
					}
				}
			}
		}

		for (String str : set) {
			userKeywords.add(str);
		}

		return userKeywords;
	}

}
