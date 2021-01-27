package tokamak.staking.app;

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
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumTokenInfo;
import com.samsung.android.sdk.blockchain.wallet.HardwareWallet;
import com.samsung.android.sdk.blockchain.account.Account;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.SBlockchain;
import com.samsung.android.sdk.blockchain.account.AccountManager;
import com.samsung.android.sdk.blockchain.coinservice.CoinNetworkInfo;
import com.samsung.android.sdk.blockchain.coinservice.CoinServiceFactory;
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
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import android.util.Log;
import android.content.Context;

//import org.unimodules.core.Promise;
import org.json.JSONException;
import org.json.JSONObject;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Array;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Bytes;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.jetbrains.annotations.NotNull;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.Filter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthBlock;

public class BlockchainModule  extends ReactContextBaseJavaModule{
    public  EthereumAccount ethereumAccount;
    private SBlockchain mSBlockchain;
    private HardwareWalletManager hardwareWalletManager;
    private AccountManager accountManager;
    private Boolean loaded;
    private HardwareWallet hardwareWallet;
    private CoinNetworkInfo coinNetworkInfo;
    private EthereumService etherService;
    private EthereumTokenInfo TONtokenInfo;
    private EthereumTokenInfo WTonInfo;
    private BigDecimal balanceInEther;
    private BigDecimal balanceInTON;
    private Web3j web3;
    private BigInteger ethereumGasPriceSlow = EthereumUtils.convertEthToGwei(BigDecimal.valueOf(4)).toBigInteger();
    private BigInteger ethereumGasPriceNormal = EthereumUtils.convertEthToGwei(BigDecimal.valueOf(10)).toBigInteger();
    private BigInteger ethereumGasPriceFast = EthereumUtils.convertEthToGwei(BigDecimal.valueOf(20)).toBigInteger();
    public static String rpcUrl = "https://mainnet.infura.io/v3/aed1b36728cf43aeaf8ce6f29e8e2727";
    public static  String rpcUrlRinkeby = "https://rinkeby.infura.io/v3/aed1b36728cf43aeaf8ce6f29e8e2727";
    public static String tonAddress = "0x3734E35231abE68818996dC07Be6a8889202DEe9";
    public static  String wtonAddress = "0x9985d94ee25a1eB0459696667f071ECE121ACce6";
    public String results;
    BlockchainModule(ReactApplicationContext context) {
        super(context);
    }

