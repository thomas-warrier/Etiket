package exportkit;

import android.content.ContentResolver;
import android.media.Image;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;

public class MailReception {


    /**
     * This program demonstrates how to download e-mail messages and save
     * attachments into files on disk.
     *
     *
     */
    private final static ArrayList<String> enseigne;

        private String saveDirectory;
        private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //grab the authentification instance
        private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance(); //the database
        private DatabaseReference myDBRef = mFirebaseDatabase.getReference(); //the reference (firestore)
        FirebaseUser user = mAuth.getCurrentUser(); //get the user
        private String userID = user.getUid(); //ID of user
        private StorageReference mStorageReference = FirebaseStorage.getInstance().getReference("File/" + userID); //the storage reference
        private boolean success =false;

        /**
         * Sets the directory where attached files will be stored.
         *
         * @param dir absolute path of the directory
         */
        public void setSaveDirectory(String dir) {
            this.saveDirectory = dir;
        }

        /**
         * Downloads new messages and saves attachments to disk if any.
         *
         * @param host
         * @param port
         * @param userName
         * @param password
         */
        public void downloadEmailAttachments(String host, String port,
                                             String userName, String password) {
            Properties properties = new Properties();

            // server setting
            properties.put("mail.pop3.host", host);
            properties.put("mail.pop3.port", port);

            // SSL setting
            properties.setProperty("mail.pop3.ssl.enable", "true");

            Session session = Session.getDefaultInstance(properties);

            try {
                // connects to the message store
                Store store = session.getStore("pop3");
                store.connect(userName, password);

                // opens the inbox folder
                Folder folderInbox = store.getFolder("INBOX");
                folderInbox.open(Folder.READ_ONLY);
                // fetches new messages from server
                Message[] arrayMessages = folderInbox.getMessages();

                for (int i = 0; i < arrayMessages.length; i++) {
                    Message message = arrayMessages[i];
                    Address[] fromAddress = message.getFrom();
                    String from = fromAddress[0].toString();

                    String subject = message.getSubject();
                    String sentDate = message.getSentDate().toString();

                    String contentType = message.getContentType();
                    String messageContent = "";

                    // store attachment file name, separated by comma
                    String attachFiles = "";

                    if (contentType.contains("multipart")) {
                        // content may contain attachments
                        Multipart multiPart = (Multipart) message.getContent();
                        int numberOfParts = multiPart.getCount();
                        for (int partCount = 0; partCount < numberOfParts; partCount++) {
                            MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                                // this part is attachment
                                String fileName = part.getFileName();
                                attachFiles += fileName + ", ";
                                part.saveFile(saveDirectory + File.separator + fileName);
                            } else {
                                // this part may be the message content
                                messageContent = part.getContent().toString();
                            }
                        }

                        if (attachFiles.length() > 1) {
                            attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
                        }
                    } else if (contentType.contains("text/plain")
                            || contentType.contains("text/html")) {
                        Object content = message.getContent();
                        if (content != null) {
                            messageContent = content.toString();
                        }
                    }

                    // print out details of each message
                    System.out.println("Message #" + (i + 1) + ":");
                    System.out.println("from :" + from);
                    String result = from.substring(from.indexOf("<") + 1, from.indexOf("@"));
                    System.out.println("\t Resultat from: " + result);

                    System.out.println("\t Sent Date: " + sentDate);
                    System.out.println("\t Attachments: " + attachFiles);
                }

                // disconnect
                folderInbox.close(false);
                store.close();
            } catch (NoSuchProviderException ex) {
                System.out.println("No provider for pop3.");
                ex.printStackTrace();
            } catch (MessagingException ex) {
                System.out.println("Could not connect to the message store");
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        /**
         * Runs this program with Gmail POP3 server
         */
        public static void main(String[] args) {
            String host = "outlook.office365.com";
            String port = "995";
            String userName = "etiket@outlook.fr";
            String password = "T2o1t1o1";

            String saveDirectory = "C:\\Users\\twarr\\Desktop\\Attachment";

            MailReception receiver = new MailReception();
            receiver.setSaveDirectory(saveDirectory);
            receiver.downloadEmailAttachments(host, port, userName, password);

        }

        private String getMarketName(String mailExpeditor,ArrayList<String> enseigne){ //to get the name of the expeditor
            for (String marketName : enseigne){
                if(mailExpeditor.toLowerCase(Locale.ROOT).contains(marketName)){
                    return marketName;
                }
            }
            return null;
        }

            public String getFileExtension(String filename) {//to get the extension of the file
                return FilenameUtils.getExtension(filename);
            }

        private boolean pushToFirebase(String marketName, Date sendDate, File[] ticketImage,String fileName){
            String ticketID = UUID.randomUUID().toString()+"."+getFileExtension(fileName);
            File file;
            Uri mFileUri = Uri.fromFile(File file);//I create a URI for my image;
            StorageReference fileReference = mStorageReference.child(ticketID);
            Ticket ticket = new Ticket((java.sql.Date) sendDate,null,null,ticketID,fileReference);
            fileReference.putFile(mFileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    success =true;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    success = false;
                }
            });
            myDBRef.child("users").child(userID).child("store").child(marketName).child(ticketID).setValue(ticket).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if(task.isSuccessful()){
                        success = true;
                    }else{
                        success = false;
                    }
                }
            });
            return success;
        }
    }
