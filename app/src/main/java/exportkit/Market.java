package exportkit;



import java.io.Serializable;
import java.util.Date;

public class Market implements Serializable {
    private String name;
    private String marketLogo;
    private Date date; //need to be the same name in the marketAdapter class
    private String email;
    private boolean favorite;
    private String marketId;

    public Market() {
    } //empty constructor for firebase

    public Market(String name,String marketLogo,Date date,String email,boolean favorite,String marketId) {
        this.name = name;
        this.marketLogo = marketLogo;
        this.date=date;
        this.email=email;
        this.favorite = favorite;
        this.marketId=marketId;
    }

    public Date getDate() {
        return date;
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

    public String getName() {
        return name;
    }

    public String getMarketId() {
        return marketId;
    }
}
