package commands;

import DAO.FileRepo;
import service.SecretService;
import service.UserService;

import java.util.List;

public class ListSecretsCommand extends AbstractCommand implements Command{

    private FileRepo fileRepo;
    private UserService userService;
    private SecretService secretService;
    @Override
    public void execute() {
        String username = super.reqProp("username");
        String password = super.reqProp("password");
        String cipherkey = super.reqProp("cipher-key");
        String namespace = super.reqProp("namespace");

        if (username == null || password == null || cipherkey == null || namespace == null) {
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

        List<String> list = secretService.listSecretsInNamespace(namespace);
        if (list.size() == 0){
            System.out.println("Nothing found.");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }
}
