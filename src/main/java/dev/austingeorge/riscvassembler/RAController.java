package dev.austingeorge.riscvassembler;

import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

@RestController
public class RAController {

    @PostMapping("/assembles")
    public Instruction[] assembles(@RequestBody String source) throws IOException {
        //===============================
        //         COMPILATION
        //===============================
        //Establish HTTP Connection
        /*URL url = new URL("https://godbolt.org/api/compiler/rv32-gcc1020/compile");
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

        String[] arr = res.split("\n");*/

        String[] arr = Utils.compile(source);

        Instruction[] instructions = new Instruction[arr.length-1];

        System.out.println(arr.length);

        //===============================
        //         ASSEMBLY
        //===============================

        //Initialize Data
        Data.initialize();


        //Iterate over all instructions
        for (int i = 1; i < arr.length; i++) {
            System.out.println("Starting Construction of Instruction " + i);
            //New Model
            //Instruction[] currInstructions = Instructions.generateInstructions(arr[i]);
            //the above function will understand whether or not it's a pseudoinstruction, and will return a list of a single inst, or more, accordingly
            //> instructions extend currInstructions (be mindful that now full length is not easily predetermined
            //** note: todo: also filter out blank return spaces

            Instruction currInst = new Instruction(arr[i]);
            instructions[i-1] = currInst;
            System.out.println(i + ": " + currInst);
        }

        for (Instruction i : instructions) {
            System.out.println(i);
        }

        /*for (String inst : arr) {
            Instruction currInst = new Instruction(inst);
            System.out.println(inst);
            System.out.println("x");
        }*/

        System.out.println("Assembles RV" + instructions);
        return instructions;
    }

    @GetMapping("/assemble")
    public String assemble(@RequestParam(value = "source", defaultValue = "int square(int x) { return x * x; }") String source) throws IOException {
        URL url = new URL("https://godbolt.org/api/compiler/rv32-gcc1020/compile");
        HttpURLConnection connect = (HttpURLConnection) url.openConnection();
        connect.setRequestMethod("POST");
        connect.setRequestProperty("Content-Type", "application/json; utf-16");
        //connect.setRequestProperty("Accept", "application/json");
        connect.setDoOutput(true);
        String jsonInputString = source;
        //String jsonInputString = "int add(int a, int b) { return (a + b); } int main() { int sum; sum = add(100, 78); return 0; }";
        //String jsonInputString = "[{\"source\": \"int add(int a, int b) { return (a + b); } int main() { int sum; sum = add(100, 78); return 0;}\", \"compiler\":\"rv32-gcc1020\", \"options\": { \"userArguments\": \"\", \"compilerOptions\": { \"skipAsm\": false, \"executorRequest\": false }, \"filters\": { \"binary\": false, \"commentOnly\": true, \"demangle\": true, \"directives\": true, \"execute\": false, \"intel\": true, \"labels\": true, \"libraryCode\": false, \"trim\": false }, \"tools\": [ {\"id\":\"clangtidytrunk\", \"args\":\"-checks=*\"} ], \"libraries\": [ {\"id\": \"range-v3\", \"version\": \"trunk\"}, {\"id\": \"fmt\", \"version\": \"400\"} ] }, \"lang\": \"c\", \"allowStoreCodeDebug\": true }]";

        try(OutputStream os = connect.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        InputStream inputStream = connect.getInputStream();

        String res = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));

        System.out.println(res);
        return res;
    }

}
