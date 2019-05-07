<?php
    
    include_once 'db-connect.php';
    
    class Hole
    {        
        private $db;  
        private $db_table = "hoyos";

        
        public function __construct()
        {
            $this->db = new DbConnect();
        }

        public function createNewHole($nombre, $id_campo, $descripcion, $metros, $par, $creador)
        {                
            $query = "insert into ".$this->db_table." (nombre, id_campo, descripcion, metros, par, creador, fecha) values ('$nombre', $id_campo, '$descripcion', $metros, $par, '$creador', CURDATE())";
           
            $inserted = mysqli_query($this->db->getDb(), $query);
                
                if($inserted == 1)
                {
                    $json['success'] = 1;
                    $json['mensaje'] = "Hoyo registrado!";                    
                }
                else
                {                    
                    $json['success'] = 0;
                    $json['mensaje'] = "No se ha podido registrar el hoyo";
                }                
                mysqli_close($this->db->getDb());            
            return $json;
        }


        public function createNewComment($comentario, $id_campo, $nombre_hoyo, $username)
        {                
            
            $query = "insert into comentarios (comentario, id_campo, nombre_hoyo, username, fecha) values ('$comentario', $id_campo, '$nombre_hoyo', '$username', NOW())";
           
            $inserted = mysqli_query($this->db->getDb(), $query);

                if($inserted == 1)
                {
                    $json['success'] = 1;
                    $json['mensaje'] = "Comentario registrado!";                    
                }
                else
                {  
                    $json['success'] = 0;
                    $json['mensaje'] = "No se ha podido registrar el comentario";
                }                
                mysqli_close($this->db->getDb());            
            return $json;
        }

        public function checkHoleExists($id_campo, $nombre)
        {            
            $json = array();            
            $holeExists = $this->doesHoleExist($id_campo, $nombre);            
            if($holeExists)
            {
                $json['success'] = 1;
                $json['message'] = "EZIN DA SORTU";
      
                $query = "select creador from ".$this->db_table." where nombre = '$nombre' AND id_campo = $id_campo Limit 1";
                $creador = mysqli_query($this->db->getDb(), $query);
                while($row = mysqli_fetch_assoc($creador))
                {
                    $json['creador'] = $row['creador'];
                }
                mysqli_close($this->db->getDb());                
            }
            else
            {
                $json['success'] = 0;
                $json['message'] = "SORTU DAITEKE";
            }
            return $json;
        }

        public function doesHoleExist($id_campo, $nombre)
        {
            $query = "select * from ".$this->db_table." where nombre = '$nombre' AND id_campo = $id_campo Limit 1";
            $result = mysqli_query($this->db->getDb(), $query);            
            if(mysqli_num_rows($result) > 0)
            {                
                
                return true;
            }            
            mysqli_close($this->db->getDb());
            return false;
        }


        public function find_hole_data($id_campo, $nombre)
        {
            
            $data = array();

            $query = "SELECT f1.url as url FROM fotos f1 WHERE f1.id_campo=$id_campo and f1.nombre_hoyo='$nombre' and f1.isProfile=true Limit 1";
            $result = mysqli_query($this->db->getDb(), $query);  
            if(mysqli_num_rows($result) > 0)
            {                

                $query = "SELECT f.url as url, (SELECT count(f.id_campo) as num_fotos FROM fotos f WHERE f.id_campo=$id_campo and f.nombre_hoyo='$nombre' and f.isProfile=false Limit 1) as num_fotos, h.nombre as nombre_hoyo, h.id_campo, c.nombre as nombre_campo, h.descripcion, metros, par, h.creador, dayofmonth(h.fecha) as dia, month(h.fecha) as mes, year(h.fecha) as anyo 

                FROM fotos f, hoyos h, campos c
                
                WHERE h.id_campo=$id_campo and h.nombre='$nombre' and h.id_campo = c.id 
                and url= (SELECT f1.url as url FROM fotos f1 WHERE f1.id_campo=$id_campo and f1.nombre_hoyo='$nombre' and f1.isProfile=true Limit 1)
                
                Limit 1";
            }
            else
            {
                $query = "SELECT null as url, (SELECT count(f.id_campo) as num_fotos FROM fotos f WHERE f.id_campo=$id_campo and f.nombre_hoyo='$nombre' and f.isProfile=false Limit 1) as num_fotos, h.nombre as nombre_hoyo, h.id_campo, c.nombre as nombre_campo, h.descripcion, metros, par, h.creador, dayofmonth(h.fecha) as dia, month(h.fecha) as mes, year(h.fecha) as anyo 

                FROM fotos f, hoyos h, campos c
                
                WHERE h.id_campo=$id_campo and h.nombre='$nombre' and h.id_campo = c.id 
                
                Limit 1";
            }
                        
           if($stmt = mysqli_query($this->db->getDb(), $query))
            {
                while($row = mysqli_fetch_assoc($stmt))
                {  
                    $temp = 
                    [
                        'nombre_hoyo'=>$row['nombre_hoyo'],
                        'id_campo'=>$row['id_campo'],
                        'nombre_campo'=>$row['nombre_campo'],
                        'descripcion'=>$row['descripcion'],
                        'metros'=>$row['metros'],
                        'par'=>$row['par'],
                        'creador'=>$row['creador'],
                        'anyo'=>$row['anyo'],
                        'mes'=>$row['mes'],
                        'dia'=>$row['dia'],
                        'num_fotos'=>$row['num_fotos'],
                        'url'=>$row['url']
                    ];
                        array_push($data, $temp);
                }     
                echo json_encode($data);   
            }
            mysqli_close($this->db->getDb());         
        }

        public function createFavouriteHole($nombre_hoyo, $id_campo, $username)
        {                
            $query = "insert into favoritos (nombre_hoyo, id_campo, username) values ('$nombre_hoyo', $id_campo, '$username')";
           
            $inserted = mysqli_query($this->db->getDb(), $query);
                
                if($inserted == 1)
                {
                    $json['success'] = 1;
                    $json['mensaje'] = "Añadido a favoritos";                    
                }
                else
                {                    
                    $json['success'] = 0;
                    $json['mensaje'] = "No se ha podido añadir a favoritos";
                }                
                mysqli_close($this->db->getDb());            
            return $json;
        }

        public function createVote($id_comentario, $username)
        {                
            $query = "insert into votaciones (id_comentario, username) values ($id_comentario, '$username')";
           
            $inserted = mysqli_query($this->db->getDb(), $query);
                
                if($inserted == 1)
                {
                    $json['success'] = 1;
                    $json['mensaje'] = "Votación realizada"; 
                    
                    $query1 = "update comentarios set votos=votos+1 where id=$id_comentario";
                    $result1 = mysqli_query($this->db->getDb(), $query1);                 
                }
                else
                {                    
                    $json['success'] = 0;
                    $json['mensaje'] = "No se ha podido realizar la votación";
                }

                                 
                mysqli_close($this->db->getDb());            
            return $json;
        }

        public function deleteFavouriteHole($nombre_hoyo, $id_campo, $username)
        {                
            $query = "delete from favoritos where nombre_hoyo='$nombre_hoyo' and id_campo=$id_campo and username='$username'";
           
            $inserted = mysqli_query($this->db->getDb(), $query);
                
                if($inserted == 1)
                {
                    $json['success'] = 1;
                    $json['mensaje'] = "Quitado de favoritos";                    
                }
                else
                {                    
                    $json['success'] = 0;
                    $json['mensaje'] = "No se ha podido quitar de favoritos";
                }                
                mysqli_close($this->db->getDb());            
            return $json;
        }


        public function deleteVote($id_comentario, $username)
        {                
            $query = "delete from votaciones where id_comentario=$id_comentario and username='$username'";
           
            $inserted = mysqli_query($this->db->getDb(), $query);
                
                if($inserted == 1)
                {
                    $json['success'] = 1;
                    $json['mensaje'] = "Voto eliminado";     
                    
                    $query1 = "update comentarios set votos=votos-1 where id=$id_comentario";
                    $result1 = mysqli_query($this->db->getDb(), $query1); 
                }
                else
                {                    
                    $json['success'] = 0;
                    $json['mensaje'] = "No se ha podido eliminar el voto";
                }                
                mysqli_close($this->db->getDb());            
            return $json;
        }



        public function find_is_hole_user_favourite($nombre_hoyo, $id_campo, $username)
        {
                   
            $exists = $this->isHoleUserFavourite($nombre_hoyo, $id_campo, $username);            
            if($exists)
            {
                $data = array();
                $query = "SELECT * FROM favoritos WHERE nombre_hoyo='$nombre_hoyo' and id_campo=$id_campo and username='$username' Limit 1";

                if($stmt = mysqli_query($this->db->getDb(), $query))
                {        
                    while($row = mysqli_fetch_assoc($stmt))
                    {  
                        $temp = 
                        [
                            'existe'=>true
                        ];
                            array_push($data, $temp);
                    }     
                    echo json_encode($data);   
                }
                mysqli_close($this->db->getDb());  
            }        
        }

        public function isHoleUserFavourite($nombre_hoyo, $id_campo, $username)
        {
           $query = "SELECT * FROM favoritos WHERE nombre_hoyo='$nombre_hoyo' and id_campo=$id_campo and username='$username' Limit 1";
           $result = mysqli_query($this->db->getDb(), $query);            
            if(mysqli_num_rows($result) > 0)
            {                
                return true;
            }            
            return false;
        }

        public function editHole($id_campo, $nombre_hoyo, $descripcion, $metros, $par)
        {                
            $query = "UPDATE ".$this->db_table." SET descripcion='$descripcion', metros=$metros, par=$par WHERE id_campo=$id_campo and nombre='$nombre_hoyo'";
            $inserted = mysqli_query($this->db->getDb(), $query);
               
                if($inserted == 1)
                {
                    $json['success'] = 1;
                    $json['message'] = "Hoyo modificado!";                    
                }
                else
                {                    
                    $json['success'] = 0;
                    $json['message'] = "No se han podido realizar los cambios";
                }                
                mysqli_close($this->db->getDb());            
            return $json;
        }


        public function find_user_favourites($username)
        {
            
            $data = array();
            $query = "SELECT f.nombre_hoyo, f.id_campo, c.nombre as nombre_campo, f.username, h.creador

            FROM favoritos f, campos c, hoyos h
            
            WHERE f.username='$username' 
            and f.id_campo=c.id
            and h.nombre=f.nombre_hoyo
            and h.id_campo=f.id_campo";

           //$resultado_final=array();

            
           if($stmt = mysqli_query($this->db->getDb(), $query))
            {
                while($row = mysqli_fetch_assoc($stmt))
                {
                
                    $aux_id_campo=$row['id_campo'];
                    $aux_nombre_hoyo=$row['nombre_hoyo'];
                    $aux_nombre_campo=$row['nombre_campo'];
                    $query3 = "SELECT url from fotos where id_campo=$aux_id_campo and nombre_hoyo='$aux_nombre_hoyo' and isProfile=true limit 1";
                    $result3 = mysqli_query($this->db->getDb(), $query3);  
                
                
                            if(mysqli_num_rows($result3) > 0)
                            {
                                if($stmt3 = mysqli_query($this->db->getDb(), $query3))
                                {
    
                                    while($row3 = mysqli_fetch_assoc($stmt3))
                                    { 
                                        $url=$row3['url'];
                                        $temp = 
                                        [
                                            'nombre_hoyo'=>$row['nombre_hoyo'],
                                            'id_campo'=>$row['id_campo'],
                                            'nombre_campo'=>$row['nombre_campo'],
                                            'creador'=>$row['creador'], 
                                            'url'=>$url
                                        ];
                                            array_push($data, $temp);
                                    }  
                                }
                            }
                            else
                            {
                                $temp = 
                                        [
                                            'nombre_hoyo'=>$row['nombre_hoyo'],
                                            'id_campo'=>$row['id_campo'],
                                            'nombre_campo'=>$row['nombre_campo'],
                                            'creador'=>$row['creador'], 
                                            'url'=>null
                                        ];
                                        array_push($data, $temp);
                            }                                
                }   
                echo json_encode($data);   
            }
            mysqli_close($this->db->getDb());         
        }



        public function find_hole_comments($id_campo, $nombre_hoyo, $username)
        {          
            
            $query1 = "SELECT id, username, fecha, comentario, votos 
            FROM comentarios
            WHERE id_campo=$id_campo
            and nombre_hoyo='$nombre_hoyo'
            order by votos desc";
            $result1 = mysqli_query($this->db->getDb(), $query1);           

            //me recorro todos los usuarios que han comentado
            $resultado_final=array();
           if($stmt1 = mysqli_query($this->db->getDb(), $query1))
            {
                //$i=0;
                while($row = mysqli_fetch_assoc($stmt1))
                {  
                   //hacer busqueda para saber si tiene foto de perfil, si no, le poenmos null

                   $aux=$row['username'];
                   $query3 = "SELECT url from fotos where username='$aux' and isProfile=true limit 1";
                   $result3 = mysqli_query($this->db->getDb(), $query3);  


                   if(mysqli_num_rows($result3) > 0)
                   {
                            if($stmt3 = mysqli_query($this->db->getDb(), $query3))
                            {

                                while($row3 = mysqli_fetch_assoc($stmt3))
                                { 
                                    $aux1=$row['id'];
                                    $query = "SELECT id_comentario, username FROM votaciones WHERE username='$username' and id_comentario=$aux1 Limit 1";
                                    $result = mysqli_query($this->db->getDb(), $query);            
                                        if(mysqli_num_rows($result) > 0)
                                        { 
                                            $temp = 
                                            [
                                                               
                                                        'id'=>$row['id'],
                                                        'username'=>$row['username'],
                                                        'comentario'=>$row['comentario'],
                                                        'fecha'=>$row['fecha'],
                                                        'votos'=>$row['votos'],
                                                        'url'=>$row3['url'],
                                                        'like'=>'bai'
                                            ];
                                        }
                                        else
                                        {
                                            $temp = 
                                            [
                                                               
                                                        'id'=>$row['id'],
                                                        'username'=>$row['username'],
                                                        'comentario'=>$row['comentario'],
                                                        'fecha'=>$row['fecha'],
                                                        'votos'=>$row['votos'],
                                                        'url'=>$row3['url'],
                                                        'like'=>'ez'
                                            ];
                                        }
                                    array_push($resultado_final, $temp);                      
                                    
                                }

                            }
                    }
                    else
                    {

                        $aux1=$row['id'];
                        $query = "SELECT id_comentario, username FROM votaciones WHERE username='$username' and id_comentario=$aux1 Limit 1";
                        $result = mysqli_query($this->db->getDb(), $query);

                        if(mysqli_num_rows($result) > 0)
                        {
                             $temp = 
                            [
                                'id'=>$row['id'],
                                'username'=>$row['username'],
                                'comentario'=>$row['comentario'],
                                'fecha'=>$row['fecha'],
                                'votos'=>$row['votos'],
                                'url'=>null,
                                'like'=>'bai'
                            ];
                        }
                        else
                        {
                            $temp = 
                            [
                                'id'=>$row['id'],
                                'username'=>$row['username'],
                                'comentario'=>$row['comentario'],
                                'fecha'=>$row['fecha'],
                                'votos'=>$row['votos'],
                                'url'=>null,
                                'like'=>'ez'
                            ];
                        }
                       
                        array_push($resultado_final, $temp);                  
                    }

                } 
                echo json_encode($resultado_final);   
            }
            mysqli_close($this->db->getDb());         
        }
    }
?>