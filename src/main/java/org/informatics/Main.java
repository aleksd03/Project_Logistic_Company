package org.informatics;

import org.informatics.logistic.data.UserRepository;
import org.informatics.logistic.entity.Role;
import org.informatics.logistic.entity.User;
import org.informatics.logistic.service.AuthService;
import org.mindrot.jbcrypt.BCrypt;

public class Main {
    public static void main(String[] args) {
        var auth = new org.informatics.logistic.service.AuthService();

        auth.login("ivan123@asd.com", "ivan123456")
                .ifPresentOrElse(
                        u -> System.out.println("Login OK: " + u.getEmail() + " (" + u.getRole() + ")"),
                        () -> System.out.println("Invalid email or password")
                );





//        AuthService authService = new AuthService();
//
//        try {
//            User newUser = authService.register(
//                    "Peter",
//                    "Nicolov",
//                    "peter123@asd.com",
//                    "peter123456",
//                    Role.EMPLOYEE
//            );
//
//            System.out.println("Successfully registered: " + newUser);
//
//        } catch (IllegalArgumentException e) {
//            System.out.println("Registration failed: " + e.getMessage());
//        }



//        UserRepository repo = new UserRepository();
//
//        String email = "peter123@asd.com";
//        String rawPassword = "peter123456";
//        String hash = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
//
//        User u = new User();
//        u.setFirstName("Peter");
//        u.setLastName("Nicolov");
//        u.setEmail(email);
//        u.setPasswordHash(hash);
//        u.setRole(Role.EMPLOYEE);
//
//        repo.save(u);
//        System.out.println("Saved user: " + email);
//
//        repo.findByEmail(email).ifPresentOrElse(
//                found -> System.out.println("Found: " + found + " " + found.getLastName() + " (" + found.getRole() + ")"),
//                () -> System.out.println("Not found: " + email)
//        );
    }
}
