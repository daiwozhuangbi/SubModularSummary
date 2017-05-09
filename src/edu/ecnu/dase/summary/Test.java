package edu.ecnu.dase.summary;

public class Test {

	public static void main(String[] args) {
		String inputfilePath="/Users/iris/Documents/LDA/testdata.xls";
		String outfilePath="/study/eclipse/iris-output/Summary/";

		Summary s = new Summary();
		try {
			s.getSummary(inputfilePath, outfilePath, 5,4);
			System.out.println("sucess!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
