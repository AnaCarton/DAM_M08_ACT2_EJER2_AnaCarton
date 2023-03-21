package com.example.dam_m08_act2_ejer2_anacarton;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;

import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    //Declaración de las variables
    String apiURL = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    TextView textView;
    Button buttonDisplay;

    //Se crea un listView donde visualizar los datos del xml, con los currency y los ratio
    ListView listViewDatos;

    ArrayList arrayList = new ArrayList<Cube>();
    public static ArrayList<String> currencyAndRatio = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("HOLA INICIO MAIN ACTIVITY");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //textView = findViewById(R.id.textView);
        //textView.setMovementMethod(new ScrollingMovementMethod());
        buttonDisplay = findViewById(R.id.buttonDisplay);

        listViewDatos = findViewById(R.id.listViewDatos);

        /*
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, MainActivity.currencyAndRatio);

        listViewDatos.setAdapter(arrayAdapter);*/

        //Se intancia un objeto de la clase Adaptador, que es un adaptador propio.
        // En este caso no posee layout.
        Adaptador adaptadorPropio = new Adaptador(MainActivity.this, arrayList);

        // Se setea el adaptador propio al listview
        listViewDatos.setAdapter(adaptadorPropio);
        buttonDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyTask myTask = new MyTask();

                //Se llama al hilo asíncrono mediante el método “execute” (), que puede recibir como parámetros una lista de Strings
                myTask.execute(apiURL, "ddd");

            }
        });

    }

    //Se crea la clase MyTask, que extiende de AsyncTask para conectarnos al servidor con URL "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml"
    public class MyTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Por favor, espere...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        // El método doInBackground() crea la conexión mediante la clase HttpURLConnection y su método connect().

        @Override
        protected String doInBackground(String... address) {
            System.out.println("HOLA INICIO DOINBACKGROUND");
            String result = new String();
            String server = address[0];
            HttpURLConnection connection = null;
            try{

                //Con la variable de tipo HttpURLConnection nos conectamos a la apiURL
                URL url = new URL(server);
                connection = (HttpURLConnection) url.openConnection();
                //La conexión se realiza mediante un método GET, ya que queremos recuperar los datos
                connection.setRequestMethod("GET");
                connection.connect();

                //Si se logra establecer la conexión la variable result tiene como valor lo siguiente
                // que nos servirá como filtro para hacer una u otra cosa en el onPostExecute
                result = "Connection stablished!";
                if(result == "Connection stablished!"){
                    System.out.println("CONEXION ESTABLECIDA");

                }



            }
            catch (Exception e){
                e.getStackTrace();
                System.out.println("ERROR CATCH:" + e.getMessage());
                //Si no se logra establecer la conexión la variable result tiene como valor "Connection failed!"
                // que nos servirá como filtro para hacer una u otra cosa en el onPostExecute
                result = "Connection failed!";
            }
            finally{
                //Se puede conectar a la URL usando los elementos “Document” del DOM.
                // Así se gestiona el resultado XML más fácilmente.
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                try{
                    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                    Document document = documentBuilder.parse(new URL(apiURL).openStream());
                    //Tras guardar el XML que devuelve la consulta en formato Document
                    //se pueden recoger todos los valores de un Nodo de XML
                    // en un elemento de tipo NodeList
                    //Con “getElementsByTagName” extraemos cada nodo del documento
                    NodeList nodeList = document.getElementsByTagName("Cube");
                    //"Cube" es un nodo del XML que devuelve la consulta
                    System.out.println("Tamaño nodelist:" + nodeList.getLength());

                    //Se va a guardar la información en un arraylist de tipo Cube, que es una clase
                    //creada previamente con los campos que sabemos que tienen los nodos
                    //currency y rate
                    //arrayList = new ArrayList<Cube>();
                    //Hay que recorrer “nodeList” ya que tiene guardado el array
                    //para poder recoger los atributos "currency" y "rate" de cada nodo
                    //se rellena el arraylist creado con los datos

                    for (int i = 0; i < nodeList.getLength(); i++){
                        System.out.println("Entro en bucle");
                        Element element = (Element) nodeList.item(i);
                        //Con “getAttribute” se extraen los contenidos de cada atributo de cada nodo "Cube"
                        String currencyAttr = element.getAttribute("currency");
                        String ratioAttr = element.getAttribute("rate");

                        Cube cube = new Cube(currencyAttr, ratioAttr);
                        //solo se añaden al arrayList los nodos que tienen atributos de currency y rate
                        if((!currencyAttr.isEmpty()) && (!ratioAttr.isEmpty())){
                            arrayList.add(cube);
                            System.out.println("elemento cube añadido al arraylist");
                            System.out.println("Tamaño: " + arrayList.size());}
                    }
                }
                catch (Exception ex){
                    ex.getMessage();
                    ex.getStackTrace();
                }


                // El resultado de la conexión se pasa al método onPostExecute(), que se ejecuta antes de finalizar el hilo.

                System.out.println("HOLA FIN DOINBACKGROUND");

                return result; //El valor de la variable resulta se pasa al onPostExecute()

            }

        }

        @Override
        protected void onPostExecute(String result){
            System.out.println("HOLA INICIO ONPOSTEXECUTE");

            super.onPostExecute(result);
            System.out.println("result es: " + result);

            //Para que desaparezca el mensaje de progreso cuando se carga el listview:
            progressDialog.dismiss();



            //textView.setText((CharSequence) arrayList);

            //Si no ha habido problemas, borramos los datos de la base de datos
            if(result == "Connection stablished!"){

                System.out.println("HAY CONEXION A INTERNET. SE BORRA Y SE INSERTA.");
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setMessage("Hay conexión a internet. Se está actualizando la base de datos.");
                alertDialog.show();

            eraseData();
            //Cargamos de nuevo los datos actualizados
            insertData();
           }

            //Si el resultado de la conexión es fallido y result = "Connection failed!"
            else{
                System.out.println("NO HAY CONEXION A INTERNET. SE INTENTA HACER SELECT PARA RECUPERAR DATOS.");

                /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setMessage("No hay conexión a internet. Se van a cargar los últimos datos actualizados.");
                alertDialog.show();*/
                selectData();
                //Como no se ha podido conectar con la URL, no se borran los datos guardados en la bdd
                //ya que no se pueden actualizar
                //en cambio, se hace un select para recuperarlos


            }


            //Se intancia un objeto de la clase Adaptador, que es un adaptador propio.
            // En este caso no posee layout.
            Adaptador adaptadorPropio = new Adaptador(MainActivity.this, arrayList);

            // Se setea el adaptador propio al listview
            listViewDatos.setAdapter(adaptadorPropio);


        }

        public void insertData(){
            System.out.println("en la llamada a insert");
            AdmBDDSQLite admBDDSQLite = new AdmBDDSQLite(MainActivity.this, "admBDDSQLite", null, 1);
            //Abrir la bdd
            SQLiteDatabase bdd = admBDDSQLite.getReadableDatabase();
            bdd = admBDDSQLite.getReadableDatabase();

            //Se insertan los datos en la bdd
            ContentValues registro = new ContentValues();
            for(int i=0; i<arrayList.size(); i++){
                Cube cube = (Cube) arrayList.get(i);


                /*
                String insertQuery = "INSERT INTO conversor " +
                        "( 'id', 'Currency', 'Rate')" +
                        " VALUES " + "( '"+i+"', '" + cube.getCurrency() +"', '" + cube.getRate() +"')";
                bdd.execSQL(insertQuery);*/
                registro.put("id", i+1);
                registro.put("Ratio",  cube.getRate());
                System.out.println("rate del cube: " + cube.getRate());
                registro.put("Currency", cube.getCurrency());
                System.out.println("currency del cube: " + cube.getCurrency());


                bdd.insert("conversor", null, registro);
               // bdd.insert("conversor", null, registro);
                System.out.println(i + "registro insertado");

            }
            System.out.println("TAMAÑO ARRAY: " + arrayList.size());
            System.out.println("fin llamada a insert");

            admBDDSQLite.close();
        }

        public void eraseData(){
            System.out.println("en la llamada a erase");
            AdmBDDSQLite admBDDSQLite = new AdmBDDSQLite(MainActivity.this, "admBDDSQLite", null, 1);
            SQLiteDatabase bdd = admBDDSQLite.getReadableDatabase();
            bdd = admBDDSQLite.getReadableDatabase();

            bdd.execSQL("delete from conversor");
            System.out.println("fin llamada a erase");
            admBDDSQLite.close();
        }



        public void selectData(){
            AdmBDDSQLite admBDDSQLite = new AdmBDDSQLite(MainActivity.this, "admBDDSQLite", null, 1);

            SQLiteDatabase bdd = admBDDSQLite.getReadableDatabase();
            bdd = admBDDSQLite.getReadableDatabase();

            String query = "select * from conversor";
            Cursor c = bdd.rawQuery(query, null);
            if (c.getCount() == 0){
                System.out.println("NO HAY CONEXION A INTERNET. NO HAY DATOS GUARDADOS.");

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setMessage("No hay conexión a internet ni datos guardados en la base de datos." +
                        "Necesitas conexión a internet al menos una vez para poder visualizar los datos.");
                alertDialog.show();

            }
            //Si no se tiene conexión a internet pero sí hay datos guardados en la base de datos de la última vez
            else{
                System.out.println("NO HAY CONEXION A INTERNET. HAY DATOS GUARDADOS. SE RECUPERAN.");

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setMessage("No hay conexión a internet. Se cargarán los datos almacenados en la base de datos de la última conexión.");
                alertDialog.show();

                arrayList.clear();
                while(c.moveToNext()){
                    String currency = c.getString(c.getColumnIndexOrThrow("Currency"));
                    String rate = c.getString(c.getColumnIndexOrThrow("Ratio"));
                    System.out.println("sout: "+ currency);
                    System.out.println("sout2: " + rate);
                    Cube cube = new Cube(currency, rate);
                    arrayList.add(cube);

                }

            }

            admBDDSQLite.close();

        }

    }
}



