
package exame2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class Exame2 {
public static Connection conexion=null;
    
    
public static Connection getConexion() throws SQLException  {
    // EN ESTE EJERCICIO SE TRTA DE REALIZAR UN LECTURA DE TRES TIPOS DE ARCHIVOS
    //DESDE UN XML UN FICHERO SERIALIZADO Y UNA TABLA
    // DESDE EL XML SE LEE EL CODIGO; CON EL CODIGO TE DIRIGES AL DELIMITADO; OBTENIENDO LA COMPOSICION
    // Y CON LA COMPOSICION OBTIENES LA GRASA Y ESPPUES CON TODOS ESTOS DATOS; ESCRIBES UN SERIALIZADO
        //SE realiza la conexion en esta parte del codigo
    
    //NOOLVIDARSE DE INICIAR EL lsnrctl start SI NO NO FUNCIONARA EL LISTENER

        String usuario = "hr";
        String password = "hr";
        String host = "localhost"; 
        String puerto = "1521";
        String sid = "orcl";
        String ulrjdbc = "jdbc:oracle:thin:" + usuario + "/" + password + "@" + host + ":" + puerto + ":" + sid;
        
           
            conexion = DriverManager.getConnection(ulrjdbc);
            return conexion;
        }

     
     public static void closeConexion() throws SQLException {
      conexion.close();
      }
    public static void crearSerializable() throws FileNotFoundException, IOException, XMLStreamException, SQLException{
        


        //CREACION DE UN FICHERO SERIALIZABLE 
        //FILEOUTPUTSTREAM PERMITE LA CREACION DE UN ARCHIVO DE TEXTO PLANO EN EL QUE SE PUEDE ESCRIBIR
        //OBJECTOUTPUTSTREAM PERMITE LA ESCRITURA DE DATOS PRIMITIVOS DE JAVA EN UN OUTPUSTREAM
        FileOutputStream fichOS = new FileOutputStream("/home/oracle/Desktop/Exame2/platoser2.txt");
        ObjectOutputStream objOS = new ObjectOutputStream(fichOS);
        
       
        //ACCESO AL FICHERO DE LOS PLATOS
        //FILE PERMITE RECOGER LA URL DE NUESTRO FICHERO A LEER
        //FILEREADER PERMITE LA LECTURA DE NUESTRO ARCHIVO
        File fich = new File("/home/oracle/Desktop/Exame2/platos.xml");
        FileReader fichLeer = new FileReader(fich);
        
        

        //PERMITE LA IMPLEMENTACION DE UN FACTORY; SE CREA UNA NUEVA INSTANCIA DE ESTE FACTORY
        XMLInputFactory xmlIF = XMLInputFactory.newInstance();
        //ESTO PERMITE SOLO LA LECTURA DE UN ARCHIVO DE TIPO XML
        XMLStreamReader xmlSR = xmlIF.createXMLStreamReader(fichLeer);
        
        


        //DATOS NECESARIOS DE ESCRITURA
        String codp ="";
        String nombreP ="";
        int grasa = 0;
        int grasaTotal = 0;
        
        //DATOS QUE SE RECOGEN DE EL 
        String codc = "";
        int peso = 0;
        
        
        //ESTO SIGNIFICA QUE MIENTRAS HAY ALGO QUE LEER EN EL ARCHIVO; SE REALICE ESTA PARTE DEL CODIGO
        while(xmlSR.hasNext()){
            int dato =0;
            String name ="";
            
            //EL EVENTTYPE NOS DEVUELVE UN INT; ASI NOS PERMITE SABER A LA HORA DE LA LECTURA 
            //EN QUE TIPO DE DATO SE ENCUENTRA NUESTRO PUNTERO
            dato = xmlSR.getEventType();
            
            
            //CON ESTO INDICAMOS SI EL ELEMENTO ES UN START ELEMENT
            //SE ALMACENA EL NOMBRE DEL START ELEMENT Y SI ES PLATO; SE OBTIENE SU DATO
            // SI ES NOMEP SE ALMACENA EL SUYO
            if (dato==XMLStreamConstants.START_ELEMENT){
                name = xmlSR.getLocalName();
                
                if(name.equals("Plato")){
                    codp = xmlSR.getAttributeValue(0);
                    
                    
                }else if(name.equals("nomep")){
                    nombreP = xmlSR.getElementText();
                    
                }
                
            }
            //COMPROBAMOS QUE TODAS LAS VARIABLES HAN SIDO RECOGIDAS
            //SI ES DISTINTO DE NULL SE PASA A LA LECTURA DEL ARCHIB¿VO D TETXO DELIMITADO
            if((codp !=null) && (nombreP !=null)){
                
                //COMENZAMOS LA LECTURA DEL FICHERO DELIMITADO
                FileReader fichR = new FileReader("/home/oracle/Desktop/Exame2/composicion.txt");
                //LA LECTURA BUFF PERMITE UNA LECTURA MAS EFICIENTE DE UN ARCHIVO; 
                 BufferedReader BuffR = new BufferedReader(fichR);
                 
                 String []array;
                 String posicion;
                 
                 while((posicion=BuffR.readLine())!=null){
                     //ESTO INDICA QUE EL SEPARADOR DE LOS ELEMENTOS ES UN #
                     array = posicion.split("#");
                     
                     //ESTE IF NOS PERMITE IR AÑADIENDO LOS DATOS QUE RECOGEMOS AL ARRAY
                     //EN ESTE CASO EL CODIGO DE LA COMPSOICION Y LOS DATOS DE PESO DEL PLATO
                     if(array[0].equals(codp)){
                         codc = array[1];
                         peso = Integer.parseInt(array[2]);
                         
                         
                         //LOS DATOS DE LA GRASA DE UN PLATO SON RECOGIDOS DESDE LA TABLA QUE SE PASO JUNTO A LOS ARCHIVOS
                         //MEDINATE UN PREPARED STATEMENT SE REALIZA AUTOMATICAMENTE
                         //CADA CODC QUE SE RECOGE SE INSERTA EN LUGAR DEL ? DE ESTA ORDEN Y NOS DA LA GRASA
                         PreparedStatement preSt = conexion.prepareStatement("select graxa from componentes where codc = ?");
                         preSt.setString(1, codc);
                         
                         ResultSet RS = preSt.executeQuery();
                         RS.next();
                         
                         grasa= RS.getInt(1);
                         grasaTotal += (grasa * peso)/100;
                         
                         
                         
                         
                         
                         
                         
                         
                         
                     }
                     
                     
                 }
//                 System.out.println(codp + " " + nombreP + " " + " " + codc + " " + peso + " " + grasaTotal);
                 
                 Platos obxp = new Platos(codp, nombreP, grasaTotal);
                 System.out.println(obxp);
                 
                 objOS.writeObject(obxp);
                 
                 //SE DEBE REALIZAR UNA LIMPIEZA DE VARIABLES CADA VEZ QUE LEEE UN NUEVO CODP
                 codp = null;
                 nombreP = null;
                 
                 BuffR.close();
                 fichR.close();
                 
                 
                 
                 
                 
                
                
                
            }
            xmlSR.next();
        }
        objOS.writeObject(null);
        fichLeer.close();
        fichOS.close();
        objOS.close();
        
    } 
     
     
     
    public static void main(String[] args) throws SQLException, IOException, FileNotFoundException, XMLStreamException, ClassNotFoundException {
      try{
        Exame2.getConexion();
      Exame2.crearSerializable();
      }catch(SQLException ex){
          Logger.getLogger(Exame2.class.getName()).log(Level.SEVERE, null, ex);
      }
    
      FileInputStream leer = new FileInputStream("/home/oracle/Desktop/Exame2/platoser2.txt");
      ObjectInputStream OIS = new ObjectInputStream(leer);
      
      
      Object fichero = 0;
      
      while((fichero =OIS.readObject())!=null){
          System.out.println(fichero);
          

            System.out.println("--------------------------------------------");

       System.out.println(fichero);
      }
      OIS.close();
      leer.close();
    }
    
}
