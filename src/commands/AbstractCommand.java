package commands;

import DAO.FileRepo;
import exceptions.DecryptionFailedException;

import java.io.IOException;

public class AbstractCommand {
    private final String homeDir = "vault";
    public String getHomeDir(){
        return this.homeDir;
    }

    protected String reqProp(String prop){
        String res = System.getProperty(prop);
        if (res == null){
            System.out.println(prop + " is required.");
            return null;
        }
        return res;
    }

    protected FileRepo initFileRepo(String username, String key){
        try {
            return new FileRepo(this.getHomeDir() + "/" + username, key);
        } catch (IOException e) {
            System.out.println("Cannot access user secrets file.");
            return null;
        } catch (DecryptionFailedException e) {
            System.out.println("Invalid cipher key");
            return null;
        }
    }

    protected boolean flushToDisk(FileRepo fileRepo){
        try {
            fileRepo.flushToDisk();
            return true;
        } catch (IOException e) {
            System.out.println("Cannot access secret file of user.");
            return false;
        }
    }
}
