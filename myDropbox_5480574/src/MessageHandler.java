
/**
 *
 * @author joakimnilfjord
 */
import java.util.*;
public class MessageHandler {
    //messages related to command typos or command line args correctness
    public static void printCommandLineErrorMessages(int errorCode,HashMap<String,Integer> validCmdAndLen) {
        if (errorCode == 1) {
            System.out.println("Not a valid command, valid commands: " + validCmdAndLen.keySet().toString());
        }
        else if (errorCode == 2) {
            System.out.println("incorrect number of arguements, list of commands with expected arg length: " + validCmdAndLen.toString());
        }
    }
    //login erros msg
    public static void printLoginError(int errorCode) {
        if (errorCode == 1) {
            System.out.println("Invalid login credentials");        
        }
        else if (errorCode == 2) {
            System.out.println("You are already logged in ");
        }
    }
    //s3 Errors msg
    public static void printS3errors(int errorCode) {
        
        if (errorCode == 1) {
            System.out.println("You must login to upload files");     
        }
        else if (errorCode == 2) {
            System.out.println("No s3 Connection, connected to you");
        }
        
    }
    //success message
    public static void successMessage() {
        System.out.println("OK");
    }
}
