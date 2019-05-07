<?php
    require_once 'hole.php';  
     
    
    $id_campo=""; 
    $num_hoyos="";

    
    if(isset($_POST['id_campo']))
    {
        $id_campo=$_POST['id_campo'];
    }
    if(isset($_POST['num_hoyos']))
    {
        $num_hoyos = $_POST['num_hoyos'];
    }
    
    $holeObject = new Hole();

    if(!empty($id_campo) && !empty($num_hoyos))
    {  
        $json_array=$holeObject->check_hole_information($id_campo, $num_hoyos);
    }
?>