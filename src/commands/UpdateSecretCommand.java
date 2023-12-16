package commands;

import DAO.FileRepo;
import service.SecretService;
import service.UserService;

public class UpdateSecretCommand extends AbstractCommand implements Command{

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
        String newvalue = super.reqProp("new-value");
        if (username == null || password == null || cipherkey == null || namespace == null || secretkey == null || newvalue == null) {
            return;
        }

        this.fileRepo = super.initFileRepo(username, cipherkey);
        if (fileRepo == null) {
            return;
        }
        if (!fileRepo.isHomeDirExists()) {
            System.out.println("Cannot access users secret file.");
            return;
        }
        this.userService = new UserService(fileRepo);
        this.secretService = new SecretService(fileRepo);

        if (!this.userService.authorizeUser(password)) {
            System.out.println("Wrong password or username.");
            return;
        }

        if (!secretService.deleteSecret(namespace,secretkey)){
            System.out.println("Secret doesn't exist.");
            return;
        }

        String newSecret = namespace + "." + secretkey + "=" + newvalue;
        secretService.createSecret(newSecret);
        if (!super.flushToDisk(fileRepo)){
            System.out.println("Secret successfully updated.");
        }
    }
}
