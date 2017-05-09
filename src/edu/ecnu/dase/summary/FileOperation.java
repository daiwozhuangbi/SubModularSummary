package edu.ecnu.dase.summary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile;

public class FileOperation {

	/**
	 * 创建文件
	 * 
	 * @param fileName
	 *            创建文件存放路径
	 * @return true 成功
	 */
	public boolean createFile(File fileName) throws Exception {
		try {
			if (!fileName.exists()) {
				fileName.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 读TXT文件内容
	 * 
	 * @param fileName
	 *            读取文件路径
	 * @return 返回读取内容
	 */
	public String readTxtFile(File fileName) throws Exception {
		String result = "";
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);
			try {
				String read;
				while ((read = bufferedReader.readLine()) != null) {
					result += read + "\r\n";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (fileReader != null) {
				fileReader.close();
			}
		}
		return result;
	}

	/**
	 * 写入内容到txt
	 * 
	 * @param content
	 *            待写入的内容
	 * @param fileName
	 *            文件路径
	 * @return flag 
	 * @throws Exception
	 *             读取文件异常
	 */
	public boolean writeTxtFile(String content, File fileName) throws Exception {
		RandomAccessFile mm = null;
		boolean flag = false;
		FileOutputStream o = null;
		try {
			o = new FileOutputStream(fileName);
			o.write(content.getBytes("GBK"));
			o.close();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mm != null) {
				mm.close();
			}
		}
		return flag;
	}

	/**
	 * 在TXT末尾写入
	 * @param filePath TXT路径
	 * @param content 待写入内容
	 */
	public void contentToTxt(String filePath, String content) {
		String str = new String(); // 原有txt内容
		String s1 = new String();// 内容更新
		try {
			File f = new File(filePath);
			if (f.exists()) {
				System.out.print("文件存在");
			} else {
				System.out.print("文件不存在");
				f.createNewFile();// 不存在则创建
			}
			BufferedReader input = new BufferedReader(new FileReader(f));

			while ((str = input.readLine()) != null) {
				s1 += str + "\n";
			}
			input.close();
			s1 += content;

			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			output.write(s1);
			output.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	// public static void main(String args[]) throws Exception{
	//// contentToTxt("/Users/iris/Desktop/demo.txt", "i am alice");
	// String result = readTxtFile(new File("/Users/iris/Desktop/demo.txt"));
	// writeTxtFile("@",new File("/Users/iris/Desktop/demo.txt"));
	// contentToTxt("/Users/iris/Desktop/demo.txt",result);
	//
	// System.out.println("sucess!");
	// }
}
