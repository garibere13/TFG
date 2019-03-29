<?php 
 
    //Importing dbdetails file 
    require_once 'config.php';
    
    //connection to database 
    $con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME) or die('Unable to Connect...');

    $username="";


        //$username = $_POST['username'];

     /*   $sql = "SELECT * FROM fotos_perfil where username='$username' limit 1";
    

   
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
                'url'=>$row['url']
            ];
        array_push($response, $temp);
    }
    //displaying the response 
    echo json_encode($response);*/

    $sql = "SELECT * FROM fotos_perfil where username='garibere13' limit 1"; 

$result1 = mysqli_query($con, $sql);

$json = array();

if(mysqli_num_rows($result1))
{
    while($row=mysqli_fetch_assoc($result1))
    {
        //$json['fotos_perfil'][]=$row;

        $temp = 
            [
                'id'=>$row['id'],
                'url'=>$row['url']
            ];
        array_push($json, $temp);
    }
}


mysqli_close($con);
echo json_encode($json);

    ?>