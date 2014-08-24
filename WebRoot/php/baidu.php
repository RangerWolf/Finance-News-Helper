<?php  
header("Content-Type: text/html; charset=gb2312");
$ctx = stream_context_create(array(  
   'http' => array(  
       'timeout' => 10 //设置一个超时时间，单位为秒  
       )  
   )  
);  
$q = $_GET["wd"];
$url = "http://news.baidu.com/ns?q1=&a2=&q3=". $q . "&tn=newsrss&sr=0&cl=2&rn=100&ct=0";
$content = file_get_contents($url, 0, $ctx);
echo $content;
//echo iconv("GB2312","UTF-8//IGNORE",$content);
?> 