
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
    public static void printConnectionerrors(int errorCode) {
        
        if (errorCode == 1) {
            System.out.println("You must login to upload files");     
        }
        else if (errorCode == 2) {
            System.out.println("No s3 Connection, connected to you");
        }
        else if (errorCode == 3) {
            System.out.println("You must login to share files");
        }
    }
    
    public static void printShareErrors(int errorNum){
        if (errorNum==1) {
            System.out.println("Sharing error: no such registered user");
        }
        else if (errorNum == 2) {
            System.out.println("You can not share with yourself");
        }
        else if (errorNum == 3) {
            System.out.println("Bad filepath, does not exist on your AWS S3");
        }
    }
    public static void objectError(int errorNum) {
        if (errorNum == 1) {
            System.out.println("share attempt failed: object does not exist");
        }
    }
    //success message
    public static void successMessage() {
        System.out.println("OK");
    }
}
