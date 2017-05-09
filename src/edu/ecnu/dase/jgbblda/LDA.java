/**
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
/**
 * 执行LDA模型
 * @author Iris
 *
 */
public class LDA {
	/**
	 * 执行LDA
	 * @param filename 待分类的语料 txt格式，一行为一篇分词后的语料 
 
	 */
	public void RunLda(String filename){
		LDACmdOption option = new LDACmdOption();
		option.est=false;
		option.estc=false;
		option.inf=true;


		try {			
			if (option.est || option.estc){				
				//option.dfile="newdocs.dat";
				option.dir="models/casestudy-china";
				option.modelName = "model-final";
				option.dfile="newDocs.txt";
				Estimator estimator = new Estimator();
				estimator.init(option);
				estimator.estimate();
			}
			else if (option.inf){
				option.dir = "models/casestudy-china";
				option.modelName = "model-final";
				option.dfile = filename;
				Inferencer inferencer = new Inferencer();
				inferencer.init(option);				
				inferencer.inference();
			}
			
		}catch (Exception e){
			System.out.println("Error in main: " + e.getMessage());
			e.printStackTrace();
			return;
		}
	}
		
}
