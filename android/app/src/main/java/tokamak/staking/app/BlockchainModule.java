package tokamak.staking.app;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.samsung.android.sdk.blockchain.*;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumFeeInfo;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumTokenInfo;
import com.samsung.android.sdk.blockchain.wallet.HardwareWallet;
import com.samsung.android.sdk.coldwallet.*;
import com.samsung.android.sdk.blockchain.account.Account;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.SBlockchain;
import com.samsung.android.sdk.blockchain.account.AccountManager;
import com.samsung.android.sdk.blockchain.coinservice.CoinNetworkInfo;
import com.samsung.android.sdk.blockchain.coinservice.CoinService;
import com.samsung.android.sdk.blockchain.coinservice.CoinServiceFactory;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumUtils;
import com.samsung.android.sdk.blockchain.exception.SsdkUnsupportedException;
import com.samsung.android.sdk.blockchain.wallet.HardwareWallet;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletManager;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;
import com.samsung.android.sdk.blockchain.network.EthereumNetworkType;
import com.samsung.android.sdk.blockchain.exception.AccountException;
import com.samsung.android.sdk.blockchain.exception.RemoteClientException;
import com.samsung.android.sdk.blockchain.exception.RootSeedChangedException;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.content.Context;

import org.jetbrains.annotations.NotNull;
import org.web3j.abi.datatypes.Bool;

public class BlockchainModule  extends ReactContextBaseJavaModule{
//    private SBPManager mSBPManager = SBPManager.getInstance();
//    EthereumAccount account = mSBPManager.getInstance().getEthereumAccount();
//        private Handler handler = new Handler();
    private  EthereumAccount ethereumAccount;
    private SBlockchain mSBlockchain;
    private HardwareWalletManager hardwareWalletManager;
    private AccountManager accountManager;
    private Boolean loaded;
//    private Handler handler = new Handler();
    private HardwareWallet hardwareWallet;
    private CoinNetworkInfo coinNetworkInfo;
    private CoinService coinService;
private EthereumService etherService;
//    private EthereumAccount ethereumAccount;
    private EthereumTokenInfo TONtokenInfo;
    private EthereumTokenInfo WTonInfo;

    private BigDecimal balanceInEther;
    private BigDecimal balanceInTON;

    private BigInteger ethereumGasPriceSlow = EthereumUtils.convertEthToGwei(BigDecimal.valueOf(4)).toBigInteger();
    private BigInteger ethereumGasPriceNormal = EthereumUtils.convertEthToGwei(BigDecimal.valueOf(10)).toBigInteger();
    private BigInteger ethereumGasPriceFast = EthereumUtils.convertEthToGwei(BigDecimal.valueOf(20)).toBigInteger();
    public static String rpcUrl = "https://mainnet.infura.io/v3/aed1b36728cf43aeaf8ce6f29e8e2727";
    public static  String rpcUrlRinkeby = "https://rinkeby.infura.io/v3/aed1b36728cf43aeaf8ce6f29e8e2727";
    public static String tonAddress = "0x3734E35231abE68818996dC07Be6a8889202DEe9";
    public static  String wtonAddress = "0x9985d94ee25a1eB0459696667f071ECE121ACce6";
    BlockchainModule(ReactApplicationContext context) {
        super(context);
    }

    @Override
    public String getName() {
        return "BlockchainModule";
    }
    @ReactMethod
    public void initialize() {
        Context context = getReactApplicationContext();
        initis(context);
        setCoinService();
        getTokenInfo(tonAddress);
        getTokenInfo(wtonAddress);
    }

