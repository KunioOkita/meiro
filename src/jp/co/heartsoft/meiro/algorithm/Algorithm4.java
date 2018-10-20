package jp.co.heartsoft.meiro.algorithm;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import jp.co.heartsoft.meiro.algorithm.BranchPoint.Direction;

public class Algorithm4 {
    /** 迷路情報 */
    private char[][] meiro;

    /** 開始位置 */
    public static final int[] START_POINT = { 0, 0 };

    /** 障害物を示す文字 */
    private static final char BARRIER = '*';

    /** 正解ルートを示す文字 */
    private static final char ANSWER_WAY = '+';

    /** ルートを示す文字 */
    private static final char INITIAL_WAY = ' ';

    /** 終了位置 */
    private int[] endPoint = new int[2];

    /** 迷路の分岐点を入れるスタック */
    private Deque<BranchPoint> meiroBranch = new ArrayDeque<BranchPoint>();

    /** 行数 */
    private int rowLength;
    /** 列数 */
    private int colLength;

    public Algorithm4(char[][] meiro) {
        this.meiro = meiro;
        this.rowLength = meiro.length;
        this.colLength = meiro[0].length;
        this.endPoint[0] = rowLength - 1;
        this.endPoint[1] = colLength - 1;
    }

    /**
     * 迷路攻略実行
     * @return
     */
    public char[][] solve(Direction direction, int[] point) {
        System.out.println(
                String.format("#solve Direction = %s, Point = %s, meiroBranch.size = %s", direction,
                        Arrays.toString(point),
                        meiroBranch.size()));
        int[] resultPoint = advance(direction, point);
        // ゴール！
        if (resultPoint[0] == endPoint[0] && resultPoint[1] == endPoint[1]) {
            makeWay(resultPoint);
        } else {
            BranchPoint preBranchPoint = meiroBranch.pop();
            // 解無し
            if (preBranchPoint == null) {
                System.out.println("解無し");
                return meiro;
            }
            backToBranchPoint(direction, resultPoint, preBranchPoint);
            solve(preBranchPoint.getDirection(), preBranchPoint.getPoint());
        }

        System.out.println(
                String.format("#solve End meiroBranch.size = %s", meiroBranch.size()));
        return this.meiro;
    }

    private int[] advance(Direction d, int[] point) {
        System.out.println(
                String.format("#advance Direction = %s, Point = %s", d, Arrays.toString(point)));
        boolean flag = true;
        int[] currentPoint = point;
        while (flag) {
            checkBranchPoint(d, currentPoint);
            int[] nextPoint = getNextPoint(d, currentPoint);
            // すでに通った道の場合は、戻っているので削除するただし、開始点は削除しない。
            if (this.meiro[currentPoint[0]][currentPoint[1]] == ANSWER_WAY && (currentPoint[0] != START_POINT[0]
                    || currentPoint[1] != START_POINT[1])) {
                removeBranchPoints(currentPoint);
                removeWay(currentPoint);
            } else {
                makeWay(currentPoint);
            }
            if (nextPoint == currentPoint) {
                break;
            }
            currentPoint = nextPoint;
        }

        return currentPoint;
    }

    /**
     * 一つ前の分岐点に戻る
     * @param point
     * @param preBranchPoint
     */
    private void backToBranchPoint(Direction direction, int[] point, BranchPoint preBranchPoint) {
        System.out.println(
                String.format("#backToBranchPoint Direction = %s, Point = %s, prePoint = %s, preDirection = %s",
                        direction,
                        Arrays.toString(point),
                        Arrays.toString(preBranchPoint.getPoint()), preBranchPoint.getDirection()));
        int[] pbPoint = preBranchPoint.getPoint();
        removeWay(point);
        if (point[0] == pbPoint[0] && point[1] == pbPoint[1]) {
            return;
        }

        switch (direction) {
        case Right:
            int[] prePoint = getPrePoint(BranchPoint.Direction.Left, point);
            if (prePoint[1] != preBranchPoint.getPoint()[1]) {
                backToBranchPoint(direction, prePoint, preBranchPoint);
            }
            break;
        case Left:
            prePoint = getPrePoint(BranchPoint.Direction.Right, point);
            if (prePoint[1] != preBranchPoint.getPoint()[1]) {
                backToBranchPoint(direction, prePoint, preBranchPoint);
            }
            break;
        case Down:
            prePoint = getPrePoint(BranchPoint.Direction.Up, point);
            if (prePoint[0] != preBranchPoint.getPoint()[0]) {
                backToBranchPoint(direction, prePoint, preBranchPoint);
            }
            break;
        case Up:
            prePoint = getPrePoint(BranchPoint.Direction.Down, point);
            if (prePoint[0] != preBranchPoint.getPoint()[0]) {
                backToBranchPoint(direction, prePoint, preBranchPoint);
            }
            break;
        default:
            break;
        }

    }

