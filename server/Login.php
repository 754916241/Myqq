<?php
/**
 *登陆
 */
require_once('Constant.php');
require_once('db_conn.php');
header("Content-type:text/html;charset=utf-8");
$qqnumber = $_POST[QQNUMBER];
$password = $_POST[PASSWORD];
$n = 1;

$sql = "select * from user WHERE qqnumber = '$qqnumber' AND password = '$password'";
$result = mysqli_query($con,$sql);
$dataCount = mysqli_num_rows($result);
if ($dataCount == 1) {
    $response = 0;
    $userfetch = mysqli_fetch_array($result);
    $userarr[0] = array(SUCCESS => $response,
        QQNUMBER => $userfetch[QQNUMBER],
        NICKNAME => $userfetch[NICKNAME],
        TRUENAME => $userfetch[TRUENAME],
        SEX => $userfetch[SEX],
        AGE => $userfetch[AGE],
        IMAGE => $userfetch[IMAGE],
        SIGNATURE => $userfetch[SIGNATURE],
        PHONE => $userfetch[PHONE],
        TOKEN => $userfetch[TOKEN],);
    $sqlfriends = "select first.*,second.qqnumber as friendqqnumber,second.nickname as friendnickname,second.truename as friendtruename,
                    second.sex as friendsex,second.age as friendage,second.phone as friendphone,second.signature as friendsignature,second.token as friendtoken,
                    second.image as friendimage,friends.status from user AS first inner JOIN friends on first.qqnumber=friends.hostId inner join user AS
                    second on friends.friendId = second.qqnumber WHERE first.qqnumber = '$qqnumber'";
    $resultfriends = mysqli_query($con,$sqlfriends);
    $dataCountfriends = mysqli_num_rows($resultfriends);
    if ($dataCountfriends >= 1) {
        while ($friendsfetch = mysqli_fetch_array($resultfriends)) {
            $userarr[$n++] = array(
                "friendqqnumber" => $friendsfetch['friendqqnumber'],
                "friendnickname" => $friendsfetch['friendnickname'],
                "friendtruename" => $friendsfetch['friendtruename'],
                "friendsex" => $friendsfetch['friendsex'],
                "friendage" => $friendsfetch['friendage'],
                "friendphone"=>$friendsfetch['friendphone'],
                "friendtoken" => $friendsfetch['friendtoken'],
                "friendimage" => $friendsfetch['friendimage'],
                "friendsignature" => $friendsfetch['friendsignature'],
                "friendstatus" => $friendsfetch['status']
            );
        }
    }
} else {
    $response = 1;
    $userarr[0] = array(SUCCESS => $response);
}
mysqli_close($con);

echo json_encode($userarr);


?>
