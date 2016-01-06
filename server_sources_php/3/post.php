<?php

$user = $_POST['user'];
$msg = $_POST['msg'];
$pic = rand(1,7);


$pic = "avatar/".$pic.".png";
$file = 'posts.json';
$current = file_get_contents($file);


$current .= ",{\"text\":\"".$msg."\",\n\"username\":\"".$user."\",\"avatar\":\"".$pic."\"}";


file_put_contents($file, $current);

echo "success"
?>