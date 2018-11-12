<?php

$servername = "localhost";
$username = "";
$password = "";
$dbname = "";

$conn = new mysqli($servername, $username, $password, $dbname);

	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		;
		$mobile=$_POST['mobile'];
				
		
	$sql = "SELECT balance FROM user_details
WHERE mobile = '$mobile' ";
	$result = mysqli_query($conn,$sql);
			
	while($row = mysqli_fetch_array($result))		 
		$balance=$row['balance'];
			
	echo $balance;
			
	}else{
echo "-1";
}