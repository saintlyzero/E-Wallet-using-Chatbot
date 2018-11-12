<?php
$servername = "localhost";
$username = "";
$password = "";
$dbname = "";

$conn = new mysqli($servername, $username, $password, $dbname);

	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		;
		$mobile=$_POST['mobile'];
				
		
	$return_arr = array();
	$sql = "SELECT date , SUM(amount) AS credit FROM transactions WHERE sender='$mobile'	
GROUP BY date order by date;";
	$result = mysqli_query($conn,$sql);
	
	$response = array(); 
	
	$response['creditDetails'] = array(); 
	
	while($row = mysqli_fetch_array($result)){
		$temp = array(); 
		$temp['date']=$row['date'];
		$temp['credit']=$row['credit'];
		
			
		array_push($response['creditDetails'],$temp);

		$sql1 = "SELECT date , SUM(amount) AS debit FROM transactions WHERE receiver='$mobile'	
GROUP BY date order by date;";
	$result1 = mysqli_query($conn,$sql);
	
	$response1 = array(); 
	
	$response1['debitDetails'] = array(); 
	
	while($row = mysqli_fetch_array($result1)){
		$temp1 = array(); 
		$temp1['date']=$row['date'];
		$temp1['credit']=$row['credit'];
		
			
		array_push($response['debitDetails'],$temp1);
	}
	}

 echo json_encode($response );
 
 

		
		
	}else{
echo 'error';
}