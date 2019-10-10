import ratpack.server.RatpackServer;
import ratpack.server.ServerConfig;
import ratpack.registry.Registry;
import ratpack.http.Headers;

import java.net.URI;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    public static void main(String... args) throws Exception {
        RatpackServer.start(server -> server
                .serverConfig(ServerConfig.embedded().publicAddress(new URI("http://MuchBetterAPI.org")))
                .registryOf(registry -> registry.add("MuchBetter"))
                .handlers(chain -> chain
                                .get(ctx -> ctx.render("Welcome to the MuchBetter Interviewer API"))
                                .get(":login", ctx -> {
                                    //Return a Token
                                    ctx.render("response");
                                })
                                .get(":balance", ctx -> {
                                    //Return balance along with currency code
                                    Headers headers = ctx.getRequest().getHeaders();
                                    ctx.render("response");
                                })
                                .get(":transaction", ctx -> {
                                    //Return a list of transactions: date, description, amount, currency
                                    Headers headers = ctx.getRequest().getHeaders();
                                    ctx.render("response");
                                })
                                .get(":spend", ctx -> {
                                    //This needs to be a POST request
                                    //Receives date, description, amount, currency
                                    Headers headers = ctx.getRequest().getHeaders();
                                }))
        );

    }
}