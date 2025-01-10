package io.sui.clients;

import io.sui.models.enoki.*;

import java.util.concurrent.CompletableFuture;

public interface EnokiClient {

    CompletableFuture<BaseEnokiResponse<NonceResponse>> requestNonce(String publicKey);

    CompletableFuture<BaseEnokiResponse<ZkLoginResponse>> requestZkLogin(String jwt);

    CompletableFuture<BaseEnokiResponse<ZkProofResponse>> requestZkProof(ZkProofRequest request);

    CompletableFuture<BaseEnokiResponse<SponsorResponse>> requestSponsor(SponsorRequest request);

    CompletableFuture<BaseEnokiResponse<SponsorExecutionResponse>> executeSponsor(SponsorExecutionRequest request);

    CompletableFuture<BaseEnokiResponse<SubnameResponse>> createSubname(String jwt, SubnameRequest request);
}