import domain.validators.FriendshipValidator;
import domain.validators.UserValidator;
import repository.file.FriendshipsRepository;
import repository.file.UsersRepository;
import service.FriendshipsService;
import service.UsersService;
import ui.MainUI;

public class Main {
    public static void main(String[] args) {
        UserValidator userValidator = new UserValidator();
        UsersRepository usersRepository = new UsersRepository(userValidator, "data/Users.csv");

        FriendshipValidator friendshipValidator = new FriendshipValidator();
        FriendshipsRepository friendshipsRepository = new FriendshipsRepository(friendshipValidator, "data/Friendships.csv");

        UsersService usersService = new UsersService(usersRepository);
        FriendshipsService friendshipsService = new FriendshipsService(friendshipsRepository, usersRepository);

        MainUI UI = new MainUI(usersService, friendshipsService);
        UI.run();
    }
}