package commands;

import DAO.FileRepo;
import exceptions.DecryptionFailedException;
import exceptions.InvalidUsernameException;
import service.UserService;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class RegisterCommand extends AbstractCommand implements Command{

    private UserService userService;
    private FileRepo fileRepo;
    @Override
    public void execute() {
        String userName = super.reqProp("username");
        String password = super.reqProp("password");
        if (userName == null || password == null){
            return;
        }
        String cipherkey = System.getProperty("cipher-key");

        String letterBase = "abcdefghklmnopqrstuvwxyzABCDEFGHKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        if (cipherkey == null){
            cipherkey = "";
            for (int i = 0; i < 16; i++){
                cipherkey += letterBase.charAt(random.nextInt(letterBase.length()));
            }
        }

        File checkFile = new File("vault/" + userName + "/0001");
        if (checkFile.exists()){
            System.out.println("User is already registered.");
            return;
        }
        this.fileRepo = super.initFileRepo(userName, cipherkey);
        if (fileRepo == null){
            return;
        }
        this.userService = new UserService(fileRepo);

        try{
            boolean registerStatus = userService.registerUser(userName, password);
            if (!registerStatus){
                System.out.println("User is already registered.");
                return;
            }
        } catch (InvalidUsernameException e){
            System.out.println("Invalid username " + userName);
            return;
        } catch (IOException e){
            System.out.println("Cannot access secret file of user.");
            return;
        }

        if (super.flushToDisk(fileRepo)){
            System.out.println("User successfully registered. Your cipher key is " + cipherkey);
        }
    }
}
