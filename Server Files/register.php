<?php
$servername = "";
$username = "";
$password = "";
$dbname = "";

$conn = new mysqli($servername, $username, $password, $dbname);

	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		;
		$name=$_POST['name'];
		$mobile=$_POST['mobile'];
		$email=$_POST['email'];
		$balance=$_POST['balance'];
				
		
				
	$sql = "INSERT INTO user_details (name, mobile, email, balance)
VALUES ('$name', '$mobile', '$email', '$balance')";

if ($conn->query($sql) === TRUE) {
    echo "User Registered";
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}	

		

}