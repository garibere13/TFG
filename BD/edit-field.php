<?php
   
    require_once 'field.php'; 

    $id = "";    
    $nombre="";
    $descripcion="";
    $num_hoyos="";
    

    if(isset($_POST['id']))
    {
        $id = $_POST['id'];
    }
    if(isset($_POST['nombre']))
    {
        $nombre = $_POST['nombre'];
    }
    if(isset($_POST['descripcion']))
    {
        $descripcion = $_POST['descripcion'];
    }
    if(isset($_POST['num_hoyos']))
    {
        $num_hoyos = $_POST['num_hoyos'];
    }
    
    
    $fieldObject = new Field();
    
    // Registration    
    if(!empty($id) && !empty($nombre) && !empty($descripcion) && !empty($num_hoyos))
    {  
        $json_registration = $fieldObject->editField($id, $nombre, $descripcion, $num_hoyos);
        echo json_encode($json_registration);        
    }

   
?>