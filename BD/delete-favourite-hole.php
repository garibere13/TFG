<?php
   
    require_once 'hole.php';    
    $nombre_hoyo="Hoyo 6";
    $id_campo="3";
    $username="garibere13";

    if(isset($_POST['nombre_hoyo']))
    {
        $nombre_hoyo = $_POST['nombre_hoyo'];
    }
    if(isset($_POST['id_campo']))
    {
        $id_campo = $_POST['id_campo'];
    }
    if(isset($_POST['username']))
    {   
        $username = $_POST['username'];   
    }
    
    $holeObject = new Hole();
    
    // Registration    
    if(!empty($nombre_hoyo) && !empty($id_campo) && !empty($username))
    {  
        $json_registration = $holeObject->deleteFavouriteHole($nombre_hoyo, $id_campo, $username);
    }
?>