package tokamak.staking.app;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableNativeArray;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.google.gson.Gson;
import com.samsung.android.sdk.blockchain.*;
import com.samsung.android.sdk.blockchain.coinservice.TransactionResult;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumTokenInfo;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumTransaction;
import com.samsung.android.sdk.blockchain.exception.AvailabilityException;
import com.samsung.android.sdk.blockchain.network.NetworkType;
import com.samsung.android.sdk.blockchain.wallet.HardwareWallet;
import com.samsung.android.sdk.blockchain.account.Account;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.SBlockchain;
import com.samsung.android.sdk.blockchain.account.AccountManager;
import com.samsung.android.sdk.blockchain.coinservice.CoinNetworkInfo;
import com.samsung.android.sdk.blockchain.coinservice.CoinServiceFactory;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumFeeInfo;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumUtils;
import com.samsung.android.sdk.blockchain.exception.SsdkUnsupportedException;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletManager;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;
import com.samsung.android.sdk.blockchain.network.EthereumNetworkType;
import com.samsung.android.sdk.blockchain.exception.AccountException;
import com.samsung.android.sdk.blockchain.exception.RemoteClientException;
import com.samsung.android.sdk.blockchain.exception.RootSeedChangedException;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import android.telecom.Call;
import android.util.Log;
import android.content.Context;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONException;
import org.json.JSONObject;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.generated.AbiTypes;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.jetbrains.annotations.NotNull;
import org.web3j.abi.datatypes.generated.Bytes16;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Bytes8;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.Filter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.utils.Bytes;
import org.web3j.utils.Numeric;

import java8.util.Optional;
import jnr.ffi.StructLayout;

public class BlockchainModule extends ReactContextBaseJavaModule {
    public EthereumAccount ethereumAccount;
    private SBlockchain mSBlockchain;
    private HardwareWalletManager hardwareWalletManager;
    private AccountManager accountManager;
    private Boolean loaded;
    private HardwareWallet hardwareWallet;
    private CoinNetworkInfo coinNetworkInfo;
    private EthereumService etherService;
    private BigDecimal balanceInEther;
    private Web3j web3;
    public static String rpcUrl = "https://mainnet.infura.io/v3/e4b3b2781dd34bc4817a1221b8a3b50a";
    public String results;

    BlockchainModule(ReactApplicationContext context) {
        super(context);
    }

    @Override
    public String getName() {
        return "BlockchainModule";
    }

    @ReactMethod
    public void connect(Promise promise) {
        Web3j web3j = Web3j.build(new HttpService(rpcUrl));
        try {
            Web3ClientVersion clientVersion = web3j.web3ClientVersion().send();
            if (!clientVersion.hasError()) {
                // Connected
                web3 = web3j;
            } else {

                // Show Error
            }
        } catch (Exception e) {
            // Show Error

        }
    }

