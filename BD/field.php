<?php
    
    include_once 'db-connect.php';
    
    class Field
    {        
        private $db;  
        private $db_table = "campos";

        
        public function __construct()
        {
            $this->db = new DbConnect();
        }

        public function find_towns()
        {
            $towns = array();
            $query = "SELECT nombre FROM municipios";
            $result=mysqli_query($this->db->getDb(), $query);

            if($stmt = $result)
            {        
                while($row = mysqli_fetch_assoc($stmt))
                { 
                    $mal=$row['nombre'];
                    $bien=$this->normaliza($mal);
                    $temp = 
                    [
                        'nombre'=>$bien
                    ];
                    array_push($towns, $temp);
                }
                echo json_encode($towns); 
            }
            mysqli_close($this->db->getDb());    
        }

        public function find_town_ID($nombre)
        {
            $data = array();
            $query = "SELECT id_municipio from municipios where nombre='$nombre' Limit 1";
            if($stmt = mysqli_query($this->db->getDb(), $query))
            {        
                while($row = mysqli_fetch_assoc($stmt))
                {  
                    $temp = 
                    [
                        'ID'=>$row['id_municipio']
                    ];
                        array_push($data, $temp);
                }     
                echo json_encode($data);   
            }
            mysqli_close($this->db->getDb());           
        }

        public function createNewField($nombre, $descripcion, $num_hoyos, $cod_pueblo, $latitud, $longitud, $username)
        {                
            $query = "insert into ".$this->db_table." (nombre, descripcion, num_hoyos, cod_pueblo, latitud, longitud, username) values ('$nombre', '$descripcion', '$num_hoyos', '$cod_pueblo', '$latitud', '$longitud', '$username')";
            $inserted = mysqli_query($this->db->getDb(), $query);
                
                if($inserted == 1)
                {
                    $json['success'] = 1;
                    $json['message'] = "Campo registrado!";                    
                }
                else
                {                    
                    $json['success'] = 0;
                    $json['message'] = "No se ha podido registrar el campo";
                }                
                mysqli_close($this->db->getDb());            
            return $json;
        }

        
        public function normaliza ($cadena)
        {
        $originales = 'ÀÁÂÃÄÅÇÈÉÊËÌÍÎÏÒÓÔÕÖÙÚÛÜàáâãäåèéêëìíîïòóôõöùúûü';
            $modificadas = 'AAAAAACEEEEIIIIOOOOOUUUUaaaaaaeeeeiiiiooooouuuu';
            $cadena = utf8_decode($cadena);
            $cadena = strtr($cadena, utf8_decode($originales), $modificadas);
            return utf8_encode($cadena);
        }      
    }
?>