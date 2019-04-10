<?php
	
	if(isset($_GET['url'])){
		$url = (string) $_GET['url'];
		$host="localhost";
		$username="coding_mapper";
		$password="";
		$db_name="coding_map";
		$mysqli = new mysqli($host,$username,$password,$db_name);
		function downloadList($urls){
			global $mysqli;
			$tmpData = "";
			
			if ($stmt = $mysqli->prepare("SELECT LinkedURLs FROM map WHERE URL=?")) {
				$stmt->bind_param("s", $urls);
				$stmt->execute();
				$stmt->bind_result($tmpData);
				$stmt->fetch();
				$stmt->close();
			}

			return $tmpData;
		}

		$jsonChildren = array();
		$tmp = array();
		$tmpSecond = array();
		$tmp2 = array();
		$tmp3 = array();
		$tmpData = downloadList(/*.string.*/
		$url);
		
		if(empty($tmpData)){
			exit('ERROR, Invalid input: '.$url);
		} else {
			$main = unserialize($tmpData);
		}

		foreach($main as $element){
			$tmpData = downloadList($element);
			
			if(empty($tmpData)){
				
				if($element != $url){
					$jsonChildren[] = array('name'=> $element, 'size' => '1000');
				}

			} else {
				$tmp = unserialize($tmpData);
				foreach($tmp as $element2){
					$tmpData = downloadList($element2);
					
					if(empty($tmpData)){
						
						if(($element != $element2) && ($element != $url) && ($element2 != $url)){
							$tmp2[] = array('name'=> $element2 , 'size' => '1000');
						}

					} else {
						$tmpSecond = unserialize($tmpData);
						foreach($tmpSecond as $element3){
							
							if(($element != $element2)&& ($element2 != $element3) && ($element3 != $element) && ($element != $url) && ($element2 != $url)){
								$tmp3[] = array('name' => $element3, 'size' => '1000');
							}

						}

						
						if(($element != $element2) && ($element != $url) && ($element2 != $url)){
							$tmp2[] = array('name' => $element2,'children'=> $tmp3);
							unset($tmp3);
						}

					}

				}

			}

			
			if(!(empty($tmp2))){
				$jsonChildren[] = array('name'=> $element,'children'=> $tmp2);
				unset($tmp2);
			}

		}

		echo json_encode(array('name' => $url, 'children' => $jsonChildren));
	}

	?>