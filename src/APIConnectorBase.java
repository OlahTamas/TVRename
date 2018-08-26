import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static java.lang.Thread.*;

public class APIConnectorBase {

    protected String ldi(String kfname) throws IOException {
        String fn = "".concat("/").concat(".keys").concat("/").concat(kfname).concat(".key");
        InputStream k = Renamer.class.getResourceAsStream(fn);
        BufferedReader r = new BufferedReader(new InputStreamReader(k));
        String x = r.readLine();

        return ci(x);
    }

    protected String ci(String wk) throws UnsupportedEncodingException {
        ArrayList<String> wkd = new ArrayList<String>(Arrays.asList(wk.split("\\,")));
        String[] ko = new String(Base64.getDecoder().decode(wkd.get(wkd.size() - 1)), "UTF-8").split(",");
        String r = new String();
        for (int i = 0; i < ko.length; i++) {
            r = r.concat(wkd.get(Integer.parseInt(ko[i]) - 1));
        }

        return new String(Base64.getDecoder().decode(r), "UTF-8");
    }

}
