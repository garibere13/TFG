<?php 
 
    //Importing dbdetails file 
    require_once 'config.php';
    
    //connection to database 
    $con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME) or die('Unable to Connect...');
    
    //sql query to fetch all images 
    $sql = "SELECT * FROM fotos";
    
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
                'name'=>$row['name'],
                'url'=>$row['url']
            ];
        array_push($response, $temp);
    }
    //displaying the response 
    echo json_encode($response);