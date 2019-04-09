<?php 
 
    //Importing dbdetails file 
    require_once 'config.php';
    
    //connection to database 
    $con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME) or die('Unable to Connect...');

    $username="";
    $id_campo="";
    $nombre_hoyo="";


    if(isset($_POST['nombre_hoyo']) && isset($_POST['id_campo']))
    {
        $nombre_hoyo = $_POST['nombre_hoyo'];
        $id_campo = $_POST['id_campo'];

        $sql = "SELECT * FROM fotos where id_campo=$id_campo and nombre_hoyo='$nombre_hoyo' and isProfile=false";
    }
    else if(isset($_POST['id_campo']))
    {
        $id_campo = $_POST['id_campo'];

        $sql = "SELECT * FROM fotos where id_campo=$id_campo and isProfile=false";
    }
    else
    {
        $username = $_POST['username'];

        $sql = "SELECT * FROM fotos where username='$username' and id_campo is null and nombre_hoyo is null and isProfile=false";
    }

   
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

    ?>