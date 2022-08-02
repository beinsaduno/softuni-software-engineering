package bg.manhattan.musicdb.service.impl;

import bg.manhattan.musicdb.model.entity.User;
import bg.manhattan.musicdb.model.service.UserServiceLoginModel;
import bg.manhattan.musicdb.model.service.UserServiceModel;
import bg.manhattan.musicdb.repository.UserRepository;
import bg.manhattan.musicdb.security.CurrentUser;
import bg.manhattan.musicdb.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    private final CurrentUser currentUser;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper mapper,
                           CurrentUser currentUser,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.currentUser = currentUser;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(UserServiceModel userModel) {
        User user = this.mapper.map(userModel, User.class);
        this.userRepository.save(user);
    }

    @Override
    public boolean loginUser(UserServiceLoginModel userModel) {
        Optional<User> dbUser = this.userRepository.findByUsername(userModel.getUsername());
        if (dbUser.isEmpty()) {
            return false;
        }

        boolean matches = this.passwordEncoder.matches(userModel.getPassword(), dbUser.get().getPasswordHash());
        if (!matches) {
            return false;
        }

        User user = dbUser.get();
        this.currentUser
                .setId(user.getId())
                .setUsername(user.getUsername());
        return true;
    }

    @Override
    public void logout() {
        this.currentUser.clear();
    }

    @Override
    public boolean isLoggedIn() {
        return currentUser.isLoggedIn();
    }

    @Override
    public Optional<User> getCurrentUser() {
        return this.userRepository.findById(this.currentUser.getId());
    }

    @Override
    public String getCurrentUserName() {
        return this.currentUser.getUsername();
    }

    @Override
    public Optional<UserServiceModel> getUserByUsername(String userName) {
        Optional<User> user = this.userRepository.findByUsername(userName);
        return toUserServiceModel(user);
    }

    @Override
    public Optional<UserServiceModel> getUserByEmail(String email) {
        Optional<User> user = this.userRepository.findByEmail(email);
        return toUserServiceModel(user);
    }

    private Optional<UserServiceModel> toUserServiceModel(Optional<User> user) {
        return user.map(value -> this.mapper.map(value, UserServiceModel.class));
    }
}
