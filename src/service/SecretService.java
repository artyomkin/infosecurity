package service;

import DAO.FileRepo;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecretService {
    FileRepo fileRepo;

    public SecretService(FileRepo fileRepo){
        this.fileRepo = fileRepo;
    }

    public boolean createSecret(String secret){
        this.fileRepo.addLine(secret.replaceAll("\n", "\\\\n"));
        return true;
    }

    public String getSecret(String namespace, String key){
        String res = null;
        String patternstr = "^"+namespace+"."+key+"=.*$";
        for (int i = 0; i < fileRepo.getLen(); i++){
            String line = fileRepo.getLine(i);
            if (Pattern.matches(patternstr, line)){
                String replacepattern = "^"+namespace+"."+key+"=";
                Pattern pattern = Pattern.compile(replacepattern);
                Matcher matcher = pattern.matcher(line);
                res = matcher.replaceAll("").replaceAll("\\\\n", "\n");
                return res;
            }
        }
        return res;
    }

    public List<String> listSecretsInNamespace(String namespace){
        List<String> res = new ArrayList<>();
        String patternstr = "^"+namespace+".*=.+$";
        String replacepattern = "=.*$";
        for (int i = 0; i < fileRepo.getLen(); i++){
            String line = fileRepo.getLine(i);
            if (Pattern.matches(patternstr, line)){
                Pattern pattern = Pattern.compile(replacepattern);
                Matcher matcher = pattern.matcher(line);
                res.add(matcher.replaceAll(""));
            }
        }
        return res;
    }

    public boolean deleteSecret(String namespace, String key){
        if (!this.fileRepo.isDeletionAllowed()){
            return false;
        }
        String patternstr = "^"+namespace+"."+ key +"=.*$";
        for (int i = 0; i < fileRepo.getLen(); i++){
            String line = fileRepo.getLine(i);
            if (Pattern.matches(patternstr, line)){
                fileRepo.deleteLine(i);
                return true;
            }
        }
        return false;
    }

    public List<String> listNamespaces() {
        List<String> res = new ArrayList<>();
        String patternstr = "^.+\\.$";
        String replacepattern = "\\..*$";
        for (int i = 2; i < fileRepo.getLen(); i++){
            String line = fileRepo.getLine(i);
            if (Pattern.matches(patternstr, line)){
                Pattern pattern = Pattern.compile(replacepattern);
                Matcher matcher = pattern.matcher(line);
                res.add(matcher.replaceAll(""));
            }
        }
        return res;
    }

    public boolean updateNamespace(String sourceNamespace, String targetNamespace) {
        boolean atLeastOneExists = false;
        for (int i = 0; i < this.fileRepo.getLen(); i++){
            String line = fileRepo.getLine(i);
            Pattern patternFind = Pattern.compile("^" + sourceNamespace + "\\..*$");
            Pattern patternReplace = Pattern.compile("^" + sourceNamespace + "\\.");
            Matcher matcherFind = patternFind.matcher(line);
            Matcher matcherReplace = patternReplace.matcher(line);
            if (matcherFind.matches()){
                atLeastOneExists = true;
            }
            line = matcherReplace.replaceAll(targetNamespace+"\\.");
            this.fileRepo.updateLine(i, line);
        }
        return atLeastOneExists;
    }

    public boolean deleteNamespace(String namespace) {
        if (!this.fileRepo.isDeletionAllowed()){
            return false;
        }
        int i = 0;
        while(i < fileRepo.getLen()){
            String line = fileRepo.getLine(i);
            Pattern patternFind = Pattern.compile("^" + namespace + "\\..*$");
            Matcher matcherFind = patternFind.matcher(line);
            if (matcherFind.matches()){
                this.fileRepo.deleteLine(i);
            } else {
                i++;
            }
        }
        return i <= fileRepo.getLen();
    }
    public void changeDeletionPermission(boolean allow){
        this.fileRepo.changeDeletionPermission(allow);
    }
}
