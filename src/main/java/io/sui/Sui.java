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

package io.sui;


import com.google.common.collect.Lists;
import com.google.common.primitives.Bytes;
import com.novi.serde.SerializationError;
import com.novi.serde.Tuple3;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.sui.bcsgen.Argument;
import io.sui.bcsgen.Intent;
import io.sui.bcsgen.ObjectDigest;
import io.sui.bcsgen.ObjectID;
import io.sui.bcsgen.SequenceNumber;
import io.sui.bcsgen.SuiAddress;
import io.sui.bcsgen.SuiAddress.Builder;
import io.sui.bcsgen.TransactionData;
import io.sui.bcsgen.ZkIssBase64Details;
import io.sui.bcsgen.ZkLoginSignature;
import io.sui.bcsgen.ZkLoginSignatureData;
import io.sui.bcsgen.ZkProof;
import io.sui.bcsgen.ZkProofPoints;
import io.sui.clients.BcsSerializationException;
import io.sui.clients.EnokiClient;
import io.sui.clients.ExecutionClient;
import io.sui.clients.ExecutionClientImpl;
import io.sui.clients.FaucetClient;
import io.sui.clients.OkhttpEnokiClient;
import io.sui.clients.OkhttpFaucetClient;
import io.sui.clients.QueryClient;
import io.sui.clients.QueryClientImpl;
import io.sui.clients.SubscribeClient;
import io.sui.clients.SubscribeClientImpl;
import io.sui.clients.TransactionBlock;
import io.sui.crypto.FileBasedKeyStore;
import io.sui.crypto.KeyResponse;
import io.sui.crypto.KeyStore;
import io.sui.crypto.SignatureScheme;
import io.sui.crypto.SigningException;
import io.sui.crypto.SuiKeyPair;
import io.sui.json.GsonJsonHandler;
import io.sui.json.JsonHandler;
import io.sui.jsonrpc.JsonRpcClientProvider;
import io.sui.jsonrpc.OkHttpJsonRpcClientProvider;
import io.sui.models.FaucetResponse;
import io.sui.models.SuiApiException;
import io.sui.models.coin.Balance;
import io.sui.models.coin.CoinMetadata;
import io.sui.models.coin.CoinSupply;
import io.sui.models.coin.PaginatedCoins;
import io.sui.models.enoki.BaseEnokiResponse;
import io.sui.models.enoki.NonceResponse;
import io.sui.models.enoki.ZkLoginResponse;
import io.sui.models.enoki.ZkProofRequest;
import io.sui.models.enoki.ZkProofResponse;
import io.sui.models.events.EventFilter;
import io.sui.models.events.EventId;
import io.sui.models.events.PaginatedEvents;
import io.sui.models.events.SuiEvent;
import io.sui.models.governance.DelegatedStake;
import io.sui.models.governance.SuiCommitteeInfo;
import io.sui.models.governance.SystemStateSummary;
import io.sui.models.governance.ValidatorsApy;
import io.sui.models.objects.Checkpoint;
import io.sui.models.objects.MoveFunctionArgType;
import io.sui.models.objects.MoveNormalizedFunction;
import io.sui.models.objects.MoveNormalizedModule;
import io.sui.models.objects.MoveNormalizedStruct;
import io.sui.models.objects.ObjectDataOptions;
import io.sui.models.objects.ObjectResponseQuery;
import io.sui.models.objects.PaginatedCheckpoint;
import io.sui.models.objects.PaginatedObjectsResponse;
import io.sui.models.objects.SuiObjectResponse;
import io.sui.models.transactions.ExecuteTransactionRequestType;
import io.sui.models.transactions.PaginatedTransactionBlockResponse;
import io.sui.models.transactions.TransactionBlockEffects;
import io.sui.models.transactions.TransactionBlockResponse;
import io.sui.models.transactions.TransactionBlockResponseOptions;
import io.sui.models.transactions.TransactionBlockResponseQuery;
import io.sui.models.transactions.TransactionFilter;
import io.sui.models.transactions.TypeTag;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.jcajce.provider.digest.Blake2b.Blake2b256;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Base64;

/**
 * The type Sui.
 *
 * @author grapebaba
 * @since 2022.11
 */
public class Sui {

  private final KeyStore keyStore;

  private final QueryClient queryClient;

  private final ExecutionClient executionClient;

  private final SubscribeClient subscribeClient;

  private final FaucetClient faucetClient;

  private final Optional<EnokiClient> enokiClient;

