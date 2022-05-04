package name.emu.monitoring;

import com.google.gson.Gson;
import name.emu.monitoring.uptimerobot.MonitorsResponse;
import okhttp3.*;

import java.io.IOException;
import java.util.TimerTask;

public class UpdateStatusTimerTask extends TimerTask {

    private static final int REQUEST_MONITOR_STATUS = 9;  // 9 = down, 8 = seems down ?

    private Application application;

    private Configuration configuration;

    private Gson gson = new Gson();

    public UpdateStatusTimerTask(Application application, Configuration configuration) {
        this.application = application;
        this.configuration = configuration;
    }

    @Override
    public void run() {
        Status status = queryUptimeRobotApi();

        application.updateStatus(status);
    }

    private Status queryUptimeRobotApi() {
        Status status = Status.UNKNOWN;
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "api_key=" + configuration.getApiKey() + "&format=json&logs=1&statuses="+REQUEST_MONITOR_STATUS);
            Request request = new Request.Builder()
                    .url("https://api.uptimerobot.com/v2/getMonitors")
                    .post(body)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String responseStr = response.body().string();
                MonitorsResponse parsedResponse = gson.fromJson(responseStr, MonitorsResponse.class);

                if ("ok".equals(parsedResponse.getStat()) && parsedResponse.getMonitors().isEmpty()) {
                    status = Status.UP;
                } else {
                    status = Status.DOWN;
                }
            } else {
                status = Status.DOWN;
            }
        } catch (IOException e) {
            status = Status.DOWN;
        }
        return status;
    }
}
