package exportkit;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.mail.Address;
import javax.mail.Flags;
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
     */

    private String saveDirectory;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //grab the authentification instance
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance(); //the database
    private DatabaseReference myDBRef = mFirebaseDatabase.getReference(); //the reference (firestore)
    FirebaseUser user = mAuth.getCurrentUser(); //get the user
    private String userID = user.getUid(); //ID of user
    private StorageReference mStorageReference = FirebaseStorage.getInstance().getReference("File/" + userID); //the storage reference
    private boolean success = false;
    private FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();
    private static Message message;

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
                ArrayList<File> listFile = new ArrayList<>();
                message = arrayMessages[i];
                Address[] fromAddress = message.getFrom();

                String from = fromAddress[0].toString();
                String subject = message.getSubject();
                Date sentDate = message.getSentDate();

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
                            File file = File.createTempFile("temp",null); //create an empty temporary file
                            listFile.add(file);
                            part.saveFile(file);//save the content in the temp file created before
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
                /*
                System.out.println("Message #" + (i + 1) + ":");
                System.out.println("from :" + from);
                String result = from.substring(from.indexOf("<") + 1, from.indexOf("@"));
                System.out.println("\t Resultat from: " + result);

                System.out.println("\t Sent Date: " + sentDate);
                System.out.println("\t Attachments: " + attachFiles);
                 */

                getMarketFromFirebase(from, new OnGetMarketDocumentReference() {
                    @Override
                    public void getMarketReference(DocumentReference docRef) {
                        String UID = UUID.randomUUID().toString();
                        Ticket ticket = new Ticket(sentDate,null,subject,listFile); //création du ticket
                        createTicket(docRef,UID,ticket);
                    }
                });

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


    private boolean pushFileToFirebase(File file) {
        Uri mFileUri = Uri.fromFile(file);//I create a URI for my image;
        StorageReference fileReference = mStorageReference.child("Users").child(userID);
        fileReference.putFile(mFileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        success = true;
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                success = false;
            }
        });
        return success;


    }

    private void getPublicMarketFromFirebase(String senderEmail, OnGetMarketListener marketListener) {
        DocumentReference docRef = mFireStore.collection("Market").document(senderEmail);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Market market = documentSnapshot.toObject(Market.class);
                marketListener.marketReciever(market);
            }
        });
    }

    private DocumentReference createMarket(Market market) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("marketLogo", market.getMarketLogo());
        docData.put("e-mail", market.getEmail());
        docData.put("dateOfLastTicket",null);
        docData.put("marketName",market.getName());
        docData.put("favorite",false);
        String UID = UUID.randomUUID().toString();
        mFireStore.collection("User").document(userID).collection("Market").document(UID).set(docData);
        mFireStore.collection("User").document(userID).collection("Market").document(UID).collection("Ticket");
        return mFireStore.collection("User").document(userID).collection("Market").document(UID);
    }

    private void createTicket(DocumentReference marketRef, String ticketID,Ticket ticket) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("title", ticket.getTitle());
        docData.put("description", ticket.getDescription());
        docData.put("date",ticket.getDate());
        docData.put("favorite",false);
        int count = 0;
        for (File file : ticket.getImageList()){
                docData.put("ImageLink"+count,pushFileToFirebase(file));
                count++;
        }
        marketRef.update("dateOfLastTicket",ticket.getDate()); //to update the date of the last ticket in the market

        mFireStore.collection("User").document(userID).collection("Market").document(marketRef.getId()).collection("Ticket").document(ticketID)
                .set(docData).addOnSuccessListener(new OnSuccessListener<Void>() { //if success we can delete the email
                    @Override
                    public void onSuccess(Void unused) {
                        try {
                            message.setFlag(Flags.Flag.DELETED, true);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getMarketFromFirebase(String emailSender,OnGetMarketDocumentReference marketListener){
        mFireStore.collection("User").document(userID).collection("Market").whereArrayContains("e-mail",emailSender).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) { //if there is no match,I create the market for the user
                        getPublicMarketFromFirebase(emailSender, new OnGetMarketListener() {
                            @Override
                            public void marketReciever(Market market) {
                                marketListener.getMarketReference(createMarket(market));
                            }

                        });
                    }
                    else{
                        QuerySnapshot document = task.getResult(); //grab the query wich contain the document
                        for (DocumentSnapshot documentSnapshot : task.getResult()){ //grab the document wich contain the right e-mail
                            marketListener.getMarketReference(documentSnapshot.getReference());
                        }
                    }


                } else {
                    Log.d("MailReception,pushMarketInto", "Error getting documents: ", task.getException());
                }
            }
        });

    }


}
