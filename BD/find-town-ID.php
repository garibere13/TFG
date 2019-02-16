<?php
    require_once 'field.php';  
    
    $nombre = ""; 

    if(isset($_POST['nombre']))
    {
        $nombre = $_POST['nombre'];
    }
    $fieldObject = new Field();

    if(!empty($nombre))
    {  
        $json_array=$fieldObject->find_town_ID($nombre);
    }
?>