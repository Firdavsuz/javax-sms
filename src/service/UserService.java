package service;

import db.UserRepository;
import entity.User;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import util.Input;

import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService {

    static UserRepository userRepository = UserRepository.getInstance();

    public static boolean isGmail(String gMail) {
        String regex = "\\w+@gmail.com";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(gMail);
        return matcher.matches();
    }

    private static boolean isCodeVerify(int code, int codeInput) {
        return code == codeInput;
    }

    public static int sendVerifyCodeForLogIn(String gMail) {

        Random random = new Random();
        int randCode = random.nextInt(1000000);

        if (isGmail(gMail)) {
            Properties properties = new Properties();

            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            String username = "firdavsimomov.ceo@gmail.com";
            String password = "wjyzzyqegqpzbxiv";

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            username,
                            password);
                }
            });

            Message message = new MimeMessage(session);
            try {
                message.setSubject("CODE || VERIFY");
                message.setText("Sizning kodingiz: " + randCode);
                message.setFrom(new InternetAddress(username));
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(gMail));
                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        return randCode;
    }

    public static void start() {
        while (true) {

            int code = sendVerifyCodeForLogIn(Input.inputStr("Gmailingizni kiriting: "));
            int codeInput = Input.inputInt("Tasdiqlash kodini kiriting: ");

            if (isCodeVerify(code, codeInput)) {

                try {
                    System.out.println("Kodingiz tasdiqlandi...");
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                userMenu();
            } else {
                System.out.println("Iltimos qaytadan urinib ko'ring!");
            }

        }
    }

    private static void userMenu() {
        while (true) {
            displayMenu();

            switch (Input.inputInt("Tanlang: ")) {
                case 1 -> add();
                case 2 -> update();
                case 3 -> findAll();
            }
        }
    }

    private static void findAll() {
        List<User> all = userRepository.findAll();
        int i = 1;
        for (User user : all) {
            System.out.println(i + ". " +  user.getName());
            i++;
        }
    }

    private static void update() {
        findAll();
        User user = userRepository.findAll().get(Input.inputInt("Choose: ") - 1);
        String newName = Input.inputStr("Yangi nomini kiriting: ");
        int code = sendVerifyCodeForLogIn(user.getEmail());
        int codeInput = Input.inputInt("Tasdiqlash kodini kiriting: ");

        if (isCodeVerify(code, codeInput)) {

            try {
                System.out.println("Kodingiz tasdiqlandi...");
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            user.setName(newName);
            System.out.println("Ism yangilandi");
        } else {
            System.out.println("Iltimos qaytadan urinib ko'ring!");
        }


    }

    private static void add() {
        userRepository.create(new User(Input.inputStr("Ismingizni yozing: "), Input.inputStr("Email yozing: ")));
        System.out.println("saqlandi.");
    }

    private static void displayMenu() {
        System.out.println("""
                1 - Add user
                2 - Update user
                3 - All users
                3 - Delete user""");
    }

}
