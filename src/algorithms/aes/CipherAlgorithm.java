package algorithms.aes;

import exceptions.DecryptionFailedException;
import structures.Array;

public interface CipherAlgorithm {
    //byte[] encrypt(String sourceText, String key);
    Array encrypt(String sourceText, String key);
    String decrypt(Array cipher, String key) throws DecryptionFailedException;
}
