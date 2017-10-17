<?php
require_once ('db_conn.php');
require_once('Constant.php');
require_once('ServerAPI.php');
/**
 * Created by PhpStorm.
 * User: wyj
 * Date: 2016/4/14
 * Time: 20:40
 */

$qqnumber = $_POST[QQNUMBER];
$friendqqnumber = $_POST['friendqqnumber'];
$result = $_POST[RESULT];
$sp = new ServerAPI(APP_KEY, APP_SECRET);
if($result == ACCEPT){
    $r = $sp->messagePublish($qqnumber, $friendqqnumber, 'RC:TxtMsg',
        "{'content':'我已通过了你的验证请求，现在可以聊天了','extra':''}");
    $sp->messageSystemPublish('123456', $friendqqnumber, OBJECT_ADD_FRIEND,
        "{'operation':'AcceptResponse','sourceUserId':$qqnumber,'targetUserId':$friendqqnumber,
        'message':'验证请求已通过','extra':''}");
    $json_server = json_decode($r);
    if ($json_server->code == 200) {
        $response["success"] = 0;
        mysqli_query($con, "update friends set status=1 WHERE hostId ='$qqnumber' AND friendId = '$friendqqnumber'");
        mysqli_query($con, "update friends set status=1 WHERE hostId ='$friendqqnumber' AND friendId = '$qqnumber'");
        $sql = "select * from user WHERE qqnumber = '$friendqqnumber'";
        $friendArray = mysqli_fetch_array(mysqli_query($con,$sql));
        $response = array(SUCCESS => 0,
            "friendsex" => $friendArray[SEX],
            "friendphone"=>$friendArray[PHONE],
            "friendtoken" => $friendArray[TOKEN],
            "friendsignature" => $friendArray[SIGNATURE]);
    } else {
        $response["success"] = 1;
    }
}

mysqli_close($con);
echo json_encode($response);
?>