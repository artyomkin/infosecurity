package algorithms.aes;

import structures.Array;

public class AddRoundKey {
    private final int NK = 4;
    private final int NR = 10;
    private final int NB = 4;
    private final int BLOCK_SIZE = 16;
    public Array addBlockRoundKey(int[][] state, Array[] keySchedule, int round){
        Array[] rounded = new Array[4];
        Array res = new Array();
        for (int i = 0; i < 4; i++){
            rounded[i] = new Array();
        }
        for (int col = 0; col < NK; col++){
            for (int i = 0; i < 4; i++){
                rounded[i].add(state[i][col] ^ keySchedule[i].get(NB * round + col));
            }
        }
        for (int i = 0; i < 4; i++){
            res.add(rounded[i]);
        }
        return res;
    }

    public Array addRoundKey(Array state, Array[] keySchedule, int round, boolean enc){
        Array res = new Array();
        for (int i = 0; i < state.length(); i += BLOCK_SIZE){
            Array segment = state.get(i, i + BLOCK_SIZE);
            int block[][] = new int[4][4];
            if (round == 0 && enc){
                for (int j = 0; j < 4; j++){
                    for (int k = 0; k < 4; k++){
                        block[k][j] = segment.get(j * 4 + k);
                    }
                }
            } else {
                for (int j = 0; j < 4; j++){
                    for (int k = 0; k < 4; k++){
                        block[j][k] = segment.get(j * 4 + k);
                    }
                }
            }
            Array resBlock = this.addBlockRoundKey(block, keySchedule, round);
            res.add(resBlock);
        }
        return res;
    }
}
