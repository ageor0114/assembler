package dev.austingeorge.riscvassembler;

//import org.apache.tomcat.util.json.JSONParser;
import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.File;
import java.io.FileReader;
import java.net.URL;

public class Data {
    /*private static String[][] table = {
            {},
            {},
    };*/
    private static JSONObject data;

    public static final File CWD = new File(System.getProperty("user.dir"));

    //Initializes all data
    public static void initialize() {
        File directory = new File("");
        String cwd = directory.getAbsolutePath();

        System.out.println(CWD);
        System.out.println(cwd);
        String jsonPath = cwd + "\\src\\main\\java\\dev\\austingeorge\\riscvassembler\\logic.json";

        //FileReader fr = new FileReader(cwd + "src\\main\\java\\dev.austingeorge.riscv-assembler\\logic.json");

        JSONParser parser = new JSONParser();
        try {
            //URL url = Data.class.getResource("logic.json");
            //Object obj = parser.parse(new FileReader("/src/logic.JSON"));
            Object obj = parser.parse(new FileReader(jsonPath));
            data = (JSONObject)obj;
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("initialized data");
        System.out.println("test query");
        System.out.println((String)((JSONObject)data.get("addi")).get("type"));
    }

    public static JSONObject data() {
        return data;
    }

}
