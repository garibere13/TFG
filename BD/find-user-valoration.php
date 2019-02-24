<?php
    require_once 'field.php';  
    
    $id_campo="";
    $username=""; 

    if(isset($_POST['id_campo']))
    {
        $id_campo = $_POST['id_campo'];
    }
    if(isset($_POST['username']))
    {
        $username = $_POST['username'];
    }
    $fieldObject = new Field();

    if(!empty($id_campo) && !empty($username))
    {  
        $json_array=$fieldObject->find_user_valoration($id_campo, $username);
    }
?>