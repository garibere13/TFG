<?php
    require_once 'hole.php';  
     
    $nombre_hoyo="";
    $id_campo=""; 
   

    if(isset($_POST['nombre_hoyo']))
    {
        $nombre_hoyo = $_POST['nombre_hoyo'];
    }
    if(isset($_POST['id_campo']))
    {
        $id_campo=$_POST['id_campo'];
    }
    
    $holeObject = new Hole();

    if(!empty($id_campo) && !empty($nombre_hoyo))
    {  
        $json_array=$holeObject->find_hole_data($id_campo, $nombre_hoyo);
    }
?>