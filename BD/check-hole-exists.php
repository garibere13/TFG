<?php
   
    require_once 'hole.php';    
    $id_campo="";
    $nombre="";

    
    if(isset($_POST['id_campo']))
    {
        $id_campo = $_POST['id_campo'];
    }
    if(isset($_POST['nombre']))
    {
        $nombre = $_POST['nombre'];
    }
    
    
    $holeObject = new Hole();
    
    // Checking    
    if(!empty($id_campo) && !empty($nombre))
    {  
        $json_registration = $holeObject->checkHoleExists($id_campo, $nombre);
        echo json_encode($json_registration);        
    }
?>