<?php
require_once ('db_conn.php');
require_once('Constant.php');
/**
 * Created by PhpStorm.
 * User: wyj
 * Date: 2016/4/14
 * Time: 20:40
 */

$qqnumber = $_POST[QQNUMBER];
$user = "";
for($i = 0;$i<sizeof($qqnumber);$i++){
    $sql = "select * from user WHERE qqnumber = '$qqnumber[$i]'";
    $result = mysqli_query($con,$sql);
    $userArray = mysqli_fetch_array($result);
    $user[$i] = array(
        NICKNAME => $userArray[NICKNAME],
        IMAGE => $userArray[IMAGE],
        TRUENAME => $userArray[TRUENAME],
        SEX => $userArray[SEX],
        AGE => $userArray[AGE],
        SIGNATURE => $userArray[SIGNATURE],
        PHONE => $userArray[PHONE],
        TOKEN => $userArray[TOKEN]
    );
}
mysqli_close($con);
echo json_encode($user);


?>