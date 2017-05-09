/*
 * Copyright (C) 2007 by
 * 
 * 	Xuan-Hieu Phan
 *	hieuxuan@ecei.tohoku.ac.jp or pxhieu@gmail.com
 * 	Graduate School of Information Sciences
 * 	Tohoku University
 * 
 *  Cam-Tu Nguyen
 *  ncamtu@gmail.com
 *  College of Technology
 *  Vietnam National University, Hanoi
 *
 * JGibbsLDA is a free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * JGibbsLDA is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JGibbsLDA; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 */
package edu.ecnu.dase.jgbblda;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

public class Dictionary {
	public Map<String,Integer> word2id;
	public Map<Integer, String> id2word;
		
	//--------------------------------------------------
	// constructors
	//--------------------------------------------------
	/**
	 * 构造函数Dictionary
	 */
	public Dictionary(){
		word2id = new HashMap<String, Integer>();
		id2word = new HashMap<Integer, String>();
	}
	
	/**
	 * 获取ID为id的word
	 * @param id id号
	 * @return word 单词
	 */
	public String getWord(int id){
		return id2word.get(id);
	}
	/**
	 * 获取word的ID
	 * @param word 词语
	 * @return id
	 */
	public Integer getID (String word){
		return word2id.get(word);
	}
	
	//----------------------------------------------------
	// checking methods，check if this dictionary contains a specified word
	//----------------------------------------------------

	/**
	 * 判断dictionary是否包含word
	 * @param word 待判断的word
	 * @return true or false
	 */
	public boolean contains(String word){
		return word2id.containsKey(word);
	}
	/**
	 * 判断dictionary是否包含id
	 * @param id 待判断的id
	 * @return true or false
	 */
	public boolean contains(int id){
		return id2word.containsKey(id);
	}
	//---------------------------------------------------
	// manupulating methods ,add a word into this dictionary , return the corresponding id
	//---------------------------------------------------
    /**
     * 将word添加到word2id、id2word中
     * @param word  单词
     * @return id id
     */
	public int addWord(String word){
		if (!contains(word)){
			int id = word2id.size();
			
			word2id.put(word, id);
			id2word.put(id,word);
			
			return id;
		}
		else return getID(word);		
	}
	
	//---------------------------------------------------
	// I/O methods,read dictionary from file
	//---------------------------------------------------

	/**
	 * 读取wordmap.txt文件中额单词存入id2word、word2id
	 * @param wordMapFile wordmap.txt文件
	 * @return true
	 */
	public boolean readWordMap(String wordMapFile){		
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(wordMapFile), "UTF-8"));
			String line;
			
			//read the number of words
			line = reader.readLine();			
			int nwords = Integer.parseInt(line);
			
			//read map
			for (int i = 0; i < nwords; ++i){
				line = reader.readLine();
				StringTokenizer tknr = new StringTokenizer(line, " \t\n\r");
				
				if (tknr.countTokens() != 2) continue;
				
				String word = tknr.nextToken();
				String id = tknr.nextToken();
				int intID = Integer.parseInt(id);
				
				id2word.put(intID, word);
				word2id.put(word, intID);
			}
			
			reader.close();
			return true;
		}
		catch (Exception e){
			System.out.println("Error while reading dictionary:" + e.getMessage());
			e.printStackTrace();
			return false;
		}		
	}
	/**
	 * 写入wordmap.txt文件
	 * @param wordMapFile 文件地址与文件名
	 * @return 成功返回true
	 */
	public boolean writeWordMap(String wordMapFile){
		try{
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(wordMapFile), "UTF-8"));
			
			//write number of words
			writer.write(word2id.size() + "\n");
			
			//write word to id
			Iterator<String> it = word2id.keySet().iterator();
			while (it.hasNext()){
				String key = it.next();
				Integer value = word2id.get(key);
				
				writer.write(key + " " + value + "\n");
			}
			
			writer.close();
			return true;
		}
		catch (Exception e){
			System.out.println("Error while writing word map " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		
		
	}
}
