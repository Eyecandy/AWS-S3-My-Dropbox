
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.s3.AmazonS3;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 *
 * @author joakimnilfjord
 */
public class DynamoDataBaseService {
    
    public static final DynamoDataBaseService instance = new DynamoDataBaseService();
    
    public static AmazonDynamoDB initializeConnection() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().
        withCredentials(new ProfileCredentialsProvider()).
        withRegion("ap-southeast-1").build();
        
        return client;
    }
  
    
    
    public void viewSharedFiles(AmazonDynamoDB dynamoDbClient,String userId) {
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDbClient);
        String partitionKey = userId;
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(partitionKey));
        
         //FileShareObject book = mapper.load(FileShareObject.class, "4");
         //System.out.println(book);
         
         DynamoDBQueryExpression<FileShareObject> queryExpression = new DynamoDBQueryExpression<FileShareObject>()
            .withKeyConditionExpression("sharedID = :val1")
            .withExpressionAttributeValues(eav);
       
       List<FileShareObject> fileShareListObject = mapper.query(FileShareObject.class, queryExpression);
      
       
       for (FileShareObject o: fileShareListObject) {
           String[] owner_filePath = o.getOwnerName_filePath().split("_");
           String owner = owner_filePath[0];
           String filePath = owner_filePath[1];
           System.out.println( filePath+" -shared to you by:"+ owner);
       }
    }
    
    public static String attemptGetItem(AmazonDynamoDB dynamoDbClient,String inputList[],String userId) {
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDbClient);
        String partitionKey = userId;
        String filePath = inputList[1];
        String owner = inputList[2];
        String sortKey = owner+"_"+filePath;
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(partitionKey));
        eav.put(":val2", new AttributeValue().withS(sortKey));
        
        DynamoDBQueryExpression<FileShareObject> queryExpression = new DynamoDBQueryExpression<FileShareObject>()
            .withKeyConditionExpression("sharedID = :val1 and ownerName_filePath = :val2")
            .withExpressionAttributeValues(eav);
        
        List<FileShareObject> fileShareListObject = mapper.query(FileShareObject.class, queryExpression);
        if (fileShareListObject.size() ==0) {
            System.out.println("You have no such file that is shared by that owner");
            return null;
        }
        else {
            
           String ownerId = fileShareListObject.get(0).getOwnerId();
           return ownerId;
            
            
        }

       
    }
    
    
    
    
    
    
    
    public void putItem( AmazonDynamoDB dynamoDbClient,String inputList[],String bucketName ) {
        String sharedUserId = RDSService.shareUserExist(inputList);
        if (sharedUserId == null) {
            MessageHandler.printShareErrors(2);
            return;
        }
        String ownerId = User.getId();
        if (sharedUserId.equals(ownerId )) {
            MessageHandler.printShareErrors(1);
            return;
        }
        AmazonS3 s3Client = User.getS3Connection();
        String filePath = inputList[1];
        boolean doesFileExist = S3Service.doesObjectExist(s3Client, bucketName, ownerId, filePath);
        if (doesFileExist == false) {
            MessageHandler.objectError(1);
            return;
        }
        
        
        
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDbClient);
        String name = User.getName();
        
        FileShareObject fileShareObject  = new FileShareObject();
        fileShareObject.setOwnerName_filePath(name+"_"+filePath);
        fileShareObject.setSharedId(sharedUserId);
        fileShareObject.setOwnerId(ownerId);
        dynamoDBMapper.save(fileShareObject);
        MessageHandler.successMessage();
        
    }
    
   
    
}
