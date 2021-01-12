package tokamak.staking.app;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.samsung.android.sdk.blockchain.*;
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

public class BlockchainModule  extends ReactContextBaseJavaModule{
//    private SBPManager mSBPManager = SBPManager.getInstance();
//    EthereumAccount account = mSBPManager.getInstance().getEthereumAccount();
//        private Handler handler = new Handler();
    private  EthereumAccount ethereumAccount;
    private SBlockchain mSBlockchain;
    private HardwareWalletManager hardwareWalletManager;
    private AccountManager accountManager;

//    private Handler handler = new Handler();
    private HardwareWallet hardwareWallet;
    private CoinNetworkInfo coinNetworkInfo;
    private CoinService coinService;
//    private EthereumAccount ethereumAccount;


    private BigInteger ethereumGasPriceSlow = EthereumUtils.convertEthToGwei(BigDecimal.valueOf(4)).toBigInteger();
    private BigInteger ethereumGasPriceNormal = EthereumUtils.convertEthToGwei(BigDecimal.valueOf(10)).toBigInteger();
    private BigInteger ethereumGasPriceFast = EthereumUtils.convertEthToGwei(BigDecimal.valueOf(20)).toBigInteger();
    public static String rpcUrl = "https://mainnet.infura.io/v3/aed1b36728cf43aeaf8ce6f29e8e2727";
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
//        EthereumAccount account = getEthereumAccount();
//        String address = account.getAddress();
//        Log.i("Tokamak App", "Account address: " + address);
//        callBack.invoke(address);
//        SBPManager.getInstance().initializeSBlockChain(context);
//        CoinNetworkInfo coinNetworkInfo = SBPManager.getInstance().
//        EthereumAccount account = SBPManager.getInstance().getEthereumAccount();
//        HardwareWallet mHardwarwWallet = SBPManager.getInstance().getHardwareWallet();
//        Log.i("Tokamak App", "module account address: " + mHardwarwWallet.toString());

    }

    @ReactMethod
    public void setupAccount (Callback callBack) {
//        restoreAccs();
//        generateNewAccount();
        setAccountStatus();
//        String address = getEthereumAccount().getAddress();
//        callBack.invoke(address);
//        Log.i("Tokamak App", "Account address: " + address);
    }
    public void initis(Context context) {
        try{
            mSBlockchain = new SBlockchain();
            mSBlockchain.initialize(context);
            accountManager = mSBlockchain.getAccountManager();
            hardwareWalletManager = mSBlockchain.getHardwareWalletManager();
            coinNetworkInfo = new CoinNetworkInfo(
                    CoinType.ETH,
                    EthereumNetworkType.MAINNET,
                    rpcUrl);
//            Log.e("Tokamak App", "coininfo" + coinNetworkInfo.toString());
            setCoinNetworkInfo(coinNetworkInfo);
            ListenableFutureTask<HardwareWallet> connectionTask =
                    mSBlockchain.getHardwareWalletManager().connect( HardwareWalletType.SAMSUNG, true);
            connectionTask.setCallback(
                    new ListenableFutureTask.Callback<HardwareWallet>() {
                @Override
                public void onSuccess(HardwareWallet hardwareWallet) {
//                    Log.e("Tokamak App", "success");
                    HardwareWallet mhardwareWallet = hardwareWalletManager.getConnectedHardwareWallet();
//                Log.e("Tokamak App", "success wallet" + mhardwareWallet.toString());
                    setHardwareWallet(mhardwareWallet);
                }
                @Override
                public void onFailure(@NotNull ExecutionException e) {
//                    Log.e("Tokamak App", "fail");
                    e.printStackTrace();
                }

                @Override
                public void onCancelled(@NotNull InterruptedException e) {
//                    Log.e("Tokamak App", "cancelled");
                    e.printStackTrace();
                }
            });
        }
        catch(SsdkUnsupportedException e) {
//            Log.e("Tokamak App", "Could not initialize SBK.");
//            Log.e("Tokamak App", "Error message: " + e.getMessage());
        }

    }
    public void setCoinNetworkInfo(CoinNetworkInfo coinNetworkInfo) {
        this.coinNetworkInfo = coinNetworkInfo;
    }

    private  void restoreAccs() {
        CoinNetworkInfo coinNetworkInfo = new CoinNetworkInfo(
                CoinType.ETH,
                EthereumNetworkType.MAINNET,
                rpcUrl);
        ListenableFutureTask.Callback<Boolean> restoreAccountsCallback =
                new ListenableFutureTask.Callback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
//                            Log.e("Tokamak App", "success restore");
//                            setAccountStatus();
                        }
                        else {
//                            Log.e("Tokamak App", "fail restore");
                        }
                    }

                    @Override
                    public void onFailure(ExecutionException e) {
                        Throwable cause = e.getCause();
                        Log.e("onFailure", cause.toString());
                        if (cause instanceof AccountException) {
//                            Log.e("Tokamak App", "restore AccountException");
                        } else if (cause instanceof RootSeedChangedException) {
//                            Log.e("Tokamak App", "restore RootSeedChangedException");
                        } else if (cause instanceof RemoteClientException) {
//                            Log.e("Tokamak App", "restore RemoteClientException");
                        }
                    }

                    @Override
                    public void onCancelled(InterruptedException exception) {
//                        Log.e("Tokamak App", "onCancelled");
                    }
                };

        accountManager.restoreAccounts(
                hardwareWallet,
                        true,
                coinNetworkInfo
                ).setCallback(restoreAccountsCallback);
    }
    private void setAccountStatus() {
        HardwareWallet connectedHardwareWallet = hardwareWalletManager.getConnectedHardwareWallet();
//        Log.e("Tokamak App", "came to account wallet" );
//        Log.e("Tokamak App", "ths is wallet" + connectedHardwareWallet);
        List<Account> accounts =
                accountManager
                        .getAccounts(
                                hardwareWallet.getWalletId(),
                                CoinType.ETH,
                                EthereumNetworkType.MAINNET
                        );

        if (!accounts.isEmpty()) {
            Log.i("Tokamak App", "Account size: " + accounts.size());
//           EthereumAccount account = (EthereumAccount) accounts.get(0);
//            setEthereumAccount(account);
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
//        Log.e("Tokamak App", "came to generate account");
//        Log.e("Tokamak App", "connected wallet " + hardwareWallet);
//        Log.e("Tokamak App", String.valueOf(connectedHardwareWallet));
        ListenableFutureTask.Callback<Account> generatingNewAccountCallback =
                new ListenableFutureTask.Callback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
//                        Log.i("Tokamak App", "Account success");
//                        Log.i("Tokamak App", "Generated account address: " + account.getAddress());
                    }

                    @Override
                    public void onFailure(ExecutionException e) {

                        Throwable cause = e.getCause();
//                        Log.i("Tokamak App fail", String.valueOf(cause));
                        if (cause instanceof AccountException) {
//                            Log.i("Tokamak App", "AccountException");
                        } else if (cause instanceof RootSeedChangedException) {
//                            Log.i("Tokamak App", "RootSeedChangedException");
                        } else if (cause instanceof RemoteClientException) {
//                            Log.i("Tokamak App", "RemoteClientException");
                        }
                    }

                    @Override
                    public void onCancelled(InterruptedException e) {

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

        public EthereumAccount getEthereumAccount(){
        return ethereumAccount;
    }

}
