<?php
require_once('ServerAPI.php');
require_once('Constant.php');
require_once('db_conn.php');
header("Content-type:text/html;charset=UTF-8");

/**
 * 获得客户端传来的数据，初始化要插入数据库的数据
 */
$qqnumber = $_POST[QQNUMBER];
$password = $_POST[PASSWORD];
$phone = $_POST[PHONE];
$imgPath = null;
$token = '';


if($_POST[IMAGE]!=null){
    $image = $_POST[IMAGE];
    $filename = date("YmdHis");
    $file = fopen($filename.".png","x");
    $data = base64_decode($image);
    fwrite($file,$data);
    fclose($file);
    $imgPath = 'http://'.HTTPURL.':82/htdocs/qq/'."$filename".".png";
}

$sp = new ServerAPI(APP_KEY,APP_SECRET);
$r = $sp->getToken($qqnumber,$qqnumber,$imgPath);//相当于java中的r = sp.getToken();返回了一个json格式的字符串
$obj = json_decode($r);

if($obj->code!=200)
{
    $response [SUCCESS] = 1;
}else{
    $token = $obj->token;
}

$result = mysqli_query($con,"INSERT INTO user(qqnumber,password,phone,token,image)
 VALUES('$qqnumber','$password','$phone','$token','$imgPath')");

if($result){
    $response [SUCCESS] = 0;
}else{
    $response [SUCCESS] = 1;
}
//数组形式(k,v)
$response[QQNUMBER]=$qqnumber;
$response[IMAGE]=$imgPath;
$response[TOKEN]=$token;
mysqli_close($con);
$arr = array($response);
echo json_encode ($arr);

?>