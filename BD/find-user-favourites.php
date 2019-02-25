<?php
    require_once 'hole.php';  
    
    $username=""; 

    if(isset($_POST['username']))
    {
        $username = $_POST['username'];
    }
    $holeObject = new Hole();

    if(!empty($username))
    {  
        $json_array=$holeObject->find_user_favourites($username);
    }
?>