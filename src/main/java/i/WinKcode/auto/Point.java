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
    //曼哈顿距离
    private float gcost;
    //欧拉值
    private double fcost;
    //节点类型 1走 2跳 3跑 4斜向走位 5放置 6破坏 7破坏&放置
    private int type;

    public Point(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.gcost = 0;
        this.fcost = 0.0f;
        this.type = 1;
    }

    public Point(int x, int y, int z,Point parent, float gcost, double fcost) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.parent = parent;
        this.gcost = gcost;
        this.fcost = fcost;
        this.type = 1;
    }

    public Point(int x, int y, int z,Point parent, float gcost, double fcost,int type) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.parent = parent;
        this.gcost = gcost;
        this.fcost = fcost;
        this.type = type;
    }

    public void setType(int type) { this.type = type; }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getType() { return type; }

    public float getGcost() {
        return gcost;
    }

    public double getFcost() {
        return fcost;
    }

    public void setGcost(float gCost) {
        this.gcost = gCost;
    }

    public void setFcost(float fCost) {
        this.fcost = fCost;
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
