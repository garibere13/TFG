<?php 
 
    //Importing dbdetails file 
    require_once 'config.php';
    
    //connection to database 
    $con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME) or die('Unable to Connect...');

    $username="";
    $id_campo="";

    if(isset($_POST['username']))
    {
        $username = $_POST['username'];
    }

    if(isset($_POST['id_campo']))
    {
        $id_campo = $_POST['id_campo'];
    }
    
    //sql query to fetch all images 
    $sql = "SELECT * FROM fotos where username='$username' and id_campo=$id_campo";
    //$sql = "SELECT * FROM fotos";
    
    //getting images 
    $result = mysqli_query($con, $sql);
    
    //response array 
    $response = array(); 

    
    //traversing through all the rows 
    while($row = mysqli_fetch_array($result))
    {       
        $temp = 
            [
                'id'=>$row['id'],
                'comentario'=>$row['comentario'],
                'url'=>$row['url']
            ];
        array_push($response, $temp);
    }
    //displaying the response 
    echo json_encode($response);