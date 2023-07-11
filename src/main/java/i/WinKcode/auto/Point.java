package i.WinKcode.auto;

/**
 * @author sjaiaa
 * @date 2023/4/11 21:01
 * @discription
 */
public class Point {
    private int x;
    private int y;
    private int z;
    public Point parent;
    private int gcost;
    private double fcost;

    public Point(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.gcost = 0;
        this.fcost = 0.0f;
    }

    public Point(int x, int y, int z,Point parent, int gcost, double fcost) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.parent = parent;
        this.gcost = gcost;
        this.fcost = fcost;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getGcost() {
        return gcost;
    }

    public double getFcost() {
        return fcost;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", gcost=" + gcost +
                ", fcost=" + fcost +
                '}';
    }

    @Override
    public boolean equals(Object o){
        Point p = (Point) o;
        return p.getX() == this.x && p.getY() == this.y && p.getZ() == this.z;
    }
}
