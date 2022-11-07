package service;

import domain.User;
import repository.file.UsersRepository;

/**
 * The information expert for the users.
 */
public class UsersService {

    private final UsersRepository usersRepository;


    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    /**
     * Manages all the logic for adding a user into the application.
     * @param firstName The first name of the user to add.
     * @param lastName The last name of the user to add.
     * @return true, if the addition succeeds. false, if it fails.
     */
    public boolean serviceAddUser(String firstName, String lastName) {
        User newUser = new User(firstName, lastName);
        newUser.setId(usersRepository.getNextID());

        return usersRepository.save(newUser) == null;

    }

    /**
     * Debug command returning the entire content of the repo of users.
     * @return An iterable going over all the users.
     */
    public Iterable<User> serviceDebugPrintAllUsers() {
        return usersRepository.findAll();
    }


}
