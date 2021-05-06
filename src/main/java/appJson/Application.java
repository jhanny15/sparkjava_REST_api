package appJson;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static spark.Spark.*;

public class Application {

    //Una mappa composto da utenti
    private static Map<String, User> users = new HashMap<>();

    //Una libreria che trasforma gli oggetti Java in formatto JSON
    private static Gson gson = new Gson();

    public static void main(String[] args) {

        //Crea l'utente e ritorna il suo ID
        post("/users", (request, response) -> {

            //json_user è il json che viene inviato nel corpo del post
            String json_user = request.body();

            //fromJson chiede il json e la classe da cui proviene
            User user = gson.fromJson(json_user, User.class);

            //Genera l'id dell'utente
            String id = UUID.randomUUID().toString();
            user.setId(id);

            //Inseriamo l'id e user dentro la mappa
            users.put(id, user);

            response.status(200);
            return "L'ID dell'utente è: " + id;
        } );


        //Mostra tutti utenti
        get("/users", (request, response) ->
                gson.toJson(users.values()));


        //Mostra l'utente con l'ID specificato
        get("/users/:id", (request, response) -> {

            //Prende l'id inserito
            String id = request.params(":id");

            //Cerca l'id nella mappa
            User user = users.get(id);
            if (user != null) {
                return "ID Utente: " + user.getId() +
                        ", Nome Utente: " + user.getUserName() +
                        ", Email: " + user.getEmail();
            }
            else {
                response.status(404);
                return "Utente non trovato";
            }
        });


        //Aggiorna l'utente dato il suo ID
        put("/users/:id", (request, response) -> {
            String id = request.params(":id");
            User user = users.get(id);

            if (user != null) {

                //Prende username e email inserito nel Params della richiesta
                String newUser = request.queryParams("username");
                String newEmail = request.queryParams("email");
                user.setUserName(newUser);
                user.setEmail(newEmail);

                return "L'utente con l'ID: " + id + " + stato aggiornato";
            }

            else {
                response.status(404);
                return "Utente non trovato";
            }
        });


        //Elimina l'utente dato il suo ID
        delete("/users/:id", (request, response) -> {
            String id = request.params(":id");
            User user = users.remove(id);

            if (user != null) {
                return "L'utente con l'ID: " + id + "è stato cancellato";
            }
            else {
                response.status(404);
                return "Utente non trovato";
            }
        });

    }
}
