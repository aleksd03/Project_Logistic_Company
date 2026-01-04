package org.informatics.service;

import org.informatics.dao.UserDao;
import org.informatics.entity.Role;
import org.informatics.entity.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;
import java.util.regex.Pattern;

public class AuthService {
    private final UserDao users = new UserDao();

    private static final Pattern EMAIL_RX =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    public User register(String firstName, String lastName, String email, String rawPassword, Role role) {

        if (isBlank(firstName) || isBlank(lastName) || isBlank(email) || isBlank(rawPassword) || role == null) {
            throw new IllegalArgumentException("All fields are required.");
        }

        String normalizedEmail = email.trim().toLowerCase();
        if (!EMAIL_RX.matcher(normalizedEmail).matches()) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        if (rawPassword.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters.");
        }

        if (users.findByEmail(normalizedEmail).isPresent()) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        String hash = BCrypt.hashpw(rawPassword, BCrypt.gensalt(12));

        User u = new User();
        u.setFirstName(firstName.trim());
        u.setLastName(lastName.trim());
        u.setEmail(normalizedEmail);
        u.setPasswordHash(hash);
        u.setRole(role);
        u.setActive(true);

        users.save(u);
        return u;
    }

    public Optional<User> login(String email, String rawPassword) {
        if (isBlank(email) || isBlank(rawPassword)) return Optional.empty();

        String normalizedEmail = email.trim().toLowerCase();
        Optional<User> u = users.findByEmail(normalizedEmail);
        if (u.isEmpty()) return Optional.empty();
        if (!u.get().isActive()) return Optional.empty();

        boolean ok = BCrypt.checkpw(rawPassword, u.get().getPasswordHash());
        return ok ? u : Optional.empty();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}

