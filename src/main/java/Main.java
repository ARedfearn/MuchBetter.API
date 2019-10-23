import ratpack.server.RatpackServer;
import ratpack.server.ServerConfig;

import java.net.URI;

public class Main {

    public static void main(String... args) throws Exception {
        RatpackServer.start(server -> server
                .serverConfig(ServerConfig.embedded().publicAddress(new URI("http://MuchBetterAPI.org")))
                .registryOf(registry -> registry.add("MuchBetter"))
                .handlers(c -> {
                            c
                                .path("login", new LoginHandler())
                                .path("balance", new BalanceHandler())
                                .path("transaction", new TransactionHandler())
                                .path("spend", new SpendHandler())
                                .path("", ctx -> {
                                    ctx.getResponse().status(200).send("Hello from MuchBetterAPI");
                                })
                                .all(ctx -> {
                                    ctx.getResponse().status(404).send("Path not found");
                                });
                        }
                )
        );
    }
}




