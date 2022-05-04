package name.emu.monitoring;

import java.util.prefs.Preferences;

public class Configuration {
    private static final String PREFS_NODE="name.emu.monitoring.urmonitor";

    private static final String KEY_APIKEY = "apiKey";

    private static final String KEY_TIMERINTERVAL = "timerInterval";

    private static final long DEFAULT_TIMERINTERVAL = 1000 * 10; //60 * 3; // default interval is 3 minutes

    private Preferences preferences;

    public void init() {
        preferences = Preferences.userRoot().node(PREFS_NODE);
    }

    public String getApiKey() {
        return preferences.get(KEY_APIKEY, null);
    }

    public void setApiKey(String apiKey) {
        preferences.put(KEY_APIKEY, apiKey);
    }

    public long getTimerInterval() {
        return preferences.getLong(KEY_TIMERINTERVAL, DEFAULT_TIMERINTERVAL);
    }
}
