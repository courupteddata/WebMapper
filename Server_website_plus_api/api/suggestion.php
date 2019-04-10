<?php

if(isset($_POST['list']))
{

$host="localhost"; 
$username="coding_mapper";
$password=""; 
$db_name="coding_map"; 
$tbl_name="work";

$list = unserialize($_POST['list']);


$mysqli = new mysqli($host,$username,$password,$db_name);

if ($stmt = $mysqli->prepare("SELECT work FROM ".$tbl_name." WHERE `index` = 1")) {
 
    
    $stmt->execute();
    $stmt->bind_result($oldData);
    $stmt->fetch();
    $stmt->close();

}

$data = unserialize($oldData);

if(!empty($data)){
$new = array_merge($data,$list);
$final = array_unique($new);



if ($stmt = $mysqli->prepare("UPDATE ".$tbl_name." SET work = ? WHERE `index` = 1")) {





    $stmt->bind_param("s",serialize($final));
    $stmt->execute();
    $stmt->close();
 
}

}
else
{
$final = array_unique($list);
if ($stmt = $mysqli->prepare("UPDATE ".$tbl_name." SET work = ? WHERE `index` = 1")) {





    $stmt->bind_param("s",serialize($final));
    $stmt->execute();
    $stmt->close();
 
}



}
}

?>