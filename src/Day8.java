import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;

enum Code
{
    none,
    nop,
    acc,
    jmp
}

class Instruction
{
    Code code = Code.none;
    int  arg = 0;
}

public class Day8
{
    List<Instruction> instructions = new ArrayList<Instruction>();

    public static void main( String[] args )
    {
        Day8 day8 = new Day8();

        Instant start = Instant.now();

        day8.parse();
        day8.execute(0, 0, false);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }

    public void parse() {
    
        // Parse lines.
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("day8_input.txt"));

            Instruction instruction;

            String codeString = "";
            String argString = "";
            Integer arg = 0;

            String line;
            char c;
     
            line = reader.readLine();
            while (line != null) {
        
                instruction = new Instruction();

                codeString = line.substring(0, 3);
                argString = line.substring(5, line.length());
                
                arg = Integer.parseInt(argString);

                if (line.substring(4, 5).equals("-"))
                    arg *= -1;

                switch (codeString) {
                    case "nop":
                        instruction.code = Code.nop;
                        break;

                    case "acc":
                        instruction.code = Code.acc;
                        instruction.arg = arg;
                        break;

                    case "jmp":
                        instruction.code = Code.jmp;
                        break;

                    default:
                        break;
                }

                instruction.arg = arg;

                instructions.add(instruction);

                line = reader.readLine();
            }
           
            reader.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // At each nop or jmp instruction flip to the other and execute a branch. If the branch completes return true.
    // Only the initial execution may branch because only one instruction can be modified.
    // Each base / branch execution tracks its own call counts for loop detection.
    boolean execute(int ipStart, int accStart, boolean inBranch) {

        int ip = ipStart;
        int acc = accStart;
        Instruction inst = null;

        int[] callCounts = new int[instructions.size()];

        while (ip < instructions.size()) {

            inst = instructions.get(ip);

            // Flip this instruction and check for completion.
            if (!inBranch) {
                
                if (inst.code == Code.nop) {
                    inst.code = Code.jmp;
                    if (execute(ip,acc, true)) {
                        return true;
                    }
                    inst.code = Code.nop;
                }

                if (inst.code == Code.jmp) {
                    inst.code = Code.nop;
                    if (execute(ip, acc, true)) {
                        return true;
                    }
                    inst.code = Code.jmp;
                }
            }

            if (callCounts[ip] > 0) {
                // Loop detected. Failure.
                return false;
            }

            callCounts[ip]++;

            switch (inst.code) {

                case nop:
                    ip++;
                    break;

                case acc:
                    acc += inst.arg;
                    ip++;
                    break;

                case jmp:
                    ip += inst.arg;
                    break;

                default:
                    break;
            }
        }

        System.out.println("Repaired! " + acc);
        return true;
    }
}