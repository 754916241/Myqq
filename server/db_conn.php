<?php
/**
 * Created by PhpStorm.
 * User: wyj
 * Date: 2017/3/18
 * Time: 20:57
 */

header("Content-type:text/html;charset=utf-8");
$con = mysqli_connect('localhost','root','','myqq');
mysqli_query($con,"SET NAMES 'utf8'");

//$con = new Medoo([
//    'database_type' => 'mysql',
//    'database_name' => 'name',
//    'server' => 'localhost',
//    'username' => 'your_username',
//    'password' => 'your_password',
//    'port' => 3306,
//    'charset' => 'utf8',
//]);