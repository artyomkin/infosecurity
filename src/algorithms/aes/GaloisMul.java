package algorithms.aes;

public class GaloisMul {
    public int mulBy02(int n){
        int res;
        if (n < 0x80){
            res = (n << 1);
        } else {
            res = (n << 1) ^ 0x1b;
        }
        return res % 0x100;
    }

    public int mulBy03(int n){
        return mulBy02(n) ^ n;
    }

    public int mulBy09(int n){
        return mulBy02(mulBy02(mulBy02(n))) ^ n;
    }

    public int mulBy0b(int n){
        return mulBy02(mulBy02(mulBy02(n))) ^ mulBy02(n) ^ n;
    }

    public int mulBy0d(int n){
        return mulBy02(mulBy02(mulBy02(n))) ^ mulBy02(mulBy02(n)) ^ n;
    }

    public int mulBy0e(int n){
        return mulBy02(mulBy02(mulBy02(n))) ^ mulBy02(mulBy02(n)) ^ mulBy02(n);
    }
}
