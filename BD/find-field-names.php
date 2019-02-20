<?php
    require_once 'field.php';    
    $fieldObject = new Field();
    $json_array=$fieldObject->find_field_names();
?>