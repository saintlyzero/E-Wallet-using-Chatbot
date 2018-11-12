<?php
$servername = "localhost";
$username = "";
$password = "";
$dbname = "";

$conn = new mysqli($servername, $username, $password, $dbname);

	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		;
		$mobile=$_POST['rPhone'];
		$amount=$_POST['amount'];
		$senderName=$_POST['sName'];
		$receiverName;
		$smobile=$_POST['sPhone'];
		$description=$_POST['description'];

			$d1 =date('Y-m-d');


				
			$sql="select name from user_details WHERE mobile='$mobile' ";
			$result = mysqli_query($conn,$sql);
			$rowcount=mysqli_num_rows($result);
	
		if($rowcount==0)
	{
		echo "0";
		
	}
	else  {

		while($row = mysqli_fetch_array($result))
			$receiverName=$row['name'];


		
		$sqlCheckBalance="select balance from user_details WHERE mobile='$mobile'";
		$result = mysqli_query($conn,$sqlCheckBalance);
		while($row = mysqli_fetch_array($result))
			$cBalance=$row['balance'];

		if($cBalance<$amount)
		{
			echo "Your Balance is Less";
			
		}
		else
		{
			echo "Initiating Transaction";

			$sql1="UPDATE user_details SET balance = (case when mobile = '$senderName' then balance-'$amount' when mobile = '$receiverName' then balance+'$amount' end)";

			if ($conn->query($sql1) === TRUE) {
    		echo "Balance Updated";
			} else {
    			echo "Error: " . $sql1 . "<br>" . $conn->error;
					}

			$sql4 = "INSERT INTO transactions (sender, receiver, amount, date, description, receiver_name, sender_name)
			VALUES ('$smobile', '$mobile', '$amount', '$d1' , '$description', '$receiverName', '$senderName')";

			if ($conn->query($sql4) === TRUE) {
    			echo "Transfer Record Updated";
			} else {
    			echo "Error: " . $sql4 . "<br>" . $conn->error;
}	


			
		}
	}
	 


}