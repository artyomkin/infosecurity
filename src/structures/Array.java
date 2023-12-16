package structures;

import sun.security.util.ByteArrayLexOrder;

public class Array {
    private int[] arr;
    private int len;
    private final int INITIAL_CAPACITY = 1;
    private final int INITIAL_LEN = 0;
    private final int BLOCK_SIZE = 16;

    public Array(){
        this.arr = new int[INITIAL_CAPACITY];
        this.len = INITIAL_LEN;
    }

    public Array(int size){
        this.arr = new int[size];
        this.len = size;
    }

    public Array(int arr[]){
        this.arr = new int[arr.length];
        for (int i = 0; i < arr.length; i++){
            this.arr[i] = arr[i];
        }
        this.len = arr.length;
    }

    public int[] getArr(){
        int[] outArr = new int[this.len];
        for (int i = 0; i < outArr.length; i++){
            outArr[i] = this.arr[i];
        }
        return outArr;
    }

    public Array(byte arr[]){
        this.arr = new int[arr.length];
        for (int i = 0; i < arr.length; i++){
            this.arr[i] = arr[i];
        }
        this.len = arr.length;
    }
    public void fromBytes(byte bytesarr[]){
        this.arr = new int[bytesarr.length / Integer.BYTES];
        for (int i = 0; i < bytesarr.length; i+=4){
            int byte0 = bytesarr[i * 4] & 0xFF;
            int byte1 = bytesarr[i * 4 + 1] & 0xFF << 8;
            int byte2 = bytesarr[i * 4 + 2] & 0xFF << 16;
            int byte3 = bytesarr[i * 4 + 3] & 0xFF << 24;
            System.out.println("0 = " + String.format("%32s",Integer.toBinaryString(byte0)).replace(' ', '0'));
            System.out.println("1 = " + String.format("%32s",Integer.toBinaryString(byte1)).replace(' ', '0'));
            System.out.println("2 = " + String.format("%32s",Integer.toBinaryString(byte2)).replace(' ', '0'));
            System.out.println("3 = " + String.format("%32s",Integer.toBinaryString(byte3)).replace(' ', '0'));
            this.arr[i] = byte0 | byte1 | byte2 | byte3;
        }

        this.len = arr.length;
    }

    public int length(){
        return this.len;
    }

    public int get(int index) throws IndexOutOfBoundsException{
        if (index >= len || index < 0){
            throw new IndexOutOfBoundsException();
        }
        return this.arr[index];
    }

    public Array get(int start, int end) throws IndexOutOfBoundsException{
        if (start >= len || start < 0 || end > len || end < 0 || start > end){
            throw new IndexOutOfBoundsException();
        }
        Array res = new Array();
        for (int i = start; i < end; i++){
            res.add(this.get(i));
        }
        return res;
    }

    public void set(int value, int index){
        if (index >= len || index < 0){
            throw new IndexOutOfBoundsException();
        }
        this.arr[index] = value;
    }

    private void increazeSize(){
        int newArr[] = new int[this.len * 2];

        for (int i = 0; i < this.arr.length; i++){
            newArr[i] = this.arr[i];
        }
        this.arr = newArr;
    }

    public void add(int value){
        if (len >= this.arr.length){
            increazeSize();
        }
        this.arr[len] = value;
        len += 1;
    }

    public void add(Array inputArr){
        for (int i = 0; i < inputArr.length(); i++){
            this.add(inputArr.get(i));
        }
    }

    public void print(){
        for (int i = 0; i < this.length(); i++){
            System.out.println(Integer.toHexString(this.get(i)));
        }
    }

    public void printRow(){
        for (int i = 0; i < this.length() / BLOCK_SIZE * 4; i++){
            for (int j = i * BLOCK_SIZE / 4; j < (i + 1) * BLOCK_SIZE / 4; j++){
                System.out.print(Integer.toHexString(this.get(j)) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public String toString(){
        return new String(this.toBytes());
    }

    public int[][] toMultiDimensionalArray(){
        int res[][] = new int[4][4];
        for (int i = 0; i < this.length()/4; i++){
            for (int j = i*4; j < (i+1)*4; j++){
                res[i][j%4] = this.get(j);
            }
        }
        return res;
    }

    public byte[] toBytes(){
        byte[] res = new byte[this.arr.length * Integer.BYTES];
        for (int i = 0; i < this.arr.length; i++){
            for (int j = 0; j < Integer.BYTES; j++){
                res[i * Integer.BYTES + j] = (byte) (this.arr[i] >>> (j * 8));
            }
        }
        return res;
    }
}
