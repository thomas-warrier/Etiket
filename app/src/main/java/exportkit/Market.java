package exportkit;



import java.io.Serializable;
import java.util.Date;

public class Market implements Serializable {
    private String marketName;
    private String marketLogo;
    private Date dateOfLastTicket; //need to be the same name in the marketAdapter class
    private String email;
    private boolean favorite;
    private String marketId;

    public Market() {
    } //empty constructor for firebase

    public Market(String marketName,String marketLogo,Date dateOfLastTicket,String email,boolean favorite,String marketId) {
        this.marketName = marketName;
        this.marketLogo = marketLogo;
        this.dateOfLastTicket =dateOfLastTicket;
        this.email=email;
        this.favorite = favorite;
        this.marketId=marketId;
    }



    public Date getDateOfLastTicket() {
        return dateOfLastTicket;
    }

    public String getMarketLogo() {
        return marketLogo;
    }

    public String getEmail() {
        return email;
    }


    public boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getMarketName() {
        return marketName;
    }

    public String getMarketId() {
        return marketId;
    }
}
