package jp.co.heartsoft.meiro.algorithm;

public class BranchPoint {

    public static enum Direction {
        Right, Left, Down, Up
    };

    /** 分岐位置 */
    private int[] point = new int[2];

    /** 進行方向 */
    private Direction direction;

    public BranchPoint(Direction d, int[] p) {
        this.point = p;
        this.direction = d;
    }

    public int[] getPoint() {
        return point;
    }

    public void setPoint(int[] point) {
        this.point = point;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

}
