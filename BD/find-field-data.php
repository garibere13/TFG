<?php
    require_once 'field.php';  
    
    $id="";

    if(isset($_POST['id']))
    {
        $id = $_POST['id'];
    }
   
    $fieldObject = new Field();

    if(!empty($id))
    {  
        $json_array=$fieldObject->find_field_data($id);
    }
?>