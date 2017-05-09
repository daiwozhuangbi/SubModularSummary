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

public class Conversion {
	/**
	 * 生成本次迭代的文件名后缀
	 * @param number 本次迭代次数
	 * @param width 文件名后缀长度
	 * @return result 文件名后缀
	 */
	public static String ZeroPad( int number, int width )
	{
	      StringBuffer result = new StringBuffer("");
	      for( int i = 0; i < width-Integer.toString(number).length(); i++ )
	         result.append( "0" );
	      result.append( Integer.toString(number) );
	     
	      return result.toString();
	}
}
