/*
Create an Arraylist of User type with 10 users.
*/
import java.util.*;
public class TaskTwoTester {

	public static void main(String[] args) {

		ArrayList<TaskTwo> arrListUser = new ArrayList<TaskTwo>();
		arrListUser.add(new TaskTwo("Deepika", 10));
		arrListUser.add(new TaskTwo("Deepika", 20));
		arrListUser.add(new TaskTwo("Deepika", 40));
		arrListUser.add(new TaskTwo("Deepika", 50));
		arrListUser.add(new TaskTwo("Deepika", 30));
		arrListUser.add(new TaskTwo("Deepika", 60));
		arrListUser.add(new TaskTwo("Deepika", 80));
		arrListUser.add(new TaskTwo("Deepika", 90));
		arrListUser.add(new TaskTwo("Deepika", 70));
		arrListUser.add(new TaskTwo("Deepika", 100));

		System.out.println("befor sorting : ");
		System.out.println("---------------");
		//displaying the elements
		for(TaskTwo tt : arrListUser){
			System.out.println(tt.getUserName() + " " + tt.getUserAge());
		}

		//sorting the elements with userAge
		Collections.sort(arrListUser, Comparator.comparing(TaskTwo::getUserAge));
		System.out.println("---------------");
		System.out.println("after sorting : ");
		System.out.println("---------------");
		//displaying the elements after sorting
		for(TaskTwo tt : arrListUser){
			System.out.println(tt.getUserName() + " " + tt.getUserAge());
		}
		
	}//mainEnds
	
}//TaskTwoTesterClassEnds

/*OUTPUT
root@v-i-v-e-k:~/GlobalVox/JAVA_Tasks# javac TaskTwoTester.java
root@v-i-v-e-k:~/GlobalVox/JAVA_Tasks# java TaskTwoTester
befor sorting : 
---------------
Deepika 10
Deepika 20
Deepika 40
Deepika 50
Deepika 30
Deepika 60
Deepika 80
Deepika 90
Deepika 70
Deepika 100
---------------
after sorting : 
---------------
Deepika 10
Deepika 20
Deepika 30
Deepika 40
Deepika 50
Deepika 60
Deepika 70
Deepika 80
Deepika 90
Deepika 100
*/