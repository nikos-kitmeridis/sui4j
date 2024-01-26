/*
 * Copyright 2022-2023 281165273grape@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package io.sui.clients;


import io.sui.json.JsonHandler;
import io.sui.models.FaucetResponse;
import io.sui.models.SuiApiException;
import io.sui.models.zklogin.SaltResponse;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * The type Okhttp faucet client.
 *
 * @author grapebaba
 * @since 2023.04
 */
public class OkhttpZkLoginClient implements ZkLoginClient {

  private final OkHttpClient httpClient;

  private final String baseUrl;

  private final JsonHandler jsonHandler;

  /**
   * Instantiates a new Okhttp faucet client.
   *
   * @param baseUrl the base url
   * @param jsonHandler the json handler
   */
  public OkhttpZkLoginClient(String baseUrl, JsonHandler jsonHandler) {
    this.jsonHandler = jsonHandler;
    this.httpClient =
        new OkHttpClient()
            .newBuilder()
            .pingInterval(Duration.ofSeconds(15))
            .writeTimeout(Duration.ofSeconds(15))
            .readTimeout(Duration.ofSeconds(15))
            .build();
    this.baseUrl = baseUrl;
  }

  /**
   * request salt.
   *
   * @param jwt the jwt returned by the ID Provider.
   * @return SaltResponse contains the salt value.
   */
  public CompletableFuture<SaltResponse> requestSalt(String jwt) {
    final CompletableFuture<SaltResponse> future = new CompletableFuture<>();
    final Request okhttpRequest;
    try {
      final String requestBodyJsonStr =
          String.format("{\"token\": \"%s\"}", jwt);
      final RequestBody requestBody =
          RequestBody.create(requestBodyJsonStr, MediaType.get("application/json; charset=utf-8"));
      okhttpRequest =
          new Request.Builder()
              .url(String.format("%s/get_salt", this.baseUrl))
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
                  final SaltResponse saltResponse;
                  if (response.isSuccessful()) {
                    saltResponse =
                        jsonHandler.fromJsonSalt(Objects.requireNonNull(responseBody).string());
                    future.complete(saltResponse);
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
