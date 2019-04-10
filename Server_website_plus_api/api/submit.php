<?php

if(isset($_POST['key']) && isset($_POST['data']) && isset($_POST['url']))
{


$key = unserialize($_POST['key']);
if($key == 'sadf789/*-')
{
$data = unserialize($_POST['data']);
$url = unserialize($_POST['url']);

$host="localhost"; 
$username="coding_mapper";
$password="RZ7MLRaXo*)fI{fD9s"; 
$db_name="coding_map"; 
$tbl_name="map";

//$data = array_unique($data);

$mysqli = new mysqli($host,$username,$password,$db_name);



if ($stmt = $mysqli->prepare("SELECT LinkedURLs FROM ".$tbl_name." WHERE URL=?")) {
 
    $stmt->bind_param("s", $url);
    $stmt->execute();
    $stmt->bind_result($oldData);
    $stmt->fetch();
    $stmt->close();

}

if($oldData == "" || $oldData == "null")
{
if ($stmt = $mysqli->prepare("INSERT INTO ".$tbl_name." (URL, LinkedURLs) VALUES (?, ?)")) {
 
    $stmt->bind_param("ss", $url, serialize($data));
    $stmt->execute();
    $stmt->close();
 
}
}
else
{
if ($stmt = $mysqli->prepare("UPDATE ".$tbl_name." SET LinkedURLs = ? WHERE URL = ?")) {

$tmpData = array_merge(unserialize($oldData),$data);
$newData = array_unique($tmpData); 


    $stmt->bind_param("ss",serialize($newData) , $url);
    $stmt->execute();
    $stmt->close();
 
}


}




echo 'check';























}





}








?>