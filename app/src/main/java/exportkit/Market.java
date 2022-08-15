package exportkit;


import android.graphics.Bitmap;

import java.util.Date;

public class Market {
    private String name;
    private Date date; //need to be the same name in the marketAdapter class
    private String marketLogo;
    String email;
    private boolean favorite;

    public Market(){} //empty constructor for firebase
    public Market(Date date, String marketLogo,String name,String email) {
        this.name = name;
        this.marketLogo = marketLogo;
        this.favorite=false;
        this.email=email;
    }

    public Date getDate() {
        return date;
    }

    public String getMarketLogo() {
        return marketLogo;
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

    public String getEmail() {
        return email;
    }

    public boolean isFavorite() {
        return favorite;
    }
}
