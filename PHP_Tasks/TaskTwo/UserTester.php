<?php
require 'User.php';
$user = array();
$user[] = new User('Pratik', 39);
$user[] = new User('Mayank', 35);
$user[] = new User('Asmita', 32);
$user[] = new User('Vivek', 28);
$user[] = new User('Bhumi', 27);
$user[] = new User('Priyanka', 25);
$user[] = new User('Henil', 25);
$user[] = new User('Hardik', 25);
$user[] = new User('Matangi', 25);
$user[] = new User('Umesh', 22);

echo "unsorted array: <br/>";
print_r($user);
echo "<br/>";
echo "<br/>";

usort($user, function ($a, $b) { return ($a->get_userAge() > $b->get_userAge()); });
echo "sorted array: <br/>";
print_r($user);
?>