package commands;

import DAO.FileRepo;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import service.SecretService;
import service.UserService;

import java.io.IOException;

public class CreateSecretCommand extends AbstractCommand implements Command{
    private FileRepo fileRepo;
    private UserService userService;
    private SecretService secretService;

    @Override
    public void execute() {
        String username = super.reqProp("username");
        String password = super.reqProp("password");
        String cipherkey = super.reqProp("cipher-key");
        String namespace = super.reqProp("namespace");
        String secretkey = super.reqProp("key");
        String value = super.reqProp("value");
        if (username == null || password == null || cipherkey == null || namespace == null || secretkey == null || value == null){
            return;
        }

        this.fileRepo = super.initFileRepo(username, cipherkey);
        if (fileRepo == null){
            return;
        }
        if (!fileRepo.isHomeDirExists()){
            System.out.println("Cannot access users secret file.");
            return;
        }
        this.userService = new UserService(fileRepo);
        this.secretService = new SecretService(fileRepo);

        if (!this.userService.authorizeUser(password)){
            System.out.println("Wrong password or username.");
            return;
        }

        String secret = namespace + "." + secretkey + "=" + value;
        this.secretService.createSecret(secret);

        if (!super.flushToDisk(fileRepo)){
            System.out.println("Secret successfully created.");
        }
    }
}
