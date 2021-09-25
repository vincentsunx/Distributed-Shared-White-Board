import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PaintBoard extends JPanel implements MouseListener, MouseMotionListener {
    private Socket socket;
    private User user;
    private DataInputStream in;
    private DataOutputStream out;
    private String drawType;
    private Color drawColor;
    private String text;
    private Graphics g;
    private PaintSourceList sourceList;


    /* constructor for PaintBoard class */
    public PaintBoard (Socket socket, User user) throws IOException {
        this.socket = socket;
        this.user = user;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        drawType = "LINE";
        drawColor = Color.GRAY;
        sourceList = new PaintSourceList();
        addMouseListener(this);
        addMouseMotionListener(this);
        setVisible(true);

    }


    /* overwrite mouseClicked store start point coordinates for drawing text*/
    @Override
    public void mouseClicked(MouseEvent e) {
        if (drawType.equals("TEXT")) {
            int sPX = e.getX();
            int sPY = e.getY();
            /* store data to sourceList */
            PaintSource data = new PaintSource(user.getName(), drawType, new Point(sPX, sPY), new Point(sPX, sPY)
                    , drawColor, text);
            sourceList.addData(data);

            if (drawType.equals("TEXT")) {
                data.seteP(new Point(sPX, sPY));
                repaint();
            }
            sendDrawingMessage("DRAW:" + data.toString());
        }
    }

    /* overwrite mousePressed stores start point coordinates for non-text*/
    @Override
    public void mousePressed(MouseEvent e) {
        if (!drawType.equals("TEXT")) {
            int spX = e.getX();
            int sPY = e.getY();

            PaintSource data = new PaintSource(user.getName(), drawType, new Point(spX, sPY), new Point(spX, sPY)
                    , drawColor, "");
            sourceList.addData(data);
        }
    }

    /* get end point coordinates */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (!drawType.equals("TEXT")) {
            /* find the source in the list with the same username */
            PaintSource source = sourceList.sourceSearch(user.getName());
            /* set end point to the pictures */
            if (source != null) {
                source.seteP(e.getPoint());
                sendDrawingMessage("DRAW:" + source.toString());
            }

            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /* keep drawing the pictures */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (!drawType.equals("TEXT")) {
            PaintSource source = sourceList.sourceSearch(user.getName());
            /* set end point to the pictures */
            if (source != null) {
                source.seteP(e.getPoint());
            }
//            repaint();
            this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /* paint graphics */
    public synchronized void paint(Graphics g) {
        /* draw all source to the paintboard */
        for (int i = 0, len = sourceList.painList.size(); i < len; i++) {
            PaintSource source = sourceList.painList.get(i);

            Draw.drawGraphics(source.getDrawType(), source.getsP()
                    , source.geteP(), source.getDrawColor(), source.getText()
                    , (Graphics2D) g);
        }
    }

    public void clear() {
        sendDrawingMessage("CLEAR:");
    }

    public void reset(PaintSourceList sourceList) {
        sendDrawingMessage("INIT_DATA:" + sourceList.toString());
    }

    /* send drawing action messages to server */
    public void sendDrawingMessage(String sourceInfo) {
        try {
            out.writeUTF(sourceInfo);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PaintSourceList getSourceList() {
        return sourceList;
    }

    public void setSourceList(PaintSourceList sourceList) {
        this.sourceList = sourceList;
    }


    public void setDrawColor(Color color){
        drawColor = color;
    }

    public void setDrawType(String type){
        drawType = type;
    }

    public void setText(String text){
        this.text = text;
    }


}
