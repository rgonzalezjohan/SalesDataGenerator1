package filegenerator1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateInfoFiles1 {
    private static final String[] CIUDAD = {"Bogota", "Medellin", "Cali", "Bucaramanga", "Ibague", "Santa Marta", "Barranquilla", "Cartagena", "Villavicencio", "Chia"};
    private static final String[] NOMBRE = {"GUSTAVO ADOLFO", "OLGA LUCIA", "ISMAEL", "YOLANDA", "MAURICIO", "MARIA NANCY", "PEDRO JESUS", "DIANA CATHERINE", "MERCEDES", "MONICA VIVIANA", "JORGE ELIECER", "ARTURO NAPOLEON", "DAMARIS"};
    private static final String[] APELLIDO = {"ACOSTA LONDOÑO", "WILCHES BOHORQUEZ", "SUAREZ TRIVIÑO", "HERNANDEZ RODRIGUEZ", "ARIZA PINZON", "RODRIGUEZ BONILLA", "AGREDO LOPEZ", "ROJAS DAZA", "MORENO ROMERO", "NIÑO PAEZ", "AREVALO AYALA", "MATOS USCATEGUI", "CORREA OROZCO"};
    private static final String[] MES = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    private static final String[] PRODUCTO = {"Laptop", "Celular", "Impresora", "Teclado", "Mouse", "Monitor", "Silla", "Escritorio", "Tablet", "Disco Duro"};
    private static final int[] PRECIO = {2000000, 1500000, 800000, 120000, 70000, 900000, 450000, 600000, 1300000, 350000};
    private static final String[] CEDULA = {"1073155047", "52076125", "19189282", "52381135", "79735643", "20586075", "5688772", "1023010258", "52240099", "52850154", "8741290", "582180", "30306521"};
    
    public static void main(String[] args) {
        try {
            createSalesManInfoFile();
            createProductsFile();
            createSalesFiles();
            System.out.println("Archivos generados exitosamente.");
        } catch (IOException e) { //Validaciones de posibles errores al generar los archivos
            System.out.println("Error generando archivos: " + e.getMessage());
        }
    }
    
    private static void createSalesManInfoFile() throws IOException {
        File file = new File("vendedores.txt");
        FileWriter writer = new FileWriter(file);
        for (int i = 0; i < NOMBRE.length; i++) { // inclusion de item adicional CIUDAD
            writer.write("CC;" + CEDULA[i] + ";" + NOMBRE[i] + ";" + APELLIDO[i]+ ";" +CIUDAD[new Random().nextInt(CIUDAD.length)] + "\n");
        }
        writer.close();
    }
    
    private static void createProductsFile() throws IOException {
        File file = new File("productos.txt");
        FileWriter writer = new FileWriter(file);
        for (int i = 0; i < PRODUCTO.length; i++) {
            writer.write((i + 1) + ";" + PRODUCTO[i] + ";" + PRECIO[i] + "\n");
        }
        writer.close();
    }
    
    private static void createSalesFiles() throws IOException {
        Random random = new Random();
        for (int i = 0; i < NOMBRE.length; i++) {
            String filename = "ventas_" + CEDULA[i] + ".txt";
            File file = new File(filename);
            FileWriter writer = new FileWriter(file);
            writer.write("CC;" + CEDULA[i] + "\n");
            for (int j = 0; j < 3; j++) { // Cada vendedor tiene 3 productos vendidos
                int productoIndex = random.nextInt(PRODUCTO.length);
                int cantidadVendida = 1 + random.nextInt(10); // inclusion de item adicional MES
                writer.write((productoIndex + 1) + ";" + cantidadVendida + ";" + MES[new Random().nextInt(MES.length)] + "\n");
            }
            writer.close();
        }
    }
}
