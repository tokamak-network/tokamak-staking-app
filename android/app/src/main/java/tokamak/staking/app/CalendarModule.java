package tokamak.staking.app;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import java.util.Map;
import java.util.HashMap;
import android.util.Log;
import android.content.Context;
import android.widget.Toast;
public class CalendarModule extends ReactContextBaseJavaModule {
    CalendarModule(ReactApplicationContext context) {
        super(context);
    }
    @Override
    public String getName() {
        return "CalendarModule";
    }
    @ReactMethod
    public void createCalendarEvent(String name, String location) {
        Context context = getReactApplicationContext();
        Toast.makeText(context, name, Toast.LENGTH_LONG).show();
        Log.d("CalendarModule", "Create event called with name: " + name
                + " and location: " + location);
    }


}
