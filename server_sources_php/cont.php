<?php
$arr = array("Asansol","Chandni Chowk","Howrah","Nalanda","New Delhi","Patliputra");

echo "[";
$ct = 0;
foreach($arr as $i){
	++$ct;
	if($ct>1)
	echo ",";	
	echo "{\"name\":\"".$i."\"}";
	
}	
echo "]";
?>