<?php
    require_once 'user.php';    
    $userObject = new User();
    $json_array=$userObject->find_usernames();
?>