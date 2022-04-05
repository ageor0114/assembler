package dev.austingeorge.riscvassembler;

import java.io.IOException;

public class TestSuite {
    public static void main(String[] args) throws IOException {
        testBinaryConversion();
    }

    public static void testInstructions() throws IOException {
        RAController controller = new RAController();
        Instruction[] instructions = controller.assembles("int dog(int x) { return x + 2; }");
    }

    public static void testBinaryConversion() {
        for (int i = -33; i < 33; i++) {
            String goal = Integer.toBinaryString(i);
            String actual = Utils.toTwos(i,12);
            System.out.println(i + " " + goal + " " + actual);
        }
    }
}
