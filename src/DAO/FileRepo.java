package DAO;

import algorithms.aes.AES;
import algorithms.aes.CipherAlgorithm;
import exceptions.DecryptionFailedException;
import structures.Array;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileRepo {

    private String homeDir;
    private CipherAlgorithm cipherAlgorithm;
    private ArrayList<String> context;
    private String key;
    private final String decryptionSuccessfulMarker = "------VAULT FILE------";
    private boolean homeDirExists;

    public FileRepo(String homeDir, String key) throws IOException, DecryptionFailedException {
        this.homeDir = homeDir;
        this.key = key;
        this.cipherAlgorithm = new AES();
        String secretFile = homeDir + "/0001";
        this.context = new ArrayList<>();
        readFromFile(secretFile);
        if (this.context.size() > 0 && !this.context.get(0).equals(decryptionSuccessfulMarker)){
            throw new DecryptionFailedException();
        }
        if (!Files.exists(Paths.get(secretFile))){
            this.homeDirExists = false;
        } else {
            this.homeDirExists = true;
        }
    }

    public boolean isHomeDirExists(){
        return this.homeDirExists;
    }

    public boolean createFile(String path) throws IOException{
        File userData = new File(path);
        if (!userData.exists()){
            userData.getParentFile().mkdirs();
            userData.createNewFile();
            return true;
        } else {
            return false;
        }
    }

    public String getHomeDir(){
        return this.homeDir;
    }

    public void addLine(String line) {
        this.context.add(line);
    }

    public void flushToDisk() throws IOException {
        if (this.context.size() == 0){
            return;
        }

        String wholeContext = "";
        for (int i = 0; i < this.context.size(); i++){
            wholeContext += this.context.get(i) + "\n";
        }
        int[] encryptedContext = this.cipherAlgorithm.encrypt(wholeContext, this.key).getArr();

        File dataFile = new File(homeDir + "/0001");
        FileOutputStream fos = new FileOutputStream(dataFile);
        for (int i = 0; i < encryptedContext.length; i++){
            fos.write(String.format("%2x", encryptedContext[i]).replace(' ', '0').getBytes());
        }
        fos.close();
    }

    public void readFromFile(String path) throws IOException, DecryptionFailedException {
        if (!Files.exists(Paths.get(path))) {
            return;
        }
        FileInputStream fis = new FileInputStream(path);
        int status = 0;
        Array encryptedContext = new Array();
        byte[] twoSymbolsBytes = new byte[2];
        status = fis.read(twoSymbolsBytes);
        while (status != -1) {
            String twoSymbols = new String(twoSymbolsBytes);
            int number = Integer.parseInt(twoSymbols, 16);
            encryptedContext.add(number);
            status = fis.read(twoSymbolsBytes);
        }

        String decryptedText = cipherAlgorithm.decrypt(encryptedContext, key);
        if (decryptedText.length() > 0){
            this.context = new ArrayList<>(Arrays.asList(decryptedText.split("\n")));
        }
        for (int i = 0; i < context.size(); i++){
            this.context.set(i, this.context.get(i).replace("\\\\n", "\n"));
        }
    }

    public String getPasswordHash(){
        return this.getLine(1);
    }

    public int getLen(){
        return this.context.size();
    }
    public String getLine(int index){
        return this.context.get(index);
    }
    public String deleteLine(int index){
        return this.context.remove(index);
    }

    public void updateLine(int i, String line) {
        this.context.set(i, line);
    }

    public void changeDeletionPermission(boolean allow) {
        if (allow){
            this.context.set(2, "allow deletion");
        } else {
            this.context.set(2, "prohibit deletion");
        }
    }

    public boolean isDeletionAllowed(){
        return this.context.get(2).equals("allow deletion");
    }
}
