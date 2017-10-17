<?php
/**
 * Created by PhpStorm.
 * User: wyj
 * Date: 2016/7/11
 * Time: 9:47
 */
require_once ('Constant.php');
require_once ('db_conn.php');
$qqnumber = $_POST[QQNUMBER];

$result = null;
$imgPath = null;

if(isset($_POST[IMAGE])){
    $image = $_POST[IMAGE];
    $filename = date("YmdHis");
    $file = fopen($filename.".png","x");
    $data = base64_decode($image);
    fwrite($file,$data);
    fclose($file);
    $imgPath = 'http://'.HTTPURL.':82/htdocs/qq/'."$filename".".png";
    $result = mysqli_query($con,"UPDATE user set image='$imgPath'WHERE qqnumber ='$qqnumber'");
}

if (isset($_POST[NICKNAME])) {
    $nickname = $_POST[NICKNAME];
    $result=mysqli_query($con,"UPDATE user set nickname='$nickname'WHERE qqnumber ='$qqnumber'");
}

if (isset($_POST[PASSWORD])) {
    $password = $_POST[PASSWORD];
    $result=mysqli_query($con,"UPDATE user set password='$password'WHERE qqnumber ='$qqnumber'");
    $response["arg"] = PASSWORD;
}

if (isset($_POST[PHONE])) {
    $phone = $_POST[PHONE];
    $result=mysqli_query($con,"UPDATE user set nickname='$phone'WHERE qqnumber ='$qqnumber'");
    $response["arg"] = $phone;
}

if (isset($_POST[SEX])) {
    $sex = $_POST[SEX];
    $result= mysqli_query($con,"UPDATE user set sex='$sex'WHERE qqnumber ='$qqnumber'");
}
if (isset($_POST[AGE])) {
    $age = $_POST[AGE];
    $result=mysqli_query($con,"UPDATE user set age='$age'WHERE qqnumber ='$qqnumber'");
}
if (isset($_POST[SIGNATURE])) {
    $signature = $_POST[SIGNATURE];
    $result=mysqli_query($con,"UPDATE user set signature='$signature'WHERE qqnumber ='$qqnumber'");
}

mysqli_close($con);
if($result){
    if(isset($_POST[IMAGE])){
        $response["success"] = 0;
        $response[IMAGE] = $imgPath;
    }
    $response["success"] = 0;
}else{
    $response["success"] = 1;
}
echo json_encode($response);