  /**
   * Instantiates a new Sui.
   *
   * @param fullNodeEndpoint the full node endpoint
   * @param faucetEndpoint the faucet endpoint
   * @param keyStorePath the key store path
   */
  public Sui(String fullNodeEndpoint, String faucetEndpoint, String keyStorePath) {
      this.keyStore = new FileBasedKeyStore(keyStorePath);
      final JsonHandler jsonHandler = new GsonJsonHandler();
      final JsonRpcClientProvider jsonRpcClientProvider =
              new OkHttpJsonRpcClientProvider(fullNodeEndpoint, jsonHandler);
      this.queryClient = new QueryClientImpl(jsonRpcClientProvider);
      this.executionClient = new ExecutionClientImpl(jsonRpcClientProvider);
      this.subscribeClient = new SubscribeClientImpl(jsonRpcClientProvider);
      this.faucetClient = new OkhttpFaucetClient(faucetEndpoint, jsonHandler);
      this.enokiClient = Optional.empty();
  }

  public Sui(String fullNodeEndpoint, String faucetEndpoint, String keyStorePath, String enokiServerEndpoint, String enokiApiKey) {
    this.keyStore = new FileBasedKeyStore(keyStorePath);
    final JsonHandler jsonHandler = new GsonJsonHandler();
    final JsonRpcClientProvider jsonRpcClientProvider =
        new OkHttpJsonRpcClientProvider(fullNodeEndpoint, jsonHandler);
    this.queryClient = new QueryClientImpl(jsonRpcClientProvider);
    this.executionClient = new ExecutionClientImpl(jsonRpcClientProvider);
    this.subscribeClient = new SubscribeClientImpl(jsonRpcClientProvider);
    this.faucetClient = new OkhttpFaucetClient(faucetEndpoint, jsonHandler);
    this.enokiClient = Optional.of(new OkhttpEnokiClient(enokiServerEndpoint, enokiApiKey, jsonHandler));
  }

  /**
   * Request sui from faucet completable future.
   *
   * @param address the address
   * @return the completable future
   */
  public CompletableFuture<FaucetResponse> requestSuiFromFaucet(String address) {
    return this.faucetClient.requestSuiFromFaucet(address);
  }

    public CompletableFuture<BaseEnokiResponse<NonceResponse>> requestNonce(String publicKey) {
        return this.enokiClient.get().requestNonce(publicKey);
    }
    public CompletableFuture<BaseEnokiResponse<ZkLoginResponse>> requestZkLogin(String jwt) {
        return this.enokiClient.get().requestZkLogin(jwt);
    }

    public CompletableFuture<BaseEnokiResponse<ZkProofResponse>> requestZkProof(ZkProofRequest proofRequest) {
        return this.enokiClient.get().requestZkProof(proofRequest);
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }

    /**
   * New address key response.
   *
   * @param signatureScheme the signature scheme
   * @return the key response
   */
  public KeyResponse newAddress(SignatureScheme signatureScheme) {
    return this.keyStore.generateNewKey(signatureScheme);
  }

  /**
   * Transfer sui completable future.
   *
   * @param sender the signer
   * @param coin the coin
   * @param recipient the recipient
   * @param amount the amount
   * @param gas the gas
   * @param gasBudget the gas budget
   * @param gasPrice the gas price
   * @param expiration the expiration
   * @param transactionBlockResponseOptions the transaction response options
   * @param requestType the request type
   * @return the completable future
   */
  public CompletableFuture<TransactionBlockResponse> transferSui(
      String sender,
      String coin,
      String recipient,
      Long amount,
      String gas,
      Long gasBudget,
      Long gasPrice,
      Long expiration,
      TransactionBlockResponseOptions transactionBlockResponseOptions,
      ExecuteTransactionRequestType requestType) {

      Function<TransactionBlock, CompletableFuture<TransactionBlockResponse>> transactionBlockCompletableFutureFunction =
              transactionBlock -> getTransactionBlockResponseCompletableFuture(
              sender, coin, recipient, amount, gas,
              gasBudget, gasPrice, expiration,
              transactionBlockResponseOptions,
              requestType, transactionBlock);

      return this.newTransactionBlock().thenCompose(transactionBlockCompletableFutureFunction);
  }

    private CompletableFuture<TransactionBlockResponse> getTransactionBlockResponseCompletableFuture(String sender, String coin, String recipient, Long amount, String gas, Long gasBudget, Long gasPrice, Long expiration, TransactionBlockResponseOptions transactionBlockResponseOptions, ExecuteTransactionRequestType requestType, TransactionBlock transactionBlock) {
        transactionBlock.setExpiration(expiration);
        transactionBlock.setSender(sender);
        Function<Argument, CompletableFuture<TransactionBlockResponse>> argumentCompletableFutureFunction =
                argument -> getTransactionBlockResponseCompletableFuture(
                        sender, recipient, gas, gasBudget,
                        gasPrice, transactionBlockResponseOptions,
                        requestType, transactionBlock, argument);
        return transactionBlock
                .splitCoins(coin, Lists.newArrayList(amount))
                .thenCompose(argumentCompletableFutureFunction);
    }

