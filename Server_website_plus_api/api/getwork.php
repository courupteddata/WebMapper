<?php

$host="localhost"; 
$username="coding_mapper";
$password=""; 
$db_name="coding_map"; 
$tbl_name="work";



$mysqli = new mysqli($host,$username,$password,$db_name);

$oldData = "";

if ($stmt = $mysqli->prepare("SELECT work FROM ".$tbl_name." WHERE `index` = 1")) {
 
  
    $stmt->execute();
    $stmt->bind_result($oldData);
    $stmt->fetch();
    $stmt->close();

}

//echo var_dump(unserialize($oldData));

$data = unserialize($oldData);
if(!empty($data))
{
$toBeReturned = array_shift($data);


if ($stmt = $mysqli->prepare("UPDATE ".$tbl_name." SET work = ? WHERE `index` = 1")) {


 


    $stmt->bind_param("s",serialize($data));
    $stmt->execute();
   $stmt->close();
 
}
}
if(empty($toBeReturned))
{
echo "google.com";
}
else
{
echo $toBeReturned;
}





















?>