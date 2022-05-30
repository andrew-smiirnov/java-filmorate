package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserService {
    @Autowired
    UserStorage userStorage;

    public void addFriend(@NotNull Long userId, @NotNull Long otherUserId) throws UserNotFoundException {
        if(userId < 0 || otherUserId < 0){
            throw new UserNotFoundException("User id или Friend id <0");
        }
        try{
            userStorage.get(userId).get().getFriends().add(otherUserId);
            userStorage.get(otherUserId).get().getFriends().add(userId);
        } catch (UserNotFoundException e) {
            String.format("User id=%s or Friend id=%s is not found", userId, otherUserId);
        }

    }

    public void deleteFriend(@NotNull Long userId, @NotNull Long otherUserId) throws UserNotFoundException {
        if(userId < 0 || otherUserId < 0){
            throw new UserNotFoundException("User id или Friend id <0");
        }
        if(userStorage.get(userId).isEmpty()) {
            throw new UserNotFoundException(String.format("User с id=%s не найден.", userId));
        }
        if(userStorage.get(userId).get().getFriends().contains(otherUserId)){
            userStorage.get(userId).get().getFriends().remove(userId);
        }
    }

    public Collection<User> allFriends(@NotNull Long userId) throws UserNotFoundException {
        if(userStorage.get(userId).isEmpty()) {
            throw new UserNotFoundException(String.format("User с id=%s User с id=%s не найден.", userId));
        }
        Optional<Set<Long>> allFriends = Optional.ofNullable(userStorage.get(userId).get().getFriends());
        Collection<User> friends = new ArrayList<>();
        if(allFriends.isPresent()){
            for (Long id: allFriends.get()) {
                friends.add(userStorage.get(id).get());
            }
        }
        return friends;

    }

    public Set<User> commonFriends(@NotNull Long userId, @NotNull Long otherUserId)
            throws UserNotFoundException {
        if(userStorage.get(userId).isEmpty() & userStorage.get(otherUserId).isEmpty()){
            throw new UserNotFoundException(String.format("User id=%ds или id=%s User с id=%s не найден.",
                    userId, otherUserId));
        }
        if(userStorage.get(userId).get().getFriends().size() == 0
                || userStorage.get(otherUserId).get().getFriends().size() == 0){
            return new HashSet<>();
        }
        if(userId <0 || otherUserId <0){
            throw new UserNotFoundException("User id или Friend id <0");
        }
        Set<Long> userFriends = userStorage.get(userId).get().getFriends();
        return userStorage.get(otherUserId).get().getFriends().stream()
                .filter(userFriends::contains)
                .map(id-> {
                    try {
                        return userStorage.get(id).get();
                    } catch (UserNotFoundException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toSet());

    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public Optional<User> get(Long userId) throws UserNotFoundException {
        return userStorage.get(userId);
    }

    public User create(User user) throws ValidationException {
        return userStorage.create(user);
    }

    public User put(User user) throws ValidationException {
        return userStorage.put(user);
    }

    public  void delete(Long userId) throws ValidationException {
        userStorage.delete(userId);
    }
}