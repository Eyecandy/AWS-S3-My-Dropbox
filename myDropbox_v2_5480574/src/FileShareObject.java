
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author joakimnilfjord
 */
 @DynamoDBTable(tableName = "myDropboxUsers")
    public class FileShareObject {
        //partition key
        @DynamoDBHashKey(attributeName = "sharedID")
        private String sharedId;
        @DynamoDBAttribute(attributeName = "owner_id")
        private String ownerId;
        //range key
        @DynamoDBRangeKey(attributeName = "ownerName_filePath")
        private String ownerName_filePath;
        
        public String getSharedId() {
            return sharedId;
        }
        public void setSharedId(String sharedId) {
            
            this.sharedId = sharedId;
        }
        
        public String getOwnerName_filePath() {
            
            return ownerName_filePath;
        }

        public void setOwnerName_filePath(String ownerName) {
            
            this.ownerName_filePath = ownerName;
        }
        
        public String getOwnerId() {
            return ownerId;
        }
        
        public void setOwnerId(String ownerId) {
            this.ownerId = ownerId;
        }
        
    
       
    }
