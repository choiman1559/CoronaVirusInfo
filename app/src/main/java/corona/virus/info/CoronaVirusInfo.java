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

/**
 * @author Choiman1559
 * github.com/choiman1559
 */

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
        return Calculate(Country, TYPE, null,3600);
    }

    @RequiresPermission(INTERNET)
    public int getInt(@CountryCode.Country String Country,@TYPE int TYPE, int limitMs) {
        return Calculate(Country,TYPE,null,limitMs);
    }

    @RequiresPermission(INTERNET)
    public int getInt(@CountryCode.Country_withoutGlobal String Country,@TYPE int TYPE, Calendar date) {
        return Calculate(Country,TYPE,date,3600);
    }

    @RequiresPermission(INTERNET)
    public int getInt(@CountryCode.Country_withoutGlobal String Country,@TYPE int TYPE,Calendar date, int limitMs) {
        return Calculate(Country,TYPE,date,limitMs);
    }

    private int Calculate(String Country, int TYPE, @Nullable Calendar date, int limitMs) {
        if(Country.equals("") && date != null) {
            new IllegalArgumentException("if you're using \"Country.Global\" argument, value of \"Calender date\" must be always \"null\"!").printStackTrace();
            return -1;
        }
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
                    temp = Objects.requireNonNull(response.body()).string();
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
        if(json == null) {
            new NullPointerException("received data not must be null!").printStackTrace();
            return -1;
        }
        temp = null;

        try {
            if (!Country.equals("") && date == null) {
                JSONArray array = new JSONArray(json);
                JSONObject data = new JSONObject(array.get(array.length() - 1).toString());
                return data.getInt(getType(TYPE));
            }

            if(date != null) {
                JSONArray array = new JSONArray(json);
                String dates = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date.getTime());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject data = new JSONObject(array.get(i).toString());
                    if (data.getString("Date").contains(dates)) {
                        return data.getInt(getType(TYPE));
                    }
                }
                new IllegalArgumentException("Date " + dates + " not found in received data.").printStackTrace();
                return -3;
            }

            JSONObject data = new JSONObject(json).getJSONObject("Global");
            switch (TYPE) {
                case ACTIVE:
                    return  data.getInt("TotalConfirmed")
                            - (data.getInt("TotalRecovered") + data.getInt("TotalDeaths"));

                case RECOVERED:
                    return data.getInt("TotalRecovered");

                case DEAD:
                    return data.getInt("TotalDeaths");

                case TOTAL:
                    return data.getInt("TotalConfirmed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        new IllegalStateException("Other undefined error.").printStackTrace();
       return -1;
    }

    @Deprecated
    @RequiresPermission(INTERNET)
    public int getInt_old(@CountryCode.Country String Country, @TYPE int TYPE, @Nullable Calendar date) {
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
                    temp = Objects.requireNonNull(response.body()).string();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        while (true) { if(temp != null) break; }
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
            case TOTAL:
                return "Confirmed";

            case DEAD:
                return "Deaths";

            case RECOVERED:
                return "Recovered";

            case ACTIVE:
                return "Active";
        }
        return "";
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
