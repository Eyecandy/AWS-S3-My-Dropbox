import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class S3Service {

    /**
     * @param args the command line arguments
     */
    
    public static AmazonS3 initialize(String bucketName) {
        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider()).withRegion("ap-southeast-1")
                .build();
        createBucket(s3client,bucketName);
        MessageHandler.successMessage();
        return s3client;
        
        //String bucketName = "hell054805742";
        //createBucket(s3client,bucketName);
        //addObjectToBucket(s3client,bucketName,"helloAFD","/Users/joakimnilfjord/Desktop/untitled.ipynb");
       //viewObjectsInBucket(s3client,bucketName);
       //deleteBucket(s3client,bucketName);
    }
    
    // Create bucket named bucketName if it does not yet exist.
    // Catch all exceptions, and print error to stdout (System.out)
    
    
    
    //checks if the object exists, used when sharing.
    public static boolean doesObjectExist(AmazonS3 s3client,String bucketName,String userId,String fileName) {
        String keyName = userId+"/"+fileName;
        return s3client.doesObjectExist(bucketName,keyName);
    }
    
    
    private static void createBucket (AmazonS3 s3client, String bucketName) {

        try {
            if(!(s3client.doesBucketExist(bucketName)))
            {
                System.out.println("creating: "+ bucketName);
                // Note that CreateBucketRequest does not specify region. So bucket is
                // created in the region specified in the client.
                s3client.createBucket(new CreateBucketRequest(
                        bucketName));
            }
            // Get location.
            String bucketLocation = s3client.getBucketLocation(new GetBucketLocationRequest(bucketName));
            //System.out.println("bucket location = " + bucketLocation);

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which " +
                    "means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which " +
                    "means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

    // Add the object in filePath on my computer to the bucketName bucket on S3, using the key keyName for the object
    // Catch all exceptions, and print error to stdout (System.out)
    public static void addObjectToBucket(AmazonS3 s3Client, String bucketName, String keyName, String filePath) {
   
        File f = new File(filePath);
        if (!f.exists()) {
            System.out.println("file does not exist " + filePath);
            return;
            
        }
        
        keyName = User.getId() +"/"+f.getName();
        System.out.println(keyName);
        //keyName = "d";
        System.out.println("adding object: " + keyName + "from" + filePath);
        TransferManager xfer_mgr = new TransferManager(s3Client);
        
        try {
            Upload xfer = xfer_mgr.upload(bucketName, keyName, f);
            //xfer.isDone()
            //  or block with Transfer.waitForCompletion()
            xfer.waitForCompletion();
           
            System.out.println("completed upload!");
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            xfer_mgr.shutdownNow(false);
        }
    }
    // Lists only objects in the bucketName bucket from that user Id
    public static void viewObjectsInBucket(AmazonS3 s3Client, String bucketName,String id) {
       //final AmazonS3 s3 = new AmazonS3Client()
        String idString = id+"/";
        
        try {

            ObjectListing object_listing = s3Client.listObjects(bucketName,idString);
            while (true) {
                for (Iterator<?> iterator =
                    object_listing.getObjectSummaries().iterator();
                    iterator.hasNext();) {
                    S3ObjectSummary summary = (S3ObjectSummary)iterator.next();
                    String[] summaryA = summary.getKey().split("/");
                    int len = summaryA.length;
                    String fname = summaryA[len-1];
                    System.out.println(fname+" "+summary.getSize()+" "+ summary.getLastModified());
                    // s3.deleteObject(bucketName, summary.getKey());
                }

                // more object_listing to retrieve?
                if (object_listing.isTruncated()) {
                    object_listing = s3Client.listNextBatchOfObjects(object_listing);
                } else {
                    break;
                }
            };
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        
       
    }

    // Delete all objects and versions in the bucketName bucket, and then
    // delete the bucket itself.
    // Catch all exceptions, and print error to stdout (System.out)
    public static void deleteBucket(AmazonS3 s3Client, String bucketName) {
        System.out.println("starting deleting...");

        deleteObjectsInBucket(s3Client,bucketName);

        try {
            s3Client.deleteBucket(bucketName);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
    }
    //Downloads object from s3, looks in folder of user by id
    public static void getObjectInBucket(String id, AmazonS3 s3Client,String bucketName,String keyName) {
         
        try {
            
            String file = keyName;
            keyName = id+"/"+keyName;
            System.out.println(keyName);
            //S3Object objectDetailsOnly = s3Client.getObjectDetails(bucketName, keyName);
            boolean objectExist = s3Client.doesObjectExist(bucketName,keyName);
            if (objectExist == false) {
                MessageHandler.objectError(1);
                return;
            
            }
            
            S3Object object = s3Client.getObject(bucketName,keyName);
            
            try {
                InputStream reader = new BufferedInputStream(object.getObjectContent());
                File f = new File(keyName);      
                OutputStream writer;
                writer = new BufferedOutputStream(new FileOutputStream(file));
                int read = -1;
                while ( ( read = reader.read() ) != -1 ) {
                    writer.write(read);
                    }
                    writer.flush();
                    writer.close();
                    reader.close();
                System.out.println("Download complete");
            }
            
            catch (IOException ex) {
                System.out.println(ex.getStackTrace());
            }
            
          
        } catch (  AmazonServiceException ex) {
            System.err.println(ex.getErrorMessage());
            Logger.getLogger(S3Service.class.getName()).log(Level.SEVERE, null,ex);
            System.exit(1);
        }  
    }

    public static void deleteObjectsInBucket(AmazonS3 s3Client, String bucketName) {
        //final AmazonS3 s3 = new AmazonS3Client();
        try {

            ObjectListing object_listing = s3Client.listObjects(bucketName);
            while (true) {
                for (Iterator<?> iterator =
                     object_listing.getObjectSummaries().iterator();
                     iterator.hasNext();) {
                    S3ObjectSummary summary = (S3ObjectSummary)iterator.next();
                    System.out.println("deleting object: "+summary.getKey());
                    s3Client.deleteObject(bucketName, summary.getKey());
                }

                // more object_listing to retrieve?
                if (object_listing.isTruncated()) {
                    object_listing = s3Client.listNextBatchOfObjects(object_listing);
                } else {
                    break;
                }
            };

            System.out.println(" - removing versions from bucket: " + bucketName);
            VersionListing version_listing = s3Client.listVersions(
                    new ListVersionsRequest().withBucketName(bucketName));
            while (true) {
                for (Iterator<?> iterator =
                     version_listing.getVersionSummaries().iterator();
                     iterator.hasNext();) {
                    S3VersionSummary vs = (S3VersionSummary)iterator.next();
                    s3Client.deleteVersion(

                            bucketName, vs.getKey(), vs.getVersionId());
                }

                if (version_listing.isTruncated()) {
                    version_listing = s3Client.listNextBatchOfVersions(
                            version_listing);
                } else {
                    break;
                }
            }

            System.out.println(" OK, bucket "+bucketName+" ready to delete!");

        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        System.out.println("Done!");
    }
}





