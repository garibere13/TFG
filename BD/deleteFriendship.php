<?php
   
    require_once 'user.php';    
    $tu="";
    $username = "";    

    if(isset($_POST['tu']))
    {
        $tu = $_POST['tu'];
    }
    if(isset($_POST['username']))
    {
        $username = $_POST['username'];
    }
    
    $userObject = new User();    

    // Login    
    if(!empty($tu) && !empty($username))
    {        
        $json_array = $userObject->deleteFriendship($tu, $username);        
        echo json_encode($json_array);
    }
?>