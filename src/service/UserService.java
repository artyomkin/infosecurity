package service;

import DAO.FileRepo;
import algorithms.sha256.HashAlgorithm;
import algorithms.sha256.SHA256;
import entity.User;
import exceptions.DecryptionFailedException;
import exceptions.InvalidUsernameException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService {

    private FileRepo fileRepo;
    private HashAlgorithm hashAlgorithm;
    private final String decryptionSuccessfulMarker = "------VAULT FILE------";
    private final String allowDeletionMarker = "allow deletion";
    public UserService(FileRepo fileRepo){
        this.fileRepo = fileRepo;
        this.hashAlgorithm = new SHA256();
    }

    public boolean registerUser(String username, String password) throws InvalidUsernameException, IOException {
        Random random = new Random();
        User user = new User();
        user.setId(random.nextInt());
        user.setUserName(username);
        validateUser(user);

        if (!fileRepo.createFile(fileRepo.getHomeDir() + "/0001")){
            return false;
        }

        fileRepo.addLine(decryptionSuccessfulMarker);
        String passwordHash = hashAlgorithm.hash(password);
        user.setPasswordHash(passwordHash);
        fileRepo.addLine(passwordHash);
        fileRepo.addLine(allowDeletionMarker);

        return true;
    }

    public boolean authorizeUser(String password){
        String fileHash = fileRepo.getPasswordHash();
        String inputHash = hashAlgorithm.hash(password);
        return fileHash.equals(inputHash);
    }

    private void validateUser(User user) throws InvalidUsernameException {
        if (!user.getUserName().matches("^[0-9a-zA-Z-_]*$")){
            throw new InvalidUsernameException();
        }
    }

}
