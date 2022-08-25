package exportkit;

import android.media.Image;

import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;

public class TicketReciever {
    private String title;
    private String description;
    private Date date;
    private ArrayList<String> imageUrlList;
    private int nbImage;
    private String ticketId;


    public TicketReciever(){}

    public TicketReciever(String title, String description,Date date,ArrayList<String> imageList,int nbImage,String ticketId) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.imageUrlList = imageList;
        this.nbImage=nbImage;
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

    public int getNbImage() {
        return nbImage;
    }

    public String getTicketId() {
        return ticketId;
    }
}
