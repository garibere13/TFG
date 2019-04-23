<?php
    require_once 'user.php';  
    
    $username = "";
    $following_followers = "";    

    if(isset($_POST['username']))
    {
        $username = $_POST['username'];
    }
    if(isset($_POST['following_followers']))
    {
        $following_followers = $_POST['following_followers'];
    }
    $userObject = new User();
    

    if(!empty($username) && !empty($following_followers))
    {  
        $json_array=$userObject->find_following_followers($username, $following_followers);
        echo $json_array;
    }
?>