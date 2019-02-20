<?php
   
    require_once 'field.php';    
    $id_campo="";
    $username="";
    $valoracion="";

    if(isset($_POST['id_campo']))
    {
        $id_campo = $_POST['id_campo'];
    }
    if(isset($_POST['username']))
    {
        $username = $_POST['username'];
    }
    if(isset($_POST['valoracion']))
    {
        $valoracion = $_POST['valoracion'];
    }
   
    $fieldObject = new Field();
    
    // Registration    
    if(!empty($id_campo) && !empty($username) && !empty($valoracion))
    {  
        $json_registration = $fieldObject->createValoration($id_campo, $username, $valoracion);
        echo json_encode($json_registration);        
    }
?>