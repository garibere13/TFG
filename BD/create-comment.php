<?php
   
    require_once 'hole.php';    
    $comentario="";
    $id_campo="";
    $nombre_hoyo="";
    $username="";

    if(isset($_POST['comentario']))
    {
        $comentario = $_POST['comentario'];
    }
    if(isset($_POST['id_campo']))
    {
        $id_campo = $_POST['id_campo'];
    }
    if(isset($_POST['nombre_hoyo']))
    {
        $nombre_hoyo = $_POST['nombre_hoyo'];
    }
    if(isset($_POST['username']))
    {
        $username = $_POST['username'];
    }
    
    
    $holeObject = new Hole();
    
    // Registration    
    if(!empty($comentario) && !empty($id_campo) && !empty($nombre_hoyo) && !empty($username))
    {  
        $json_registration = $holeObject->createNewComment($comentario, $id_campo, $nombre_hoyo, $username);
    }
?>