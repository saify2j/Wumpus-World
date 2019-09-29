package sample.utils;

public class WumpusWorld {

    final int WorldDimX = 10;
    final int WorldDimY = 10;
    int [][] breeze = new int[WorldDimX][WorldDimY];
    int [][] pit = new int[WorldDimX][WorldDimY];
    int [][] stench = new int[WorldDimX][WorldDimY];

    public WumpusWorld() {

        for(int i=0; i<WorldDimX; i++) {
            for(int j=0; j<WorldDimY; j++){
                breeze[i][j] = -1;
            }
        }
        breeze[0][0] = 0;
        pit[0][0] = 0;
        stench[0][0] = 0;
    }


}
