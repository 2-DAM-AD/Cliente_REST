package es.batoi.bfg;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import es.batoi.bfg.exceptions.ExceptionError404;
import es.batoi.bfg.exceptions.ExceptionError412;
import es.batoi.bfg.exceptions.ExceptionError500;
import es.batoi.bfg.modelos.Artista;
import es.batoi.bfg.modelos.Cancion;
import es.batoi.bfg.utiles.Helper;
import es.batoi.bfg.utiles.Utilidades;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static final String URL_ARTISTAS = "http://127.0.0.1:1818/bfg/artistas";
    public static final String URL_CANCIONES = "http://127.0.0.1:1818/bfg/canciones";
    public static final String TABLA_ARTISTAS = "artistas";
    public static final String TABLA_CANCIONES = "canciones";
    public static final String METODO_GET = "GET";
    public static final String METODO_POST = "POST";
    public static final String METODO_PUT = "PUT";
    public static final String METODO_DELETE = "DELETE";

    public static void main(String[] args) {
        HttpClient httpCliente = HttpClient.newHttpClient();

        Scanner scanner = new Scanner(System.in);
        HttpResponse<String> respuesta = null;
        boolean isTablaCorrecta;
        boolean isAccionCorrecta = false;
        boolean isList;

        String tablaSeleccionada = "";
        String metodoEjecutado = "";

        do {
            isList = false;

            System.out.println("Tablas disponibles:\n1. Artistas\n2. Canciones\n");
            System.out.print("Selecciona una tabla: ");

            int idTablaSeleccionada = Utilidades.pedirNumeroSeleccionUsuario(scanner);

            switch (idTablaSeleccionada) {
                case 1:
                    tablaSeleccionada = TABLA_ARTISTAS;
                    isTablaCorrecta = true;
                    break;

                case 2:
                    tablaSeleccionada = TABLA_CANCIONES;
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
            System.out.println("\nOperaciones disponibles:\n1. Obtener datos (GET)\n2. Insertar datos (POST)\n3. Actualizar datos (PUT)\n4. Eliminar datos (DELETE)");
            System.out.print("\nSelecciona una operación: ");

            int idOperacionSeleccionada = Utilidades.pedirNumeroSeleccionUsuario(scanner);

            try {
                switch (idOperacionSeleccionada) {
                    // ! Código 404 (al intentar obtener un registro inexistente (not found))
                    // * Código 200 (se ha encontrado el registro (ok))
                    case 1:
                        isAccionCorrecta = true;
                        metodoEjecutado = METODO_GET;
                        Helper helper = HttpGet.ejecutarGet(httpCliente, tablaSeleccionada);
                        isList = helper.isList();
                        respuesta = helper.getHttpResponse();

                        break;

                    // ! Código 500 (no se puede añadir una canción con un artista inexistente)
                    // * Código 201 (se ha insertado correctamente (created))
                    case 2:
                        isAccionCorrecta = true;
                        metodoEjecutado = METODO_POST;
                        respuesta = HttpPost.ejecutarPost(httpCliente, tablaSeleccionada);

                        break;

                    // ! Código 412 (la url es distinta al id insertado (poco probable))
                    // * Código 204 (al actualizar no devuelve cuerpo, solo el código 204 (si el id no existe igualmente se insertará))
                    case 3:
                        isAccionCorrecta = true;
                        metodoEjecutado = METODO_PUT;
                        respuesta = HttpPut.ejecutarPut(httpCliente, tablaSeleccionada);

                        break;

                    // ! Código 412 (la url es distinta al id insertado (poco probable))
                    // ! Código 500 (no se puede eliminar un registro con claves ajenas 'activas')
                    // * Código 204 (al eliminar no devuelve cuerpo, solo el código 204 (si no existe no falla))
                    case 4:
                        isAccionCorrecta = true;
                        metodoEjecutado = METODO_DELETE;
                        respuesta = HttpDelete.ejecutarDelete(httpCliente, tablaSeleccionada);
                        break;

                    default:
                        isAccionCorrecta = false;
                        respuesta = null;

                        System.err.println("\n¡Error! No has introducido una operación válida. Vuelve a intentarlo");

                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            System.err.println("¡Error! Espera en selección de tabla fallida");
                        }
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            } ;
        } while (!isAccionCorrecta || respuesta == null);

        int codigoRespuesta = respuesta.statusCode();
        String cuerpoRespuesta = respuesta.body();

        /**
         * * 200 -> 1 | 201 -> 1 | 204 -> 2 (distintos mensajes)
         * ? 404 -> 1 | 412 -> 2 (mismo mensaje)
         * ! 500 -> 2 (distintos mensajes)
         * ! ConnectException (si está desconectado)
         */
        try {
            switch (codigoRespuesta) {
                case 200:
                    System.out.println("\nCódigo 200: Ok");
                    break;
                case 201:
                    System.out.println("\nCódigo 201: Objeto insertado correctamente");
                    break;
                case 204:
                    System.out.print("\nCódigo 204:");

                    if (metodoEjecutado.equals(METODO_PUT)) {
                        System.out.println(" Objeto actualizado correctamente");
                    }

                    if (metodoEjecutado.equals(METODO_DELETE)) {
                        System.out.println(" Objeto eliminado correctamente");
                    }

                    break;

                // Excepciones de error
                case 404:
                    throw new ExceptionError404();
                case 412:
                    throw new ExceptionError412();
                case 500:
                    if (metodoEjecutado.equals(METODO_POST)) {
                        throw new ExceptionError500("El ID del artista de la canción no existe.");
                    }

                    if (metodoEjecutado.equals(METODO_DELETE)) {
                        throw new ExceptionError500("No puedes eliminar un artista que tenga canciones registradas.");
                    }

                    break;

                default:
                    System.err.println("¡Error no contemplado!");
                    throw new Exception("Código " + codigoRespuesta + ": ¡Error no contemplado!");

            }

            convertirBodyToObject(cuerpoRespuesta, tablaSeleccionada, isList);

        } catch (ExceptionError404 | ExceptionError412 | ExceptionError500 e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: Hacer método que controle los códigos de la respuesta y sus posibles excepciones

    // TODO: Recibir body de HttpResponse y convertirlo en el objeto correspondiente
    private static void convertirBodyToObject(String cuerpoRespuesta, String tablaSeleccionada, boolean isList) {
        if (!cuerpoRespuesta.equals("")) {
            String json = cuerpoRespuesta;

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(json);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            json = gson.toJson(element);

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                if (isList) {
                    switch (tablaSeleccionada) {
                        case TABLA_ARTISTAS:
                            List<Artista> participantJsonList = objectMapper.readValue(json, new TypeReference<List<Artista>>() {
                            });
                            for (Artista artista : participantJsonList) {
                                System.out.println(artista);
                            }

                            break;
                        case TABLA_CANCIONES:
                            List<Cancion> participantJsonList1 = objectMapper.readValue(json, new TypeReference<List<Cancion>>() {
                            });
                            for (Cancion artista : participantJsonList1) {
                                System.out.println(artista);
                            }

                            break;
                    }
                } else {
                    switch (tablaSeleccionada) {
                        case TABLA_ARTISTAS:
                            System.out.println(objectMapper.readValue(json, Artista.class));
                            break;

                        case TABLA_CANCIONES:
                            System.out.println(objectMapper.readValue(json, Cancion.class));
                            break;
                    }
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
