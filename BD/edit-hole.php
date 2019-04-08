<?php
   
    require_once 'hole.php'; 

    $id_campo = "";    
    $nombre_hoyo="";
    $descripcion="";
    $metros="";
    $par="";
    

    if(isset($_POST['id_campo']))
    {
        $id_campo = $_POST['id_campo'];
    }
    if(isset($_POST['nombre_hoyo']))
    {
        $nombre_hoyo = $_POST['nombre_hoyo'];
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
    
    
    $holeObject = new Hole();
    
    // Registration    
    if(!empty($id_campo) && !empty($nombre_hoyo) && !empty($descripcion) && !empty($metros) && !empty($par))
    {  
        $json_registration = $holeObject->editHole($id_campo, $nombre_hoyo, $descripcion, $metros, $par);
        echo json_encode($json_registration);        
    }

   
?>