    private CompletableFuture<TransactionBlockResponse> getTransactionBlockResponseCompletableFuture(String sender, String recipient, String gas, Long gasBudget, Long gasPrice, TransactionBlockResponseOptions transactionBlockResponseOptions, ExecuteTransactionRequestType requestType, TransactionBlock transactionBlock, Argument argument) {
        Builder recipientAddressBuilder = new Builder();
        recipientAddressBuilder.value = transactionBlock.geAddressBytes(recipient);
        transactionBlock.transferObjects(
                Lists.newArrayList(argument),
                transactionBlock.pure(recipientAddressBuilder.build()));
        CompletableFuture<TransactionData> transactionDataCompletableFuture = transactionBlock
                        .setGasData(
                                gas != null ? Lists.newArrayList(gas) : Lists.newArrayList(),
                                sender,
                                gasBudget,
                                gasPrice)
                        .thenCompose((Function<Void, CompletableFuture<TransactionData>>) unused -> transactionBlock.build());

        return transactionDataCompletableFuture.thenCompose(
                (Function<TransactionData, CompletableFuture<TransactionBlockResponse>>)
                        transactionData ->
                                executeTransaction(
                                        sender, transactionData,
                                        transactionBlockResponseOptions, requestType));
    }

    public TransactionData transferSuiTransactionData(String sender,
                                                    String coin,
                                                    String recipient,
                                                    Long amount,
                                                    String gas,
                                                    Long gasBudget,
                                                    Long gasPrice,
                                                    Long expiration,
                                                    Tuple3<ObjectID, SequenceNumber, ObjectDigest> gasObj) {
      try {
          TransactionBlock transactionBlock = this.newTransactionBlock().get();
          transactionBlock.setExpiration(expiration);
          transactionBlock.setSender(sender);
          Argument argument = transactionBlock.splitCoins(coin, Lists.newArrayList(amount)).get();
          SuiAddress.Builder recipientAddressBuilder = new Builder();
          recipientAddressBuilder.value = transactionBlock.geAddressBytes(recipient);
          transactionBlock.transferObjects(
                  Lists.newArrayList(argument),
                  transactionBlock.pure(recipientAddressBuilder.build()));
          TransactionData transactionData =
                  transactionBlock
                          .setGasData(
                                  gas != null
                                          ? Lists.newArrayList(gas)
                                          : Lists.newArrayList(),
                                  sender,
                                  gasBudget,
                                  gasPrice)
                          .thenCompose(
                                  (Function<Void, CompletableFuture<TransactionData>>)
                                          unused -> transactionBlock.build(gasObj)).get();
          return transactionData;
      } catch (Exception e) {
          throw new RuntimeException(e);
      }
  }

  public String getZkLoginSignature(ZkProofResponse zkProofResponse, String maxEpoch, String userSignature) {
      try {
          ZkProof zkProof = new ZkProof(
                  new ZkProofPoints(
                          zkProofResponse.getProofPoints().getA(),
                          zkProofResponse.getProofPoints().getB(),
                          zkProofResponse.getProofPoints().getC()),
                  new ZkIssBase64Details(
                          zkProofResponse.getIssBase64Details().getValue(),
                          zkProofResponse.getIssBase64Details().getIndexMod4()),
                  zkProofResponse.getHeaderBase64(),
                  zkProofResponse.getAddressSeed()
          );
          ZkLoginSignatureData zkLoginSignatureData = new ZkLoginSignatureData(
                  zkProof,
                  maxEpoch,
                  java.util.Arrays.asList(ArrayUtils.toObject(java.util.Base64.getDecoder().decode(userSignature))));
          ZkLoginSignature zkLoginSignature = new ZkLoginSignature(zkLoginSignatureData);
          byte[] bytes = zkLoginSignature.bcsSerialize();
          byte[] flaggedSignature = Bytes.concat(new byte[] {SignatureScheme.ZkLogin.getScheme()}, bytes);
          return Base64.toBase64String(flaggedSignature);
      } catch (Exception e) {
          throw new RuntimeException(e);
      }
  }

  /**
   * New transaction block completable future.
   *
   * @return the completable future
   */
  public CompletableFuture<TransactionBlock> newTransactionBlock() {
    return CompletableFuture.completedFuture(new TransactionBlock(queryClient));
  }

