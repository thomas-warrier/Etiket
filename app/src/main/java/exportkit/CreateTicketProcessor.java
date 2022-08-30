package exportkit;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class CreateTicketProcessor {
    private MailReception mailReception;
    private  Map<String, Object> docData;
    private TicketSender ticket;
    private int count;
    private String ticketID;
    private DocumentReference marketRef;
    private static ArrayList<String> imageArray;

    public CreateTicketProcessor(MailReception mailReception, Map<String, Object> docData,TicketSender ticket,String ticketID, DocumentReference marketRef){
        this.mailReception=mailReception;
        this.docData=docData;
        this.ticket=ticket;
        this.ticketID=ticketID;
        this.marketRef=marketRef;
    }

    public void pushFile(){
        if(ticket.getImageList().isEmpty()){
            finalizeTicket();
        }
        imageArray=new ArrayList<>();
        for (File file : ticket.getImageList()){
            mailReception.pushFileToFirebase(file, new OnGetUrlListener() {
                @Override
                public void urlReciever(String url) {
                    imageArray.add(url);
                    count++;
                    if(count==ticket.getImageList().size()){
                        finalizeTicket();
                    }
                }
            });
        }
    }

    private void finalizeTicket() {
        docData.put("imageArray",imageArray);
        docData.put("ticketId", ticketID);
        marketRef.update("dateOfLastTicket", ticket.getDate()); //to update the date of the last ticket in the market

        mailReception.mFireStore.collection("User").document(mailReception.userID).collection("Market").document(marketRef.getId()).collection("Ticket").document(ticketID)
                .set(docData);
    }
}
