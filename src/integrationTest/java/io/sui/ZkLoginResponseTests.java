package io.sui;

import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Random;

//import jakarta.servlet.http.HttpServletResponse;
//import org.eclipse.jetty.server.Request;
//import org.eclipse.jetty.server.Response;
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.server.handler.AbstractHandler;
//import org.eclipse.jetty.util.Callback;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import io.sui.crypto.ED25519KeyPair;
import io.sui.zklogin.Nonce;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZkLoginResponseTests {

    private static final String BASE_NODE_URL = "http://localhost:9000";
    private static final String BASE_FAUCET_URL = "http://localhost:9123";

    private static final String KEY_STORE_PATH = Paths
        .get("src", "integrationTest", "sui.keystore")
        .toAbsolutePath()
        .toString();

    private static final Sui SUI = new Sui(BASE_NODE_URL, BASE_FAUCET_URL, KEY_STORE_PATH);

    @AfterAll
    static void tearDown() throws IOException {
//        Files.delete(Paths.get("src", "integrationTest", "sui.keystore").toAbsolutePath());
    }

    @Test
    public void executeZkLoginFlow() throws Exception {
//        Server server = new Server(8080); // Use your desired port
//
//        server.setHandler(new AbstractHandler() {
//            @Override
//            public boolean handle(Request request, Response response, Callback callback) throws Exception {
//                if ("/oauth-callback".equals(request.getHttpURI().getPath())) {
//                    // Handle the OAuth callback here
//                    // Extract the authorization code and perform the token exchange
//
//                    response.setStatus(HttpServletResponse.SC_OK);
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        server.start();
        // ===

        ED25519KeyPair ephemeralKeypair = ED25519KeyPair.generate();
        BigInteger epoch = SUI.getLatestSuiSystemState().get().getEpoch();
        BigInteger maxEpoch = epoch.add(BigInteger.TEN);
        BigInteger randomness = Nonce.generateRandomness();
        String nonce = Nonce.generateNonce(ephemeralKeypair.publicKeyBytes(), maxEpoch.intValue(), randomness);
        // ===

        String queryParams = String.format("client_id=%s&redirect_uri=%s&response_type=id_token&scope=openid&nonce=%s",
            URLEncoder.encode("573120070871-0k7ga6ns79ie0jpg1ei6ip5vje2ostt6.apps.googleusercontent.com", StandardCharsets.UTF_8),
            URLEncoder.encode("https://sui-zklogin.vercel.app/", StandardCharsets.UTF_8),
            URLEncoder.encode(nonce, StandardCharsets.UTF_8));

        String loginURL = "https://accounts.google.com/o/oauth2/v2/auth?" + queryParams;

        URL url = new URL(loginURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        // Additional configurations can be set here, e.g., timeouts, headers

        // Normally, you'd handle the response here
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + connection.getResponseMessage());

        // Handle the response (e.g., read the response body)
        // ...

        connection.disconnect();
        // ===
//        server.join();
    }

    @Test
    void ensureLength() throws Exception {
        for (int i =0; i < 1000; i++) {
            System.out.println(i);
            ED25519KeyPair ephemeralKeypair = ED25519KeyPair.generate();
            BigInteger epoch = BigInteger.valueOf(Math.abs(new Random().nextInt() % 158));
            BigInteger maxEpoch = epoch.add(BigInteger.TEN);
            BigInteger randomness = Nonce.generateRandomness();
            String nonce = Nonce.generateNonce(ephemeralKeypair.publicKeyBytes(), maxEpoch.intValue(), randomness);
            assertEquals(27, nonce.length());
        }
    }
}
