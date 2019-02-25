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

        public function checkHoleExists($id_campo, $nombre)
        {            
            $json = array();            
            $holeExists = $this->doesHoleExist($id_campo, $nombre);            
            if($holeExists)
            {
                $json['success'] = 1;
                $json['message'] = "EZIN DA SORTU";
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
                mysqli_close($this->db->getDb());
                return true;
            }            
            mysqli_close($this->db->getDb());
            return false;
        }


        public function find_hole_data($id_campo, $nombre)
        {
            
            $data = array();
            $query = "SELECT h.nombre as nombre_hoyo, c.nombre as nombre_campo, h.descripcion, metros, par, c.creador, dayofmonth(h.fecha) as dia, month(h.fecha) as mes, year(h.fecha) as anyo FROM hoyos h, campos c WHERE h.id_campo=$id_campo and h.nombre='$nombre' and h.id_campo = c.id";
            
            
           if($stmt = mysqli_query($this->db->getDb(), $query))
            {
                while($row = mysqli_fetch_assoc($stmt))
                {  
                    $temp = 
                    [
                        'nombre_hoyo'=>$row['nombre_hoyo'],
                        'nombre_campo'=>$row['nombre_campo'],
                        'descripcion'=>$row['descripcion'],
                        'metros'=>$row['metros'],
                        'par'=>$row['par'],
                        'creador'=>$row['creador'],
                        'anyo'=>$row['anyo'],
                        'mes'=>$row['mes'],
                        'dia'=>$row['dia']
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


        public function find_user_favourites($username)
        {
            
            $data = array();
            $query = "SELECT f.nombre_hoyo, f.id_campo, c.nombre as nombre_campo, f.username FROM favoritos f, campos c WHERE f.username='$username' and f.id_campo=c.id";
            
            
           if($stmt = mysqli_query($this->db->getDb(), $query))
            {
                while($row = mysqli_fetch_assoc($stmt))
                {  
                    $temp = 
                    [
                        'nombre_hoyo'=>$row['nombre_hoyo'],
                        'id_campo'=>$row['id_campo'],
                        'nombre_campo'=>$row['nombre_campo']
                    ];
                        array_push($data, $temp);
                }     
                echo json_encode($data);   
            }
            mysqli_close($this->db->getDb());         
        }
    }
?>