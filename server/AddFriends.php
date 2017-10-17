<?php
require_once('db_conn.php');
require_once('Constant.php');
require_once('ServerAPI.php');
/**
 * Created by PhpStorm.
 * User: wyj
 * Date: 2016/4/14
 * Time: 20:40
 */
$friendqqnumber = $_POST['friendqqnumber'];
$qqnumber = $_POST[QQNUMBER];
$message = $_POST['message'];
$sp = new ServerAPI(APP_KEY, APP_SECRET);

$resIsFriends = mysqli_query($con, "select * from friends WHERE hostId = '$qqnumber'AND friendId = '$friendqqnumber'");
$dataCount = mysqli_num_rows($resIsFriends);
if ($dataCount == 0) {
    //向融云服务器请求向friendqqnumber发送系统请求
    $r = $sp->messageSystemPublish('123456', $friendqqnumber, OBJECT_ADD_FRIEND,
        "{'operation':'Request','sourceUserId':$qqnumber,'targetUserId':$friendqqnumber,
        'message':$message,'extra':''}");
    $json_server = json_decode($r);
    mysqli_query($con, "insert into friends(hostId,friendId,status)VALUES ('$qqnumber','$friendqqnumber',2)");
    mysqli_query($con, "insert into friends(hostId,friendId,status)VALUES ('$friendqqnumber','$qqnumber',3)");
    if ($json_server->code == 200) {
        $response["success"] = 0;
    } else {
        $response["success"] = 2;
    }
    //-----------------------------------------------------
} else {
    $response['success'] = 1;
}
echo json_encode($response);
?>