package edu.ecnu.dase.summary;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/** 
 * 定义数组Y，每行代表一个单词，每列代表一个句子
 * @author Iris
 */
public class CountParaForLP {
	/** 设置需取出的语料库开头字段 **/
	public static final String TAG_START_CONTENT = "";
	/** 设置需取出的语料库结尾字段 **/
	public static final String TAG_END_CONTENT = "";
	/** 取出excel的GET_COLUM列 **/
	public static final int GET_COLUM = 4;
	
	/**
	 * 将语料库切分成单独的句子，存入List<br>
	 * 过滤掉长度超过50的句子
	 * @param filename 语料库路径
	 * @param resulttemp 随机抽取的文献的行数
	 * @return list 返回句子存储结果
	 */
	public List<String> getSentence(String filename,int[] resulttemp) {

		// 制造一个数组 存储拆分完成后所有的数据
		List<String> list = new ArrayList<String>();
		String temp = null;
		try {
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(filename));
			for (int l = 0; l < wb.getNumberOfSheets(); l++) {// l张表格
				Row row = null;
				for (int k = 0; k<resulttemp.length; k++) {// k行
					row = wb.getSheetAt(l).getRow(resulttemp[k]);
					Cell ckj = row.getCell(GET_COLUM - 1);
					temp = ckj.getRichStringCellValue().getString();
					// 从控制台接收一个字符串
					if (temp.length() > 0) {
						temp = temp.trim();
						
						// 接收的字符串按 , 拆分
						String inputStrings[] = temp.split(",");
						// 准备要写入的文件地址 创建一个输出流

						// 循环读取按要求拆分后的数据
						for (int i = 0; i < inputStrings.length; i++) {
							// 第二次 按。拆分循环
							String inputStringx[] = inputStrings[i].split("。");

							for (int ii = 0; ii < inputStringx.length; ii++) {
								// 第三次 按拆;分循环
								String inputStringy[] = inputStringx[ii]
										.split("；");

								for (int iii = 0; iii < inputStringy.length; iii++) {
									// 第四次 按拆！分循环
									String inputStringz[] = inputStringy[iii]
											.split("！");

									for (int iv = 0; iv < inputStringz.length; iv++) {
										// 第五次 按拆？分循环
										String inputStringa[] = inputStringz[iv]
												.split("？");

										for (int v = 0; v < inputStringa.length; v++) {

											list.add(inputStringa[v].trim());
											// System.out.println(inputStringa[v]);

										}
									}
								}
							}
						}
					}
				}
			}
			wb.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}