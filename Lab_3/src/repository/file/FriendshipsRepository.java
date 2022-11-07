package repository.file;

import domain.Friendship;
import domain.validators.Validator;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The concrete repository for friendships.
 */
public class FriendshipsRepository extends AbstractFileRepository<Long, Friendship>{
    public FriendshipsRepository(Validator<Friendship> validator, String fileName) {
        super(validator, fileName);
    }

    /**
     * Looks up a friendship in the repository. Useful for getting a friendship when you don't have its ID.
     * @param friendship The friendship to search for.
     * @return The friendship as it was found in the repository if it was found, null otherwise.
     */
    public Friendship lookUP(Friendship friendship)
    {
        for(Friendship f: this.findAll())
        {
            if(friendship.equals(f))
            {
                return f;
            }
        }

        return null;
    }

    @Override
    public Friendship extractEntityFromLine(String line) {
        String[] words = line.split(";");

        Long friendshipID = Long.parseLong(words[0]);
        Long user1ID = Long.parseLong(words[1]);
        Long user2ID = Long.parseLong(words[2]);
        LocalDateTime friendsFrom = LocalDateTime.parse(words[3]);

        Friendship f = new Friendship(user1ID, user2ID, friendsFrom);

        f.setId(friendshipID);
        return f;
    }

    @Override
    public String createEntityAsString(Friendship friendship)
    {
        return friendship.getId() + ";" + friendship.getU1ID() + ";" + friendship.getU2ID() + ";" + friendship.getFriendsFrom();
    }

    /**
     * Looks at the IDs used in the repository and finds one that is free for another entity. (Wasteful implementation)
     * @return A valid ID to be used for a friendship entity.
     */
    public long getNextID()
    {
        long minID = 0;
        for (Friendship f: findAll()) {
            if(f.getId() > minID)
                minID = f.getId();
        }
        return minID + 1;
    }
}