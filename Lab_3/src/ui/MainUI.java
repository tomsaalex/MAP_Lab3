package ui;

import domain.Friendship;
import domain.User;
import domain.validators.ValidationException;
import service.FriendshipsService;
import service.UsersService;

import java.util.List;
import java.util.Scanner;

/**
 * The class responsible for the main UI of the application.
 */
public class MainUI {

    UsersService usersService;
    FriendshipsService friendshipsService;

    /**
     * Debug command for printing all the users in the application.
     */
    private void uiDebugPrintUsers() {
        Iterable<User> users = usersService.serviceDebugPrintAllUsers();
        users.forEach(System.out::println);
    }

    /**
     * Debug command for printing all the friendships in the application.
     */

    private void uiDebugPrintFriendships() {
        Iterable<Friendship> friends = friendshipsService.serviceDebugPrintAllFriendships();
        friends.forEach(System.out::println);
    }

    /**
     * Manages the user interface for adding users to the application.
     */
    private void uiAddUser() {
        Scanner inputScanner = new Scanner(System.in);
        String firstName;
        String lastName;

        try {
            System.out.print("Enter the user's first name: ");
            firstName = inputScanner.nextLine();
            System.out.print("Enter the user's last name: ");
            lastName = inputScanner.nextLine();

            boolean res = usersService.serviceAddUser(firstName, lastName);
            if(res)
                System.out.println("Insert executed perfectly.");
            else
                System.out.println("Insert could not be performed.");
        } catch (IllegalArgumentException | ValidationException iae) {
            System.out.println(iae.getMessage());
        }
    }

    /**
     * Manages the user interface for removing users from the application.
     */
    private void uiDeleteUser() {
        Scanner inputScanner = new Scanner(System.in);
        long id;

        try {
            System.out.print("Please enter the ID of the user you wish to remove: ");
            id = Long.parseLong(inputScanner.nextLine());

            if (friendshipsService.serviceDeleteUser(id) == null)
                System.out.println("There is no user with that ID to delete.");
            else
                System.out.println("Deletion completed successfully.");
        } catch (NumberFormatException e) {
            System.out.println("The given input was not a number");
        }
    }

    /**
     * Manages the user interface for removing friendships from the application.
     */
    private void uiAddFriendship() {
        Scanner inputScanner = new Scanner(System.in);
        System.out.print("Enter the ID of the first user: ");
        Long user1ID = inputScanner.nextLong();
        System.out.print("Enter the ID of the second user: ");
        Long user2ID = inputScanner.nextLong();

        boolean res = friendshipsService.serviceAddFriendship(user1ID, user2ID);

        if (res) {
            System.out.println("The friendship was added successfully.");
        } else {
            System.out.println("The friendship could not be added successfully.");
        }
    }

    /**
     * Manages the user interface for removing friendships from the application.
     */
    private void uiDeleteFriendship() {
        Scanner inputScanner = new Scanner(System.in);
        System.out.print("Enter the ID of the first user: ");
        Long user1ID = inputScanner.nextLong();
        System.out.print("Enter the ID of the first user: ");
        Long user2ID = inputScanner.nextLong();

        boolean res = friendshipsService.serviceRemoveFriendship(user1ID, user2ID);

        if (res) {
            System.out.println("The friendship was removed successfully.");
        } else {
            System.out.println("The friendship could not be removed successfully.");
        }
    }

    /**
     * Prints the number of communities in the application.
     */
    private void uiCountCommunities() {
        int compCount = friendshipsService.serviceCountConnectedComponents();
        if(compCount == 1)
            System.out.println("There is currently one community in this network.");
        else
            System.out.println("There are currently " + compCount + " communities in the network.");
    }

    /**
     * Prints the most sociable community.
     */
    private void uiMostSociableCommunity() {
        List<User> mostSociableCommunity = friendshipsService.serviceGetMostSociableCommunity();

        if(mostSociableCommunity.size() == 0) {
            System.out.println("There are no communities with more than one user.");
            return;
        }
        System.out.println("The most sociable community is made out of the following users: ");

        for(User u: mostSociableCommunity)
            System.out.println(u);

    }

    public MainUI(UsersService usersService, FriendshipsService friendshipsService) {
        this.usersService = usersService;
        this.friendshipsService = friendshipsService;
    }

    /**
     * Starts the main loop of the application.
     */
    public void run() {
        String command;
        Scanner inputScanner = new Scanner(System.in);

        mainProgramLoop:
        while (true) {
            System.out.print("Enter a command: ");
            command = inputScanner.nextLine();

            switch (command) {
                case "add_user" -> uiAddUser();
                case "rem_user" -> uiDeleteUser();
                case "add_frnd" -> uiAddFriendship();
                case "rem_frnd" -> uiDeleteFriendship();
                case "count_coms" -> uiCountCommunities();
                case "msc" -> uiMostSociableCommunity();
                case "printU" -> uiDebugPrintUsers();
                case "printF" -> uiDebugPrintFriendships();
                case "exit" -> {
                    break mainProgramLoop;
                }
                default -> System.out.println("Unknown command");
            }
        }
    }
}
