<?php
    require_once 'field.php';  
    
    $username="";
    $latitud="";
    $longitud="";

    if(isset($_POST['username']))
    {
        $username = $_POST['username'];
    }
    if(isset($_POST['latitud']))
    {
        $latitud = $_POST['latitud'];
    }
    if(isset($_POST['longitud']))
    {
        $longitud = $_POST['longitud'];
    }
   
    $fieldObject = new Field();

    if(!empty($username) && !empty($latitud) && !empty($longitud))
    {  
        $json_array=$fieldObject->open_field($username, $latitud, $longitud);
    }
?>