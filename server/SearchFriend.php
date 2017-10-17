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
    $dataCount = mysqli_num_rows($result);
    if($dataCount == 1){
        $userArray = mysqli_fetch_array($result);
        $response = array(SUCCESS => 0,
            NICKNAME => $userArray[NICKNAME],
            IMAGE=>$userArray[IMAGE]);

    }else{
        $response['success'] = 1;
    }

mysqli_close($con);
echo json_encode($response);


?>