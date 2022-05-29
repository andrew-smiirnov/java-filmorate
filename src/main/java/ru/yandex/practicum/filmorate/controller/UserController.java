package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;


@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UserStorage userStorage;

    @GetMapping
    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable(required = false) Long id) throws ValidationException {
        if (id < 0) {
            throw new IncorrectParameterException("User id < 0");
        }
        return userStorage.get(id).get();
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getAllFriends(@PathVariable Long id) throws UserNotFoundException {
        return userService.allFriends(id).get();
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getAllFriends(@PathVariable Long id, @PathVariable Long otherId) throws UserNotFoundException {
        return userService.commonFriends(id, otherId).get();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) throws ValidationException {
        return userStorage.create(user);
    }

    @PutMapping
    public User put(@RequestBody @Valid User user) throws ValidationException {
        return userStorage.put(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) throws UserNotFoundException {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws ValidationException {
        userStorage.delete(id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) throws UserNotFoundException {
        userService.deleteFriend(id, friendId);
    }
}
