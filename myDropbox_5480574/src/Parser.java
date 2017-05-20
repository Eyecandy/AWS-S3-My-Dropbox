
/**
 *
 * @author joakimnilfjord
 */
import java.util.*;
public class Parser {
    //starts mainloop, initialize hashMap for commandValidation
    //I.e check correctness of command format
    //if Command is correct, then it gets sent to CommandHandler class
    public void parseLine(boolean running) {
        HashMap<String,Integer> validCmdAndLen = new HashMap();
        validCmdAndLen.put("newuser",4);validCmdAndLen.put("login",3);
        validCmdAndLen.put("logout",1);validCmdAndLen.put("put",2);
        validCmdAndLen.put("view",1);validCmdAndLen.put("get",2);
        validCmdAndLen.put("quit",1);
        boolean isValidCommand;
        while(running) {
            Scanner sc = new Scanner(System.in);
            System.out.print(">> ");
            String input = sc.nextLine();
            String[] inputList = input.split(" ");
            isValidCommand = cmdValidator(inputList,validCmdAndLen);
            if (isValidCommand) {
                running = CommandHandler.takeAction(inputList,running);
            }
        }
    }
    //compares typed in keyWord with keyWords in hashmap and expected arguement length for that keyWord.
    public boolean cmdValidator(String[] inputList,HashMap<String,Integer> validCmdAndLen) {
        String keyWord = inputList[0];
        int inputLength = inputList.length;
        boolean isContaining = validCmdAndLen.containsKey(keyWord);
        int correctLength;
        boolean isValid;
        if (isContaining) {
            correctLength = validCmdAndLen.get(keyWord);
            if (correctLength == inputLength) {
                return isValid = true;
            }
            else {
                MessageHandler.printCommandLineErrorMessages(2, validCmdAndLen);
                return isValid = false;
            }
        }
        else {
            MessageHandler.printCommandLineErrorMessages(1, validCmdAndLen);
            return isValid = false;
        }
    }
}
