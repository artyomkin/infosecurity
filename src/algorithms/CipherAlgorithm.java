package algorithms;

public interface CipherAlgorithm {
    String encrypt(String sourceText);
    String decrypt(String cipher);
}
