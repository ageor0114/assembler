package dev.austingeorge.riscvassembler;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.stream.Collectors;

public class Utils {
    public static String[] compile(String source) throws IOException {
        //===============================
        //         COMPILATION
        //===============================
        //Establish HTTP Connection
        URL url = new URL("https://godbolt.org/api/compiler/rv32-gcc1020/compile");
        HttpURLConnection connect = (HttpURLConnection) url.openConnection();
        connect.setRequestMethod("POST");
        connect.setRequestProperty("Content-Type", "application/json; utf-16");
        connect.setDoOutput(true);
        String jsonInputString = source;

        //Parse Input Stream
        try(OutputStream os = connect.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        InputStream inputStream = connect.getInputStream();

        String res = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));

        String[] arr = res.split("\n");

        return arr;
    }

    public static String toTwos(int imm, int bits) {
        String res = Integer.toBinaryString(imm);

        if (imm < 0) {
            //Trim
            return res.substring(res.length()-bits);
        } else {
            //Pad 0's
            int width = bits - res.length();
            for (int i = 0; i < width; i++) res = "1" + res;
            return res;
        }


        /*if (imm >= 0) {
            int curr = (int)Math.pow(2,bits-1);
            int rest = imm;
            String res = "";
            while (curr > 0) {
                if (rest >= curr) {
                    res += "1";
                    rest -= curr;
                }
                curr /= 2;
            }
            return res;
        }*/

    }
}