    @ReactMethod
    public void getBlockNumber(Promise promise) throws ExecutionException, InterruptedException, IOException {
        EthBlockNumber block = web3.ethBlockNumber().sendAsync().get();
        Integer blockNumber = block.getBlockNumber().intValue();
        EthBlock.Block ethBlock = web3
                .ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber)), false).send()
                .getBlock();
        promise.resolve(block.getBlockNumber().intValue());
    }

    @ReactMethod
    public void getTimeStamp(Integer blockNumber, Promise promise)
            throws ExecutionException, InterruptedException, IOException {
        EthBlock.Block ethBlock = web3
                .ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber)), false).send()
                .getBlock();
        promise.resolve(ethBlock.getTimestamp().intValue());
    }

    @ReactMethod
    public void getPastEvents(String address, String encoded, Promise promise)
            throws ExecutionException, InterruptedException, JSONException {
        Gson g = new Gson();
        EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, address);
        filter.addSingleTopic(encoded);
        EthLog ethLog = web3.ethGetLogs(filter).sendAsync().get();
        WritableArray array = new WritableNativeArray();
        for (Object logs : ethLog.getLogs()) {
            JSONObject jo = new JSONObject(g.toJson(logs));
            WritableMap wm = convertJsonToMap(jo);
            array.pushMap(wm);
        }
        promise.resolve(array);
    }
    @ReactMethod
    private void estimateGasLimit(String address, Promise promise) throws ExecutionException, InterruptedException {


        Context context = getReactApplicationContext();
        EthereumService etherService = (EthereumService) CoinServiceFactory.getCoinService(context, coinNetworkInfo);
        etherService.estimateGasLimit(ethereumAccount,address, null, null ).setCallback(new ListenableFutureTask.Callback<BigInteger>() {
            @Override
            public void onSuccess(BigInteger bigInteger) {
                promise.resolve(bigInteger.floatValue());
            }

            @Override
            public void onFailure(@NotNull ExecutionException e) {

            }

            @Override
            public void onCancelled(@NotNull InterruptedException e) {

            }
        });
    }
    @ReactMethod
    private void getTransactionDetails (String txId, Promise promise) throws ExecutionException, InterruptedException {
        EthGetTransactionReceipt transactionReceipt  = web3.ethGetTransactionReceipt(txId).sendAsync().get();
        Optional<TransactionReceipt> receipt = transactionReceipt.getTransactionReceipt();
        if (transactionReceipt.getTransactionReceipt().isPresent() ) {
            WritableMap infoMap = Arguments.createMap();
            infoMap.putString("status", receipt.get().getStatus());
            infoMap.putString("blockHash", receipt.get().getBlockHash());
            infoMap.putInt("blockNumber", receipt.get().getBlockNumber().intValue());
            infoMap.putString("transactionHash", receipt.get().getTransactionHash());
            infoMap.putString("transactionIndex", receipt.get().getTransactionIndex().toString());
            infoMap.putString("from", receipt.get().getFrom());
            infoMap.putString("to", receipt.get().getTo());
            infoMap.putString("contractAddress", receipt.get().getContractAddress());
            infoMap.putInt("cumulativeGasUsed", receipt.get().getCumulativeGasUsed().intValue());
            infoMap.putInt("gasUsed", receipt.get().getGasUsed().intValue());
            promise.resolve(infoMap);
        }
        else {
            promise.resolve(null);
        }
    }
    @ReactMethod
