package repository.file;

import domain.User;
import domain.validators.Validator;

/**
 * The concrete repository for users.
 */
public class UsersRepository extends AbstractFileRepository<Long, User>{

    public UsersRepository(Validator<User> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    public User extractEntityFromLine(String line) {
        String[] words = line.split(";");

        User u = new User(words[1], words[2]);
        Long ID = Long.parseLong(words[0]);
        u.setId(ID);
        return u;
    }

    @Override
    public String createEntityAsString(User entity) {
        return entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName();
    }
    /**
     * Looks at the IDs used in the repository and finds one that is free for another entity. (Wasteful implementation)
     * @return A valid ID to be used for a user entity.
     */
    public long getNextID()
    {
        long minID = 0;
        for (User u: findAll()) {
            if(u.getId() > minID)
                minID = u.getId();
        }
        return minID + 1;
    }

}
