package db;

import entity.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements Repository<User> {

    private final List<User> users;
    private static UserRepository singleton;
    private static final String PATH = "src/db/user_db.txt";

    public UserRepository(List<User> users) {
        this.users = users;
    }

    public static UserRepository getInstance() {
        if (singleton == null) {
            singleton = new UserRepository(loadData());
        }
        return singleton;
    }

    @SuppressWarnings("unchecked")
    private static List<User> loadData() {
        try (
                InputStream inputStream = new FileInputStream(PATH);
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        ) {
            return (List<User>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }

    }

    private void uploadData() {
        try (
                OutputStream outputStream = new FileOutputStream(PATH);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)
        ) {
            objectOutputStream.writeObject(users);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(User user) {
        users.add(user);
        uploadData();
    }

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public void update(User user, Integer id) {

    }

    @Override
    public void delete(User user) {
        users.remove(user);
        uploadData();
    }
}
