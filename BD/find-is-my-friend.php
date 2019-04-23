<?php
    require_once 'user.php';  
    
    $tu="";
    $username=""; 


    if(isset($_POST['tu']))
    {
        $tu = $_POST['tu'];
    }
    if(isset($_POST['username']))
    {
        $username = $_POST['username'];
    }
    $userObject = new User();

    if(!empty($tu) && !empty($username))
    {  
        $json_array=$userObject->find_is_my_friend($tu, $username);
    }
?>