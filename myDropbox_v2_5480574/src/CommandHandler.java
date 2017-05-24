
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.s3.AmazonS3;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools  | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author joakimnilfjord
 */
public class CommandHandler {
    static String  bucketName = "mydropbox5480574";
    //Decides which action to take, calls functions accordingly.
    public static boolean takeAction(String[] inputList,boolean running)   {
        String keyWord = inputList[0];
        switch (keyWord) {
            case "newuser":
                RDSService.register(inputList);
                return running = true;
            case "login":
                attemptLogin(inputList);
                return running = true;
            case "put":
                String filepath = inputList[1];
                upload(filepath);
                return running = true;
            case "get":
                download(inputList);
                return running = true;
            case "share":
                share(inputList);
                return running = true;
            
            case "view":
                viewUserObject();
                return running = true;
                
            case "quit":
                return running = false;
                
            case "logout":
                logOut();
                return running = true;
        }     
        return running = true;
    }
    //gives user s3Connection if user successfully logged in.
    //gives user dynamoDb connection if user successfully logged in.
    //called by attemptLogin
    private static void openConnectionsToUser(String id) {
        if (id != null) {
            AmazonS3 S3client = S3Service.initialize(bucketName);
            AmazonDynamoDB dynamoDbClient = DynamoDataBaseService.initializeConnection();
            User.setS3Connection(S3client);
            User.setDynamoDbClient(dynamoDbClient);
        }
        else {
            MessageHandler.printLoginError(1);
        }
    }
    //calls S3Service for upload, if User is logged in.
    private static void upload(String filepath) {
        AmazonS3 s3Client = User.getS3Connection();
        
        if (s3Client != null) {
            S3Service.addObjectToBucket(s3Client, bucketName, filepath, filepath);
             MessageHandler.successMessage();
        }
        else {
            MessageHandler.printConnectionerrors(1);
        }
    }
    //if user logged in call S3service for upload
    private static void viewUserObject() {
       AmazonS3 s3Client = User.getS3Connection();
       if (s3Client != null ) {
            MessageHandler.successMessage();
            String id = User.getId();
            S3Service.viewObjectsInBucket(s3Client, bucketName,id);
            DynamoDataBaseService.instance.viewSharedFiles(User.getDynamoDbClient(), id,bucketName);
            
       }
       else {
           MessageHandler.printConnectionerrors(2);
       }
    }
    //attempt to login, only works if no user already logged in
    private static void attemptLogin(String[] inputList) {
        
        if (User.getId() == null) {
            String id= RDSService.login(inputList);
            User.setId(id);
            String name = inputList[1];
            User.setName(name);
            openConnectionsToUser(id);
            
        }
        else {
            MessageHandler.printLoginError(2);
        }
    }
    //set S3connection & userId of user to NULL.
    private static void logOut() {
        if (User.getId() != null) {
            User.setS3Connection(null);
            User.setId(null);
            User.setDynamoDbClient(null);
            MessageHandler.successMessage();
        }
        else {
            System.out.println("You are not logged in, you have to login to logout");
        }
    }
    //if user has connection call S3Service for download
    private static void download(String inputList[]) {
        String userId = User.getId();
        AmazonDynamoDB dynamoDbClient = User.getDynamoDbClient();
        if (inputList.length == 3 && dynamoDbClient != null) {
        
         String idToDownloadFrom = DynamoDataBaseService.attemptGetItem(dynamoDbClient, inputList, userId);
         if (idToDownloadFrom != null) {
             userId = idToDownloadFrom;
         }
        }
        else {
            return;
        }
        String keyName = inputList[1];
        AmazonS3 s3Client = User.getS3Connection();
        if (s3Client != null) {
            MessageHandler.successMessage();
            S3Service.getObjectInBucket( userId, s3Client, bucketName, keyName);
        }
        else {
            MessageHandler.printConnectionerrors(2);
        }
    }
    //if user is logged in,then allow sharing
    private static void share(String inputList[]) {
        AmazonDynamoDB dynamoDbClient = User.getDynamoDbClient();
        if (User.getDynamoDbClient()!= null) {
            DynamoDataBaseService.instance.putItem(dynamoDbClient,inputList,bucketName);
        }
        else {
            MessageHandler.printConnectionerrors(3);
        
        }
    
    }
}
