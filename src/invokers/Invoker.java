package invokers;

import commands.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Invoker {

    public HashMap<String, Command> commands;

    public Invoker(){
        this.commands = new HashMap<>();
        this.commands.put("register", new RegisterCommand());
        this.commands.put("create-secret", new CreateSecretCommand());
        this.commands.put("get-secret", new GetSecretCommand());
        this.commands.put("list-secrets", new ListSecretsCommand());
        this.commands.put("delete-secret", new DeleteSecretCommand());
        this.commands.put("update-secret", new UpdateSecretCommand());
        this.commands.put("move-secret", new MoveSecretCommand());
        this.commands.put("create-namespace", new CreateNamespaceCommand());
        this.commands.put("list-namespaces", new ListNamespacesCommand());
        this.commands.put("update-namespace", new UpdateNamespaceCommand());
        this.commands.put("delete-namespace", new DeleteNamespaceCommand());
        this.commands.put("allow-deletion", new ChangeDeletionPermissionCommand(true));
        this.commands.put("prohibit-deletion", new ChangeDeletionPermissionCommand(false));
    }

    public void invoke(){
        String commandName = System.getProperty("command");

        Command command = commands.get(commandName);
        if (command == null){
            System.out.println("Command " + commandName + " not found.");
            return;
        }
        command.execute();
    }
}
