 <!DOCTYPE html>
<html>
<head>
	<title>TaskOne</title>
</head>
<body>

<?php
echo "enter the value of n : ";
?>

<form action="TaskOne.php" method="post">
	<input type="text" name="number" id="number">
	<input type="submit" name="btn" value="generate values">
</form>
<br>

<?php
	
	$arrOfRandNumbers = [];
	$arrOfPrimeNumbers = [];
	$flag = false;

	if($_SERVER['REQUEST_METHOD'] == "POST" and isset($_POST['number']))
	    {
	        func();
	    }
	    function func()
	    {

	    	if($_POST['number'] < 15)
	    	{
	    		echo "input should be greater than 15 :(";
	    	}
	    	elseif($_POST['number'] >= 15)
	    	{
	    		for ($i = 1; $i <= $_POST['number']; $i++)
	    		{
			    	$arrOfRandNumbers[$i] = rand(1,50);
				}
				echo "n array of random numbers are : <br/>";
				print_r($arrOfRandNumbers);
				echo "<br/>";
				$j = 1;
				for ($i = 1; $i <= $_POST['number']; $i++)
	    		{
			    	$flag = isPrime($arrOfRandNumbers[$i]);
			    	if($flag==true)
			    	{
			    		$arrOfPrimeNumbers[$j] = $arrOfRandNumbers[$i];
			    	}
			    	$flag = false;
			    	$j++;
				}
				echo "Prime numbers from above array are : <br/>";
				print_r($arrOfPrimeNumbers);
				echo "<br/>";
				echo "largest element from random array is : ";
				echo(max($arrOfRandNumbers));echo "<br/>";
				echo "smallest element from random array is : ";
				echo(min($arrOfRandNumbers));echo "<br/>";
	    	}
	    	else
	    	{
	    		echo "invalid input :( ";
	    	}
		}#funcEnds
		function isPrime($number)
		{
			if ($number == 1) 
		    return false;
		    if ($number == 2) 
		    return false;
		    for ($i = 2; $i <= $number/2; $i++){ 
		        if ($number % $i == 0) 
		            return false;
		    } 
    		return true; 

		}#isPrimeFunctionEnds
?>

</body>
</html> 