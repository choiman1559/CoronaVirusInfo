package corona.virus.info;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class CoronaVirusInfo {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TOTAL, UNDER_TREATMENT, RECOVERED,DEAD})
    public @interface TYPE {}

    public static final int TOTAL = 0;
    public static final int UNDER_TREATMENT = 1;
    public static final int RECOVERED = 2;
    public static final int DEAD = 3;

    public int getInt(@Nullable @CountryCode.Country String Country, @TYPE int TYPE) throws IOException {
        URLConnection connection = new URL("https://api.covid19api.com/summary").openConnection();
        try(Scanner scanner = new Scanner(connection.getInputStream());){
            String response = scanner.useDelimiter("\\A").next();
            System.out.println(response);
        }
        return 0;
    }
}
