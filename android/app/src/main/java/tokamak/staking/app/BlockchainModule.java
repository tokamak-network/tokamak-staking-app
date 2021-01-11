package tokamak.staking.app;
import tokamak.staking.app.SBPManager;

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

import java.util.Map;
import java.util.HashMap;
import android.util.Log;
import android.content.Context;

public class BlockchainModule  extends ReactContextBaseJavaModule{
    EthereumAccount account = SBPManager.getEthereumAccount();

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
        SBPManager.getInstance().initializeSBlockChain(context);

//        Log.i("Tokamak App", "module account address: " + account.getAddress());

    }
}
