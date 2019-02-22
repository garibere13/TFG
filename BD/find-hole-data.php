<?php
    require_once 'hole.php';  
    
    $id_campo=""; 
    $nombre="";

    if(isset($_POST['id_campo']))
    {
        $id_campo=$_POST['id_campo'];
    }
    if(isset($_POST['nombre']))
    {
        $nombre = $_POST['nombre'];
    }
    $holeObject = new Hole();

    if(!empty($id_campo) && !empty($nombre))
    {  
        $json_array=$holeObject->find_hole_data($id_campo, $nombre);
    }
?>