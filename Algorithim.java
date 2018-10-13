package bayesAlgo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Algorithim {
	public static void main(String[] args) {
		try {
			trainingData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void trainingData() throws IOException{
		ArrayList<String> listClass0=new ArrayList<String>();
		ArrayList<String> listClass1=new ArrayList<String>();
		ArrayList<String> listDataClass0=new ArrayList<String>();
		ArrayList<String> listDataClass1=new ArrayList<String>();
		ArrayList<String> allTrainingData=new ArrayList<String>();
		ArrayList<String> allTestData=new ArrayList<String>();
		ArrayList<String> allDataClass0=new ArrayList<String>();
		ArrayList<String> allDataClass1=new ArrayList<String>();
		ArrayList< String> eachWordList=new ArrayList<String>();
		HashMap<String, Integer> mapCountWordClass0=new HashMap<String,Integer>();
		ArrayList< String> eachWordListClass1=new ArrayList<String>();
		HashMap<String, Integer> mapCountWordClass1=new HashMap<String,Integer>();
		
		

		String classLine=null;
		String dataLine=null;
		String classTestLine=null;
		String dataTestLine=null;
		int accuracy = 0;
		int countNumber=0;
		BufferedReader readerClass=null;
		BufferedReader readerData=null;
		BufferedReader readerTestData=null;
		BufferedReader readerTestClass =null;
		try {
			readerClass = new BufferedReader(new FileReader("src/bayesAlgo/trainingClass.txt"));
			readerData = new BufferedReader(new FileReader("src/bayesAlgo/trainingData.txt"));
			readerTestData = new BufferedReader(new FileReader("src/bayesAlgo/testData.txt"));
			readerTestClass	= new BufferedReader(new FileReader("src/bayesAlgo/testClass.txt"));
			//Read test data set
			while ((classTestLine = readerTestClass.readLine()) != null && (dataTestLine=readerTestData.readLine())!=null){
				allTestData.add(dataTestLine);
				if(classTestLine.equals("0")){
					allDataClass0.add(dataTestLine);
				}
				else{
					allDataClass1.add(dataTestLine);
				}
			}
			//Read Training data set
			while ((classLine = readerClass.readLine()) != null && (dataLine=readerData.readLine())!=null){
				allTrainingData.add(dataLine);
				if(classLine.equals("0")){
					listClass0.add(classLine);
					listDataClass0.add(dataLine);
				}
				else{
					listClass1.add(classLine);
					listDataClass1.add(dataLine);
				}
			}
			//count the total number of elements in class0 and class1 list
			double countListClass0=listClass0.size(); //170
			double countlistClass1=listClass1.size(); //152
			double totalCountofClasses=countListClass0+countlistClass1; //322
			double priorClass0=countListClass0/totalCountofClasses;
			double priorClass1=countlistClass1/totalCountofClasses;
			
			//take counts of each word in listDataClass 0
			String stopWordArray[]={"and","are","as","both","from","has","in","is","more","most","often","the","this"};
			
			for(int i=0;i<listDataClass0.size();i++){
				String eachSentence=listDataClass0.get(i);
				String arrayString []=eachSentence.split(" ");
				for(int j=0;j<arrayString.length;j++){
					countNumber=0;
					for(int k=0;k<stopWordArray.length;k++){
						if(arrayString[j].equalsIgnoreCase(stopWordArray[k])){
							countNumber++;
							break;
						}
					}
					if(countNumber==0){
						eachWordList.add(arrayString[j]);
					}
				}
			}
			
			for (String temp : eachWordList) {
				Integer count = mapCountWordClass0.get(temp);
				mapCountWordClass0.put(temp, (count == null) ? 1 : count + 1);
			}
		
			//take counts of each word in listDataClass 1
			for(int i=0;i<listDataClass1.size();i++){
				String eachSentence=listDataClass1.get(i);
				String arrayString []=eachSentence.split(" ");
				for(int j=0;j<arrayString.length;j++){
					countNumber=0;
					for(int k=0;k<stopWordArray.length;k++){
						if(arrayString[j].equalsIgnoreCase(stopWordArray[k])){
							countNumber++;
							break;
						}
					}
					if(countNumber==0){
						eachWordListClass1.add(arrayString[j]);
					}
						
				}
			}
			for (String temp : eachWordListClass1) {
				Integer count = mapCountWordClass1.get(temp);
				mapCountWordClass1.put(temp, (count == null) ? 1 : count + 1);
			}

			double totalWordDataClass0=eachWordList.size(); //total words in class 0
			double totalWordDataClass1=eachWordListClass1.size(); //total words in class 1
			double totalDistinctWordInBothClass=mapCountWordClass0.size()+mapCountWordClass1.size();//total distinct word count
			//System.out.println(totalDistinctWordInBothClass);

			//calculating conditional probability of training data set
			ArrayList<Double> probListClass0=new ArrayList<Double>();
			ArrayList<Double> probListClass1=new ArrayList<Double>();

			for(int i=0;i<allTrainingData.size();i++){
				String eachWordOfSentenceArray []=null;
				eachWordOfSentenceArray =allTrainingData.get(i).split(" ");
				
				for(int j=0;j<eachWordOfSentenceArray.length;j++){
				
					String eachWord=eachWordOfSentenceArray[j];
					//check occurrence of the word and calculate condt prob in class 0
					double occurenceClass0=0.0;
					if(mapCountWordClass0.containsKey(eachWord)){
						occurenceClass0=mapCountWordClass0.get(eachWord);
					}
					double condtProbClass0=(occurenceClass0+1.0)/(totalWordDataClass0+totalDistinctWordInBothClass);
					
					probListClass0.add(condtProbClass0);

					//check occurrence of the word and calculate condt prob in class 1
					double occurenceClass1=0.0;
					if(mapCountWordClass1.containsKey(eachWord)){
						occurenceClass1=mapCountWordClass1.get(eachWord);
					}
					double condtProbClass1=(occurenceClass1+1.0)/(totalWordDataClass1+totalDistinctWordInBothClass);
					
					probListClass1.add(condtProbClass1);

				}


				//Calculate total condt probability of class0
				double finalCondtProbClass0=priorClass0;
				for(int j=0;j<probListClass0.size();j++){
					finalCondtProbClass0=finalCondtProbClass0*probListClass0.get(j);
				}
				

				//calculate total condt prob of class1
				double finalCondtProbClass1=priorClass1;
				for(int j=0;j<probListClass1.size();j++){
					finalCondtProbClass1=finalCondtProbClass1*probListClass1.get(j);
				}
				
				//Find Accuracy
				if(finalCondtProbClass0>finalCondtProbClass1){
					if(listDataClass0.contains(allTrainingData.get(i))){
						accuracy++;
					}
				
				}
				else{
					if(listDataClass1.contains(allTrainingData.get(i))){
						accuracy++;
					}
					
				}

				probListClass0.clear();
				probListClass1.clear();			
			}
			System.out.println("Training Data accuracy--->>");
			System.out.println(accuracy / (totalCountofClasses)*100);

			accuracy=0;
			//calculating condt prob of test data set
			for(int i=0;i<allTestData.size();i++){
				String eachWordOfSentence []=allTestData.get(i).split(" ");
				for (int j = 0; j < eachWordOfSentence.length; j++) {
					String eachWord=eachWordOfSentence[j];
					double occurenceClass0=0.0;
					if(mapCountWordClass0.containsKey(eachWord)){
						occurenceClass0=mapCountWordClass0.get(eachWord);
					}
					double condtProbClass0=(occurenceClass0+1.0)/(totalWordDataClass0+totalDistinctWordInBothClass);
					probListClass0.add(condtProbClass0);

					double occurenceClass1=0.0;
					if(mapCountWordClass1.containsKey(eachWord)){
						occurenceClass1=mapCountWordClass1.get(eachWord);
					}
					double condtProbClass1=(occurenceClass1+1.0)/(totalWordDataClass1+totalDistinctWordInBothClass);
					probListClass1.add(condtProbClass1);
				}
				//Calculate total condt probability of class0
				double finalCondtProbClass0=priorClass0;
				for(int j=0;j<probListClass0.size();j++){
					finalCondtProbClass0=finalCondtProbClass0*probListClass0.get(j);
				}
				double finalCondtProbClass1=priorClass1;
				for(int j=0;j<probListClass1.size();j++){
					finalCondtProbClass1=finalCondtProbClass1*probListClass1.get(j);
				}
				//accuracy
				if(finalCondtProbClass0>finalCondtProbClass1){
					if(allDataClass0.contains(allTestData.get(i))){
						accuracy++;
					}
					
				}
				else{
					if(allDataClass1.contains(allTestData.get(i))){
						accuracy++;
					}
					
				}
				probListClass0.clear();
				probListClass1.clear();
			}
			double testDataSize=allTestData.size();
			System.out.println("Test data accuracy--->>");
			System.out.println(accuracy / (testDataSize)*100);

			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			readerTestClass.close();
			readerTestData.close();
			readerClass.close();
			readerData.close();
		}

	}
}