  /**
   * Move call completable future.
   *
   * @param sender the signer
   * @param packageObjectId the package object id
   * @param module the module
   * @param function the function
   * @param typeArguments the type arguments
   * @param arguments the arguments
   * @param gas the gas
   * @param gasBudget the gas budget
   * @param gasPrice the gas price
   * @param expiration the expiration
   * @param transactionBlockResponseOptions the transaction response options
   * @param requestType the request type
   * @return the completable future
   */
  public CompletableFuture<TransactionBlockResponse> moveCall(
      String sender,
      String packageObjectId,
      String module,
      String function,
      List<TypeTag> typeArguments,
      List<?> arguments,
      String gas,
      Long gasBudget,
      Long gasPrice,
      Long expiration,
      TransactionBlockResponseOptions transactionBlockResponseOptions,
      ExecuteTransactionRequestType requestType) {
    return this.newTransactionBlock()
        .thenCompose(
            (Function<TransactionBlock, CompletableFuture<TransactionBlockResponse>>)
                transactionBlock -> {
                  transactionBlock.setExpiration(expiration);
                  transactionBlock.setSender(sender);
                  return transactionBlock
                      .moveCall(packageObjectId, module, function, typeArguments, arguments)
                      .thenCompose(
                          (Function<Argument, CompletableFuture<TransactionBlockResponse>>)
                              argument -> {
                                CompletableFuture<TransactionData>
                                    transactionDataCompletableFuture =
                                        transactionBlock
                                            .setGasData(
                                                gas != null
                                                    ? Lists.newArrayList(gas)
                                                    : Lists.newArrayList(),
                                                sender,
                                                gasBudget,
                                                gasPrice)
                                            .thenCompose(
                                                (Function<Void, CompletableFuture<TransactionData>>)
                                                    unused -> transactionBlock.build());

                                return transactionDataCompletableFuture.thenCompose(
                                    (Function<
                                            TransactionData,
                                            CompletableFuture<TransactionBlockResponse>>)
                                        transactionData ->
                                            executeTransaction(
                                                sender, transactionData,
                                                transactionBlockResponseOptions, requestType));
                              });
                });
  }

  /**
   * Merge coin completable future.
   *
   * @param sender the sender
   * @param destCoin the dest coin
   * @param sourceCoins the source coins
   * @param gas the gas
   * @param gasBudget the gas budget
   * @param gasPrice the gas price
   * @param expiration the expiration
   * @param transactionBlockResponseOptions the transaction response options
   * @param requestType the request type
   * @return the completable future
   */
  public CompletableFuture<TransactionBlockResponse> mergeCoin(
      String sender,
      String destCoin,
      List<String> sourceCoins,
      String gas,
      Long gasBudget,
      Long gasPrice,
      Long expiration,
      TransactionBlockResponseOptions transactionBlockResponseOptions,
      ExecuteTransactionRequestType requestType) {
    return this.newTransactionBlock()
        .thenCompose(
            (Function<TransactionBlock, CompletableFuture<TransactionBlockResponse>>)
                transactionBlock -> {
                  transactionBlock.setExpiration(expiration);
                  transactionBlock.setSender(sender);
                  return transactionBlock
                      .mergeCoins(destCoin, sourceCoins)
                      .thenCompose(
                          (Function<Argument, CompletableFuture<TransactionBlockResponse>>)
                              argument -> {
                                CompletableFuture<TransactionData>
                                    transactionDataCompletableFuture =
                                        transactionBlock
                                            .setGasData(
                                                gas != null
                                                    ? Lists.newArrayList(gas)
                                                    : Lists.newArrayList(),
                                                sender,
                                                gasBudget,
                                                gasPrice)
                                            .thenCompose(
                                                (Function<Void, CompletableFuture<TransactionData>>)
                                                    unused -> transactionBlock.build());

                                return transactionDataCompletableFuture.thenCompose(
                                    (Function<
                                            TransactionData,
                                            CompletableFuture<TransactionBlockResponse>>)
                                        transactionData ->
                                            executeTransaction(
                                                sender, transactionData,
                                                transactionBlockResponseOptions, requestType));
                              });
                });
  }

  /**
   * Transfer objects completable future.
   *
   * @param sender the sender
   * @param suiObjects the sui objects
   * @param recipient the recipient
   * @param gas the gas
   * @param gasBudget the gas budget
   * @param gasPrice the gas price
   * @param expiration the expiration
   * @param transactionBlockResponseOptions the transaction response options
   * @param requestType the request type
   * @return the completable future
   */
  public CompletableFuture<TransactionBlockResponse> transferObjects(
      String sender,
      List<String> suiObjects,
      String recipient,
      String gas,
      Long gasBudget,
      Long gasPrice,
      Long expiration,
      TransactionBlockResponseOptions transactionBlockResponseOptions,
      ExecuteTransactionRequestType requestType) {
    return this.newTransactionBlock()
        .thenCompose(
            (Function<TransactionBlock, CompletableFuture<TransactionBlockResponse>>)
                transactionBlock -> {
                  transactionBlock.setExpiration(expiration);
                  transactionBlock.setSender(sender);
                  return transactionBlock
                      .transferObjects(suiObjects, recipient)
                      .thenCompose(
                          (Function<Argument, CompletableFuture<TransactionBlockResponse>>)
                              argument -> {
                                CompletableFuture<TransactionData>
                                    transactionDataCompletableFuture =
                                        transactionBlock
                                            .setGasData(
                                                gas != null
                                                    ? Lists.newArrayList(gas)
                                                    : Lists.newArrayList(),
                                                sender,
                                                gasBudget,
                                                gasPrice)
                                            .thenCompose(
                                                (Function<Void, CompletableFuture<TransactionData>>)
                                                    unused -> transactionBlock.build());

                                return transactionDataCompletableFuture.thenCompose(
                                    (Function<
                                            TransactionData,
                                            CompletableFuture<TransactionBlockResponse>>)
                                        transactionData ->
                                            executeTransaction(
                                                sender, transactionData,
                                                transactionBlockResponseOptions, requestType));
                              });
                });
  }

