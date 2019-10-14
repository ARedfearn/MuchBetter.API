import com.fasterxml.jackson.databind.ObjectMapper;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfig;
import ratpack.registry.Registry;
import ratpack.http.Headers;

import static ratpack.jackson.Jackson.json;


import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    public static void main(String... args) throws Exception {
        RatpackServer.start(server -> server
                .serverConfig(ServerConfig.embedded().publicAddress(new URI("http://MuchBetterAPI.org")))
                .registryOf(registry -> registry.add("MuchBetter"))
                .handlers(c -> c
                    .path("login", ctx -> {
                        //Creates a new User and Returns Initial Token, Balance, Currency, Transactions.
                        ctx.render(new Process.ProcessRequest().Login());
                    })
                    .path("balance", ctx -> {
                        //Returns Balance and Currency
                        ctx.render(new Process.ProcessRequest("7a4d13f4-1baa-4fd6-a4c1-df566ba95650").Balance());
                    })
                    .path("transaction", ctx ->{
                        //Returns Transactions
                        ctx.render(new Process.ProcessRequest("7a4d13f4-1baa-4fd6-a4c1-df566ba95650").Transaction());
                    })
                    .path("spend", ctx -> {
                        //Adds Transaction
                        ctx.render("Spend response");
                    })
                )
        );
    }
}