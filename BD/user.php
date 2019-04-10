<?php
    
    include_once 'db-connect.php';
    
    class User
    {        
        private $db;  
        private $db_table = "usuarios";
        
        public function __construct()
        {
            $this->db = new DbConnect();
        }
        
        public function doesLoginExist($username, $password)
        {
            $query = "select * from ".$this->db_table." where username = '$username' AND password = '$password' Limit 1";
            $result = mysqli_query($this->db->getDb(), $query);            
            if(mysqli_num_rows($result) > 0)
            {                
                mysqli_close($this->db->getDb());
                return true;
            }            
            mysqli_close($this->db->getDb());
            return false;
        }
        
        public function createNewRegisterUser($nombre, $apellido1, $apellido2, $username, $password, $handicap)
        {               
            $query = "insert into ".$this->db_table." (nombre, apellido1, apellido2, username, password, fecha, handicap) values ('$nombre', '$apellido1', '$apellido2', '$username', '$password', CURDATE(), $handicap)";
            $inserted = mysqli_query($this->db->getDb(), $query);
                
                if($inserted == 1)
                {
                    $json['success'] = 1;
                    $json['message'] = "¡Usuario registrado!";                    
                }
                else
                {                    
                    $json['success'] = 0;
                    $json['message'] = "Ya existe otro usuario con ese 'username'";
                }                
                mysqli_close($this->db->getDb());            
            return $json;
        }


        public function editUser($nombre, $apellido1, $apellido2, $username, $password, $handicap)
        {                
            $query = "UPDATE ".$this->db_table." SET nombre='$nombre', apellido1='$apellido1', apellido2='$apellido2', password='$password', handicap=$handicap WHERE username='$username'";
            $inserted = mysqli_query($this->db->getDb(), $query);
               
                if($inserted == 1)
                {
                    $json['success'] = 1;
                    $json['message'] = "¡Usuario modificado!";                    
                }
                else
                {                    
                    $json['success'] = 0;
                    $json['message'] = "No se han podido realizar los cambios";
                }                
                mysqli_close($this->db->getDb());            
            return $json;
        }
        
        public function loginUser($username, $password)
        {            
            $json = array();            
            $canUserLogin = $this->doesLoginExist($username, $password);            
            if($canUserLogin)
            {
                $json['success'] = 1;
                $json['message'] = "¡Bienvenido!";
            }
            else
            {
                $json['success'] = 0;
                $json['message'] = "Credenciales incorrectas";
            }
            return $json;
        }

        public function find_usernames()
        {
            $usernames = array();
            $query = "SELECT username FROM usuarios";
            if($stmt = mysqli_query($this->db->getDb(), $query))
            {        
                while($row = mysqli_fetch_assoc($stmt))
                {  
                    $temp = 
                    [
                        'username'=>$row['username']
                    ];
                        array_push($usernames, $temp);
                }
                echo json_encode($usernames); 
            }
            mysqli_close($this->db->getDb());
        }

        public function find_username_data($username)
        {
            $data = array();
       
            $query = "SELECT f1.url as url FROM fotos f1 WHERE f1.username='$username' and f1.isProfile=true Limit 1";
            $result = mysqli_query($this->db->getDb(), $query);  
            if(mysqli_num_rows($result) > 0)
            {                

                $query = "SELECT f.url as url, (SELECT count(f.url) as num_fotos FROM fotos f WHERE f.username='$username' and f.id_campo is null and f.nombre_hoyo is null and f.isProfile=false Limit 1) as num_fotos, nombre, apellido1, apellido2, u.username, password, dayofmonth(fecha) as dia, month(fecha) as mes, year(fecha) as anyo, puntuacion, handicap 

                from usuarios u, fotos f
                            
                WHERE  u.username='$username' 
                and url= (SELECT f1.url as url FROM fotos f1 WHERE f1.username='$username' and f1.isProfile=true Limit 1)
                            
                Limit 1";
            }
            else
            {
                $query = "SELECT null as url, (SELECT count(f.url) as num_fotos FROM fotos f WHERE f.username='$username' and f.id_campo is null and f.nombre_hoyo is null and f.isProfile=false Limit 1) as num_fotos, nombre, apellido1, apellido2, u.username, password, dayofmonth(fecha) as dia, month(fecha) as mes, year(fecha) as anyo, puntuacion, handicap 

                from usuarios u, fotos f
                
                WHERE  u.username='$username' 
                                
                Limit 1";
            }

            if($stmt = mysqli_query($this->db->getDb(), $query))
            {        
                while($row = mysqli_fetch_assoc($stmt))
                {  
                    $temp = 
                    [
                        'nombre'=>$row['nombre'],
                        'apellido1'=>$row['apellido1'],
                        'apellido2'=>$row['apellido2'],
                        'username'=>$row['username'],
                        'password'=>$row['password'],
                        'anyo'=>$row['anyo'],
                        'mes'=>$row['mes'],
                        'dia'=>$row['dia'],
                        'puntuacion'=>$row['puntuacion'],
                        'handicap'=>$row['handicap'],
                        'num_fotos'=>$row['num_fotos'],
                        'url'=>$row['url']
                    ];
                        array_push($data, $temp);
                }     
                echo json_encode($data);   
            }
            mysqli_close($this->db->getDb());           
        }
    }
?>