  /**
   * Publish completable future.
   *
   * @param sender the signer
   * @param compiledModules the compiled modules
   * @param depIds the dep ids
   * @param gas the gas
   * @param gasBudget the gas budget
   * @param gasPrice the gas price
   * @param expiration the expiration
   * @param transactionBlockResponseOptions the transaction response options
   * @param requestType the request type
   * @return the completable future
   */
  public CompletableFuture<TransactionBlockResponse> publish(
      String sender,
      List<String> compiledModules,
      List<String> depIds,
      String gas,
      Long gasBudget,
      Long gasPrice,
      Long expiration,
      TransactionBlockResponseOptions transactionBlockResponseOptions,
      ExecuteTransactionRequestType requestType) {

    return this.newTransactionBlock()
        .thenCompose(
            (Function<TransactionBlock, CompletableFuture<TransactionBlockResponse>>)
                transactionBlock -> {
                  transactionBlock.setExpiration(expiration);
                  transactionBlock.setSender(sender);
                  Argument result = transactionBlock.publish(compiledModules, depIds);
                  SuiAddress.Builder senderAddressBuilder = new Builder();
                  senderAddressBuilder.value = transactionBlock.geAddressBytes(sender);
                  transactionBlock.transferObjects(
                      Lists.newArrayList(result),
                      transactionBlock.pure(senderAddressBuilder.build()));

                  CompletableFuture<TransactionData> transactionDataCompletableFuture =
                      transactionBlock
                          .setGasData(
                              gas != null ? Lists.newArrayList(gas) : Lists.newArrayList(),
                              sender,
                              gasBudget,
                              gasPrice)
                          .thenCompose(
                              (Function<Void, CompletableFuture<TransactionData>>)
                                  unused -> transactionBlock.build());

                  return transactionDataCompletableFuture.thenCompose(
                      (Function<TransactionData, CompletableFuture<TransactionBlockResponse>>)
                          transactionData ->
                              executeTransaction(
                                  sender, transactionData,
                                  transactionBlockResponseOptions, requestType));
                });
  }

  /**
   * Subscribe event disposable.
   *
   * @param eventFilter the event filter
   * @param onNext the on next
   * @param onError the on error
   * @return the disposable
   */
  public Disposable subscribeEvent(
      EventFilter eventFilter, Consumer<SuiEvent> onNext, Consumer<SuiApiException> onError) {
    return this.subscribeClient.subscribeEvent(eventFilter, onNext, onError);
  }

  /**
   * Subscribe transaction disposable.
   *
   * @param transactionFilter the transaction filter
   * @param onNext the on next
   * @param onError the on error
   * @return the disposable
   */
  public Disposable subscribeTransaction(
      TransactionFilter transactionFilter,
      Consumer<TransactionBlockEffects> onNext,
      Consumer<SuiApiException> onError) {
    return this.subscribeClient.subscribeTransaction(transactionFilter, onNext, onError);
  }

  /**
   * Gets object.
   *
   * @param id the id
   * @param objectDataOptions the object data options
   * @return the object
   */
  public CompletableFuture<SuiObjectResponse> getObject(
      String id, ObjectDataOptions objectDataOptions) {
    return queryClient.getObject(id, objectDataOptions);
  }

  /**
   * Gets objects owned by address.
   *
   * @param address the address
   * @param query the query
   * @param cursor the cursor
   * @param limit the limit
   * @return the objects owned by address
   */
  public CompletableFuture<PaginatedObjectsResponse> getOwnedObjects(
      String address, ObjectResponseQuery query, String cursor, Integer limit) {
    return queryClient.getOwnedObjects(address, query, cursor, limit);
  }

  /**
   * Gets total transaction number.
   *
   * @return the total transaction number
   */
  public CompletableFuture<Long> getTotalTransactionBlocks() {
    return queryClient.getTotalTransactionBlocks();
  }

  /**
   * Gets transaction.
   *
   * @param digest the digest
   * @param options the options
   * @return the transaction
   */
  public CompletableFuture<TransactionBlockResponse> getTransactionBlock(
      String digest, TransactionBlockResponseOptions options) {
    return queryClient.getTransactionBlock(digest, options);
  }

