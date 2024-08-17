import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    //游戏局面静态类
    public static class chessSituation {
        public static int[][] mainChessBoard = {
                {39,41,44,46,42,45,43,40,38},
                {0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 },
                {0 ,37,0 ,0 ,0 ,0 ,0 ,36,0 },
                {35,0 ,34,0 ,33,0 ,32,0 ,31},
                {0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 },
                {0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 },
                {11,0 ,12,0 ,13,0 ,14,0 ,15},
                {0 ,16,0 ,0 ,0 ,0 ,0 ,17,0 },
                {0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 },
                {18,20,23,25,22,26,24,21,19}};
        public static boolean gameState = true;
    }

    //棋子父类
    public static abstract class chessPieceClass {
        //棋子基本信息表
        int x_Coordinate;
        int y_Coordinate;
        int chessPieceNumber;
        int camp; //阵营：红1黑2
        //用于遍历取用的方向表
        int[] actionDirectionPre1 = {1, 2, 3, 4}; //上↑，下↓，左←，右→
        int[] actionDirectionPre2 = {5, 6, 7, 8}; //左上↖，左下↙，右上↗，右下↘
        int[] actionDirectionEs = {11, 12}; //对于马的横纵
        //方向距离与落点x,y，存储为4元数组
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
                        x_Coordinate = j;
                        y_Coordinate = i;
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
    public static class Bing extends chessPieceClass {
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
                    if (!((y_Coordinate>=0)&&(y_Coordinate<5))) {
                        actionDirectionPre1[2] = 0;
                        actionDirectionPre1[3] = 0;
                    }
                    break;
                case 2:
                    //黑方的兵正好相反
                    if ((y_Coordinate>=0)&&(y_Coordinate<5)) {
                        actionDirectionPre1[2] = 0;
                        actionDirectionPre1[3] = 0;
                    }
                    break;
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
                        break;
                    case 3:
                        x_new -= 1;
                        break;
                    case 4:
                        x_new += 1;
                        break;
                }
                //添加到列表，并进行边界判断
                if (((x_new>=0)&&(x_new<=8))&&((y_new>=0)&&(y_new<=9))) {
                    Integer[] adpL = {bingFirst.get(i),1,x_new,y_new};
                    actionDirectPoint.add(adpL);
                }
            }
        }

        @Override
        public void moveAction() {
            //吃子，与特殊处理
            for (int i = 0; i < actionDirectPoint.size(); i++) {
                Integer[] tL = actionDirectPoint.get(i);
                //检查大表中目前的xy值对应的棋子
                int eatingChess = chessSituation.mainChessBoard[tL[3]][tL[2]];
                //判断阵营
                int eatingChessCamp = 0; //红1黑2
                if ((eatingChess < 30)&&(eatingChess>0)) {
                    eatingChessCamp = 1;
                } else if (eatingChessCamp > 30) {
                    eatingChessCamp = 2;
                }
                //不能吃自己阵营的子
                if (eatingChessCamp != camp) {
                    Integer[] newList = {tL[0], tL[1], tL[2], tL[3], eatingChess};
                    actionList.add(newList);
                }
            }
        }
    }

    //将：第二难的棋子
    public static class Jiang extends chessPieceClass {
        //构造方法：获取该将的位置
        public Jiang(int bingNum) {
            super(bingNum);
        }

        @Override
        public void directAction() {
            //将是横向走法，使用1表
            ArrayList<Integer> jiangFirst = new ArrayList<>();
            //直接流动赋值
            for (int i = 0; i < 4; i++) {
                jiangFirst.add(actionDirectionPre1[i]);
            }
            //将在走法上并没有限制，不在走法上削减将,直接模拟运行
            //x_new 在重写的时候改进了一下写法，前面的懒得改了
            for (int i = 0; i < 4; i++) {
                int x_new = x_Coordinate;
                int y_new = y_Coordinate;
                //这里的xnew, ynew是临时变量，放出去会出大毛病
                switch (actionDirectionPre1[i]) {
                    case 1:
                        y_new -= 1;
                        break;
                    case 2:
                        y_new += 1;
                        break;
                    case 3:
                        x_new -= 1;
                        break;
                    case 4:
                        x_new += 1;
                }
                //将的活动范围是九宫，x3-5,y0-2(b),7-9(r)
                switch (camp) {
                    case 1: //红
                        if (((x_new<=5)&&(x_new>=3))&&((y_new>=7)&&(y_new<=9))) {
                            Integer[] adpL = {jiangFirst.get(i),1,x_new,y_new};
                            actionDirectPoint.add(adpL);
                        }
                        break;
                    case 2:
                        if (((x_new<=5)&&(x_new>=3))&&((y_new>=0)&&(y_new<=2))) {
                            Integer[] adpL = {jiangFirst.get(i),1,x_new,y_new};
                            actionDirectPoint.add(adpL);
                        }
                        break;
                }
            }
        }

        @Override
        public void moveAction() {
            //吃子，与特殊处理
            for (int i = 0; i < actionDirectPoint.size(); i++) {
                Integer[] tL = actionDirectPoint.get(i);
                //检查大表中目前的xy值对应的棋子
                int eatingChess = chessSituation.mainChessBoard[tL[3]][tL[2]];
                //判断阵营
                int eatingChessCamp = 0; //红1黑2
                if ((eatingChess < 30)&&(eatingChess>0)) {
                    eatingChessCamp = 1;
                } else if (eatingChessCamp > 30) {
                    eatingChessCamp = 2;
                }
                //不能吃自己阵营的子
                if (eatingChessCamp != camp) {
                    Integer[] newList = {tL[0], tL[1], tL[2], tL[3], eatingChess};
                    actionList.add(newList);
                }
            }
        }
    }

    public static class Ju extends chessPieceClass {
        public Ju(int cPNumber) {
            super(cPNumber);
        }

        @Override
        public void directAction() {
            //首先扫描上下左右四个方向的棋子，把边界/最近棋子存入
            int upNumber = 0;
            int downNumber = 0;
            int leftNumber = 0;
            int rightNumber = 0;
            //循环扫描：上
            for (int i = 0; i < chessSituation.mainChessBoard.length; i++) {
                int thisChess = chessSituation.mainChessBoard[y_Coordinate][x_Coordinate];
                if (thisChess == 0) {
                    upNumber += 1;
                } else if (thisChess == ) {
                    
                }

            }
        }

        @Override
        public void moveAction() {

        }
    }

    public static void main(String[] args) {
        Ju j = new Ju(22);
        //监测输出信息
        j.directAction();
        j.moveAction();
        for (int i = 0; i < (j.actionList).size(); i++) {
            Integer[] m = (j.actionList).get(i);
            System.out.println(Arrays.toString(m));
        }
    }
}