    @Override
    public String getName() {
        return "BlockchainModule";
    }
    @ReactMethod
    public void connect (Promise promise) {
        Web3j web3j = Web3j.build(new HttpService(rpcUrlRinkeby));
        try {
            Web3ClientVersion clientVersion = web3j.web3ClientVersion().send();
            if(!clientVersion.hasError()){
                //Connected
                web3 = web3j;
            }
            else {
                Log.e("Tokamak App", "not connected web3" );
                //Show Error
            }
        }
        catch (Exception e) {
            //Show Error
            Log.e("Tokamak App", "web3 error" +e);
        }
    }
    @ReactMethod
    public void getBlockNumber (Promise promise) throws ExecutionException, InterruptedException, IOException {
        EthBlockNumber block = web3.ethBlockNumber().sendAsync().get();
        Integer blockNumber = block.getBlockNumber().intValue();

//                Log.e("Tokamak App", "connected web3" + block.getBlockNumber() );
        EthBlock.Block ethBlock = web3.ethGetBlockByNumber(
                DefaultBlockParameter.valueOf(
                        BigInteger.valueOf(blockNumber)), false).send().getBlock();
        promise.resolve(block.getBlockNumber().intValue());
    }
    @ReactMethod
    public void getTimeStamp (Integer blockNumber, Promise promise) throws ExecutionException, InterruptedException, IOException {

//                Log.e("Tokamak App", "connected web3" + block.getBlockNumber() );
        EthBlock.Block ethBlock = web3.ethGetBlockByNumber(
                DefaultBlockParameter.valueOf(
                        BigInteger.valueOf(blockNumber)), false).send().getBlock();
        promise.resolve(ethBlock.getTimestamp().intValue());
    }
    @ReactMethod
    public void getPastEvents (String address, String encoded, Promise promise) throws ExecutionException, InterruptedException, JSONException {
        Gson g = new Gson();
        EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST,  DefaultBlockParameterName.LATEST, address );
        filter.addSingleTopic(encoded);
        EthLog ethLog = web3.ethGetLogs(filter).sendAsync().get();
        Log.e("Tokamak App", "getPastEvents" + ethLog.getLogs() );
        WritableArray array = new WritableNativeArray();
        for (Object logs: ethLog.getLogs()){
            JSONObject jo = new JSONObject(g.toJson(logs));
            WritableMap wm = convertJsonToMap(jo);
            array.pushMap(wm);
        }
        promise.resolve(array);
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
    public void initis( Callback callBack) {
        Context context = getReactApplicationContext();
        try{
            mSBlockchain = new SBlockchain();
            mSBlockchain.initialize(context);
            accountManager = mSBlockchain.getAccountManager();

            hardwareWalletManager = mSBlockchain.getHardwareWalletManager();
            coinNetworkInfo = new CoinNetworkInfo(
                    CoinType.ETH,
                    EthereumNetworkType.RINKEBY,
                    rpcUrlRinkeby);
            setCoinNetworkInfo(coinNetworkInfo);
            ListenableFutureTask<HardwareWallet> connectionTask =
                    mSBlockchain.getHardwareWalletManager().connect( HardwareWalletType.SAMSUNG, true);
            connectionTask.setCallback(
                    new ListenableFutureTask.Callback<HardwareWallet>() {
                        @Override
                        public void onSuccess(HardwareWallet hardwareWallet) {
                            hardwareWallet = hardwareWalletManager.getConnectedHardwareWallet();
//                    HardwareWallet mhardwareWallet = hardwareWalletManager.getConnectedHardwareWallet();
                            setHardwareWallet(hardwareWallet);
                            Log.e("Tokamak App", "initialized " );
                            callBack.invoke(true);
//                    restoreAccs();
                        }
                        @Override
                        public void onFailure(@NotNull ExecutionException e) {
                            e.printStackTrace();
                        }
                        @Override
                        public void onCancelled(@NotNull InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
        }
        catch(SsdkUnsupportedException e) {
            Log.e("Tokamak App", "Error message: " + e.getMessage());
        }

    }
    public void setCoinNetworkInfo(CoinNetworkInfo coinNetworkInfo) {
        this.coinNetworkInfo = coinNetworkInfo;
    }
    @ReactMethod
    private  void restoreAccs(Callback callBack) {
        CoinNetworkInfo coinNetworkInfo = new CoinNetworkInfo(
                CoinType.ETH,
                EthereumNetworkType.RINKEBY,
                rpcUrlRinkeby);
        ListenableFutureTask.Callback<Boolean> restoreAccountsCallback =
                new ListenableFutureTask.Callback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        callBack.invoke(result);
                        if (result) {
                            Log.e("Tokamak App", "success restore");
//                            setAccountStatus();
                        }
                        else {
                            Log.e("Tokamak App", "fail restore");
                        }
                    }

                    @Override
                    public void onFailure(ExecutionException e) {
                        Throwable cause = e.getCause();
                        Log.e("onFailure", cause.toString());
                        if (cause instanceof AccountException) {
                            Log.e("Tokamak App", "restore AccountException");
                        } else if (cause instanceof RootSeedChangedException) {
                            Log.e("Tokamak App", "restore RootSeedChangedException");
                        } else if (cause instanceof RemoteClientException) {
                            Log.e("Tokamak App", "restore RemoteClientException");
                        }
                    }

                    @Override
                    public void onCancelled(InterruptedException exception) {
                        Log.e("Tokamak App", "onCancelled");
                    }
                };

        accountManager.restoreAccounts(
                hardwareWallet,
                true,
                coinNetworkInfo
        ).setCallback(restoreAccountsCallback);
    }
    @ReactMethod
    private void setAccountStatus(Callback callBack) {
//        HardwareWallet connectedHardwareWallet = hardwareWalletManager.getConnectedHardwareWallet();

        List<Account> accounts =
                accountManager
                        .getAccounts(
                                hardwareWallet.getWalletId(),
                                CoinType.ETH,
                                EthereumNetworkType.RINKEBY
                        );

        if (!accounts.isEmpty()) {
            String account =  accounts.get(0).getAddress();
            setEthereumAccount((EthereumAccount) accounts.get(0));
            callBack.invoke(account);
        }
        else {
            Log.e("Tokamak App", "account empty.");
            generateNewAccount(callBack);
        }
    }
    public void setHardwareWallet(HardwareWallet hardwareWallet) {
        this.hardwareWallet = hardwareWallet;
    }
    public void generateNewAccount(Callback callBack) {
        ListenableFutureTask.Callback<Account> generatingNewAccountCallback =
                new ListenableFutureTask.Callback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
                        setAccountStatus(callBack);
                    }

                    @Override
                    public void onFailure(ExecutionException e) {

                        Throwable cause = e.getCause();
                        Log.i("Tokamak App fail", String.valueOf(cause));
                        if (cause instanceof AccountException) {
                            Log.i("Tokamak App", "AccountException");
                        } else if (cause instanceof RootSeedChangedException) {
                            Log.i("Tokamak App", "RootSeedChangedException");
                        } else if (cause instanceof RemoteClientException) {
                            Log.i("Tokamak App", "RemoteClientException");
                        }
                    }

                    @Override
                    public void onCancelled(InterruptedException e) {
                        Log.i("Tokamak App", "Account creation cancelled");
                    }
                };

