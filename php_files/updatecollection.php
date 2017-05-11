<?php

if($_SERVER["REQUEST_METHOD"]=="POST"){
require 'connection.php';
updatchk();
}

function updatchk()
{
global $connect;
$dID  = $_POST["dID"];
    $chk = $_POST["confirm"];

$query = "Update collectionunit1 SET confirm='$chk' WHERE dID='$dID';";
mysqli_query($connect, $query) or die (mysqli_error($connect));
mysqli_close($connect);
}

?>