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
            $query = "insert into ".$this->db_table." (nombre, id_campo, descripcion, metros, par, creador) values ('$nombre', $id_campo, '$descripcion', $metros, $par, '$creador')";
           
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
            $query = "SELECT h.nombre as nombre_hoyo, c.nombre as nombre_campo, h.descripcion, metros, par, c.creador FROM hoyos h, campos c WHERE h.id_campo=$id_campo and h.nombre='$nombre' and h.id_campo = c.id";
            
            
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
                        'creador'=>$row['creador']
                    ];
                        array_push($data, $temp);
                }     
                echo json_encode($data);   
            }
            mysqli_close($this->db->getDb());         
        }
    }
?>