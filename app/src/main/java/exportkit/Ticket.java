package exportkit;

import android.media.Image;
import com.google.firebase.storage.StorageReference;

import java.lang.ref.Reference;
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
    private String imageId;
    private boolean favoris;
    private StorageReference id;

    public Ticket(Date date, String titre, String description, String image, StorageReference id) {
        this.date = date;
        this.titre = titre;
        this.description = description;
        this.imageId = image;
        this.favoris = false;
        this.id = id;

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

    public Image getImageId() {
        /*faire un systéme qui va chercher l'image qui a l'id*/
        return null;
    }

    public String getId() {
        return id;
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
