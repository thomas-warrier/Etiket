package exportkit;

import android.media.Image;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

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
        private String saveDirectory;

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

            MailTest receiver = new MailTest();
            receiver.setSaveDirectory(saveDirectory);
            receiver.downloadEmailAttachments(host, port, userName, password);

        }

        private boolean pushToFirebase(String marketName, Date sendDate, File[] ticketImage){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String userId = user.getUid();
            String ticketImageID = UUID.randomUUID().toString();
            Ticket ticket = new Ticket(sendDate,null,null);
            myRef.child(users).child(userID).child(marketName).setValue()
        }
    }
}
