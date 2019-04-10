
    <?php
function array_unique2(&$aray) {
    $aHash = array();
    foreach ($aray as $key => &$val) if (@$aHash[$val]++) unset ($aray[$key]);
    array_merge($aray);
}

$arr = array("test.com", "test.com","google.com","google.com","swgfadf","reallylongdomain.com","example","example.com");
$arr2 = array("tesghjt.com", "test.com","googhjle.com","google.com","swgdfghfadf","reallylondfghgdomain.com","examdhple","exampldghe.com");






    //$max = 100000;
    //$arr = range(1,$max,3);
    //$arr2 = range(1,$max,2);
    $arr = array_merge($arr,$arr2);

    $time = -microtime(true);
    $res1 = array_unique($arr);
    $time += microtime(true);
    echo "deduped to ".count($res1)." in ".$time;
    // deduped to 666667 in 32.300781965256

    $time = -microtime(true);
    $res2 = array();
    foreach($arr as $key=>$val) {   
        $res2[$val] = true;
    }
    $res2 = array_keys($res2);
    $time += microtime(true);
    echo "<br />deduped to ".count($res2)." in ".$time;
    // deduped to 666667 in 0.84372591972351

   // $arr6 = range(1,$max,3);
   // $arr5 = range(1,$max,2);
   // $arr4 = array_merge($arr5,$arr6);

  //  $time = -microtime(true);
//$test = array_unique2($arr4);
//$test = array_keys($test);
//$time += microtime(true);
  //  echo "<br />deduped to ".count($test)." in ".$time;



    














?>