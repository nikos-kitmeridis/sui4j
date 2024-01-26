package io.sui.clients;

import io.sui.models.zklogin.SaltResponse;

import java.util.concurrent.CompletableFuture;

public interface ZkLoginClient {

    CompletableFuture<SaltResponse> requestSalt(String jwt);
}
