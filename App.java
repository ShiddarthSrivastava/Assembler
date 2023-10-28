import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        Map<String, Integer> labels = new HashMap<>();
        int locCounter = 0;
        String[] input = {
            "LOC 6",
            "Data 10",
            "Data 3",
            "Data 7",
            "Data 0",
            "Data 12",
            "Data 9",
            "Data 18",
            "Data 12",
            "LDX 2,7",
            "LDR 3,0,10",
            "LDR 2,2,10",
            "LDR 1,2,10,1",
            "LDA 0,0,0",
            "LDX 1,9",
            "JZ 0,1,0",
            "LOC 1024",
            "End: HLT"
        };

        for (String line : input) {
            String[] parts = line.split(" ");
            String opcode = parts[0];

            if (opcode.equals("LOC")) {
                locCounter = Integer.parseInt(parts[1]);
            } else if (opcode.equals("Data")) {
                int dataValue = Integer.parseInt(parts[1]);
                System.out.printf("%04X %04X %s %d%n", locCounter, dataValue, opcode, dataValue);
                locCounter++;
            } else if (opcode.equals("End:")) {
                labels.put(parts[0].substring(0, parts[0].length() - 1), locCounter);
            } else if (opcode.endsWith(":")) {
                labels.put(parts[0].substring(0, parts[0].length() - 1), locCounter);
            } else {
                String[] operands = parts[1].split(",");
                String binaryInstruction = convertToBinary(opcode, operands, labels);
                System.out.printf("%04X %04X %s %s%n", locCounter, Integer.parseInt(binaryInstruction, 16), opcode, line.substring(parts[0].length()));
                locCounter++;
            }
        }
    }

    private static String convertToBinary(String opcode, String[] operands, Map<String, Integer> labels) {
        // Define your binary encoding logic for each assembly instruction here.
        // I'm providing a simple example for the given instructions.
        if (opcode.equals("LDX")) {
            int reg1 = Integer.parseInt(operands[0]);
            int reg2 = Integer.parseInt(operands[1]);
            return String.format("%04X", 0x8000 | (reg1 << 8) | reg2);
        } else if (opcode.equals("LDR")) {
            int reg1 = Integer.parseInt(operands[0]);
            int reg2 = Integer.parseInt(operands[1]);
            int offset = Integer.parseInt(operands[2]);
            int immFlag = operands.length > 3 ? 0x8000 : 0;
            return String.format("%04X", 0x4000 | (reg1 << 8) | (reg2 << 4) | offset | immFlag);
        } else if (opcode.equals("LDA")) {
            int reg1 = Integer.parseInt(operands[0]);
            int reg2 = Integer.parseInt(operands[1]);
            int immFlag = operands.length > 2 ? 0x8000 : 0;
            return String.format("%04X", 0xC000 | (reg1 << 8) | (reg2 << 4) | immFlag);
        } else if (opcode.equals("JZ")) {
            int reg1 = Integer.parseInt(operands[0]);
            int reg2 = Integer.parseInt(operands[1]);
            int offset = Integer.parseInt(operands[2]);
            return String.format("%04X", 0x8000 | (reg1 << 8) | (reg2 << 4) | offset);
        } else {
            return "0000"; // Handle other opcodes as needed.
        }
    }
}
