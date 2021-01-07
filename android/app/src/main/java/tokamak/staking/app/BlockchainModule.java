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

public class BlockchainModule  extends ReactContextBaseJavaModule{

    BlockchainModule(ReactApplicationContext context) {
        super(context);
    }

    @Override
    public String getName() {
        return "BlockchainModule";
    }
    @ReactMethod
    public void initialize(String name, String location) {

        Context context = getReactApplicationContext();

        Log.d("CalendarModule", "Create event called with name: " + name
                + " and location: " + location);
    }
}