        accountManager
                .generateNewAccount(
                        hardwareWallet,
                        coinNetworkInfo
                ).setCallback(generatingNewAccountCallback);
    }
    public void setEthereumAccount(EthereumAccount ethereumAccount){
        this.ethereumAccount = ethereumAccount;
    }
    public EthereumAccount getEthereumAccount () {
        return ethereumAccount;
    }

    public void setCoinService() {
        Context context = getReactApplicationContext();
        this.etherService = (EthereumService) CoinServiceFactory.getCoinService(context, coinNetworkInfo);


    }
    @ReactMethod
    private void  getBalance (Promise promise) {
        EthereumAccount account = getEthereumAccount();
        Context context = getReactApplicationContext();
        EthereumService etherService = (EthereumService) CoinServiceFactory.getCoinService(context, coinNetworkInfo);

        etherService.getBalance(account).setCallback(
                new ListenableFutureTask.Callback<BigInteger>() {
                    @Override
                    public void onSuccess(BigInteger result) {
                        balanceInEther = EthereumUtils.convertWeiToEth(result);
                        Log.i("Tokamak App", "Balance" + balanceInEther);
                        promise.resolve(balanceInEther.floatValue());
                    }
                    @Override
                    public void onFailure(ExecutionException exception) {
                        Log.i("Tokamak App", "Balance fail" + exception);
                    }
                    @Override
                    public void onCancelled(InterruptedException exception) {
                        Log.i("Tokamak App", "Balance cancel" + exception);
                        //cancelled
                    }
                });
    }
    @ReactMethod
    private void callSmartFunc (String method, String address, String input1, String input2, String input3, Promise promise){
        Log.i("Tokamak App", "callSmartFunc came"+ method );
        Context context = getReactApplicationContext();
        EthereumService etherService = (EthereumService) CoinServiceFactory.getCoinService(context, coinNetworkInfo);
        String encodedFunction = getEncodedFunc(method, input1, input2, input3);
        etherService
                .callSmartContractFunction(
                        ethereumAccount,
                        address,
                        encodedFunction
                )
                .setCallback(new ListenableFutureTask.Callback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        results = result;
                        Log.i("Tokamak App", "callSmartMethod called" + result);
                        promise.resolve(result);
                        //success
                    }
                    @Override
                    public void onFailure(ExecutionException exception) {
                        //failure
                        Log.i("Tokamak App", "callSmartMethod failed" );
                    }
                    @Override
                    public void onCancelled(InterruptedException exception) {
                        //cancelled
                        Log.i("Tokamak App", "call method cancelled");
                    }
                });
    }

        @ReactMethod
        private void  callSmartMethod (String method, String address, String input1, String input2 ,Promise promise ){
            Log.i("Tokamak App", "call method came" );
            Context context = getReactApplicationContext();
            EthereumService etherService = (EthereumService) CoinServiceFactory.getCoinService(context, coinNetworkInfo);
            String encodedFunction = getFunction(method, input1, input2);
            etherService
                    .callSmartContractFunction(
                            ethereumAccount,
                            address,
                            encodedFunction
                    )
                    .setCallback(new ListenableFutureTask.Callback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            results = result;
                            Log.i("Tokamak App", "callSmartMethod called" + result);
                            promise.resolve(result);
                            //success
                        }
                        @Override
                        public void onFailure(ExecutionException exception) {
                            //failure
                            Log.i("Tokamak App", "callSmartMethod failed" );
                        }
                        @Override
                        public void onCancelled(InterruptedException exception) {
                            //cancelled
                            Log.i("Tokamak App", "call method cancelled");
                        }
                    });
        }
    @ReactMethod
    private void callMethod (String method, String address, String user, Promise promise) {
        Context context = getReactApplicationContext();
        EthereumService etherService = (EthereumService) CoinServiceFactory.getCoinService(context, coinNetworkInfo);
        String encodedFunction = getEncodedFunction(method, user);
        etherService
                .callSmartContractFunction(
                        ethereumAccount,
                        address,
                        encodedFunction
                )
                .setCallback(new ListenableFutureTask.Callback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        results = result;
                        Log.i("Tokamak App", "call method called" + method);
                        promise.resolve(result);
                        //success
                    }
                    @Override
                    public void onFailure(ExecutionException exception) {
                        //failure
                        Log.i("Tokamak App", "call method failed" + method);
                    }
                    @Override
                    public void onCancelled(InterruptedException exception) {
                        //cancelled
                        Log.i("Tokamak App", "call method cancelled");
                    }
                });
    }
    @NotNull
    public  String getEncodedFunc(String method, String input1, String input2, String input3){
        List<Type> inputParameters = Arrays.asList(new Address(input1),new Address(input2), new Uint(new BigInteger(input3)));
        List outputParameters = Arrays.asList(new TypeReference<Uint>() {}
        );
        Function transferFunction = new Function(method, inputParameters, outputParameters);
        Log.i("Tokamak App", "came to address");
        return FunctionEncoder.encode(transferFunction);
    }

    @NotNull
    public String getFunction (String method, String input1, String input2) {

        if (input1.length() != 1 && input1.charAt(1) == 'x'){
            List<Type> inputParameters = Arrays.asList(new Address(input1),new Address(input2));
            List outputParameters = Arrays.asList(new TypeReference<Uint>() {}
            );
            Function transferFunction = new Function(method, inputParameters, outputParameters);
            Log.i("Tokamak App", "came to address");
            return FunctionEncoder.encode(transferFunction);
        }
        else {
            Log.i("Tokamak App", "came to uint");
            List<Type> inputParameters = Arrays.asList(new Uint(new BigInteger(input1)),new Uint(new BigInteger(input2)) );
            List outputParameters = Arrays.asList(new TypeReference<Uint>() {}
            );
            Function transferFunction = new Function(method, inputParameters, outputParameters);

            return FunctionEncoder.encode(transferFunction);
        }
    }
    @NotNull
    public String getEncodedFunction(String method, String user){
        if (user.equals("")){
            List<Type> inputParameters = Arrays.asList();
            List outputParameters = Arrays.asList(
                    new TypeReference<Uint>() {
                    }
            );
            Function transferFunction = new Function(method, inputParameters, outputParameters);
            return FunctionEncoder.encode(transferFunction);
        }
       else if(user.length() != 1 && user.charAt(1) == 'x'){

                List<Type> inputParameters = Arrays.asList(new Address(user));
                List outputParameters = Arrays.asList(new TypeReference<Uint>() {}
                );
                Function transferFunction = new Function(method, inputParameters, outputParameters);
                Log.i("Tokamak App", "came to address " + method);
                return FunctionEncoder.encode(transferFunction);
        }
       else {

            Log.i("Tokamak App", "came to uint");
            List<Type> inputParameters = Arrays.asList(new Uint(new BigInteger(user)));
            List outputParameters = Arrays.asList(new TypeReference<Uint>() {}
            );
            Function transferFunction = new Function(method, inputParameters, outputParameters);

            return FunctionEncoder.encode(transferFunction);
        }
    }

    public void setLoaded (Boolean loading) {
        this.loaded = loading;
    }

    public Boolean getLoaded () {
        return loaded;
    }

}