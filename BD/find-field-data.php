<?php
    require_once 'field.php';  
    
    $id="4";
    $username="garibere13"; 

    if(isset($_POST['id']))
    {
        $id = $_POST['id'];
    }
    if(isset($_POST['username']))
    {
        $username = $_POST['username'];
    }
    $fieldObject = new Field();

    if(!empty($id) && !empty($username))
    {  
        $json_array=$fieldObject->find_field_data($id, $username);
    }
?>