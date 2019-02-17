<?php
   
    require_once 'field.php';    
    $nombre="";
    $descripcion="";
    $num_hoyos="";
    $cod_pueblo = "";    
    $latitud = ""; 
    $longitud="";
    $username="";

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
    if(isset($_POST['cod_pueblo']))
    {
        $cod_pueblo = $_POST['cod_pueblo'];
    }
    if(isset($_POST['latitud']))
    {   
        $latitud = $_POST['latitud'];   
    }
    if(isset($_POST['longitud']))
    {   
        $longitud = $_POST['longitud'];   
    }
    if(isset($_POST['username']))
    {   
        $username = $_POST['username'];   
    }
    
    $fieldObject = new Field();
    
    // Registration    
    if(!empty($nombre) && !empty($descripcion) && !empty($num_hoyos) && !empty($cod_pueblo) && !empty($latitud) && !empty($longitud) && !empty($username))
    {  
        $json_registration = $fieldObject->createNewField($nombre, $descripcion, $num_hoyos, $cod_pueblo, $latitud, $longitud, $username);
    }
?>