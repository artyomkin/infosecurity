package algorithms.aes;

import structures.Array;

public class KeyExpansion {
    private final int[] RCON;
    private final int NK = 4;
    private final int NR = 10;
    private final int NB = 4;

    public KeyExpansion() {
        this.RCON = new int[]{
            0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };
    }

    public Array[] keyExpansion(Array secretKey){
        Array keySchedule[] = new Array[4];
        for (int i = 0; i < keySchedule.length; i++){
            keySchedule[i] = new Array();
        }
        for (int r = 0; r < 4; r++){
            for (int c = 0; c < NK; c++){
                keySchedule[r].add(secretKey.get(r + 4 * c));
            }
        }

        for (int col = NK; col < NB * (NR + 1); col++){
            if (col % NK == 0){
                int tmpRow[] = new int[] { keySchedule[1].get(col - 1), keySchedule[2].get(col - 1), keySchedule[3].get(col - 1), keySchedule[0].get(col - 1) };

                for (int j = 0; j < tmpRow.length; j++){
                    tmpRow[j] = AES.SBOX[tmpRow[j]];
                }

                for (int row = 0; row < 4; row++){
                    int s = keySchedule[row].get(col - 4) ^ tmpRow[row] ^ RCON[NR * row + col / NK - 1];
                    keySchedule[row].add(s);
                }
            } else {
                for (int row = 0; row < 4; row++){
                    int s = keySchedule[row].get(col - 4) ^ keySchedule[row].get(col - 1);
                    keySchedule[row].add(s);
                }
            }
        }
        return keySchedule;
    }
}