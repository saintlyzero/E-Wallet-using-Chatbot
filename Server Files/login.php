<?php

$servername = "localhost";
$username = "";
$password = "";
$dbname = "";

$conn = new mysqli($servername, $username, $password, $dbname);

	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		;
		$email=$_POST['email'];
				
		
	$return_arr = array();
	$sql = "SELECT * FROM user_details
WHERE email = '$email' ";
	$result = mysqli_query($conn,$sql);
	
	$response = array(); 
	
		
	while($row = mysqli_fetch_array($result)){
		$temp = array(); 
		$temp['name']=$row['name'];
		$temp['phone']=$row['phone'];
		$temp['email']=$row['email'];
		$temp['balance']=$row['balance'];
			
		array_push($response,$temp);
	}

echo json_encode($response);
		
	}else{
echo '-1';
}