  /**
   * Query transaction blocks completable future.
   *
   * @param query the query
   * @param cursor the cursor
   * @param limit the limit
   * @param isDescOrder the is desc order
   * @return the completable future
   */
  public CompletableFuture<PaginatedTransactionBlockResponse> queryTransactionBlocks(
      TransactionBlockResponseQuery query, String cursor, Integer limit, boolean isDescOrder) {
    return queryClient.queryTransactionBlocks(query, cursor, limit, isDescOrder);
  }

  /**
   * Multi get transaction blocks completable future.
   *
   * @param digests the digests
   * @param options the options
   * @return the completable future
   */
  public CompletableFuture<List<TransactionBlockResponse>> multiGetTransactionBlocks(
      List<String> digests, TransactionBlockResponseOptions options) {
    return queryClient.multiGetTransactionBlocks(digests, options);
  }

  /**
   * Multi get objects completable future.
   *
   * @param objectIds the object ids
   * @param options the options
   * @return the completable future
   */
  public CompletableFuture<List<SuiObjectResponse>> multiGetObjects(
      List<String> objectIds, ObjectDataOptions options) {
    return queryClient.multiGetObjects(objectIds, options);
  }

  /**
   * Gets events.
   *
   * @param eventFilter the event filter
   * @param cursor the cursor
   * @param limit the limit
   * @param isDescOrder the is desc order
   * @return the events
   */
  public CompletableFuture<PaginatedEvents> queryEvents(
      EventFilter eventFilter, EventId cursor, Integer limit, boolean isDescOrder) {
    return queryClient.queryEvents(eventFilter, cursor, limit, isDescOrder);
  }

  /**
   * Gets normalized move modules by package.
   *
   * @param packageId the package id
   * @return the normalized move modules by package
   */
  public CompletableFuture<Map<String, MoveNormalizedModule>> getNormalizedMoveModulesByPackage(
      String packageId) {
    return queryClient.getNormalizedMoveModulesByPackage(packageId);
  }

  /**
   * Gets committee info.
   *
   * @param epoch the epoch
   * @return the committee info
   */
  public CompletableFuture<SuiCommitteeInfo> getCommitteeInfo(BigInteger epoch) {
    return queryClient.getCommitteeInfo(epoch);
  }

  /**
   * Gets move function arg types.
   *
   * @param suiPackage the sui package
   * @param module the module
   * @param function the function
   * @return the move function arg types
   */
  public CompletableFuture<List<MoveFunctionArgType>> getMoveFunctionArgTypes(
      String suiPackage, String module, String function) {
    return queryClient.getMoveFunctionArgTypes(suiPackage, module, function);
  }

  /**
   * Gets normalized move function.
   *
   * @param suiPackage the sui package
   * @param module the module
   * @param function the function
   * @return the normalized move function
   */
  public CompletableFuture<MoveNormalizedFunction> getNormalizedMoveFunction(
      String suiPackage, String module, String function) {
    return queryClient.getNormalizedMoveFunction(suiPackage, module, function);
  }

  /**
   * Gets normalized move module.
   *
   * @param suiPackage the sui package
   * @param module the module
   * @return the normalized move module
   */
  public CompletableFuture<MoveNormalizedModule> getNormalizedMoveModule(
      String suiPackage, String module) {
    return queryClient.getNormalizedMoveModule(suiPackage, module);
  }

  /**
   * Gets normalized move struct.
   *
   * @param suiPackage the sui package
   * @param module the module
   * @param struct the struct
   * @return the normalized move struct
   */
  public CompletableFuture<MoveNormalizedStruct> getNormalizedMoveStruct(
      String suiPackage, String module, String struct) {
    return queryClient.getNormalizedMoveStruct(suiPackage, module, struct);
  }

  /**
   * Gets checkpoints.
   *
   * @param cursor the cursor
   * @param limit the limit
   * @param isDescOrder the is desc order
   * @return the checkpoints
   */
  public CompletableFuture<PaginatedCheckpoint> getCheckpoints(
      String cursor, Integer limit, boolean isDescOrder) {
    return queryClient.getCheckpoints(cursor, limit, isDescOrder);
  }

  /**
   * Gets coin metadata.
   *
   * @param coinType the coin type
   * @return the coin metadata
   */
  public CompletableFuture<CoinMetadata> getCoinMetadata(String coinType) {
    return queryClient.getCoinMetadata(coinType);
  }

  /**
   * Gets reference gas price.
   *
   * @return the reference gas price
   */
  public CompletableFuture<Long> getReferenceGasPrice() {
    return queryClient.getReferenceGasPrice();
  }

  /**
   * get all balances by address.
   *
   * @param address the sui address
   * @return the completable future
   */
  public CompletableFuture<List<Balance>> getAllBalances(String address) {
    return queryClient.getAllBalances(address);
  }

