<?php
   
    require_once 'user.php';    
    $nombre="";
    $apellido1="";
    $apellido2="";
    $username = "";    
    $password = ""; 
    $handicap = ""; 

    if(isset($_POST['nombre']))
    {
        $nombre = $_POST['nombre'];
    }
    if(isset($_POST['apellido1']))
    {
        $apellido1 = $_POST['apellido1'];
    }
    if(isset($_POST['apellido2']))
    {
        $apellido2 = $_POST['apellido2'];
    }
    if(isset($_POST['username']))
    {
        $username = $_POST['username'];
    }
    if(isset($_POST['password']))
    {   
        $password = $_POST['password'];   
    }
    if(isset($_POST['handicap']))
    {   
        $handicap = $_POST['handicap'];   
    }
    
    $userObject = new User();
    
    // Registration    
    if(!empty($nombre) && !empty($apellido1) && !empty($apellido2) && !empty($username) && !empty($password) && !empty($handicap))
    {  
        $json_registration = $userObject->createNewRegisterUser($nombre, $apellido1, $apellido2, $username, $password, $handicap);
        echo json_encode($json_registration);        
    }

    // Login    
    if(!empty($username) && !empty($password))
    {        
        $json_array = $userObject->loginUser($username, $password);        
        echo json_encode($json_array);
    }
?>