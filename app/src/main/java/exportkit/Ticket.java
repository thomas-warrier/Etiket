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
public class Ticket {
    private Date date;
    private String title;
    private String description;
    private ArrayList<File> imageList;
    private StorageReference fileReference;

    public Ticket(){}

    public Ticket(Date date, String title, String description,ArrayList<File> imageList) {
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

    public Image getFileId() {
        /*faire un systéme qui va chercher l'image qui a l'id*/
        return null;
    }

    public StorageReference getFileReference() {
        return fileReference;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRecent(Ticket ticket) {
        LocalDateTime localTime = LocalDateTime.now();
        long diff = ChronoUnit.DAYS.between(localTime, (Temporal) ticket.date);
        if (diff <= 3) {
            return true;
        }
        return false;
    }
}
