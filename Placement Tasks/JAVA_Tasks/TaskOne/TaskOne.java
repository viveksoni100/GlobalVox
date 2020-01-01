/*
Create an array of n random elements (n>15)
Convert array to ArrayList.
Find Prime numbers in the ArrayList.
Store prime numbers in another ArrayList
Also find smallest and largest element
*/

import java.util.Scanner;
import java.util.Arrays;
import java.util.*;
public class TaskOne {

	//instance-variables
	static ArrayList<Integer> arrList;
	static ArrayList<Integer> anotherArrList = new ArrayList<Integer>();

	//constructors

	//methods
	public static void main(String[] args) {

		int n=0;

		Scanner sc = new Scanner(System.in);
		System.out.print("enter the value of n : ");
		n = sc.nextInt();
		if (n<15){
			System.out.print("oooppsss! value of n should be greater than 15 :(");
		}

		int[] randNum = new int[n];

		//methods
		generateRandomElementsAndStoreItInArray(randNum);
		// System.out.println("Numbers Generated: " + Arrays.toString(randNum));

		convertingArrayToArrayList(randNum);

		findPrimeNumbersFromArrayList(arrList);

		//displaying another array list which contains prime numbers
		System.out.println(anotherArrList);

		System.out.println("Largest element is : " + Collections.max(arrList));
		System.out.println("Smallest element is : " + Collections.min(arrList));

	}//mainMethodEnds

	static void generateRandomElementsAndStoreItInArray(int[] randNum){

		for(int i = 0; i < randNum.length; i++) {
		    randNum[i] = (int)(Math.random()*50 + 1);
		}//forEnds

	}//generateRandomElementsAndStoreItInArrayMethodEnds

	static void convertingArrayToArrayList(int[] randNum){

		arrList = new ArrayList<Integer>(randNum.length);
		for (Integer i : randNum) {
		    arrList.add(i);
		}
		System.out.println("elements in array_list : ");
		System.out.println("------------------------");
		System.out.println(arrList);

	}//convertingArrayToArrayListMethodEnds

	static void findPrimeNumbersFromArrayList(ArrayList<Integer> arrList){

		System.out.println("elements which are prime : ");
		System.out.println("--------------------------");
		for(Integer i : arrList){
			if(isPrime(i)){
				storingPrimeNumbersInAnotherArrayList(i);
			}
		}
	}//findPrimeNumbersFromArrayListMethodEnds

	static boolean isPrime(Integer i){

		int j,m=0,flag=0;      
		m=i/2;      

		if(i==0||i==1){  
			return false;
		}else{  
			for(j=2;j<=m;j++){      
				if(i%j==0){      
					flag=1;      
					break;      
				}      
			}//forEnds     
   			if(flag==0)  { return true; }
  		}//elseEnds
  		return false;
	}//isPrimeMethodEnds

	static void storingPrimeNumbersInAnotherArrayList(Integer i){

		anotherArrList.add(i);

	}//storingPrimeNumbersInAnotherArrayListMethodEnds

}//TaskOneClassEnds

/*OUTPUT

root@v-i-v-e-k:~/GlobalVox/JAVA_Tasks# javac TaskOne.java
root@v-i-v-e-k:~/GlobalVox/JAVA_Tasks# java TaskOne
enter the value of n : 15
elements in array_list : 
------------------------
[47, 41, 19, 44, 1, 11, 34, 30, 14, 46, 47, 34, 30, 26, 24]
elements which are prime : 
--------------------------
[47, 41, 19, 11, 47]
Largest element is : 47
Smallest element is : 1


*/