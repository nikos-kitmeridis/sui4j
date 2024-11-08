package io.sui.clients;

import io.sui.json.JsonHandler;
import io.sui.models.SuiApiException;
import io.sui.models.enoki.BaseEnokiResponse;
import io.sui.models.enoki.NonceResponse;
import io.sui.models.enoki.ZkLoginResponse;
import io.sui.models.enoki.ZkProofRequest;
import io.sui.models.enoki.ZkProofResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class OkhttpEnokiClient implements EnokiClient {

    private final OkHttpClient httpClient;

    private final String baseUrl;

    private final String apiKey;

    private final JsonHandler jsonHandler;

    /**
     * Instantiates a new Okhttp faucet client.
     *
     * @param baseUrl the base url
     * @param jsonHandler the json handler
     */
    public OkhttpEnokiClient(String baseUrl, String apiKey, JsonHandler jsonHandler) {
        this.jsonHandler = jsonHandler;
        this.apiKey = apiKey;
        this.httpClient =
                new OkHttpClient()
                        .newBuilder()
                        .pingInterval(Duration.ofSeconds(15))
                        .writeTimeout(Duration.ofSeconds(15))
                        .readTimeout(Duration.ofSeconds(15))
                        .build();
        this.baseUrl = baseUrl;
    }


    @Override
    public CompletableFuture<BaseEnokiResponse<NonceResponse>> requestNonce(String publicKey) {
        final CompletableFuture<BaseEnokiResponse<NonceResponse>> future = new CompletableFuture<>();
        final Request okhttpRequest;
        try {
            final String requestBodyJsonStr =
                    String.format("{\"ephemeralPublicKey\": \"%s\"}", publicKey);
            final RequestBody requestBody =
                    RequestBody.create(requestBodyJsonStr, MediaType.get("application/json; charset=utf-8"));
            okhttpRequest =
                    new Request.Builder()
                            .header("Authorization", String.format("Bearer %s", apiKey))
                            .url(String.format("%s/v1/zklogin/nonce", this.baseUrl))
                            .post(requestBody)
                            .build();
        } catch (Throwable throwable) {
            future.completeExceptionally(throwable);
            return future;
        }

        this.httpClient
                .newCall(okhttpRequest)
                .enqueue(
                        new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                future.completeExceptionally(e);
                            }

                            @Override
                            public void onResponse(Call call, @NotNull Response response) {
                                try {
                                    final ResponseBody responseBody = response.body();
                                    final BaseEnokiResponse<NonceResponse> nonceResponse;
                                    if (response.isSuccessful()) {
                                        nonceResponse =
                                                jsonHandler.fromNonce(Objects.requireNonNull(responseBody).string());
                                        future.complete(nonceResponse);
                                    } else if (response.code() == 403) {
                                        future.completeExceptionally(new SuiApiException(new HttpForbiddenException()));
                                    }
                                } catch (Throwable throwable) {
                                    future.completeExceptionally(throwable);
                                }
                            }
                        });

        return future;
    }

    public CompletableFuture<BaseEnokiResponse<ZkLoginResponse>> requestZkLogin(String jwt) {
        final CompletableFuture<BaseEnokiResponse<ZkLoginResponse>> future = new CompletableFuture<>();
        final Request okhttpRequest;
        try {
            okhttpRequest =
                    new Request.Builder()
                            .header("zklogin-jwt", jwt)
                            .header("Authorization", String.format("Bearer %s", apiKey))
                            .url(String.format("%s/v1/zklogin", this.baseUrl))
                            .get()
                            .build();
        } catch (Throwable throwable) {
            future.completeExceptionally(throwable);
            return future;
        }

        this.httpClient
                .newCall(okhttpRequest)
                .enqueue(
                        new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                future.completeExceptionally(e);
                            }

                            @Override
                            public void onResponse(Call call, @NotNull Response response) {
                                try {
                                    final ResponseBody responseBody = response.body();
                                    final BaseEnokiResponse<ZkLoginResponse> zkLoginResponse;
                                    if (response.isSuccessful()) {
                                        zkLoginResponse =
                                                jsonHandler.fromZkLogin(Objects.requireNonNull(responseBody).string());
                                        future.complete(zkLoginResponse);
                                    } else if (response.code() == 403) {
                                        future.completeExceptionally(new SuiApiException(new HttpForbiddenException()));
                                    }
                                } catch (Throwable throwable) {
                                    future.completeExceptionally(throwable);
                                }
                            }
                        });

        return future;
    }

    @Override
    public CompletableFuture<BaseEnokiResponse<ZkProofResponse>> requestZkProof(ZkProofRequest request) {
        final CompletableFuture<BaseEnokiResponse<ZkProofResponse>> future = new CompletableFuture<>();
        final Request okhttpRequest;
        try {
            final RequestBody requestBody =
                    RequestBody.create(request.toString(), MediaType.get("application/json; charset=utf-8"));
            okhttpRequest =
                    new Request.Builder()
                            .header("Authorization", String.format("Bearer %s", apiKey))
                            .header("zklogin-jwt", request.getJwt())
                            .url(String.format("%s/v1/zklogin/zkp", this.baseUrl))
                            .post(requestBody)
                            .build();
        } catch (Throwable throwable) {
            future.completeExceptionally(throwable);
            return future;
        }

        this.httpClient
                .newCall(okhttpRequest)
                .enqueue(
                        new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                future.completeExceptionally(e);
                            }

                            @Override
                            public void onResponse(Call call, @NotNull Response response) {
                                try {
                                    final ResponseBody responseBody = response.body();
                                    final BaseEnokiResponse<ZkProofResponse> proofResponse;
                                    if (response.isSuccessful()) {
                                        proofResponse =
                                                jsonHandler.fromProof(Objects.requireNonNull(responseBody).string());
                                        future.complete(proofResponse);
                                    } else if (response.code() == 403) {
                                        future.completeExceptionally(new SuiApiException(new HttpForbiddenException()));
                                    }
                                } catch (Throwable throwable) {
                                    future.completeExceptionally(throwable);
                                }
                            }
                        });

        return future;
    }
}
