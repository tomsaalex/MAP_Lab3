package service;

import domain.Friendship;
import domain.User;
import graph.GraphManager;
import repository.file.FriendshipsRepository;
import repository.file.UsersRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * The information expert for friendships.
 */
public class FriendshipsService {
    public FriendshipsService(FriendshipsRepository friendshipsRepository, UsersRepository usersRepository) {
        this.friendshipsRepository = friendshipsRepository;
        this.usersRepository = usersRepository;
        graphManager = new GraphManager();
        loadUsersFriendLists();
    }

    private FriendshipsRepository friendshipsRepository;
    private UsersRepository usersRepository;
    private GraphManager graphManager;

    /**
     * A function used for loading all the friendships from the friendship repo into the internal friends list of the users.
     */
    private void loadUsersFriendLists()
    {
        for(Friendship f: friendshipsRepository.findAll())
        {
            this.usersRepository.findOne(f.getU1ID()).addFriend(f.getU2ID());
            this.usersRepository.findOne(f.getU2ID()).addFriend(f.getU1ID());
        }
    }

    /**
     * Encapsulates all the logic for adding a friendship into the application.
     * @param user1ID The ID of the first user of the friendship.
     * @param user2ID The ID of the second user of the friendship.
     * @return false, if the addition fails. true, if it succeeds.
     */
    public boolean serviceAddFriendship(Long user1ID, Long user2ID) {
        User user1 = usersRepository.findOne(user1ID);
        User user2 = usersRepository.findOne(user2ID);

        if(user1 == null || user2 == null || user1 == user2)
        {
            return false;
        }

        Friendship newFriendship = new Friendship(user1ID, user2ID);
        newFriendship.setId(friendshipsRepository.getNextID());
        Friendship res = friendshipsRepository.save(newFriendship);

        if(res == null)
        {
            user1.addFriend(user2.getId());
            user2.addFriend(user1.getId());

            return true;
        }

        return false;
    }

    /**
     * Encapsulates all the logic for removing a friendship from the application.
     * @param user1ID The ID of the first user of the friendship.
     * @param user2ID The ID of the second user of the friendship.
     * @return false, if the removal fails. true, if it succeeds.
     */
    public boolean serviceRemoveFriendship(Long user1ID, Long user2ID)
    {
        User user1 = usersRepository.findOne(user1ID);
        User user2 = usersRepository.findOne(user2ID);

        if(user1 == null || user2 == null)
            return false;

        if(!user1.removeFriend(user2.getId()))
            return false;

        if(!user2.removeFriend(user1.getId()))
            return false;

        Friendship friendshipToSearch = new Friendship(user1ID, user2ID);
        Friendship friendship = friendshipsRepository.lookUP(friendshipToSearch);

        if(friendship == null)
            return false;

        friendshipsRepository.delete(friendship.getId());

        return true;
    }

    /**
     * Debug command returning the entire content of the repo of friendships.
     * @return An iterable going over all the friendships.
     */
    public Iterable<Friendship> serviceDebugPrintAllFriendships()
    {
        return friendshipsRepository.findAll();
    }

    /**
     * Manages the logic for getting the number of connected components of the network.
     * @return Returns the number of connected components found.
     */
    public int serviceCountConnectedComponents()
    {
        return graphManager.countConnectedComponents(usersRepository.findAll(), friendshipsRepository.findAll());
    }

    /**
     * Manages the logic for getting the most sociable community in the network (the one with the longest path in it).
     * @return Returns the members of said community.
     */
    public List<User> serviceGetMostSociableCommunity()
    {
        List<Long> userIDs = graphManager.getLongestPathComponent(usersRepository.findAll(), friendshipsRepository.findAll());
        List<User> usersFromComponent = new ArrayList<>();

        userIDs.forEach(uID -> usersFromComponent.add(usersRepository.findOne(uID)));
        return usersFromComponent;
    }

    /**
     * Encapsulates all the logic for removing a user from the application.
     * @param userID The ID of the user to remove.
     * @return false, if the removal fails. true, if it succeeds.
     */
    public User serviceDeleteUser(long userID) {
        Iterable<Friendship> friendships = friendshipsRepository.findAll();

        List<Friendship> friendshipList = new ArrayList<>();

        friendships.forEach(friendshipList::add);

        for(Friendship f: friendshipList)
        {
            if(f.getU1ID() == userID || f.getU2ID() == userID)
                friendshipsRepository.delete(f.getId());

        }

        return usersRepository.delete(userID);
    }
}
