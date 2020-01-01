<?php
$strOne = array("Vivek","Deepika","Hardik","Hetaxi");
echo "elements in array one are : <br/>";
print_r($strOne);
echo "<br/>";

$strTwo = array("Vivek","Gauri","Abhishek","Hetaxi");
echo "elements in array two are : <br/>";
print_r($strTwo);
echo "<br/>";

$result = array_intersect($strOne, $strTwo);

echo "common elements in both the array are : <br/>";
print_r($result);
?>