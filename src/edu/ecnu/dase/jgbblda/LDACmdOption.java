package edu.ecnu.dase.jgbblda;

import org.kohsuke.args4j.*;

public class LDACmdOption {
	
	@Option(name="-est", usage="Specify whether we want to estimate model from scratch")
	public boolean est = false;//是否从头再来训练模型
	
	@Option(name="-estc", usage="Specify whether we want to continue the last estimation")
	public boolean estc = false;//决定是否是基于先前已有的模型基础上继续用新数据训练模型
	
	@Option(name="-inf", usage="Specify whether we want to do inference")
	public boolean inf = false;//是否使用先前已经训练好的模型进行推断
	
	@Option(name="-dir", usage="Specify directory")
	public String dir = "";//数据结果（模型数据）保存位置
	
	@Option(name="-dfile", usage="Specify data file")
	public String dfile = "";//训练数据或原始数据文件名
	
	@Option(name="-model", usage="Specify the model name")
	public String modelName = "";//选择使用哪一个迭代的模型结果来进行推断
	
	@Option(name="-alpha", usage="Specify alpha")
	public double alpha = -1.0;//平滑系数
	
	@Option(name="-beta", usage="Specify beta")
	public double beta = -1.0;
	
	@Option(name="-ntopics", usage="Specify the number of topics")
	public int K = 50; //类簇数目，谨慎设置
	
	@Option(name="-niters", usage="Specify the number of iterations")
	public int niters = 1000;//迭代数目，谨慎设置
	
	@Option(name="-savestep", usage="Specify the number of steps to save the model since the last save")
	public int savestep = 1000; //指定把迭代结果模型保存到硬盘上的迭代跨度，即每迭代10次保存一次。
	
	@Option(name="-twords", usage="Specify the number of most likely words to be printed for each topic")
	public int twords = 100;//对每一个类别（话题）选前多少个最大概率词项
	
	@Option(name="-withrawdata", usage="Specify whether we include raw data in the input")
	public boolean withrawdata = false;
	
	@Option(name="-wordmap", usage="Specify the wordmap file")
	public String wordMapFileName = "wordmap.txt";//生成的副产品的文件名
}
