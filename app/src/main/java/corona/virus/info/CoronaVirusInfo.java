package corona.virus.info;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    public int getInt(@CountryCode.Country String Country,@TYPE int TYPE, int limitMs) {
        return getInt(Country,TYPE,null,limitMs);
    }

    @RequiresPermission(INTERNET)
    public int getInt(@CountryCode.Country String Country, @TYPE int TYPE) {
        return getInt(Country, TYPE, null,3600);
    }

    @RequiresPermission(INTERNET)
    public int getInt(@CountryCode.Country String Country,@TYPE int TYPE,@Nullable Calendar date) {
        return getInt(Country,TYPE,date,3600);
    }

    @RequiresPermission(INTERNET)
    public int getInt(@CountryCode.Country_withoutGlobal String Country, @TYPE int TYPE, @Nullable Calendar date, int limitMs) {
        if(Country.equals("") && date != null)
            throw new IllegalArgumentException("if you're using \"Country.Global\" argument, value of \"Calender date\" must be always \"null\"!");
        String url = !Country.equals("") ? "https://api.covid19api.com/country/" + Country : "https://api.covid19api.com/summary";

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException ignore) {
                throw new RuntimeException(ignore.getMessage(),ignore.getCause());
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
        Calendar stopwatch = Calendar.getInstance();
        stopwatch.setTime(new Date(System.currentTimeMillis()));
        stopwatch.add(Calendar.SECOND,limitMs / 1000);

        while (temp == null) { if(stopwatch.getTimeInMillis() - System.currentTimeMillis() <= 0) return -2; }
        String json = temp;
        if(json == null) return -1;
        temp = null;

        try {
            JSONArray array = new JSONArray(json);
            if (!Country.equals("") && date == null) {
                JSONObject data = new JSONObject(array.get(array.length() - 1).toString());
                return data.getInt(getType(TYPE));
            }

            for(int i = 0;i < array.length();i++) {
                JSONObject data = new JSONObject(array.get(i).toString());
                if(data.getString("Date").contains(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date.getTime()))) {
                    return data.getInt(getType(TYPE));
                }
            }

            String[] lines = json.split(Objects.requireNonNull(System.getProperty("line.separator")));

            if(Country.equals("")) switch (TYPE) {
                case ACTIVE:
                    return Integer.parseInt(Between(lines[3])) -
                            (Integer.parseInt(Between(lines[7])) +
                                    Integer.parseInt(Between(lines[5])));

                case RECOVERED:
                    return Integer.parseInt(Between(lines[7]));

                case DEAD:
                    return Integer.parseInt(Between(lines[5]));

                case TOTAL:
                    return Integer.parseInt(Between(lines[3]));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

       return -1;
    }

    @Deprecated
    @RequiresPermission(INTERNET)
    public int getInt_old(@CountryCode.Country_withoutGlobal String Country, @TYPE int TYPE, @Nullable Calendar date, int limitMs) {
        if(Country.equals("") && date != null)
            throw new IllegalArgumentException("if you're using \"Country.Global\" argument, value of \"Calender date\" must be always \"null\"!");
        String url = !Country.equals("") ? "https://api.covid19api.com/country/" + Country : "https://api.covid19api.com/summary";

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException ignore) {
                throw new RuntimeException(ignore.getMessage(),ignore.getCause());
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
        Calendar stopwatch = Calendar.getInstance();
        stopwatch.setTime(new Date(System.currentTimeMillis()));
        stopwatch.add(Calendar.SECOND,limitMs / 1000);

        while (temp == null) { if(stopwatch.getTimeInMillis() - System.currentTimeMillis() <= 0) return -2; }
        String[] lines = null;
        try { lines = new JSONObject(temp).toString().split(Objects.requireNonNull(System.getProperty("line.separator"))); }
        catch (Exception e) { e.printStackTrace(); }
        if(lines == null) return -1;
        temp = null;

        if(lines[0].contains("code") && lines[0].contains("message")) throw new RuntimeException("Http error occured : " + lines[0]);
        if (!Country.equals("") && date == null) {
            return Integer.parseInt(Between(lines[lines.length - TYPE]));
        }

        for (int i = 0; i < lines.length; i++) {
            if (date != null && lines[i].contains(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date.getTime()))) {
                return Integer.parseInt(Between(lines[i - (TYPE - 3)]));
                }
            if(Country.equals("") && !lines[i].contains(",")) lines[i] = lines[i] + ",";
        }

        if(Country.equals("")) switch (TYPE) {
            case ACTIVE:
                return Integer.parseInt(Between(lines[3])) -
                        (Integer.parseInt(Between(lines[7])) +
                                Integer.parseInt(Between(lines[5])));

            case RECOVERED:
                return Integer.parseInt(Between(lines[7]));

            case DEAD:
                return Integer.parseInt(Between(lines[5]));

            case TOTAL:
                return Integer.parseInt(Between(lines[3]));
        }
        return -1;
    }

    private String getType(@TYPE int type) {
        switch (type) {
            case 7:
                return "Confirmed";

            case 6:
                return "Deaths";

            case 5:
                return "Recovered";

            case 4:
                return "Active";

            default:
                return null;
        }
    }

    private String Between(String str) {
        if (str == null) {
            return null;
        }
        int start = str.indexOf(": ");
        if (start != -1) {
            int end = str.indexOf(",", start + ": ".length());
            if (end != -1) {
                return str.substring(start + ": ".length(), end);
            }
        }
        return null;
    }
}