    private void makeWay(int[] point) {
        System.out.println(String.format("#makeWay Point = %s", Arrays.toString(point)));
        meiro[point[0]][point[1]] = ANSWER_WAY;
    }

    private void removeWay(int[] point) {
        System.out.println(String.format("#removeWay Point = %s", Arrays.toString(point)));
        meiro[point[0]][point[1]] = INITIAL_WAY;
    }

    private void removeBranchPoints(int[] targetPoint) {
        while (true) {
            BranchPoint bPoint = meiroBranch.pop();
            if (bPoint == null) {
                break;
            }
            // 位置が違う場合。
            if (targetPoint[0] != bPoint.getPoint()[0] || targetPoint[1] != bPoint.getPoint()[1]) {
                meiroBranch.push(bPoint);
                break;
            }
        }
    }

    /**
     * 行き止まり確認
     * 1. 障害物に当たる
     * 2. 迷路の壁に当たる。
     * @param d
     * @param point
     * @return
     */
    private boolean isDeadEnd(Direction d, int[] point) {
        int row = point[0];
        int col = point[1];

        // 迷路の上下の壁に激突
        if (row < 0 || row >= rowLength) {
            return true;
        }

        // 迷路の左右の壁に激突
        if (col < 0 || col >= colLength) {
            return true;
        }

        // 障害物に激突
        if (this.meiro[row][col] == BARRIER) {
            return true;
        }

        return false;
    }

    /**
     * 次の進行路を取得する<br/>
     * 進めない場合は、同じポイントが返却される。
     * @param d
     * @param point
     * @return
     */
    private int[] getNextPoint(Direction d, int[] point) {
        System.out.println(
                String.format("#getNextPoint Direction = %s, Point = %s", d, Arrays.toString(point)));
        int[] nextP = new int[2];
        switch (d) {
        case Right:
            nextP[0] = point[0];
            nextP[1] = point[1] + 1;
            if (isDeadEnd(d, nextP)) {
                return point;
            }
            break;
        case Left:
            nextP[0] = point[0];
            nextP[1] = point[1] - 1;
            if (isDeadEnd(d, nextP)) {
                return point;
            }
            break;
        case Down:
            nextP[0] = point[0] + 1;
            nextP[1] = point[1];
            if (isDeadEnd(d, nextP)) {
                return point;
            }
            break;
        case Up:
            nextP[0] = point[0] - 1;
            nextP[1] = point[1];
            if (isDeadEnd(d, nextP)) {
                return point;
            }
            break;
        default:
            break;
        }

        return nextP;
    }

    /**
     * 次の進行路を取得する<br/>
     * 進めない場合は、同じポイントが返却される。
     * @param d
     * @param point
     * @return
     */
    private int[] getPrePoint(Direction d, int[] point) {
        System.out.println(
                String.format("#getPrePoint Direction = %s, Point = %s", d, Arrays.toString(point)));
        int[] preP = new int[2];
        switch (d) {
        case Right:
            preP[0] = point[0];
            preP[1] = point[1] + 1;
            break;
        case Left:
            preP[0] = point[0];
            preP[1] = point[1] - 1;
            break;
        case Down:
            preP[0] = point[0] + 1;
            preP[1] = point[1];
            break;
        case Up:
            preP[0] = point[0] - 1;
            preP[1] = point[1];
            break;
        default:
            break;
        }

        return preP;
    }

    /**
     * 分岐点かどうかをチェックする。分岐点の場合は分岐点ポイントにスタックしておく<br/>
     * 現在進んでいる方向以外に進める方向があるかをチェックする。<br/>
     * 左右に進んでいる場合は、上下をチェック。上下に進んでいる場合は左右をチェック<br/>
     */
    private void checkBranchPoint(Direction direction, int[] point) {
        System.out.println(
                String.format("#checkBranchPoint Direction = %s, Point = %s", direction, Arrays.toString(point)));
        int[] next = new int[2];
        switch (direction) {
        case Right:
        case Left:
            next = getNextPoint(BranchPoint.Direction.Up, point);
            if (next != point) {
                this.meiroBranch.push(new BranchPoint(BranchPoint.Direction.Up, point));
            }
            next = getNextPoint(BranchPoint.Direction.Down, point);
            if (next != point) {
                this.meiroBranch.push(new BranchPoint(BranchPoint.Direction.Down, point));
            }
            break;
        case Down:
        case Up:
            next = getNextPoint(BranchPoint.Direction.Left, point);
            if (next != point) {
                this.meiroBranch.push(new BranchPoint(BranchPoint.Direction.Left, point));
            }
            next = getNextPoint(BranchPoint.Direction.Right, point);
            if (next != point) {
                this.meiroBranch.push(new BranchPoint(BranchPoint.Direction.Right, point));
            }
            break;
        }
    }
}
