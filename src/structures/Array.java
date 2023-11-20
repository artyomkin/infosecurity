package structures;

public class Array {
    private int[] arr;
    private int len;
    private final int INITIAL_CAPACITY = 1;
    private final int INITIAL_LEN = 0;

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

    private byte[] toBytes(){
        byte[] res = new byte[this.length()];
        for (int i = 0; i < res.length; i++){
            res[i] = (byte) this.get(i);
        }
        return res;
    }

    public String toString(){
        return new String(this.toBytes());
    }
}
