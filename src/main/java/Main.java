import ratpack.http.Headers;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfig;
import ratpack.util.MultiValueMap;

import java.net.URI;

public class Main {

    public static void main(String... args) throws Exception {
        RatpackServer.start(server -> server
                .serverConfig(ServerConfig.embedded().publicAddress(new URI("http://MuchBetterAPI.org")))
                .registryOf(registry -> registry.add("MuchBetter"))
                .handlers(c -> c
                        .path("login", ctx -> {
                            //GET; Creates a new User. Returns a generated Token and Initial Balance, Currency and Transactions.
                            Process.ProcessRequest process = new Process.ProcessRequest();
                            String response = process.Login();
                            ctx.getResponse().status(process.Status).send(response);
                        })
                        .path("balance", ctx -> {
                            //GET; Returns the balance and currency for the specified user (identified by their token)
                            Headers headers = ctx.getRequest().getHeaders();
                            String token = headers.get("Authorization");

                            Process.ProcessRequest process = new Process.ProcessRequest(token);
                            String response = process.Balance();

                            ctx.getResponse().status(process.Status).send(response);
                        })
                        .path("transaction", ctx ->{
                            //GET; Returns all of a users transactions.
                            Headers headers = ctx.getRequest().getHeaders();
                            String token = headers.get("Authorization");

                            Process.ProcessRequest process = new Process.ProcessRequest(token);
                            String response = process.Transaction();

                            ctx.getResponse().status(process.Status).send(response);
                        })
                        .path("spend", ctx -> {
                            //POST; Adds a single transaction for the specified user.
                            //Requires the parameters date, description, amount and currency.
                            MultiValueMap<String, String> params = ctx.getRequest().getQueryParams();
                            Headers headers = ctx.getRequest().getHeaders();
                            String token = headers.get("Authorization");

                            Process.ProcessRequest process = new Process.ProcessRequest(token);
                            String response = process.Spend(params);
                            ctx.getResponse().status(process.Status).send(response);
                        })
                        .path("", ctx -> {
                            ctx.getResponse().status(200).send("MuchBetterAPI");
                        })
                        .all(ctx -> {
                            ctx.getResponse().status(404).send("Path not found");
                        })
                )
        );
    }
}




