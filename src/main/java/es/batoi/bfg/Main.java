package es.batoi.bfg;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {
    public static final String URL_ARTISTAS = "http://127.0.0.1:1818/bfg/artistas";
    public static final String URL_CANCIONES = "http://127.0.0.1:1818/bfg/canciones";

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient httpCliente = HttpClient.newHttpClient();

        Scanner scanner = new Scanner(System.in);
        HttpResponse<String> respuesta = null;
        boolean isTablaCorrecta;
        boolean isAccionCorrecta = false;

        String tablaSeleccionada = "";

        do {
            System.out.println("Tablas disponibles:\n1. Artistas\n2. Canciones\n");
            System.out.print("Selecciona una tabla: ");

            int idTablaSeleccionada = Utilidades.pedirNumeroSeleccionUsuario(scanner);

            switch (idTablaSeleccionada) {
                case 1:
                    tablaSeleccionada = "artistas";
                    isTablaCorrecta = true;
                    break;

                case 2:
                    tablaSeleccionada = "canciones";
                    isTablaCorrecta = true;
                    break;

                default:
                    isTablaCorrecta = false;
                    System.err.println("\n¡Error! No has introducido una tabla válida. Vuelve a intentarlo\n");

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        System.err.println("¡Error! Espera en selección de tabla fallida");
                    }
            }
        } while (!isTablaCorrecta);

        do {
            System.out.println("\nOperaciones disponibles:\n1. Obtener datos (GET)\n2. Insertar datos ()\n3. Actualizar datos ()\n4. Eliminar datos (DELETE)");
            System.out.print("\nSelecciona una operación: ");

            int idOperacionSeleccionada = Utilidades.pedirNumeroSeleccionUsuario(scanner);

            switch (idOperacionSeleccionada) {
                case 1:
                    isAccionCorrecta = true;

                    respuesta = HttpGet.ejecutarGet(httpCliente, tablaSeleccionada);

                    break;

                case 2:
                    isAccionCorrecta = true;
                    break;

                case 3:
                    isAccionCorrecta = true;
                    break;

                case 4:
                    isAccionCorrecta = true;
                    break;

                default:
                    respuesta = null;

                    System.err.println("\n¡Error! No has introducido una operación válida. Vuelve a intentarlo");

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        System.err.println("¡Error! Espera en selección de tabla fallida");
                    }
            }
        } while (!isAccionCorrecta || respuesta == null);

        convertirBodyToJson(respuesta);

    }

    private static void convertirBodyToJson(HttpResponse<String> respuesta) {
        String json = respuesta.body();

        JsonParser parser  = new JsonParser();
        JsonElement element = parser.parse(json);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        json = gson.toJson(element);

        System.out.println("\n" + json);
    }
}
