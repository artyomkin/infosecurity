package commands;

import DAO.FileRepo;
import service.SecretService;
import service.UserService;

public class UpdateNamespaceCommand extends AbstractCommand implements Command{
    private FileRepo fileRepo;
    private UserService userService;
    private SecretService secretService;

    @Override
    public void execute() {
        String username = super.reqProp("username");
        String password = super.reqProp("password");
        String cipherkey = super.reqProp("cipher-key");
        String sourceNamespace = super.reqProp("source-namespace");
        String targetNamespace = super.reqProp("target-namespace");
        if (username == null || password == null || cipherkey == null || sourceNamespace == null || targetNamespace == null) {
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

        if(this.secretService.updateNamespace(sourceNamespace, targetNamespace)){
            if (this.flushToDisk(fileRepo)){
                System.out.println("Namespace updated.");
            }
            System.out.println("Couldn't access secret file.");
            return;
        }
        System.out.println("Namespace " + sourceNamespace + " doesn't exist");
    }
}
