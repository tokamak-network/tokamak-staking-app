package tokamak.staking.app;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.samsung.android.sdk.blockchain.*;
import com.samsung.android.sdk.coldwallet.*;

import java.util.Map;
import java.util.HashMap;
import android.util.Log;
import android.content.Context;
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

import java.math.BigDecimal;
import java.math.BigInteger;

public class SBPManager {
    private static SBPManager INSTANCE;
    private SBlockchain SBlockchain;
    private HardwareWalletManager hardwareWalletManager;
    private AccountManager accountManager;


    private HardwareWallet hardwareWallet;
    private CoinNetworkInfo coinNetworkInfo;
    private CoinService coinService;

    private BigInteger ethereumGasPriceSlow = EthereumUtils.convertEthToGwei(BigDecimal.valueOf(4)).toBigInteger();
    private BigInteger ethereumGasPriceNormal = EthereumUtils.convertEthToGwei(BigDecimal.valueOf(10)).toBigInteger();
    private BigInteger ethereumGasPriceFast = EthereumUtils.convertEthToGwei(BigDecimal.valueOf(20)).toBigInteger();

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

        } catch (SsdkUnsupportedException e) {
            Log.e("Tokamak App", "Could not initialize SBK.");
            Log.e("Tokamak App", "Error message: " + e.getMessage());
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

    public AccountManager getAccountManager() {
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
