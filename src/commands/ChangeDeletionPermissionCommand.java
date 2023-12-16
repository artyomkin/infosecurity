package commands;

import DAO.FileRepo;
import service.SecretService;
import service.UserService;

public class ChangeDeletionPermissionCommand extends AbstractCommand implements Command{
    private FileRepo fileRepo;
    private UserService userService;
    private SecretService secretService;
    private boolean allow;

    public ChangeDeletionPermissionCommand(boolean allow){
        this.allow = allow;
    }

    @Override
    public void execute() {
        String username = super.reqProp("username");
        String password = super.reqProp("password");
        String cipherkey = super.reqProp("cipher-key");
        if (username == null || password == null || cipherkey == null ){
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
        this.secretService.changeDeletionPermission(allow);
        if (this.flushToDisk(fileRepo)){
            if (allow){
                System.out.println("Deletion is allowed.");
            } else {
                System.out.println("Deletion is prohibited.");
            }
        } else {
            System.out.println("Cannot access secrets file.");
        }
    }
}
