<?php
require_once('Constant.php');
$response['success'] = 0;
$response[NICKNAME] = $userArray[NICKNAME];
$response[IMAGE] = $userArray[IMAGE];

echo json_encode($response);
?>

