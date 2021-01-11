package tokamak.staking.app;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.samsung.android.sdk.blockchain.*;
import com.samsung.android.sdk.coldwallet.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import android.util.Log;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

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

import com.samsung.android.sdk.blockchain.CoinType;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.SBlockchain;
import com.samsung.android.sdk.blockchain.account.Account;
import com.samsung.android.sdk.blockchain.account.AccountManager;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.android.sdk.blockchain.coinservice.CoinNetworkInfo;
import com.samsung.android.sdk.blockchain.coinservice.CoinServiceFactory;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumService;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumUtils;
import com.samsung.android.sdk.blockchain.exception.SsdkUnsupportedException;
import com.samsung.android.sdk.blockchain.network.EthereumNetworkType;
import com.samsung.android.sdk.blockchain.wallet.HardwareWallet;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletManager;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;


import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class SBPManager {
    private static SBPManager INSTANCE;
    private SBlockchain SBlockchain;
    private HardwareWalletManager hardwareWalletManager;
    private AccountManager accountManager;

    private Handler handler = new Handler();
    private HardwareWallet hardwareWallet;
    private CoinNetworkInfo coinNetworkInfo;
    private CoinService coinService;
    private EthereumAccount ethereumAccount;


    private BigInteger ethereumGasPriceSlow = EthereumUtils.convertEthToGwei(BigDecimal.valueOf(4)).toBigInteger();
    private BigInteger ethereumGasPriceNormal = EthereumUtils.convertEthToGwei(BigDecimal.valueOf(10)).toBigInteger();
    private BigInteger ethereumGasPriceFast = EthereumUtils.convertEthToGwei(BigDecimal.valueOf(20)).toBigInteger();
    public static String rpcUrl = "https://mainnet.infura.io/v3/aed1b36728cf43aeaf8ce6f29e8e2727";

    public static SBPManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SBPManager();
            INSTANCE.setSBlockchain(new SBlockchain());
        }
        return INSTANCE;
    }

    public void initializeSBlockChain(Context context) {
        try {
            SBlockchain.initialize(context);
            accountManager = SBlockchain.getAccountManager();
            hardwareWalletManager = SBlockchain.getHardwareWalletManager();
            coinNetworkInfo = new CoinNetworkInfo(
                    CoinType.ETH,
                    EthereumNetworkType.MAINNET,
                    rpcUrl);
            connectToKeyStore(context);

        } catch (SsdkUnsupportedException e) {
            Log.e("Tokamak App", "Could not initialize SBK.");
            Log.e("Tokamak App", "Error message: " + e.getMessage());
        }
    }
    private void connectToKeyStore(Context context) {
//        Toast.makeText(context, "connecting...", Toast.LENGTH_LONG).show();
//        Toast.makeText(context, "connecting...", Toast.LENGTH_LONG).show();
        hardwareWalletManager.connect(
                HardwareWalletType.SAMSUNG,
                false).setCallback(new ListenableFutureTask.Callback<HardwareWallet>() {
            @Override
            public void onFailure(@NotNull ExecutionException e) {
                Log.e("Tokamak App", "could not connect to the wallet.");
                e.printStackTrace();
            }

            @Override
            public void onSuccess(HardwareWallet hardwareWallet) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Tokamak App", "connected to the wallet.");
                        setAccountStatus();
                    }
                });
               
            }

            @Override
            public void onCancelled(@NotNull InterruptedException e) {
                Log.e("Tokamak App", "couldn't connect to the wallet.");
                e.printStackTrace();
            }
        });
    }

    private void setAccountStatus() {
        HardwareWallet connectedHardwareWallet = hardwareWalletManager.getConnectedHardwareWallet();
        Boolean supportedCoinTypes = hardwareWalletManager.getConnectedHardwareWallet().isConnected();
        String version = connectedHardwareWallet.getVersion();

        Log.e("Tokamak App", version);
        List<Account> accounts =
                accountManager
                        .getAccounts(
                                connectedHardwareWallet.getWalletId(),
                                CoinType.ETH,
                                EthereumNetworkType.MAINNET
                        );

        if (!accounts.isEmpty()) {
            Log.e("Tokamak App", "reached account.");
            ethereumAccount = (EthereumAccount) accounts.get(accounts.size() - 1);
        }
        else {
            Log.e("Tokamak App", "account empty.");
        }
    }

    public SBlockchain getSBlockchain() {
        return SBlockchain;
    }
    public void setSBlockchain(SBlockchain mSBlockchain) {
        this.SBlockchain = mSBlockchain;
    }
    public CoinNetworkInfo getCoinNetworkInfo() {
        return coinNetworkInfo;
    }

    public void setCoinNetworkInfo(CoinNetworkInfo coinNetworkInfo) {
        this.coinNetworkInfo = coinNetworkInfo;
    }

    public HardwareWallet getHardwareWallet() {
        return hardwareWallet;
    }

    public void setHardwareWallet(HardwareWallet hardwareWallet) {
        this.hardwareWallet = hardwareWallet;
    }

    public  AccountManager getAccountManager() {
        return accountManager;
    }

    public CoinService getCoinService() {
        return coinService;
    }

    public void setCoinService(Context applicationContext) {
        this.coinService = CoinServiceFactory.getCoinService(applicationContext, coinNetworkInfo);
    }

    // Setting GasPrice & GasLimit as per Metamask
    public BigInteger getEthereumGasPriceSlow() {
        return ethereumGasPriceSlow;
    }
    public BigInteger getEthereumGasPriceNormal() {
        return ethereumGasPriceNormal;
    }

    public BigInteger getEthereumGasPriceFast() {
        return ethereumGasPriceFast;
    }
}
