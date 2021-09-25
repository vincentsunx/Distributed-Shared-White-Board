import java.awt.*;

public class PaintSource {
    private String username;
    private String drawType;
    private Color drawColor = Color.BLACK;
    private String text = "";
    private Point sP;
    private Point eP;

    public PaintSource () {
        drawType = "LINE";
        sP = new Point(0, 0);
        eP = new Point(0, 0);

    }
    /* constructor for PaintSource Class */
    public PaintSource (String username, String drawType, Point sP, Point eP, Color drawColor, String text) {
        this.username = username;
        this.drawType = drawType;
        this.drawColor = drawColor;
        this.sP = sP;
        this.eP = eP;
        this.text = text;
    }

    /* read string and split it to specific data*/
    public PaintSource(String infoStr) {
        String info[] = infoStr.split(" ");
        this.username = info[0];
        this.drawType = info[1];
        this.drawColor = new Color(Integer.parseInt(info[2]));
        this.sP = new Point(Integer.parseInt(info[3].split("\\.")[0]), Integer.parseInt(info[3].split("\\.")[1]));
        this.eP = new Point(Integer.parseInt(info[4].split("\\.")[0]), Integer.parseInt(info[4].split("\\.")[1]));
        /* check text available or not */
        if( info.length > 5){
            this.text = info[5];
        }
        else{
            this.text = "";
        }
    }

    public String toString() {
        return username + " " + drawType + " " + drawColor.getRGB() + " " +
                sP.x + "." + sP.y + ' ' + eP.x + "." + eP.y + ' ' + text;
    }


    public String getUsername() {
        return username;
    }

    public String getDrawType() {
        return drawType;
    }

    public Color getDrawColor() {
        return drawColor;
    }

    public String getText() {
        return text;
    }

    public Point getsP() {
        return sP;
    }

    public Point geteP() {
        return eP;
    }

    public void seteP(Point eP) {
        this.eP = eP;
    }
}
