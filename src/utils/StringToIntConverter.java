package utils;

import structures.Array;

public class StringToIntConverter {

    private final int FILLER_VALUE = 0;
    private final int BLOCK_SIZE = 16;
    private final int SHIFT_TO_UNSIGNED_BYTE = 128;

    public Array convert(String str){
        byte[] source = str.getBytes();
        int[] intsource = new int[source.length];
        for (int i = 0; i < source.length; i++){
            intsource[i] = source[i] + SHIFT_TO_UNSIGNED_BYTE;
        }

        Array arr = new Array(intsource.length);
        for (int i = 0; i < source.length; i++){
            arr.set(intsource[i], i);
        }

        if (arr.length() % BLOCK_SIZE != 0){
            for (int i = 0; i < arr.length() % BLOCK_SIZE; i++){
                arr.add(FILLER_VALUE);
            }
        }
        return arr;
    }

    public String convert(Array bytes){
        byte[] resbytes = new byte[bytes.length()];
        for (int i = 0; i < bytes.length(); i++){
            resbytes[i] = (byte) (bytes.get(i) - 128);
        }
        return new String(resbytes);
    }
}
