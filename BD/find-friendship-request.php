<?php
    require_once 'user.php';  
    
    $username = "";    

    if(isset($_POST['username']))
    {
        $username = $_POST['username'];
    }
    $userObject = new User();
    

    if(!empty($username))
    {  
        $json_array=$userObject->find_friendship_request($username);
        echo $json_array;
    }
?>