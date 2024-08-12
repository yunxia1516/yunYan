import java.util.ArrayList;

public class Main {
    //游戏局面静态类
    public static class chessSituation {
        public static int[][] mainChessBoard = {
                {39,41,44,46,42,45,43,40,38},
                {0,0,0,0,0,0,0,0,0},
                {0,37,0,0,0,0,0,36,0},
                {35,0,34,0,33,0,32,0,31},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {11,0,12,0,13,0,14,0,15},
                {0,16,0,0,0,0,0,17,0},
                {0,0,0,0,0,0,0,0,0},
                {18,20,23,25,22,26,24,21,19}
        };
    }

    //棋子父类
    public abstract class chessPieceClass {
        //棋子基本信息表
        int x_Coordinate;
        int y_Coordinate;
        int chessPieceNumber;
        int camp; //阵营：红1黑2
        //用于遍历取用的方向表
        int[] actionDirectionPre1 = {1, 2, 3, 4}; //上↑，下↓，左←，右→
        int[] actionDirectionPre2 = {5, 6, 7, 8}; //左上↖，左下↙，右上↗，右下↘
        int[] actionDirectionEs = {11, 12}; //对于马的横纵
        //方向与落点x,y，存储为3元数组
        ArrayList<Integer[]> actionDirectPoint  = new ArrayList<>();
        //该棋子一共可能的下法,五元数组：移动方向，移动距离，目标位置x，y，吃子编号，[评价值]
        ArrayList<Integer[]> actionList = new ArrayList<>();
        
        //构造方法，自动获取棋子位置，输入是棋子编号
        public chessPieceClass(int cPNumber) {
            chessPieceNumber = cPNumber;
            //判断阵营
            if (cPNumber < 30) {
                camp = 1;
            } else {
                camp = 2;
            }
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 9; j++) {
                    if (chessSituation.mainChessBoard[i][j] == chessPieceNumber) {
                        x_Coordinate = i;
                        y_Coordinate = j;
                        break;
                    }
                }
            }
        }

        //向动directAction方法
        public abstract void directAction();

        //移动计算MoveAction方法
        public abstract void moveAction();
    }

    //小棋子类
    public class Bing extends chessPieceClass {
        //构造方法：获取该兵的位置
        public Bing(int bingNum) {
            super(bingNum);
        }

        @Override
        public void directAction() {
            //兵是上左右的走法，使用走法1表
            ArrayList<Integer> bingFirst = new ArrayList<>();
            //根据规则，兵无下，取消2号方向
            actionDirectionPre1[1] = 0;
            //根据阵营判断位置是否符合条件
            switch (camp) {
                case 1:
                    //红方的兵，大于等于0小于5，在敌方
                    if ((y_Coordinate>=0)|(y_Coordinate<5)) {
                        actionDirectionPre1[3] = 3;
                        actionDirectionPre1[4] = 4;
                    } else if ((y_Coordinate>=5)|(y_Coordinate<=9)) {
                        actionDirectionPre1[3] = 0;
                        actionDirectionPre1[4] = 0;
                    }
                case 2:
                    //黑方的兵正好相反
                    if ((y_Coordinate>=0)|(y_Coordinate<5)) {
                        actionDirectionPre1[3] = 0;
                        actionDirectionPre1[4] = 0;
                    } else if ((y_Coordinate>=5)|(y_Coordinate<=9)) {
                        actionDirectionPre1[3] = 3;
                        actionDirectionPre1[4] = 4;
                    }
            }
            //遍历这个临时数组，为零的忽略
            for (int i = 0; i < 4; i++) {
                if (actionDirectionPre1[i] != 0) {
                    bingFirst.add(actionDirectionPre1[i]);
                }
            }
            //判断每一种方向的初步移动，因为只需要移动一格，所以简单
            for (int i = 0; i < bingFirst.size(); i++) {
                //创建新坐标
                int x_new = x_Coordinate;
                int y_new = y_Coordinate;
                switch (bingFirst.get(i)) {
                    case 1:
                        y_new -= 1;
                    case 3:
                        x_new -= 1;
                    case 4:
                        x_new += 1;
                }
                //添加到列表
                Integer[] adpL = {bingFirst.get(i),x_new,y_new};
                actionDirectPoint.add(adpL);
            }
        }

        @Override
        public void moveAction() {

        }
    }

    public static void main(String[] args) {

    }
}