private void esitmatedGasLimitForDelegate (String toContractAddress, String function, String input1, String input2, String input3,
                                           Promise promise) throws ExecutionException, InterruptedException, DecoderException {
    String encodedFunction = convertFunction(function, input1, input2, input3);
    EthEstimateGas estimatedLimit = web3.ethEstimateGas(Transaction.createEthCallTransaction(ethereumAccount.getAddress(),toContractAddress, encodedFunction)).sendAsync().get();
        promise.resolve(estimatedLimit.getResult());
}
    @ReactMethod
    private void esitmatedGasLimitForRequestWithdrawal (String toContractAddress, String function, String input1, String input2,
                                                        Promise promise) throws ExecutionException, InterruptedException, DecoderException {
        String encodedFunction = convertFunctionWithdrawal(function, input1, input2);
        EthEstimateGas estimatedLimit = web3.ethEstimateGas(Transaction.createEthCallTransaction(ethereumAccount.getAddress(),toContractAddress, encodedFunction)).sendAsync().get();
        promise.resolve(estimatedLimit.getResult());
    }

    @ReactMethod
    private void esitmatedGasLimitForWithdraw (String toContractAddress, String function, String input1, String input2, Boolean input3,
                                                 Promise promise) throws ExecutionException, InterruptedException, DecoderException {
        String encodedFunction = convertWithdraw(function, input1, input2, input3);
        EthEstimateGas estimatedLimit = web3.ethEstimateGas(Transaction.createEthCallTransaction(ethereumAccount.getAddress(),toContractAddress, encodedFunction)).sendAsync().get();
        promise.resolve(estimatedLimit.getResult());
    }
    private static WritableMap convertJsonToMap(JSONObject jsonObject) throws JSONException {
        WritableMap map = new WritableNativeMap();

        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                map.putMap(key, convertJsonToMap((JSONObject) value));
            } else if (value instanceof Boolean) {
                map.putBoolean(key, (Boolean) value);
            } else if (value instanceof Integer) {
                map.putInt(key, (Integer) value);
            } else if (value instanceof Double) {
                map.putDouble(key, (Double) value);
            } else if (value instanceof String) {
                map.putString(key, (String) value);
            } else {
                map.putString(key, value.toString());
            }
        }
        return map;
    }

    @ReactMethod
    public void initis(Callback callBack) {
        Context context = getReactApplicationContext();
        try {
            mSBlockchain = new SBlockchain();
            mSBlockchain.initialize(context);
            accountManager = mSBlockchain.getAccountManager();

            hardwareWalletManager = mSBlockchain.getHardwareWalletManager();
            coinNetworkInfo = new CoinNetworkInfo(CoinType.ETH, EthereumNetworkType.MAINNET, rpcUrl);
            setCoinNetworkInfo(coinNetworkInfo);
            ListenableFutureTask<HardwareWallet> connectionTask = mSBlockchain.getHardwareWalletManager()
                    .connect(HardwareWalletType.SAMSUNG, true);
            connectionTask.setCallback(new ListenableFutureTask.Callback<HardwareWallet>() {
                @Override
                public void onSuccess(HardwareWallet hardwareWallet) {
                    hardwareWallet = hardwareWalletManager.getConnectedHardwareWallet();
                    setHardwareWallet(hardwareWallet);
                    callBack.invoke(true);
                }
                @Override
                public void onFailure(@NotNull ExecutionException e) {
                    callBack.invoke(false);
                    e.printStackTrace();
                }

                @Override
                public void onCancelled(@NotNull InterruptedException e)
                {
                    e.printStackTrace();
                }
            });
        } catch (SsdkUnsupportedException e) {
        }
    }

    public void setCoinNetworkInfo(CoinNetworkInfo coinNetworkInfo) {
        this.coinNetworkInfo = coinNetworkInfo;
    }

    @ReactMethod
    private void restoreAccs(Callback callBack) {
        CoinNetworkInfo coinNetworkInfo = new CoinNetworkInfo(CoinType.ETH, EthereumNetworkType.MAINNET, rpcUrl);
        ListenableFutureTask.Callback<Boolean> restoreAccountsCallback = new ListenableFutureTask.Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                callBack.invoke(result);
            }

            @Override
            public void onFailure(ExecutionException e) {
                Throwable cause = e.getCause();
                if (cause instanceof AccountException) {
                } else if (cause instanceof RootSeedChangedException) {
                } else if (cause instanceof RemoteClientException) {
                }
            }

            @Override
            public void onCancelled(InterruptedException exception) {
            }
        };

        accountManager.restoreAccounts(hardwareWallet, true, coinNetworkInfo).setCallback(restoreAccountsCallback);
    }

    @ReactMethod
    private void setAccountStatus(Callback callBack) {
        List<Account> accounts = accountManager.getAccounts(hardwareWallet.getWalletId(), CoinType.ETH,
                EthereumNetworkType.MAINNET);
        if (!accounts.isEmpty()) {
            String account = accounts.get(0).getAddress();
            setEthereumAccount((EthereumAccount) accounts.get(0));
            callBack.invoke(account);
        } else {
            generateNewAccount(callBack);
        }
    }

    public void setHardwareWallet(HardwareWallet hardwareWallet) {
        this.hardwareWallet = hardwareWallet;
    }

    public void generateNewAccount(Callback callBack) {
        ListenableFutureTask.Callback<Account> generatingNewAccountCallback = new ListenableFutureTask.Callback<Account>() {
            @Override
            public void onSuccess(Account account)
            {
                setAccountStatus(callBack);
            }

            @Override
            public void onFailure(ExecutionException e) {

                Throwable cause = e.getCause();
                if (cause instanceof AccountException) {
                } else if (cause instanceof RootSeedChangedException) {
                } else if (cause instanceof RemoteClientException) {
                }
            }

            @Override
            public void onCancelled(InterruptedException e) {
            }
        };

        accountManager.generateNewAccount(hardwareWallet, coinNetworkInfo).setCallback(generatingNewAccountCallback);
    }

    public void setEthereumAccount(EthereumAccount ethereumAccount) {
        this.ethereumAccount = ethereumAccount;
    }

    public EthereumAccount getEthereumAccount() {
        return ethereumAccount;
    }

    public void setCoinService() {
        Context context = getReactApplicationContext();
        this.etherService = (EthereumService) CoinServiceFactory.getCoinService(context, coinNetworkInfo);
    }


    @ReactMethod
    private void getBalance(Promise promise) {
        EthereumAccount account = getEthereumAccount();
        Context context = getReactApplicationContext();
        EthereumService etherService = (EthereumService) CoinServiceFactory.getCoinService(context, coinNetworkInfo);

        etherService.getBalance(account).setCallback(new ListenableFutureTask.Callback<BigInteger>() {
            @Override
            public void onSuccess(BigInteger result) {
                balanceInEther = EthereumUtils.convertWeiToEth(result);
                promise.resolve(result.floatValue());
            }

            @Override
            public void onFailure(ExecutionException exception) {
            }

            @Override
            public void onCancelled(InterruptedException exception) {
                // cancelled
            }
        });
    }

    @ReactMethod
    private void  getNetworkType (Promise promise) {
       NetworkType networkType = coinNetworkInfo.getNetworkType();
        promise.resolve(networkType.getType());

    }
    @ReactMethod
    private void approveAndCall(String toContractAddress, String function, String gasPrice, String gasLimit, String input1, String input2, String input3,
            Promise promise) throws DecoderException {
        hardwareWallet = hardwareWalletManager.getConnectedHardwareWallet();
        Context context = getReactApplicationContext();
        EthereumService etherService = (EthereumService) CoinServiceFactory.getCoinService(context, coinNetworkInfo);
        String encodedFunction = convertFunction(function, input1, input2, input3);
        try {
            etherService.sendSmartContractTransaction(hardwareWallet, ethereumAccount, toContractAddress,
                    (new BigInteger(gasPrice)),  (new BigInteger(gasLimit)),
                    encodedFunction, null, null // nonce
            ).setCallback(new ListenableFutureTask.Callback<TransactionResult>() {
                @Override
                public void onSuccess(TransactionResult result) {
                    // success
                    WritableMap infoMap = Arguments.createMap();
                    infoMap.putString("hash", result.getHash());
                    infoMap.putInt("code", result.getError().getCode());
                    promise.resolve(infoMap);
                }

                @Override
                public void onFailure(ExecutionException exception) {
                }

                @Override
                public void onCancelled(InterruptedException exception) {
                }
            });
        } catch (AvailabilityException e) {
            // handle exception
        }
    }

    @NotNull
    public String convertFunction(String method, String input1, String input2, String input3) throws DecoderException {

        byte[] bytes = Hex.decodeHex(input3.toCharArray());
        List<Type> inputParameters = Arrays.asList(new Address(input1), new Uint(new BigInteger(input2)),
                new DynamicBytes(bytes));
        List outputParameters = Arrays.asList(new TypeReference<Uint>() {
        });
        Function transferFunction = new Function(method, inputParameters, outputParameters);

        return FunctionEncoder.encode(transferFunction);
    }

    /// withdraw

    @ReactMethod
    private void withdraw(String toContractAddress, String function, String gasPrice, String gasLimit, String input1, String input2, Boolean input3,
            Promise promise) throws DecoderException {
        hardwareWallet = hardwareWalletManager.getConnectedHardwareWallet();
        Context context = getReactApplicationContext();
        EthereumService etherService = (EthereumService) CoinServiceFactory.getCoinService(context, coinNetworkInfo);
        String encodedFunction = convertWithdraw(function, input1, input2, input3);

        try {
            etherService.sendSmartContractTransaction(hardwareWallet, ethereumAccount, toContractAddress,
                    (new BigInteger(gasPrice)),  (new BigInteger(gasLimit)),
                    encodedFunction, null, null // nonce
            ).setCallback(new ListenableFutureTask.Callback<TransactionResult>() {
                @Override
                public void onSuccess(TransactionResult result) {
                    // success
                    WritableMap infoMap = Arguments.createMap();
                    infoMap.putString("hash", result.getHash());
                    infoMap.putInt("code", result.getError().getCode());
                    promise.resolve(infoMap);
                }

                @Override
                public void onFailure(ExecutionException exception) {
                }

                @Override
                public void onCancelled(InterruptedException exception) {
                }
            });
        } catch (AvailabilityException e) {
            // handle exception
        }
    }

    @NotNull
    public String convertWithdraw(String method, String input1, String input2, Boolean input3) {
        List<Type> inputParameters = Arrays.asList(new Address(input1), new Uint(new BigInteger(input2)),
                new Bool(input3));
        List outputParameters = Arrays.asList(new TypeReference<Uint>() {
        });
        Function transferFunction = new Function(method, inputParameters, outputParameters);

        return FunctionEncoder.encode(transferFunction);
    }


    @ReactMethod
    private void requestWithdrawal(String toContractAddress, String function,  String gasPrice, String gasLimit,String input1, String input2,
            Promise promise) throws DecoderException {
        hardwareWallet = hardwareWalletManager.getConnectedHardwareWallet();
        Context context = getReactApplicationContext();
        EthereumService etherService = (EthereumService) CoinServiceFactory.getCoinService(context, coinNetworkInfo);
        String encodedFunction = convertFunctionWithdrawal(function, input1, input2);

        try {
            etherService.sendSmartContractTransaction(hardwareWallet, ethereumAccount, toContractAddress,
                    (new BigInteger(gasPrice)),  (new BigInteger(gasLimit)),
                    encodedFunction, null, null // nonce
            ).setCallback(new ListenableFutureTask.Callback<TransactionResult>() {
                @Override
                public void onSuccess(TransactionResult result) {
                    // success
                    WritableMap infoMap = Arguments.createMap();
                    infoMap.putString("hash", result.getHash());
                    infoMap.putInt("code", result.getError().getCode());
                    promise.resolve(infoMap);
                }

                @Override
                public void onFailure(ExecutionException exception) {
                }

                @Override
                public void onCancelled(InterruptedException exception) {
                }
            });
        } catch (AvailabilityException e) {
            // handle exception
        }
    }

    @NotNull
    public String convertFunctionWithdrawal(String method, String input1, String input2) throws DecoderException {

        List<Type> inputParameters = Arrays.asList(new Address(input1), new Uint(new BigInteger(input2)));
        List outputParameters = Arrays.asList(new TypeReference<Uint>() {
        });
        Function transferFunction = new Function(method, inputParameters, outputParameters);

        return FunctionEncoder.encode(transferFunction);
    }

    @ReactMethod
    private void callSmartFunc(String method, String address, String input1, String input2, String input3,
            Promise promise) {
        Context context = getReactApplicationContext();
        EthereumService etherService = (EthereumService) CoinServiceFactory.getCoinService(context, coinNetworkInfo);
        String encodedFunction = getEncodedFunc(method, input1, input2, input3);
        etherService.callSmartContractFunction(ethereumAccount, address, encodedFunction)
                .setCallback(new ListenableFutureTask.Callback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        results = result;
                        promise.resolve(result);
                        // success
                    }

                    @Override
                    public void onFailure(ExecutionException exception) {
                    }

                    @Override
                    public void onCancelled(InterruptedException exception) {
                    }
                });
    }

    @ReactMethod
    private void callSmartMethod(String method, String address, String input1, String input2, Promise promise) {
        Context context = getReactApplicationContext();
        EthereumService etherService = (EthereumService) CoinServiceFactory.getCoinService(context, coinNetworkInfo);
        String encodedFunction = getFunction(method, input1, input2);
        etherService.callSmartContractFunction(ethereumAccount, address, encodedFunction)
                .setCallback(new ListenableFutureTask.Callback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        results = result;
                        promise.resolve(result);
                        // success
                    }

                    @Override
                    public void onFailure(ExecutionException exception) {
                    }

                    @Override
                    public void onCancelled(InterruptedException exception) {
                    }
                });
    }

    @ReactMethod
    private void callSmartMethodIntOutput(String method, String address, String input1, String input2, Promise promise) {
        Context context = getReactApplicationContext();
        EthereumService etherService = (EthereumService) CoinServiceFactory.getCoinService(context, coinNetworkInfo);
        String encodedFunction = getFunction(method, input1, input2);
        etherService.callSmartContractFunction(ethereumAccount, address, encodedFunction)
                .setCallback(new ListenableFutureTask.Callback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        results = result;
                        BigInteger resultInt = new BigInteger(result.substring(2), 16);
                        promise.resolve(resultInt.toString());
                        // success
                    }

                    @Override
                    public void onFailure(ExecutionException exception) {
                    }

                    @Override
                    public void onCancelled(InterruptedException exception) {
                    }
                });
    }

    @ReactMethod
    public void getFeeInfo(Callback callBack) {
        Context context = getReactApplicationContext();
        EthereumService etherService = (EthereumService) CoinServiceFactory.getCoinService(context, coinNetworkInfo);
        etherService.getFeeInfo().setCallback(new ListenableFutureTask.Callback<EthereumFeeInfo>() {
            @Override
            public void onSuccess(EthereumFeeInfo ethereumFeeInfo) {
                BigDecimal fast = EthereumUtils.convertWeiToEth(ethereumFeeInfo.getFast());
                callBack.invoke(ethereumFeeInfo.getFast().floatValue(),ethereumFeeInfo.getNormal().floatValue(),ethereumFeeInfo.getSlow().floatValue());
            }

            @Override
            public void onFailure(@NotNull ExecutionException e) {
            }

            @Override
            public void onCancelled(@NotNull InterruptedException e) {
            }
        });
    }

    @ReactMethod
    private void callMethod(String method, String address, String user, Promise promise) {
        Context context = getReactApplicationContext();

        EthereumService etherService = (EthereumService) CoinServiceFactory.getCoinService(context, coinNetworkInfo);
        String encodedFunction = getEncodedFunction(method, user);
        etherService.callSmartContractFunction(ethereumAccount, address, encodedFunction)
                .setCallback(new ListenableFutureTask.Callback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        results = result;
                       promise.resolve(result);
                        // success
                    }

                    @Override
                    public void onFailure(ExecutionException exception) {
                    }

                    @Override
                    public void onCancelled(InterruptedException exception) {
                    }
                });
    }

    @ReactMethod
    private void callMethodIntOutput(String method, String address, String user, Promise promise) {
        Context context = getReactApplicationContext();

        EthereumService etherService = (EthereumService) CoinServiceFactory.getCoinService(context, coinNetworkInfo);
        String encodedFunction = getEncodedFunction(method, user);
        etherService.callSmartContractFunction(ethereumAccount, address, encodedFunction)
                .setCallback(new ListenableFutureTask.Callback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        results = result;
                        BigInteger resultInt = new BigInteger(result.substring(2), 16);
                        promise.resolve(resultInt.toString());
                        // success
                    }

                    @Override
                    public void onFailure(ExecutionException exception) {
                    }

                    @Override
                    public void onCancelled(InterruptedException exception) {
                    }
                });
    }

    @NotNull
    public String getEncodedFunc(String method, String input1, String input2, String input3) {
        List<Type> inputParameters = Arrays.asList(new Address(input1), new Address(input2),
                new Uint(new BigInteger(input3)));
        List outputParameters = Arrays.asList(new TypeReference<Uint>() {
        });
        Function transferFunction = new Function(method, inputParameters, outputParameters);
        return FunctionEncoder.encode(transferFunction);
    }

    @NotNull
    public String getFunction(String method, String input1, String input2) {

        if (input1.length() != 1 && input1.charAt(1) == 'x') {
            List<Type> inputParameters = Arrays.asList(new Address(input1), new Address(input2));
            List outputParameters = Arrays.asList(new TypeReference<Uint>() {
            });
            Function transferFunction = new Function(method, inputParameters, outputParameters);
            return FunctionEncoder.encode(transferFunction);
        } else {
            List<Type> inputParameters = Arrays.asList(new Uint(new BigInteger(input1)),
                    new Uint(new BigInteger(input2)));
            List outputParameters = Arrays.asList(new TypeReference<Uint>() {
            });
            Function transferFunction = new Function(method, inputParameters, outputParameters);

            return FunctionEncoder.encode(transferFunction);
        }
    }

    @NotNull
    public String getEncodedFunction(String method, String user) {
        if (user.equals("")) {
            List<Type> inputParameters = Arrays.asList();
            List outputParameters = Arrays.asList(new TypeReference<Uint>() {
            });
            Function transferFunction = new Function(method, inputParameters, outputParameters);
            return FunctionEncoder.encode(transferFunction);
        } else if (user.length() != 1 && user.charAt(1) == 'x') {

            List<Type> inputParameters = Arrays.asList(new Address(user));
            List outputParameters = Arrays.asList(new TypeReference<Uint>() {
            });
            Function transferFunction = new Function(method, inputParameters, outputParameters);
            return FunctionEncoder.encode(transferFunction);
        } else {
            List<Type> inputParameters = Arrays.asList(new Uint(new BigInteger(user)));
            List outputParameters = Arrays.asList(new TypeReference<Uint>() {
            });
            Function transferFunction = new Function(method, inputParameters, outputParameters);

            return FunctionEncoder.encode(transferFunction);
        }
    }

    public void setLoaded(Boolean loading) {
        this.loaded = loading;
    }

    public Boolean getLoaded() {
        return loaded;
    }

}