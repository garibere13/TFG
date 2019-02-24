<?php
   
    require_once 'field.php';    
    $id_campo="3";
    $username="garibere13";
    $valoracion="3";

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
        $json_registration = $fieldObject->renewValoration($id_campo, $username, $valoracion);
        echo json_encode($json_registration);        
    }
?>