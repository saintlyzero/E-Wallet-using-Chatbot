<?php

/*
	Add your Server Details

*/ 
$servername = "localhost";
$username = "";
$password = "";
$dbname = "";

$conn = new mysqli($servername, $username, $password, $dbname);

	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		;
		$mobile=$_POST['mobile'];
		$startDate=$_POST['sDate'];
		$endDate=$_POST['eDate'];
		
	
		
	$return_arr = array();
	$sql = "SELECT * from transactions where sender='$mobile' AND date between '$startDate' AND '$endDate';";
	$sql1 = "SELECT * from transactions where receiver='$mobile' AND date between '$startDate' AND '$endDate';";
	$result = mysqli_query($conn,$sql);
	
	$response = array(); 
	
	$response['data'] = array(); 
	
	while($row = mysqli_fetch_array($result)){
		$temp = array(); 
		$temp['type']="Debit";
		$temp['sender']=$row['sender'];
		$temp['receiver']=$row['receiver'];
		$temp['amount']=$row['amount'];
		$temp['date']=$row['date'];
		$temp['description']=$row['description'];
		$temp['receiver_name']=$row['receiver_name'];
		$temp['sender_name']=$row['sender_name'];
			
		array_push($response['data'],$temp);
	}

	$result1 = mysqli_query($conn,$sql1);
	while($row = mysqli_fetch_array($result1)){
		$temp1 = array(); 
		$temp1['type']="Credit";
		$temp1['sender']=$row['sender'];
		$temp1['receiver']=$row['receiver'];
		$temp1['amount']=$row['amount'];
		$temp1['date']=$row['date'];
		$temp1['description']=$row['description'];
		$temp1['receiver_name']=$row['receiver_name'];
		$temp1['sender_name']=$row['sender_name'];
			
		array_push($response['data'],$temp1);
	}


echo ($response);
		
		
	}else{
echo 'error';
}
