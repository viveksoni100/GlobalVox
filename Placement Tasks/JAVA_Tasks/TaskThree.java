/*
Create two hash sets of type String, and compare those to search common elements.
*/
import java.util.*;
public class TaskThree {

	public static void main(String[] args) {
		
	HashSet<String> set = new HashSet();
	set.add("Vivek");
	set.add("Henil");
	set.add("Hardik");
	set.add("Umesh");
	set.add("Rohit");
	set.add("Matangi");
	set.add("Priyanka");
	set.add("Bhumi");

	HashSet<String> set2 = new HashSet();
	set2.add("Deepika");
	set2.add("Sweta");
	set2.add("Richa");
	set2.add("Uma");
	set2.add("Parita");
	set2.add("Priyanka");
	set2.add("Kajal");
	set2.add("Bhumi");

	for(String element : set) {

        if(set2.contains(element)) {
            System.out.println(element + " is common");
        }
    }//forEnds

	}//mainMethodEnds

}//TaskThreeEnds

/*
OUTPUT

root@v-i-v-e-k:~/GlobalVox/JAVA_Tasks# java TaskThree
Bhumi is common
Priyanka is common
root@v-i-v-e-k:~/GlobalVox/JAVA_Tasks# 

*/