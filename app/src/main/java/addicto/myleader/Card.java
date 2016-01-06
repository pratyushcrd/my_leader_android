package addicto.myleader;

public class Card {
    private String line1;
    private String img_dp;
    private String img;

    public Card(String line1, String line2, String imgx) {
        this.line1 = line1;
        this.img_dp = line2;
        img = imgx;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return img_dp;
    }

    public String getImage() {
        return img;
    }
}
