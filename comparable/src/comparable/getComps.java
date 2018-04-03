package comparable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class getComps {
	static int count; 
	public static void main(String[] args) throws IOException {
		
	     Company [] companyArray = scanCSV("test.csv");

	     promptUser(companyArray);

	}
	//get information from csv, and create an object array
	public static Company[] scanCSV (String csvFile) throws IOException{
		
		 //count how many lines are in the csv file, can hardcode array value if you know it
		 BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile));
	     String input;
	     while((input = bufferedReader.readLine()) != null)
	     {
	         count++;
	     }
	     
	     //create Company array that is the size of the rows, subtract one cuz we were getting one extra null
	     Company [] companyArray = new Company[count-1];

	     //make a reader
	     BufferedReader br = null;
	     String line = "";
	     String cvsSplitBy = ",";
	       
	     br = new BufferedReader(new FileReader(csvFile));
	     //skip first line (I think)
	     br.readLine();
	     int j = 0;
	     //get info from the csv and put it in a company object
	     while ((line = br.readLine()) != null) {
	    	 String[] companyInfo = line.split(cvsSplitBy);
	    	 
	    	 Company company = new Company();
	    	 company.name = companyInfo[0];
	    	 company.mrkCap = Double.parseDouble(companyInfo[1]);
	    	 company.peRatio = Double.parseDouble(companyInfo[2]);
	    	 company.industry = companyInfo[3];

	    	 //store that company object in the array
	    	 companyArray[j]= company;
	    	 j++;
	     }
		return companyArray;
	}
	
	//interact with users
	public static void promptUser(Company[] companyArray) throws IOException{
	 Scanner scan = new Scanner(System.in);
   	 System.out.println("Welcome to the Company Comparable Analyzer!");
   	 System.out.println("Please enter a valid company on the S&P 500 index: ");
   	 String compName = scan.nextLine();
   	 System.out.println("Please enter how many comparables you would like to generate: ");
   	 int compNum = scan.nextInt();
   	 
   	 //printing here for now
   	 for(int i = 0; i<count-1; i++){
   		//be careful with this because all of the object.names have spaces after them 
   		if (compName.equals( companyArray[i].name)){
   			Double [] cofArray = getCoefficients("training.txt", companyArray);
   			Company [] comparableArray = getComparables(companyArray[i], cofArray[0], cofArray[1], companyArray);
   			i = count;
   			//sort them in the future
   			System.out.println("name mrktCap peRatio industry Score");
   			for(int j = 0; j<compNum; j++){
   				System.out.println(comparableArray[j].name +": " +comparableArray[j].mrkCap +"|" +comparableArray[j].peRatio +"|" +
   						comparableArray[j].industry +"|" +comparableArray[j].score);
   			}
   			
   		}else{//make this happen less often
   		   	System.out.println("The company you chose is not in the Index");
   		}
   	}
	}
	
	public static int compareCompanies(Company a, Company b){
		return a.score.compareTo(b.score);
	}
	
	
	//get coefficients plan
	/*
	 * csv with training comparable companies ( x amount)
	 * csv with Bloomberg file
	 * for each example, find the company object and its comparable company objects(like search company Array for them)
	 * could keep track of average distance with each metric
	 * could think about if a lot of comparables are close for something its 
	 * make a line of best fit for each company, and its comparables then
	 * average all of them across the companies and use each of those variabels as the 
	 * 
	 * could also make coefficients s.t. nothing is weighted too heavily,
	 * like differences in market cap sum up to 10ish
	 * differences in peratio sum up to 10ish
	 * manual input in terms of which one matters more
	 * 
	 * Could just run like 1000 times trying different coefficients, and see which one is closest, Imma do that now because it will be interesting
	 * each time u get a company right its +1
	 * with temp arrays
	 * 
	 * ok Im gonna do that replan
	 * 
	 * have a temp coefficientArray, which would consist of random vals, test it, if it gets more training data corrects, count it
	 * if not, boii that shit
	 */
	public static Double[] getCoefficients (String trainingComparablesFile, Company[] companyArray) throws IOException{
		//create array in order to store coefficients make it the same size as # of metrics
		Random rand = new Random();
		Double [] coefficientArray = new Double[2];
		Double [] tempArray = new Double[2];
		
		 BufferedReader bufferedReader = new BufferedReader(new FileReader(trainingComparablesFile));	
	     String input;
	     int exampleNum = 0;
	     while((input = bufferedReader.readLine()) != null)
	     {
	         exampleNum++;
	     }
	     
		 BufferedReader br = null;
	     String line = "";
	     String cvsSplitBy = ",";
	     br = new BufferedReader(new FileReader(trainingComparablesFile));

	     //limit of how many exmplaes 
	     //could prolly do this the way sarah did the other one
	     String [][] trainingData = new String[exampleNum][5];
	     int length;
	     //get training data in nice format
	     
	     
	     int j = 0;
	     while((line = br.readLine()) != null){

	    	 trainingData[j] = line.split(cvsSplitBy);
	    	 j++;
	    	 //System.out.println(trainingData[j][0]);
	     } 
    	 
	    	 
//	    	 String[] data = line.split(cvsSplitBy);
//	    	 length = data.length;
//	    	 //comparableExamples[j] = data;
//	    	 //String name = data[0];
	     
	     //get array of company objects to save time with the look ups
	     Company [] companiesFromTraining = new Company[exampleNum];
	     int boolin=0;
	     for(int l = 0; l<exampleNum; l++){
	    	 for(int u = 0; u<count-1; u++){
	    		 //if the name of the first company, aka the one being compared to others
	    		 //is the samae as the name of a company object, they are the same
	    		 if(trainingData[l][0].equals(companyArray[u].name)){
	    			 companiesFromTraining[l] = companyArray[u];
	    			 boolin =1;
	    			 u = count;//break
	    		 }
	    	 }
	    	 if (boolin == 0){
	    		 //basically example is sus, should do something in this case
	    	 }
	    	 
	     }
	     
	     //put this outside of loop to save time
	          
	     //get info from the comparables file and put into Array
	     
	     Company [] randComparables = new Company[exampleNum];
	     //however many elements are in this, compare to first n elements 
	     int bestCorrect = 0;
	     int tempCorrect = 0;
	     
	     //run random coeffieicents 1000 times
	     for (int i = 0; i<1000; i++){
	    	 //randomly assign them both a value
	    	 tempArray[0] = rand.nextDouble();
	    	 tempArray[1] = rand.nextDouble();
	    	 
	    	 //call get comparables with current, and those coefficients
	    	 //current should be the hth from training
	    	 for(int h =0; h<exampleNum-1; h++){
	    		System.out.println(tempArray[0]);
				 System.out.println(tempArray[1]);
	    		 randComparables= getComparables(companiesFromTraining[h], tempArray[0], tempArray[1], companyArray);
	    		 //compare to training data (iterate through both?)
	    		 //see if the real ones are consistent with the top 5 produced comparables
	    		 for(int x =1; x<4; x++){
	    			 for(int y =0; y<4; y++){
	    				 //System.out.println(trainingData[y][x]);
	    				 //System.out.println(randComparables[h].name);
	    				 if(randComparables[h].name.equals(trainingData[y][x])){
	    					 tempCorrect++;
	    				 }

	    			 }
	    			 
	    		 }

	    	 }
	    	 

	    	 //split by commas
	    	 //System.out.println(bestCorrect);
	    	 System.out.println(tempCorrect);
	    	 if(tempCorrect > bestCorrect){
	    		 System.out.println("here");
	    		 bestCorrect = tempCorrect;
	    		 
	    		 coefficientArray = tempArray;
	    	 }
	    	 tempCorrect = 0;
	    	 
	     }
	     
	     System.out.println(coefficientArray[0]);
	     System.out.println(coefficientArray[1]);
	     
		return coefficientArray;
		
		
	}
	
	public static Company[] getComparables(Company current, Double mrkCapCof, Double peRatioCof, Company[] companyArray){

		//get scores for each company relative to our company
		for(int i = 0; i<count-1; i++){
			//may want to create separate method here
			companyArray[i].score = mrkCapCof*(Math.abs(companyArray[i].mrkCap-current.mrkCap) + peRatioCof * Math.abs(companyArray[i].peRatio-current.peRatio));
		}
		
		Arrays.sort(companyArray);
		
		return companyArray;
	}
}

