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
            $query = "SELECT id_municipio, nombre FROM municipios";
            $result=mysqli_query($this->db->getDb(), $query);

            if($stmt = $result)
            {        
                while($row = mysqli_fetch_assoc($stmt))
                { 
                    $mal=$row['nombre'];
                    $bien=$this->normaliza($mal);
                    $temp = 
                    [
                        'id_municipio'=>$row['id_municipio'],
                        'nombre'=>$bien
                    ];
                    array_push($towns, $temp);
                }
                echo json_encode($towns); 
            }
            mysqli_close($this->db->getDb());    
        }


        public function find_fields_map_location()
        {
            $fields = array();
            $query = "SELECT id, nombre, latitud, longitud FROM ".$this->db_table."";
            $result=mysqli_query($this->db->getDb(), $query);

            if($stmt = $result)
            {        
                while($row = mysqli_fetch_assoc($stmt))
                { 
                    $mal=$row['nombre'];
                    $bien=$this->normaliza($mal);
                    $temp = 
                    [
                        'id'=>$row['id'],
                        'nombre'=>$bien,
                        'latitud'=>$row['latitud'],
                        'longitud'=>$row['longitud']
                    ];
                    array_push($fields, $temp);
                }
                echo json_encode($fields); 
            }
            mysqli_close($this->db->getDb());    
        }

        public function createNewField($nombre, $descripcion, $num_hoyos, $cod_pueblo, $latitud, $longitud, $username)
        {                
            $query = "insert into ".$this->db_table." (nombre, descripcion, num_hoyos, cod_pueblo, latitud, longitud, creador) values ('$nombre', '$descripcion', $num_hoyos, $cod_pueblo, $latitud, $longitud, '$username')";
           
            $inserted = mysqli_query($this->db->getDb(), $query);
                
                if($inserted == 1)
                {
                    $json['success'] = 1;
                    $json['mensaje'] = "Campo registrado!";                    
                }
                else
                {                    
                    $json['success'] = 0;
                    $json['mensaje'] = "No se ha podido registrar el campo";
                }                
                mysqli_close($this->db->getDb());            
            return $json;
        }

        public function find_field_data($id, $username)
        {
            $data = array();
            $query = "SELECT c.valoracion, descripcion, c.id as id_campo ,c.nombre as nombre_campo, provincia, m.nombre as pueblo, num_hoyos, latitud, longitud, creador FROM campos c , municipios m, provincias p WHERE id=$id and c.cod_pueblo = m.id_municipio and m.id_provincia=p.id_provincia";

           if($stmt = mysqli_query($this->db->getDb(), $query))
            {        
                while($row = mysqli_fetch_assoc($stmt))
                {  
                    $temp = 
                    [
                        'id'=>$row['id_campo'],
                        'nombre'=>$row['nombre_campo'],
                        'provincia'=>$row['provincia'],
                        'pueblo'=>$row['pueblo'],
                        'descripcion'=>$row['descripcion'],
                        'num_hoyos'=>$row['num_hoyos'],
                        'latitud'=>$row['latitud'],
                        'longitud'=>$row['longitud'],
                        'creador'=>$row['creador'],
                        'valoracion'=>$row['valoracion']
                    ];
                        array_push($data, $temp);
                }     
                echo json_encode($data);   
            }
            mysqli_close($this->db->getDb());           
        }

        public function find_user_valoration($id_campo, $username)
        {
                   
            $exists = $this->doesValorationExist($id_campo, $username);            
            if($exists)
            {
                $data = array();
                $query = "SELECT valoracion FROM valoraciones WHERE id_campo=$id_campo and username='$username' Limit 1";

                if($stmt = mysqli_query($this->db->getDb(), $query))
                {        
                    while($row = mysqli_fetch_assoc($stmt))
                    {  
                        $temp = 
                        [
                            'valoracion'=>$row['valoracion']
                        ];
                            array_push($data, $temp);
                    }     
                    echo json_encode($data);   
                }
                mysqli_close($this->db->getDb());  
            }        
        }

        public function doesValorationExist($id_campo, $username)
        {
           $query = "SELECT * FROM valoraciones WHERE id_campo=$id_campo and username='$username' Limit 1";
           $result = mysqli_query($this->db->getDb(), $query);            
            if(mysqli_num_rows($result) > 0)
            {                
               // mysqli_close($this->db->getDb());
                return true;
            }            
           // mysqli_close($this->db->getDb());
            return false;
        }


        public function createValoration($id_campo, $username, $valoracion)
        {                
            $query = "insert into valoraciones (id_campo, username, valoracion) values ($id_campo, '$username', $valoracion)";
            $inserted = mysqli_query($this->db->getDb(), $query);
                
                if($inserted == 1)
                {
                    $json['success'] = 1;
                    $json['message'] = "¡Valoracion registrada!";  

                    $this->update_field_valoration($id_campo);  
                }
                else
                {                    
                    $json['success'] = 0;
                    $json['message'] = "No puede volver a valorar este campo";
                }                
                mysqli_close($this->db->getDb());            
            return $json;
        }

        public function update_field_valoration($id_campo)
        {          
            $query = "UPDATE ".$this->db_table." SET valoracion=(SELECT avg(valoracion) FROM valoraciones WHERE id_campo=$id_campo) WHERE id=$id_campo";
            $inserted = mysqli_query($this->db->getDb(), $query);            
            //mysqli_close($this->db->getDb());            
        }

        public function find_field_names()
        {
            $names = array();
            $query = "SELECT id, nombre FROM ".$this->db_table."";
            if($stmt = mysqli_query($this->db->getDb(), $query))
            {        
                while($row = mysqli_fetch_assoc($stmt))
                {  
                    $temp = 
                    [
                        'id'=>$row['id'],
                        'nombre'=>$row['nombre']
                    ];
                        array_push($names, $temp);
                }
                echo json_encode($names); 
            }
            mysqli_close($this->db->getDb());
        }

        public function renewValoration($id_campo, $username, $valoracion)
        {                
            $query = "UPDATE valoraciones SET valoracion=$valoracion WHERE id_campo=$id_campo and username='$username'";
            $inserted = mysqli_query($this->db->getDb(), $query);
                
                if($inserted == 1)
                {
                    $json['success'] = 1;
                    $json['message'] = "¡Cambio de valoracion registrada!";  

                    $this->update_field_valoration($id_campo);  
                }
                else
                {                    
                    $json['success'] = 0;
                    $json['message'] = "No se ha podido renovar la valoración de este campo";
                }                
                mysqli_close($this->db->getDb());            
            return $json;
        }
        
        public function normaliza ($cadena)
        {
        $originales = 'ÑñÀÁÂÃÄÅÇÈÉÊËÌÍÎÏÒÓÔÕÖÙÚÛÜàáâãäåèéêëìíîïòóôõöùúûü';
            $modificadas = 'NnAAAAAACEEEEIIIIOOOOOUUUUaaaaaaeeeeiiiiooooouuuu';
            $cadena = utf8_decode($cadena);
            $cadena = strtr($cadena, utf8_decode($originales), $modificadas);
            return utf8_encode($cadena);
        }      
    }
?>