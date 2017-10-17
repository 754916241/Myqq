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

$sql = "select * from user WHERE qqnumber = '$qqnumber'";
$result = mysqli_query($con,$sql);

    $userArray = mysqli_fetch_array($result);
    $response = array(
        NICKNAME => $userArray[NICKNAME],
        TRUENAME => $userArray[TRUENAME],
        SEX => $userArray[SEX],
        AGE => $userArray[AGE],
        IMAGE => $userArray[IMAGE],
        SIGNATURE => $userArray[SIGNATURE],
        PHONE => $userArray[PHONE],
        TOKEN => $userArray[TOKEN]);

mysqli_close($con);
echo json_encode($response);


?>