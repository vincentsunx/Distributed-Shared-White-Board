import java.awt.*;

public class Draw {
    Point sP;
    Point eP;

    /* constructor for Draw class*/
    public Draw(Point sP, Point eP) {
        this.sP = sP;
        this.eP = eP;
    }

    /* drawing method*/
    public static void drawGraphics(String type, Point sP, Point eP
            , Color drawColor, String text, Graphics2D g) {
        if(type.equals("LINE")){
            drawLine(sP, eP, drawColor, g);
        }
        else if (type.equals("CIRCLE")){
            drawCircle(sP, eP, drawColor, g);
        }
        else if (type.equals("RECT")){
            drawRectangle(sP, eP, drawColor, g);
        }
        else if(type.equals("OVAL")){
            drawOval(sP, eP, drawColor, g);
        }
        else if (type.equals("TEXT")){
            drawText(sP, text, drawColor, g);
        }
    }

    private static void drawLine(Point sP, Point eP
            , Color drawColor, Graphics2D g) {
        g.setColor(drawColor);
        g.drawLine(sP.x, sP.y, eP.x, eP.y);
    }

    private static void drawRectangle(Point sP, Point eP
            , Color drawColor, Graphics2D g) {
        g.setColor(drawColor);

        int width = Math.abs(eP.x - sP.x);
        int height = Math.abs(eP.y - sP.y);
        int x = Math.min(sP.x, eP.x);
        int y = Math.min(sP.y, eP.y);

        g.drawRect(x, y, width, height);
    }

    private static void drawCircle(Point sP, Point eP
            , Color drawColor, Graphics2D g) {
        g.setColor(drawColor);

        int width = Math.abs(eP.x - sP.x);
        int height = Math.abs(eP.y - sP.y);



        if (width < height) {
            int x = Math.min(sP.x, eP.x);
            int y = Math.min(sP.y, eP.y);
            g.drawOval(x, y, width, width);
        }
        else {
            int x = Math.min(sP.x, eP.x);
            int y = Math.min(sP.y, eP.y);
            g.drawOval(x, y, height, height);
        }
    }

    private static void drawText(Point sP, String text
            , Color drawColor, Graphics2D g) {
        g.setColor(drawColor);
        g.drawString(text, sP.x, sP.y);
    }


    private static void drawOval(Point sP, Point eP
            , Color drawColor, Graphics2D g){

        int width = Math.abs(eP.x - sP.x);
        int height = Math.abs(eP.y - sP.y);
        int x = Math.min(sP.x, eP.x);
        int y = Math.min(sP.y, eP.y);
        g.setColor(drawColor);
        g.drawOval(x, y, width, height);

    }
}
