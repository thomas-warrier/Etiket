package exportkit;


import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;

public class TicketReciever {
    private String title;
    private String description;
    private Date date;
    private ArrayList<String> imageArray;
    private String ticketId;
    private boolean favorite;
    public TicketReciever(){}

    public TicketReciever(String title, String description, Timestamp date,boolean favorite, String ticketId) {
        this.date = date.toDate();
        this.title = title;
        this.description = description;
        this.favorite=favorite;
        this.ticketId=ticketId;
    }
    public ArrayList<String> getImageUrlList() {
        return imageArray;
    }

    public Date getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageArray(ArrayList<String> imageArray){
        this.imageArray=imageArray;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getTicketId() {
        return ticketId;
    }
}