  /**
   * get all coins owned by address.
   *
   * @param address the sui address
   * @param cursor the cursor
   * @param limit the limit
   * @return the completable future
   */
  public CompletableFuture<PaginatedCoins> getAllCoins(
      String address, String cursor, Integer limit) {
    return queryClient.getAllCoins(address, cursor, limit);
  }

  /**
   * get all Coin with coin_type objects owned by an address.
   *
   * @param address the owner address
   * @param coinType the coin type
   * @param cursor the cursor
   * @param limit the limit
   * @return the completable future
   */
  public CompletableFuture<PaginatedCoins> getCoins(
      String address, String coinType, String cursor, Integer limit) {
    return queryClient.getCoins(address, coinType, cursor, limit);
  }

  /**
   * get the total coin balance for one coin type, owned by the address owner.
   *
   * @param address the owner address
   * @param coinType the coin type
   * @return the completable future
   */
  public CompletableFuture<Balance> getBalance(String address, String coinType) {
    return queryClient.getBalance(address, coinType);
  }

  /**
   * get a checkpoint based on a checkpoint sequence number or digest.
   *
   * @param checkpointId the checkpoint sequence number or digest
   * @return the completable future
   */
  public CompletableFuture<Checkpoint> getCheckpoint(String checkpointId) {
    return queryClient.getCheckpoint(checkpointId);
  }

  /**
   * Gets validators apy.
   *
   * @return the validators apy
   */
  public CompletableFuture<ValidatorsApy> getValidatorsApy() {
    return queryClient.getValidatorsApy();
  }

  /**
   * Gets total supply.
   *
   * @param coin the coin
   * @return the total supply
   */
  public CompletableFuture<CoinSupply> getTotalSupply(String coin) {
    return queryClient.getTotalSupply(coin);
  }

  /**
   * Gets stakes by ids.
   *
   * @param stakeIds the stake ids
   * @return the stakes by ids
   */
  public CompletableFuture<List<DelegatedStake>> getStakesByIds(List<String> stakeIds) {
    return queryClient.getStakesByIds(stakeIds);
  }

  /**
   * Gets stakes.
   *
   * @param owner the owner
   * @return the stakes
   */
  public CompletableFuture<List<DelegatedStake>> getStakes(String owner) {
    return queryClient.getStakes(owner);
  }

  /**
   * Gets latest sui system state.
   *
   * @return the latest sui system state
   */
  public CompletableFuture<SystemStateSummary> getLatestSuiSystemState() {
    return queryClient.getLatestSuiSystemState();
  }

  /**
   * Gets events.
   *
   * @param transactionDigest the transaction digest
   * @return the events
   */
  public CompletableFuture<List<SuiEvent>> getEvents(String transactionDigest) {
    return queryClient.getEvents(transactionDigest);
  }

  /**
   * Gets latest checkpoint sequence number.
   *
   * @return the latest checkpoint sequence number
   */
  public CompletableFuture<BigInteger> getLatestCheckpointSequenceNumber() {
    return queryClient.getLatestCheckpointSequenceNumber();
  }

  /**
   * Dry run transaction completable future.
   *
   * @param txBytes the tx bytes
   * @return the completable future
   */
  public CompletableFuture<TransactionBlockEffects> dryRunTransaction(String txBytes) {
    return executionClient.dryRunTransaction(txBytes);
  }

  /**
   * Gets by address.
   *
   * @param address the address
   * @return the by address
   */
  public SuiKeyPair<?> getByAddress(String address) {
    return keyStore.getByAddress(address);
  }

  /**
   * Addresses navigable set.
   *
   * @return the navigable set
   */
  public NavigableSet<String> addresses() {
    return keyStore.addresses();
  }

  /**
   * Execute transaction completable future.
   *
   * @param signer the signer
   * @param transactionData the transaction data
   * @param transactionBlockResponseOptions the transaction response options
   * @param requestType the request type
   * @return the completable future
   */
  public CompletableFuture<TransactionBlockResponse> executeTransaction(
      String signer,
      TransactionData transactionData,
      TransactionBlockResponseOptions transactionBlockResponseOptions,
      ExecuteTransactionRequestType requestType) {
    return this.executeTransaction(
        signer,
        transactionData,
        transactionDataIntent(),
        transactionBlockResponseOptions,
        requestType);
  }

  /**
   * Execute transaction completable future.
   *
   * @param transactionData the transaction data
   * @param signatures the signatures
   * @param transactionBlockResponseOptions the transaction block response options
   * @param requestType the request type
   * @return the completable future
   */
  public CompletableFuture<TransactionBlockResponse> executeTransaction(
      TransactionData transactionData,
      List<String> signatures,
      TransactionBlockResponseOptions transactionBlockResponseOptions,
      ExecuteTransactionRequestType requestType) {
    try {
      return executionClient.executeTransaction(
          Base64.toBase64String(transactionData.bcsSerialize()),
          signatures,
          transactionBlockResponseOptions,
          requestType);
    } catch (SerializationError e) {
      throw new BcsSerializationException(e);
    }
  }

