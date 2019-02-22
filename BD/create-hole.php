<?php
   
    require_once 'hole.php';    
    $nombre="";
    $id_campo="";
    $descripcion="";
    $metros="";    
    $par=""; 
    $creador="patxi";

    if(isset($_POST['nombre']))
    {
        $nombre = $_POST['nombre'];
    }
    if(isset($_POST['id_campo']))
    {
        $id_campo = $_POST['id_campo'];
    }
    if(isset($_POST['descripcion']))
    {
        $descripcion = $_POST['descripcion'];
    }
    if(isset($_POST['metros']))
    {
        $metros = $_POST['metros'];
    }
    if(isset($_POST['par']))
    {   
        $par = $_POST['par'];   
    }
    if(isset($_POST['creador']))
    {   
        $creador = $_POST['creador'];   
    }
    
    $holeObject = new Hole();
    
    // Registration    
    if(!empty($nombre) && !empty($id_campo) && !empty($descripcion) && !empty($metros) && !empty($par) && !empty($creador))
    {  
        $json_registration = $holeObject->createNewHole($nombre, $id_campo, $descripcion, $metros, $par, $creador);
    }
?>