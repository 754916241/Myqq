<?php
require_once('ServerAPI.php');
require_once('Constant.php');
require_once('db_conn.php');
header("Content-type:text/html;charset=UTF-8");

$sp = new ServerAPI(APP_KEY,APP_SECRET);
$r = $sp->getToken('895980035','超神','http://192.168.0.108:82/htdocs/qq/hc.png');//相当于java中的r = sp.getToken();返回了一个json格式的字符串
$obj = json_decode($r);

if($obj->code!=200)
{
    $response [SUCCESS] = 1;
}else{
    $token = $obj->token;
}

$result = mysqli_query($con,"INSERT INTO user(qqnumber,password,phone,token,image)
 VALUES('895980035','123456','13354242167','$token','http://192.168.0.108:82/htdocs/qq/hc.png')");

if($result){
    $response [SUCCESS] = 0;
}else{
    $response [SUCCESS] = 1;
}
mysqli_close($con);
echo $response[SUCCESS];
?>