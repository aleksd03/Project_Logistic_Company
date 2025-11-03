package org.informatics.logistic.service;

import org.informatics.logistic.data.UserRepository;
import org.informatics.logistic.entity.Role;
import org.informatics.logistic.entity.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;
import java.util.regex.Pattern;

public class AuthService {
    private final UserRepository users = new UserRepository();
    private static final Pattern EMAIL_RX =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    public User register(String firstName, String lastName, String email, String rawPassword, Role role) {

        if (isBlank(firstName) || isBlank(lastName) || isBlank(email) || isBlank(rawPassword) || role == null) {
            throw new IllegalArgumentException("All fields are required.");
        }
        email = email.trim().toLowerCase();
        if (!EMAIL_RX.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        if (rawPassword.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters.");
        }

        if (users.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        String hash = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        User u = new User();
        u.setFirstName(firstName.trim());
        u.setLastName(lastName.trim());
        u.setEmail(email);
        u.setPasswordHash(hash);
        u.setRole(role);
        users.save(u);

        return u;
    }

    public Optional<User> login(String email, String rawPassword) {
        if (email == null || rawPassword == null) return Optional.empty();
        email = email.trim().toLowerCase();

        var opt = users.findByEmail(email);
        if (opt.isEmpty()) return Optional.empty();

        var u = opt.get();

        if (!u.isActive()) return Optional.empty();

        boolean ok = BCrypt.checkpw(rawPassword, u.getPasswordHash());
        return ok ? Optional.of(u) : Optional.empty();
    }

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}

