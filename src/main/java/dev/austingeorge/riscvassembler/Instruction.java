package dev.austingeorge.riscvassembler;

import org.json.simple.JSONObject;

/* Notes:
    For dynamic descriptions of each operation, use formatted string templates, like Greeting.
    Pass in a few vars into a particular template and get back the beautiful desc
 */

public class Instruction {
    //todo - i don't like that all of these are public
    public String instruction;
    public String type;

    //Components
    public String op;
    public String rd;
    public String rs1;
    public String rs2;
    public int imm; //todo - would a String be better here

    //ASSEMBLY: Flavor Text
    public String opName;
    public String description;

    //MACHINE CODE: Flavor Text ?

    //MACHINE CODE: Binary
    public String binary;
    public String opcode;
    public String funct3;
    public String xRD;
    public String xRS1;
    public String xRS2;
    public String bRD;
    public String bRS1;
    public String bRS2;
    public String bImm;

    public String[] binaryColors; //ex) ["RED","RED","BLUE",...]

    //HARDWARE: Active Cct Components?? Datapath?
    public HardwareStage[] stages;


    public Instruction(String instruction) {
        this.instruction = instruction;
        System.out.println("Pre-Processed Instruction: " + instruction);
        clean();
        System.out.println("Cleaned Instruction: " + instruction);
        determineType();
        System.out.println("Instruction (Operation, Type): (" + op + ", " + type + ")");
        parseComponents();
    }

    //Eliminate extra white space
    private void clean() {
        String res = "";
        char prev = ' ';
        //Iterate over instruction
        for (char c : instruction.toCharArray()) {
            if (prev != ' ' || c != ' ') {
                res += c;
                prev = c;
            }
        }
        instruction = res;
    }

    //Determines the type, which affects how the rest of the string is parsed
    private void determineType() {
        String operation = "";
        int i = 0;
        System.out.println("About to determine type of " + instruction);
        while (i < instruction.length() && instruction.charAt(i) != ' ') {
            operation += instruction.charAt(i);
            i++;
        }
        op = operation;
        System.out.println(operation);
        JSONObject opData = (JSONObject)Data.data().get(op);
        System.out.println(opData);

        //Fetch type
        if (opData == null) {
            type = "label";
            //todo - maybe don't fill these?? i think the design goal that irrelevant <--> null is much more elegant to implement
            opName = "";
            opcode = "";
            funct3 = "";
        } else {
            type = (String)opData.get("type");
            opName = (String)opData.get("opName");
            opcode = (String)opData.get("opcode");
            funct3 = (String)opData.get("funct3");
            //todo - funct7, but not always present tbh (could be in the parseComp, once type is known
        }
    }

    //Parses the components of the string
    private void parseComponents() {
        switch(type) {
            case "I":
                parseI();
                break;
            case "ccc":
                determineType();
                break;
            default:
                System.out.println("parsing label");
        }
    }

    //Parse I Types
    private void parseI() {
        if (opcode.equals("000011")) {
            //======================
            //      Load Types
            //======================
            String[] arr = instruction.split(" ");
            String[] components = arr[1].split(",");

            rd = components[0];

            String address = components[1];
            String immString = "";
            int i = 0;

            while (address.charAt(i) != '(') {
                immString += address.charAt(i);
                i++;
            }

            imm = Integer.parseInt(immString);
            i++;

            rs2 = "";
            while (address.charAt(i) != ')') {
                rs2 += address.charAt(i);
                i++;
            }
        } else {
            //======================
            //     Non-Load Types
            //======================
            String[] arr = instruction.split(" ");
            String[] components = arr[1].split(",");
            System.out.println("arr[1]" + arr[1]);

            rd = components[0];
            rs1 = components[1];
            imm = Integer.parseInt(components[2]);

            xRD = (String)((JSONObject)Data.data().get(rd)).get("register");
            xRS1 = (String)((JSONObject)Data.data().get(rs1)).get("register");

            bRD = (String)((JSONObject)Data.data().get(rd)).get("binary");
            bRS1 = (String)((JSONObject)Data.data().get(rs1)).get("binary");
            bImm = Utils.toTwos(imm,12);
        }
        //Grab binaries
    }

    //toString
    public String toString() {
        return type + ": " + instruction;
    }

}
