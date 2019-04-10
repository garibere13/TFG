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
            $query = "SELECT id, nombre, latitud, longitud, creador FROM ".$this->db_table."";
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
                        'longitud'=>$row['longitud'],
                        'creador'=>$row['creador']
                    ];
                    array_push($fields, $temp);
                }
                echo json_encode($fields); 
            }
            mysqli_close($this->db->getDb());    
        }

        public function createNewField($nombre, $descripcion, $num_hoyos, $cod_pueblo, $latitud, $longitud, $username)
        {                
            $query = "insert into ".$this->db_table." (nombre, descripcion, num_hoyos, cod_pueblo, latitud, longitud, creador, fecha) values ('$nombre', '$descripcion', $num_hoyos, $cod_pueblo, $latitud, $longitud, '$username', CURDATE())";
           
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

        public function find_field_data($id)
        {            
            $data = array();            
           

                 $query = "SELECT f1.url as url FROM fotos f1 WHERE f1.id_campo=$id and f1.isProfile=true Limit 1";
                 $result = mysqli_query($this->db->getDb(), $query);  
                 if(mysqli_num_rows($result) > 0)
                 {                
                     $query = "SELECT f.url as url, (SELECT count(f.id_campo) as num_fotos FROM fotos f WHERE f.id_campo=$id and f.isProfile=false Limit 1) as num_fotos, c.valoracion, descripcion, c.id as id_campo ,c.nombre as nombre_campo, provincia, m.nombre as pueblo, num_hoyos, latitud, longitud, creador, dayofmonth(fecha) as dia, month(fecha) as mes, year(fecha) as anyo 
                     FROM fotos f, campos c , municipios m, provincias p 
                     WHERE c.id=$id
                     and c.cod_pueblo = m.id_municipio 
                     and m.id_provincia=p.id_provincia 
                     and url=(SELECT f1.url as url FROM fotos f1 WHERE f1.id_campo=$id and f1.isProfile=true Limit 1)
                          Limit 1";
                 }
                 else
                 {
                    $query = "SELECT null as url, (SELECT count(f.id_campo) as num_fotos FROM fotos f WHERE f.id_campo=$id and f.isProfile=false Limit 1) as num_fotos, c.valoracion, descripcion, c.id as id_campo ,c.nombre as nombre_campo, provincia, m.nombre as pueblo, num_hoyos, latitud, longitud, creador, dayofmonth(fecha) as dia, month(fecha) as mes, year(fecha) as anyo 
                    FROM fotos f, campos c , municipios m, provincias p 
                    WHERE c.id=$id
                    and c.cod_pueblo = m.id_municipio 
                    and m.id_provincia=p.id_provincia 
                         Limit 1";
                 }



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
                        'anyo'=>$row['anyo'],
                        'mes'=>$row['mes'],
                        'dia'=>$row['dia'],
                        'valoracion'=>$row['valoracion'],
                        'num_fotos'=>$row['num_fotos'],
                        'url'=>$row['url']
                    ];
                        array_push($data, $temp);
                }     
                echo json_encode($data);   
            }
            mysqli_close($this->db->getDb());           
        }


        public function open_field($username, $latitud, $longitud)
        {            
            $data = array();            
           

            $query = "SELECT id from campos where creador='$username' and latitud=$latitud and longitud=$longitud";
                 

           if($stmt = mysqli_query($this->db->getDb(), $query))
            {        
                while($row = mysqli_fetch_assoc($stmt))
                {  
                    $temp = 
                    [
                        'id'=>$row['id']
                    ];
                        array_push($data, $temp);
                }     
                echo json_encode($data);   
            }
            mysqli_close($this->db->getDb());           
        }



        public function editField($id, $nombre, $descripcion, $num_hoyos)
        {                
            $query = "UPDATE ".$this->db_table." SET nombre='$nombre', descripcion='$descripcion', num_hoyos=$num_hoyos WHERE id=$id";
            $inserted = mysqli_query($this->db->getDb(), $query);
               
                if($inserted == 1)
                {
                    $json['success'] = 1;
                    $json['message'] = "Campo modificado!";                    
                }
                else
                {                    
                    $json['success'] = 0;
                    $json['message'] = "No se han podido realizar los cambios";
                }                
                mysqli_close($this->db->getDb());            
            return $json;
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
            $query = "SELECT id, nombre, creador FROM ".$this->db_table."";
            if($stmt = mysqli_query($this->db->getDb(), $query))
            {        
                while($row = mysqli_fetch_assoc($stmt))
                {  
                    $temp = 
                    [
                        'id'=>$row['id'],
                        'nombre'=>$row['nombre'],
                        'creador'=>$row['creador']
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