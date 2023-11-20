import algorithms.AES;
import exceptions.ElementNotFoundException;
import structures.Array;
import utils.StringToIntConverter;

public class Main {
    public static void main(String... args) throws ElementNotFoundException {
        String str = "абвгдеюяАБВЭЮЯ .,!\"\'";

        AES aes = new AES();
        StringToIntConverter stringToIntConverter = new StringToIntConverter();
        Array sourceBytes = stringToIntConverter.convert(str);
        Array encBytes = aes.subBytes(sourceBytes);

        encBytes.print();
        System.out.println("length = " + encBytes.length());
        System.out.println(encBytes.toString());

        Array decBytes = aes.unsubBytes(encBytes);
        System.out.println("length = " + decBytes.length());
        System.out.println(stringToIntConverter.convert(decBytes));


    }
}
