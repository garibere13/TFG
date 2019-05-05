<?php
   
    require_once 'hole.php';    
    $id_comentario="";
    $username="";

    if(isset($_POST['id_comentario']))
    {
        $id_comentario = $_POST['id_comentario'];
    }
    if(isset($_POST['username']))
    {   
        $username = $_POST['username'];   
    }
    
    $holeObject = new Hole();
    
    // Registration    
    if(!empty($id_comentario) && !empty($username))
    {  
        $json_registration = $holeObject->deleteVote($id_comentario, $username);
    }
?>