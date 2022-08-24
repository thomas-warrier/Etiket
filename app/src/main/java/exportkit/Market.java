package exportkit;



import java.util.Date;

public class Market {
    private String name;
    private Date date; //need to be the same name in the marketAdapter class
    private String marketLogo;
    private boolean favorite;

    public Market() {
    } //empty constructor for firebase

    public Market(String name,String marketLogo,Date date,boolean favorite) {
        this.name = name;
        this.marketLogo = marketLogo;
        this.date=date;
        this.favorite = favorite;
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

}
