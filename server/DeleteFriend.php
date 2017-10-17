<?php
/**
 * Created by PhpStorm.
 * User: wyj
 * Date: 2016/4/22
 * Time: 20:07
 */
require_once('db_conn.php');
require_once('Constant.php');
$friendqqnumber = $_POST['friendqqnumber'];
$hostqqnumber = $_POST[QQNUMBER];
$response = null;

mysqli_query($con, "delete from friends WHERE hostId = '$hostqqnumber'AND friendId = '$friendqqnumber'");
mysqli_query($con, "delete from friends WHERE hostId = '$friendqqnumber'AND friendId = '$hostqqnumber'");
$response ['success'] = 0;
mysqli_close($con);

echo json_encode($response);

?>