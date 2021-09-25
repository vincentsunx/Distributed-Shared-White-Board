import java.util.ArrayList;

public class PaintSourceList {
    public ArrayList<PaintSource> painList = null;

    /* constructor for PaintSourceList */
    public PaintSourceList() {
        painList = new ArrayList<PaintSource>();
    }

    /* split the whole string by ";" to a single paintsource. And then store it to the paintList*/
    public PaintSourceList(String info) {
        painList = new ArrayList<PaintSource>();

        if (!info.equals("")) {
            String[] strList = info.split(";");
            for (String s : strList) {
                painList.add(new PaintSource(s));
            }
        }
    }

    public synchronized void addData(PaintSource source) {
        painList.add(source);
    }

    /* reset source list*/
    public void resetSource(String info) {
        painList = new ArrayList<PaintSource>();

        if (!info.equals("")) {
            String[] strList = info.split(";");

            for (String s : strList) {
                painList.add(new PaintSource(s));
            }
        }
    }

    /* search paintsource from backwards*/
    public PaintSource sourceSearch(String username) {
        for (int i = painList.size() - 1; i >=0; i--) {
            if (username.equals(painList.get(i).getUsername())) {
//                System.out.println(painList.get(i).getUsername());
                return painList.get(i);
            }
        }

        return null;
    }

    public String toString() {
        ArrayList<String> strList = new ArrayList<String>();

        for (PaintSource paintSource : painList) {
            strList.add(paintSource.toString());
        }

        return String.join(";", strList);
    }
}
