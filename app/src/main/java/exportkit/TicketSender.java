package exportkit;

import android.media.Image;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.ArrayList;


/**
 *
 * @author twarr
 */
public class TicketSender {
    private Date date;
    private String title;
    private String description;
    private ArrayList<File> imageList;

    public TicketSender(){}

    public TicketSender(Date date, String title, String description, ArrayList<File> imageList) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.imageList = imageList;
    }
    public ArrayList<File> getImageList() {
        return imageList;
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


}
