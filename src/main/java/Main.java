import com.fasterxml.jackson.databind.ObjectMapper;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfig;
import ratpack.registry.Registry;
import ratpack.http.Headers;

import static ratpack.jackson.Jackson.json;


import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    public static void main(String... args) throws Exception {
        RatpackServer.start(server -> server
                .serverConfig(ServerConfig.embedded().publicAddress(new URI("http://MuchBetterAPI.org")))
                .registryOf(registry -> registry.add("MuchBetter"))
                .handlers(c -> c
                    .path("login", ctx -> {
                        ctx.render(new Process.ProcessRequest().Login());
                    })
                    .path("balance", ctx -> {
                        ctx.render(new Process.ProcessRequest().Balance());
                    })
                    .path("transaction", ctx ->{
                        ctx.render(new Process.ProcessRequest().Transaction());
                    })
                    .path("spend", ctx -> {
                        ctx.render("Spend response");
                    })
                )
        );
    }
}