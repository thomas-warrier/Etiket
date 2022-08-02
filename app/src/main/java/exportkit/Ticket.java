package exportkit;

import android.media.Image;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.sql.Date;


/**
 *
 * @author twarr
 */
public class Ticket {
    private Date date;
    private String titre;
    private String description;
    private String fileId;
    private boolean favoris;
    private StorageReference fileReference;

    public Ticket(){}

    public Ticket(Date date, String titre, String description, String ticketID, StorageReference fileReference) {
        this.date = date;
        this.titre = titre;
        this.description = description;
        this.fileId = ticketID;
        this.favoris = false;
        this.fileReference = fileReference;

    }

    public Date getDate() {
        return date;
    }

    public String getTitre() {
        return titre;
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

    public boolean isFavoris() {
        return favoris;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFavoris(boolean favoris) {
        this.favoris = favoris;
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