  /**
   * Execute transaction completable future.
   *
   * @param signer the signer
   * @param transactionData the transaction data
   * @param intent the intent
   * @param transactionBlockResponseOptions the transaction response options
   * @param requestType the request type
   * @return the completable future
   */
  public CompletableFuture<TransactionBlockResponse> executeTransaction(
      String signer,
      TransactionData transactionData,
      Intent intent,
      TransactionBlockResponseOptions transactionBlockResponseOptions,
      ExecuteTransactionRequestType requestType) {
    final SuiKeyPair<?> suiKeyPair = keyStore.getByAddress(signer);
    final byte[] publicKey = suiKeyPair.publicKeyBytes();
    final SignatureScheme signatureScheme = suiKeyPair.signatureScheme();

    final byte[] txBytes;
    final byte[] intentBytes;
    try {
      txBytes = transactionData.bcsSerialize();
      intentBytes = intent.bcsSerialize();
    } catch (SerializationError e) {
      CompletableFuture<TransactionBlockResponse> future = new CompletableFuture<>();
      future.completeExceptionally(new SuiApiException(new BcsSerializationException(e)));
      return future;
    }

    return signAndExecuteTransaction(
        txBytes,
        intentBytes,
        transactionBlockResponseOptions,
        requestType,
        suiKeyPair,
        publicKey,
        signatureScheme);
  }

  /**
   * Sign transaction block string.
   *
   * @param signer the signer
   * @param transactionData the transaction data
   * @param intent the intent
   * @return the string
   * @throws SigningException the signing exception
   */
  public String signTransactionBlock(String signer, TransactionData transactionData, Intent intent)
      throws SigningException {
    final SuiKeyPair<?> suiKeyPair = keyStore.getByAddress(signer);
    final byte[] publicKey = suiKeyPair.publicKeyBytes();
    final SignatureScheme signatureScheme = suiKeyPair.signatureScheme();

    final byte[] txBytes;
    final byte[] intentBytes;
    try {
      txBytes = transactionData.bcsSerialize();
      intentBytes = intent.bcsSerialize();

      final byte[] signature;
      final Blake2b256 blake2b256 = new Blake2b256();
      final byte[] hash = blake2b256.digest(Arrays.concatenate(intentBytes, txBytes));
      signature = suiKeyPair.sign(hash);

      final byte[] serializedSignatureBytes =
          Arrays.concatenate(new byte[] {signatureScheme.getScheme()}, signature, publicKey);
      return Base64.toBase64String(serializedSignatureBytes);
    } catch (SerializationError e) {
      throw new BcsSerializationException(e);
    }
  }

  private CompletableFuture<TransactionBlockResponse> signAndExecuteTransaction(
      byte[] transactionData,
      byte[] intentBytes,
      TransactionBlockResponseOptions transactionBlockResponseOptions,
      ExecuteTransactionRequestType requestType,
      SuiKeyPair<?> suiKeyPair,
      byte[] publicKey,
      SignatureScheme signatureScheme) {
    final byte[] signature;
    try {
      final Blake2b256 blake2b256 = new Blake2b256();
      final byte[] hash = blake2b256.digest(Arrays.concatenate(intentBytes, transactionData));
      signature = suiKeyPair.sign(hash);

    } catch (SigningException e) {
      CompletableFuture<TransactionBlockResponse> future = new CompletableFuture<>();
      future.completeExceptionally(new SuiApiException(e));
      return future;
    }

    final byte[] serializedSignatureBytes =
        Arrays.concatenate(new byte[] {signatureScheme.getScheme()}, signature, publicKey);
    final String serializedSignature = Base64.toBase64String(serializedSignatureBytes);

      return executeTransaction(transactionData, transactionBlockResponseOptions, requestType, serializedSignature);
  }

    public CompletableFuture<TransactionBlockResponse> executeTransaction(
            byte[] transactionData,
            TransactionBlockResponseOptions transactionBlockResponseOptions,
            ExecuteTransactionRequestType requestType,
            String serializedSignature) {
        return executionClient.executeTransaction(
                Base64.toBase64String(transactionData),
                Lists.newArrayList(serializedSignature),
                transactionBlockResponseOptions,
                requestType);
    }

    /**
   * Transaction data intent intent.
   *
   * @return the intent
   */
  public Intent transactionDataIntent() {
    final Intent.Builder intentBuilder = new Intent.Builder();
    intentBuilder.app_id = 0;
    intentBuilder.scope = 0;
    intentBuilder.version = 0;

    return intentBuilder.build();
  }
}
