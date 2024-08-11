public class Main {
    public static class chessman {
        int [][] chessBoard_1;
        int x;
        int y;
        public chessman(int[][] cb,int name) {
            chessBoard_1 = cb;
            for (int i = 0; i < 10; i++) {
                //System.out.println(i);
                for (int j = 0; j < 9; j++) {
                    if (chessBoard_1[i][j] == name) {
                        x = j;
                        y = i;
                        System.out.println(x);
                        System.out.println(y);
                        break;
                    }
                }
            }
        }
    }
    public static class Jiang extends chessman {
        public Jiang(int[][] cb, int name) {
            super(cb, name);
        }
        public int move() {
            //1up 2down 3left 4right
            int[] probablity = {1,2,3,4};
            int probablityLength = 4;
            //缩减情况
            if (x == 3) {
                probablity[3] = 0;
                probablityLength-=1;
            } else if (x == 5) {
                probablity[4] = 0;
                probablityLength-=1;
            }
            if ((y == 2)|(y == 9)) {
                probablity[2] = 0;
                probablityLength-=1;
            } else if ((y == 0)|(y == 7)) {
                probablity[1] = 0;
                probablityLength-=1;
            }
            //将向上下左右运动，不能超过九宫
            //先模拟走完的情景
            int num = 1;
            while (num<=probablityLength) {
                if (probablity[num-1]==0) {
                    continue;
                } else {
                    switch (probablity[num]) {
                        case 1: y++;
                        case 2: y--;
                        case 3: x--;
                        case 4: x++;
                    }
                    num ++;
                }
            }
            return probablityLength;
        }
    }
    public static void main(String[] args) {
        int[][] mainChessBoard = {
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
        int redEatingNumber;
        int blackEatingNumber;
        Jiang j = new Jiang(mainChessBoard, 22);
    }
}
