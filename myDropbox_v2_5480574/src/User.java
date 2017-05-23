
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.s3.AmazonS3;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author joakimnilfjord
 */
public class User {
    private static String idx;
    private static AmazonS3 s3clientx;
    private static AmazonDynamoDB dynamoDbClientx;
    private static String namex;

    
    
    
    
    public static String getName() {
        return namex;
    }

    public static void setName(String namex) {
        User.namex = namex;
    }

 
    public static AmazonDynamoDB getDynamoDbClient() {
        return dynamoDbClientx;
    }

    public static void setDynamoDbClient(AmazonDynamoDB dynamoDbClient) {
        dynamoDbClientx = dynamoDbClient;
    }

    
    
    
    
    
    
    //static setters and getters
    //
    
    public static void setId(String id) {
        idx = id;
    }
    
    public static void setS3Connection(AmazonS3 s3client) {
       s3clientx = s3client;
    }
    
   
     public static AmazonS3 getS3Connection() {
         return s3clientx;
    }
    
    public static String  getId() {
        return idx;
    }
    

    
    
    
    
    
}
