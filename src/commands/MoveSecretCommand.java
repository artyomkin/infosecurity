package commands;

import DAO.FileRepo;
import service.SecretService;
import service.UserService;

public class MoveSecretCommand extends AbstractCommand implements Command{

    private FileRepo fileRepo;
    private UserService userService;
    private SecretService secretService;

    @Override
    public void execute() {
        String username = super.reqProp("username");
        String password = super.reqProp("password");
        String cipherkey = super.reqProp("cipher-key");
        String source_namespace = super.reqProp("source-namespace");
        String target_namespace = super.reqProp("target-namespace");
        String secretkey = super.reqProp("key");
        if (username == null || password == null || cipherkey == null || source_namespace == null || secretkey == null || target_namespace == null) {
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

        String secValue = secretService.getSecret(source_namespace, secretkey);

        if (!secretService.deleteSecret(source_namespace,secretkey)){
            System.out.println("Secret doesn't exist.");
            return;
        }

        String newSecret = target_namespace + "." + secretkey + "=" + secValue;
        secretService.createSecret(newSecret);
        if (!super.flushToDisk(fileRepo)){
            System.out.println("Secret successfully moved.");
        }
    }
}
