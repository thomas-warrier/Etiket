package exportkit;


import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;

public class TicketReciever {
    private String title;
    private String description;
    private Date date;
    private ArrayList<String> imageUrlList;
    private String ticketId;
    private boolean favorite;


    public TicketReciever(){}

    public TicketReciever(String title, String description, Timestamp date, ArrayList<String> imageList,boolean favorite, String ticketId) {
        this.date = date.toDate();
        this.title = title;
        this.description = description;
        this.imageUrlList = imageList;
        this.favorite=favorite;
        this.ticketId=ticketId;
    }
    public ArrayList<String> getImageList() {
        return imageUrlList;
    }

    public String getImage(int pos){return imageUrlList.get(pos);}

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

    public ArrayList<String> getImageUrlList() {
        return imageUrlList;
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
