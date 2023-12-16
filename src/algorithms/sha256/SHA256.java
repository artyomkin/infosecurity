package algorithms.sha256;


public class SHA256 implements HashAlgorithm{

    public static int ror(int n, int k) {
        return (n >>> k) | (n << (32 - k));
    }

    public static int Ch(int x, int y, int z) {
        return (z ^ (x & (y ^ z)));
    }

    public static int Maj(int x, int y, int z) {
        return ((x & y) | (z & (x | y)));
    }

    public static int S0(int x) {
        return (ror(x, 2) ^ ror(x, 13) ^ ror(x, 22));
    }

    public static int S1(int x) {
        return (ror(x, 6) ^ ror(x, 11) ^ ror(x, 25));
    }

    public static int R0(int x) {
        return (ror(x, 7) ^ ror(x, 18) ^ (x >>> 3));
    }

    public static int R1(int x) {
        return (ror(x, 17) ^ ror(x, 19) ^ (x >>> 10));
    }

    private static final int[] K = {
            0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b,
            0x59f111f1, 0x923f82a4, 0xab1c5ed5, 0xd807aa98, 0x12835b01,
            0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7,
            0xc19bf174, 0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc,
            0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da, 0x983e5152,
            0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147,
            0x06ca6351, 0x14292967, 0x27b70a85, 0x2e1b2138, 0x4d2c6dfc,
            0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
            0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819,
            0xd6990624, 0xf40e3585, 0x106aa070, 0x19a4c116, 0x1e376c08,
            0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f,
            0x682e6ff3, 0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208,
            0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
    };

    public static int[] processBlock(int[] bytes, int[] H) {
        int[] W = new int[64];
        int t1, t2, a, b, c, d, e, f, g, h;
        int i;

        for (i = 0; i < 16; i++) {
            W[i] = 0x00;
            W[i] = bytes[4*i] << 24;
            W[i] |= bytes[4*i+1] << 16;
            W[i] |= bytes[4*i+2] << 8;
            W[i] |= bytes[4*i+3];
        }
        for (i = 16; i < 64; i++) {
            W[i] = R1(W[i - 2]) + W[i - 7] + R0(W[i - 15]) + W[i - 16];
        }
        a = H[0];
        b = H[1];
        c = H[2];
        d = H[3];
        e = H[4];
        f = H[5];
        g = H[6];
        h = H[7];
        for (i = 0; i < 64; i++) {
            t1 = h + S1(e) + Ch(e, f, g) + K[i] + W[i];
            t2 = S0(a) + Maj(a, b, c);
            h = g;
            g = f;
            f = e;
            e = d + t1;
            d = c;
            c = b;
            b = a;
            a = t1 + t2;
        }
        H[0] += a;
        H[1] += b;
        H[2] += c;
        H[3] += d;
        H[4] += e;
        H[5] += f;
        H[6] += g;
        H[7] += h;
        return H;
    }

    int[][] explode(int[] bytes){
        int field_len = 4;
        int inputBytesLen = bytes.length;
        int bitLen = bytes.length * 8;
        int payloadLen = bytes.length + field_len;
        int paddingLen = 64 - payloadLen % 64;
        int blockCount = (int) payloadLen / 64 + 1;
        int[][] blocks = new int[blockCount][64];

        for (int i = 0; i < blockCount; i++){
            for (int j = 0; j < 64; j++){
                if (inputBytesLen == 0) {
                    if (paddingLen > 0){
                        blocks[i][j] = 0x00;
                        paddingLen--;
                    } else {
                        field_len--;
                        blocks[i][j] = bitLen >> 8 * field_len;
                    }
                } else {
                    blocks[i][j] = bytes[i*64 + j];
                    inputBytesLen--;
                    if (inputBytesLen == 0){
                        j++;
                        blocks[i][j] = 0x80;
                        paddingLen--;;
                    }
                }
            }
        }
        return blocks;
    }

    public String hash(String message){
        byte[] srcBytes = message.getBytes();
        int[] bytes = new int[srcBytes.length];
        for (int i = 0; i < bytes.length; i++){
            bytes[i] = (int) srcBytes[i];
        }
        int[][] blocks = explode(bytes);
        String res = "";
        int[] H = new int[] {
                0x6a09e667,
                0xbb67ae85,
                0x3c6ef372,
                0xa54ff53a,
                0x510e527f,
                0x9b05688c,
                0x1f83d9ab,
                0x5be0cd19
        };
        for (int i = 0; i < blocks.length; i++){
            H = processBlock(blocks[i], H);
        }
        for (int j = 0; j < H.length; j++){
            res += String.format("%08x", H[j]);
        }
        return res;
    }
}
