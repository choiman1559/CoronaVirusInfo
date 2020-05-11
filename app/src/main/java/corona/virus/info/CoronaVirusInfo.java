package corona.virus.info;

import android.util.Log;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.Manifest.permission.INTERNET;

public class CoronaVirusInfo {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TOTAL, ACTIVE, RECOVERED, DEAD})
    public @interface TYPE {
    }

    public static final int ACTIVE = 4;
    public static final int RECOVERED = 5;
    public static final int DEAD = 6;
    public static final int TOTAL = 7;

    volatile String temp = null;


    @RequiresPermission(INTERNET)
    public int getInt(@CountryCode.Country String Country, @TYPE int TYPE) {
        return getInt(Country, TYPE, null);
    }

    @RequiresPermission(INTERNET)
    public int getInt(@CountryCode.Country String Country, @TYPE int TYPE, @Nullable Calendar date) {
        if(Country.equals("") && date != null) return -1;
        String url = !Country.equals("") ? "https://api.covid19api.com/country/" + Country : "https://api.covid19api.com/summary";

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException ignore) {
                throw new RuntimeException("request Failed while get HTTP body!");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) {
                try {
                    temp = response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        while (temp == null) {
        }
        String[] lines = temp.split(Objects.requireNonNull(System.getProperty("line.separator")));
        if(lines[0].contains("code") && lines[0].contains("message")) throw new RuntimeException("Http error occured : " + lines[0]);
        if (!Country.equals("") && date == null)
            return Integer.parseInt(Between(lines[lines.length - TYPE], ": ", ","));

        if(date != null ) Log.d("test",new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date));
        for (int i = 0; i < lines.length; i++) {
            if (date != null) {
                Log.d("test","for");
            if (lines[i].contains(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date))) {
                Log.d("test", Between(lines[i - (TYPE - 3)], ": ", ","));
                return Integer.parseInt(Between(lines[i - (TYPE - 3)], ": ", ","));
                }
            }
            if(Country.equals("") && !lines[i].contains(",")) lines[i] = lines[i] + ",";
        }

        if(Country.equals("")) switch (TYPE) {
            case ACTIVE:
                return Integer.parseInt(Between(lines[3], ": ", ",")) -
                        (Integer.parseInt(Between(lines[7], ": ", ",")) +
                                Integer.parseInt(Between(lines[5], ": ", ",")));

            case RECOVERED:
                return Integer.parseInt(Between(lines[7], ": ", ","));

            case DEAD:
                return Integer.parseInt(Between(lines[5], ": ", ","));

            case TOTAL:
                return Integer.parseInt(Between(lines[3], ": ", ","));
        }
        return -1;
    }

    private String Between(String str, String open, String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        int start = str.indexOf(open);
        if (start != -1) {
            int end = str.indexOf(close, start + open.length());
            if (end != -1) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }
}
