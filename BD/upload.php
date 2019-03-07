<?php 
 
        //importing dbDetails file 
        include_once 'config.php';        
 
        //this is our upload folder 
        $upload_path = 'uploads/';
        
        //Getting the server ip 
        $server_ip = gethostbyname(gethostname());
        
        //creating the upload url 
        $upload_url = 'http://'.$server_ip.'/TFG/BD/'.$upload_path; 
        
        //response array 
        $response = array(); 
 
 
    if($_SERVER['REQUEST_METHOD']=='POST')
    {
 
        //checking the required parameters from the request 
        if(isset($_POST['comentario']) and isset($_POST['username']) and isset($_FILES['image']['name']))
        {
        
                //connecting to the database                 
                $con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME) or die('Unable to Connect...');
                
                //getting comment from the request 
                $comentario = $_POST['comentario'];
                $username = $_POST['username'];

                if(isset($_POST['id_campo']))
                {
                    $id_campo = $_POST['id_campo'];
                }

                if(isset($_POST['nombre_hoyo']))
                {
                    $nombre_hoyo = $_POST['nombre_hoyo'];
                }
                
                //getting file info from the request 
                $fileinfo = pathinfo($_FILES['image']['name']);
                
                //getting the file extension 
                $extension = $fileinfo['extension'];
                
                //file url to store in the database 
                $file_url = $upload_url . getFileName() . '.' . $extension;
                
                //file path to upload in the server 
                $file_path = $upload_path . getFileName() . '.'. $extension; 

                //trying to save the file in the directory 
                try
                {
                    //saving the file 
                    move_uploaded_file($_FILES['image']['tmp_name'], $file_path);
                    
                    
                    if(isset($_POST['nombre_hoyo']))
                    {
                        $sql = "INSERT INTO fotos (id, url, comentario, username, id_campo, nombre_hoyo) VALUES (NULL, '$file_url', '$comentario', '$username', $id_campo, '$nombre_hoyo');";
                        if(mysqli_query($con, $sql))
                            {
                                //filling response array with values 
                                $response['error'] = false; 
                                $response['url'] = $file_url; 
                                $response['comentario'] = $comentario;
                                $response['username'] = $username;
                                $response['id_campo'] = $id_campo;
                                $response['nombre_hoyo'] = $nombre_hoyo;
                            }
                    }

                    else if(isset($_POST['id_campo']))
                    {
                        $sql = "INSERT INTO fotos (id, url, comentario, username, id_campo) VALUES (NULL, '$file_url', '$comentario', '$username', $id_campo);";
                        if(mysqli_query($con, $sql))
                        {
                            //filling response array with values 
                            $response['error'] = false; 
                            $response['url'] = $file_url; 
                            $response['comentario'] = $comentario;
                            $response['username'] = $username;
                            $response['id_campo'] = $id_campo;
                        }
                    
                    }

                    else
                    {
                        $sql = "INSERT INTO fotos (id, url, comentario, username) VALUES (NULL, '$file_url', '$comentario', '$username');";
                        if(mysqli_query($con, $sql))
                            {
                                //filling response array with values 
                                $response['error'] = false; 
                                $response['url'] = $file_url; 
                                $response['comentario'] = $comentario;
                                $response['username'] = $username;
                            }
                    }                            
         
                }
                catch(Exception $e)
                {
                    $response['error']=true;
                    $response['message']=$e->getMessage();
                } 
        //displaying the response 
        echo json_encode($response);
        
        mysqli_close($con);
        }
        else
        {
            $response['error']=true;
            $response['message']='Please choose a file';
        }
    }
 

 function getFileName()
 {
    $con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME) or die('Unable to Connect...');
    $sql = "SELECT max(id) as id FROM fotos";
    $result = mysqli_fetch_array(mysqli_query($con, $sql));
    
    mysqli_close($con);
    if($result['id']==null)
    return 1; 
    else 
    return ++$result['id']; 
}

 ?>