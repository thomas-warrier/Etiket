package exportkit;


import android.graphics.Bitmap;

import java.util.Date;

public class Market {
    private Date date; //need to be the same name in the marketAdapter class
    private Bitmap marketLogo;
    private boolean favorite;

    public Market(){} //empty constructor for firebase
    public Market(Date date, Bitmap marketLogo) {
        this.date = date;
        this.marketLogo = marketLogo;
        this.favorite=false;
    }

    public Date getDate() {
        return date;
    }

    public Bitmap getMarketLogo() {
        return marketLogo;
    }

    public boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
