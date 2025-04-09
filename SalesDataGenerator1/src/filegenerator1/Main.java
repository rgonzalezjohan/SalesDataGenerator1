package filegenerator1;

import java.io.*;
import java.util.*;

public class Main {//creacion del metodo principal 
    public static void main(String[] args) {
        try {
            // de esta forma cargamos los archivos que se generaron anteriormente en formato txt
            Map<Integer, Product> products = loadProducts("productos.txt");

            // se debe tener en cuenta el nombre correcto de los archivos
            Map<String, Salesman> salesmen = loadSalesmen("vendedores.txt");

            // en esta parte del comando le estamos indicando que valide los archivos de ventas en la carpeta de origen de nuestro proyecto
            File folder = new File(".");
            for (File file : folder.listFiles()) {
                if (file.getName().startsWith("ventas_") && file.getName().endsWith(".txt")) { // con este condicional nos aseguramos de que tome los archivos que son los correctos y en el formato adecuado
                    processSalesFile(file, products, salesmen);//analiza la informacion dentro de los txt de los vendedores para procesarlas en un archivo
                }
            }

            // basicamente estamos llamando a los metodos para generar los reportes finales el de vendedores y productos
            generateSalesmenReport(salesmen);
            generateProductsReport(products);
// nos muestra en la consola el resultado de la ejecucion del codigo, tanto positiva como negativa
            System.out.println("Reportes generados exitosamente.");
        } catch (Exception e) {
            System.out.println("Error durante el proceso: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // En este metodo se leera el archivo productos
    private static Map<Integer, Product> loadProducts(String filename) throws IOException {//toma la informacion del archivo y genera la excepcion en dado caso de que haya un error
        Map<Integer, Product> map = new HashMap<>();//genera el espacio donde iran los productos
        BufferedReader reader = new BufferedReader(new FileReader(filename));//abre el archivo para ser leido linea por linea
        String line;
        while ((line = reader.readLine()) != null) {//abre el archivo para ser leido linea por linea hasta el final del archivo
            String[] parts = line.split(";");// lee cada linea y separa la informacion obtenida
            int id = Integer.parseInt(parts[0]);//extrae el id del producto
            String name = parts[1];//extrae el nombre del producto
            int price = Integer.parseInt(parts[2]);//extrae el precio del producto
            map.put(id, new Product(name, price));//crea como un objeto cada producto con la informacion obtenida
        }
        reader.close();//cierra el archivo
        return map;//devuelve o actualiza la informacion
    }

    // En este metodo se leera el archivo vendedores 
    private static Map<String, Salesman> loadSalesmen(String filename) throws IOException {//toma la informacion del archivo y genera la excepcion en dado caso de que haya un error 
        Map<String, Salesman> map = new HashMap<>();//genera el espacio donde ira el campo vendedores
        BufferedReader reader = new BufferedReader(new FileReader(filename));//abre el archivo para ser leido linea por linea
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");// lee cada linea y separa la informacion obtenida
            String id = parts[1];// extrae la cedula del vendedor			
            String fullName = parts[2] + " " + parts[3]; // extra el nombre completo del vendedor
            map.put(id, new Salesman(fullName));//guarda la informacion del nombre del vendedor como objeto
        }
        reader.close();//cierra el archivo
        return map;//actualiza la informacion o retroalimenta por asi decirlo
    }

    // este metodo es para el procesamiento de cada vendedor
    private static void processSalesFile(File file, Map<Integer, Product> products, Map<String, Salesman> salesmen) throws IOException { // recibde todos los datos de productos y vendedores y genera una excepcion por si algun error 
        BufferedReader reader = new BufferedReader(new FileReader(file)); // lee el archivo y comienza a desglozar los datos
        String line = reader.readLine(); // lee la primera linea en este caso la cedula del vendedor
        String[] parts = line.split(";"); // divide la informacion
        String salesmanId = parts[1]; // guarda la cedula como dato individual

        while ((line = reader.readLine()) != null) { // en este ciclo permite leer cada archivo de cada vendedor y separar los por datos individuales 
            parts = line.split(";");
            int productId = Integer.parseInt(parts[0]); // separa y guarda el dato del ID del producto como un numero entero
            int quantity = Integer.parseInt(parts[1]); // separa y guarda el dato de cantidad de producto vendido como un entero
            // en este archivo encontramos otro dato como el mes que aun no lo vamos a incluir en los reportes actuales, el ideal seria crear otro tipo de reportes para analizar mas informacion

            if (products.containsKey(productId) && salesmen.containsKey(salesmanId)) { // genera una validacion de la informacion dentro de la memoria interna por decirlo asi
                Product product = products.get(productId);
                Salesman salesman = salesmen.get(salesmanId);

                product.quantitySold += quantity; // va registrando y aumentando o actualizando la informacion
                salesman.totalSales += quantity * product.price;
            }
        }
        reader.close();//cierra el archivo para evitar perdida de informacion
    }

    // este es el metodo para generar el reporte de vendedores 
    private static void generateSalesmenReport(Map<String, Salesman> salesmen) throws IOException {//se incluye una forma para informa si se detecta una falla dentro del archivo
        List<Salesman> list = new ArrayList<>(salesmen.values());// toma los valores y los guarda en una lista
        list.sort((a, b) -> Integer.compare(b.totalSales, a.totalSales)); // en esta parte nos organiza la lista anteriormente guardada en forma descendente 

        FileWriter writer = new FileWriter("reporte_vendedores.csv");// crea y le asignamos el nombre al reporte
        writer.write("Nombre Completo;Total Ventas\n"); // asignamos el nombre a las columnas de nuestra lista en el reporte final
        for (Salesman s : list) {
            writer.write(s.fullName + ";" + s.totalSales + "\n"); // practicamente llenamos nuestra lista con los valores finales 
        }
        writer.close(); // cierra el archivo 
    }

    // Genera el reporte final de productos 
    private static void generateProductsReport(Map<Integer, Product> products) throws IOException { // recibe los parametros y al igual que en el anterior metodo, genera la excepcion si se presenta alguna falla
        List<Product> list = new ArrayList<>(products.values());// toma los valores y los guarda en una lista
        list.sort((a, b) -> Integer.compare(b.quantitySold, a.quantitySold));// ordenamos la lista de los productos de mayor a menor segun la cantidad vendida

        FileWriter writer = new FileWriter("reporte_productos.csv");// se crea y se le asigna el nombre al reporte
        writer.write("Nobre Producto;Precio Unitario;Cantidad Vendida\n");//le asignamos el nombre a las columnas de nuestra lista 
        for (Product p : list) {
            writer.write(p.name + ";" + p.price + ";" + p.quantitySold + "\n");// practicamente llenamos nuestra lista con los valores finales 
        }
        writer.close();// cierra el archivo
    }
}

// Creacion de una clase privada para agrupar los datos obtenidos de productos
class Product {
    String name; // guarda el nombre del producto
    int price; // guarda el precio unitario
    int quantitySold; // guarda la cantidad de unidades vendidas

    Product(String name, int price) {
        this.name = name; //asigna el valore como parametro en este caso el nombre del producto
        this.price = price;
        this.quantitySold = 0; // da inicio a la cantidad de venta en 0
    }
}

//Creacion de una clase privada para agrupar los datos obtenidos de vendedores
class Salesman {
    String fullName; // guarda el dato del nombre completo
    int totalSales;//  guarda el dato de la totalidad de ventas

    Salesman(String fullName) {
        this.fullName = fullName;
        this.totalSales = 0;
    }
}


    