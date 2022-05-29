package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
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

    public Optional<Collection<User>> allFriends(@NotNull Long userId) throws UserNotFoundException {
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
        return Optional.of(friends);

    }

    public Optional<Set<User>> commonFriends(@NotNull Long userId, @NotNull Long otherUserId)
            throws UserNotFoundException {
        if(userStorage.get(userId).isEmpty() & userStorage.get(otherUserId).isEmpty()){
            throw new UserNotFoundException(String.format("User id=%ds или id=%s User с id=%s не найден.",
                    userId, otherUserId));
        }
        if(userStorage.get(userId).get().getFriends().size() == 0
                || userStorage.get(otherUserId).get().getFriends().size() == 0){
            return Optional.of(new HashSet<>());
        }
        if(userId <0 || otherUserId <0){
            throw new UserNotFoundException("User id или Friend id <0");
        }
        Set<Long> userFriends = userStorage.get(userId).get().getFriends();
        return Optional.of(userStorage.get(otherUserId).get().getFriends().stream()
                .filter(userFriends::contains)
                .map(id-> {
                    try {
                        return userStorage.get(id).get();
                    } catch (UserNotFoundException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toSet()));

    }
}