    @ReactMethod
    public void setupAccount (Callback callBack) {
        String address = ethereumAccount.getAddress();
//        BigDecimal balance = balanceInEther;

        callBack.invoke(address, balanceInEther.floatValue(), balanceInTON.floatValue());
    }
    public void initis(Context context) {
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

                    restoreAccs();
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

    private  void restoreAccs() {
        CoinNetworkInfo coinNetworkInfo = new CoinNetworkInfo(
                CoinType.ETH,
                EthereumNetworkType.RINKEBY,
                rpcUrlRinkeby);
        ListenableFutureTask.Callback<Boolean> restoreAccountsCallback =
                new ListenableFutureTask.Callback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
                            Log.e("Tokamak App", "success restore");
                            setAccountStatus();

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
    private void setAccountStatus() {
//        HardwareWallet connectedHardwareWallet = hardwareWalletManager.getConnectedHardwareWallet();

        List<Account> accounts =
                accountManager
                        .getAccounts(
                                hardwareWallet.getWalletId(),
                                CoinType.ETH,
                                EthereumNetworkType.RINKEBY
                        );

        if (!accounts.isEmpty()) {
           setLoaded(true);
            Log.i("Tokamak App", "Account size: " + accounts.size());
            Log.i("Tokamak App", "Account size loaded: " + loaded);
            ethereumAccount = (EthereumAccount) accounts.get(0);
            addTokenAddress(ethereumAccount, tonAddress, "TON");
            addTokenAddress(ethereumAccount, wtonAddress, "WTON");
            getBalance(ethereumAccount);
//            getTokenBalance(ethereumAccount);

        }
        else {
            Log.e("Tokamak App", "account empty.");
            generateNewAccount();
        }
    }
        public void setHardwareWallet(HardwareWallet hardwareWallet) {
        this.hardwareWallet = hardwareWallet;
    }
    public void generateNewAccount() {
        ListenableFutureTask.Callback<Account> generatingNewAccountCallback =
                new ListenableFutureTask.Callback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
//                        ethereumAccount =  (EthereumAccount) account;
                        setAccountStatus();
                    }

                    @Override
                    public void onFailure(ExecutionException e) {

                        Throwable cause = e.getCause();
                        Log.i("Tokamak App fail", String.valueOf(cause));
                        if (cause instanceof AccountException) {
                            Log.i("Tokamak App", "AccountException");
                            setAccountStatus();
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

public void setCoinService() {
    Context context = getReactApplicationContext();
    this.etherService = (EthereumService) CoinServiceFactory.getCoinService(context, coinNetworkInfo);

}
private void  getBalance (EthereumAccount account) {
    Log.i("Tokamak App", "came to balance");
    etherService.getBalance(account).setCallback(
            new ListenableFutureTask.Callback<BigInteger>() {
                @Override
                public void onSuccess(BigInteger result) {
                    //success
                    Log.i("Tokamak App", "Balance" + result);
                    balanceInEther = EthereumUtils.convertWeiToEth(result);
                    Log.i("Tokamak App", "Balance" + balanceInEther);
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

public void getTokenInfo (String address) {

    etherService.getTokenInfo(address).setCallback(
            new ListenableFutureTask.Callback<EthereumTokenInfo>() {
                @Override
                public void onSuccess(EthereumTokenInfo result) {
                    Log.e("Tokamak App", "token success" + result);
                    switch (result.getSymbol()) {
                        case "TON":
                            TONtokenInfo = result;
                            break;
                        case "WTON":
                            WTonInfo = result;
                            break;
                        default:
                            break;
                    }
                }
                @Override
                public void onFailure(ExecutionException exception) {
                    Log.e("Tokamak App", "token fail" + exception);
                }
                @Override
                public void onCancelled(InterruptedException exception) {
                    Log.e("Tokamak App", "token cancel" + exception);
                }
            });
}

public void addTokenAddress (EthereumAccount account, String address, String symbol) {
    etherService.addTokenAddress(
            account,
            address)
            .setCallback(new ListenableFutureTask.Callback<EthereumAccount>() {
                @Override
                public void onSuccess(final EthereumAccount account) {
                    //success
                    Log.e("Tokamak App", "addTokenAddress" + account);
                    ethereumAccount = account;
                    getTokenBalance(ethereumAccount, symbol);
                }
                @Override
                public void onFailure(ExecutionException exception) {
                    //failure
                    Log.e("Tokamak App", "addTokenAddress" + exception);
                }
                @Override
                public void onCancelled(InterruptedException exception) {
                    Log.e("Tokamak App", "addTokenAddress" + exception);
                    //cancelled
                }
            });
}

public void getTokenBalance (EthereumAccount account, String symbol) {
    etherService.getTokenBalance(account).setCallback(
            new ListenableFutureTask.Callback<BigInteger>() {
                @Override
                public void onSuccess(BigInteger result) {
                    if (symbol == "TON") {
                        BigDecimal convertedTokenBalance = new BigDecimal(result).
                                divide(BigDecimal.TEN.pow(TONtokenInfo.getDecimals()));
                        balanceInTON = convertedTokenBalance;
                        Log.i("Tokamak App", "TON Balance" + convertedTokenBalance);
                    }
                    if (symbol == "WTON") {
                        BigDecimal convertedTokenBalance = new BigDecimal(result).
                                divide(BigDecimal.TEN.pow(TONtokenInfo.getDecimals()));
                        Log.i("Tokamak App", "Power Balance" + convertedTokenBalance);
                    }
                    //success

                }
                @Override
                public void onFailure(ExecutionException exception) {
                    Log.i("Tokamak App", "Token Balance fail" + exception);
                }
                @Override
                public void onCancelled(InterruptedException exception) {
                    Log.i("Tokamak App", "Token Balance cancel" + exception);
                    //cancelled
                }
            });
}
    public void setLoaded (Boolean loading) {
        this.loaded = loading;
    }

    public Boolean getLoaded () {
        return loaded;
    }
        public EthereumAccount getEthereumAccount(){
        return ethereumAccount;
    }

}
