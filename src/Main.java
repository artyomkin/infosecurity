import exceptions.DecryptionFailedException;
import invokers.Invoker;

import java.util.Properties;

public class Main {
    public static void main(String... args){
        Invoker invoker = new Invoker();
        invoker.invoke();
    }
}
