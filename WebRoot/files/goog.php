<?php
$q = $_GET["wd"];
header("Content-Type: text/html; charset=utf-8");
$url = "http://news.google.com.hk/news/feeds?pz=1&cf=all&ned=cn&hl=zh-CN&q=" . $q . "&output=rss&scoring=n&num=100";
$news = simplexml_load_file($url);
echo $news->asXML();
/*
$feeds = array();

$i = 0;

foreach ($news->channel->item as $item) 
{
    preg_match('@src="([^"]+)"@', $item->description, $match);
    $parts = explode('<font size="-1">', $item->description);

    $feeds[$i]['title'] = (string) $item->title;
    $feeds[$i]['newsUrl'] = (string) $item->link;
    $feeds[$i]['from'] = strip_tags($parts[1]);
    $feeds[$i]['description'] = strip_tags($parts[2]);
    $feeds[$i]['dateTime'] = (string)$item->pubDate;

    $i++;
}

//echo '<pre>';
//print_r($feeds);
echo json_encode($feeds);
//echo '</pre>';
*/
?>