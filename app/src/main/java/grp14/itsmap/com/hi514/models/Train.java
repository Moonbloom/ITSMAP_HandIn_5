package grp14.itsmap.com.hi514.models;

import java.io.Serializable;

public class Train implements Serializable {

    //region Variables
    private String name;
    private int wid;
    private double x;
    private double y;
    //endregion

    //region Constructor
    public Train() {

    }

    public Train(String name, int wid, double x, double y) {
        this.name = name;
        this.wid = wid;
        this.x = x;
        this.y = y;
    }
    //endregion

    //region Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //endregion

    //region Wid
    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }
    //endregion

    //region X
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }
    //endregion

    //region Y
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    //endregion
}