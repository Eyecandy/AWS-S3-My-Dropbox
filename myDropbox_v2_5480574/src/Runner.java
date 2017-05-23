
/**
 *
 * @author joakimnilfjord
 */
public class Runner {
    //starts the application, sets running to true
    //sends running to the Parser class.
    public static void main(String[] args) {
        System.out.println("Welcome to myDropbox Application");
        Parser parser = new Parser();
        boolean running = true;
        User.setId(null);
        User.setS3Connection(null);
        parser.parseLine(running);
        System.out.println("Good Bye");
        
    }
}
