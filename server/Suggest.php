<?php
/**
 * Created by PhpStorm.
 * User: wyj
 * Date: 2017/4/11
 * Time: 13:58
 */
require_once ('db_conn.php');
require_once('Constant.php');

$qqnumber = $_POST[QQNUMBER];
$suggest = $_POST[SUGGEST];

$sql = "INSERT INTO suggest(qqnumber,suggest) VALUES ('$qqnumber','$suggest')";
mysqli_query($con,$sql);
mysqli_close($con);