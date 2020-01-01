/*
Create a User class. store user name and age.
*/

public class TaskTwo {

	//instance-variables
	private String userName;
	private int userAge;

	//constructors
	TaskTwo(String userName, int userAge){
		this.userName = userName;
		this.userAge = userAge;
	}

	//methods
	public int getUserAge(){
		return userAge;
	}

	public String getUserName(){
		return userName;
	}

}//TaskTwoClassEnds