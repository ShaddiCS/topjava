package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.HasId;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.UserTo;

@Component
public class EmailValidator<T extends HasId> implements Validator {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return HasId.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        if(o instanceof User) {
            User user = (User) o;

            User userFromDb = userRepository.getByEmail(user.getEmail());
            if(userFromDb != null && (SecurityUtil.safeGet() == null || SecurityUtil.authUserId() != userFromDb.getId())) {
                errors.rejectValue("email", "duplicate email","User with this email already exists");
            }
        } else if (o instanceof UserTo){
            UserTo user = (UserTo) o;

            User userFromDb = userRepository.getByEmail(user.getEmail());
            if (userFromDb != null && (SecurityUtil.safeGet() == null || SecurityUtil.authUserId() != userFromDb.getId())) {
                errors.rejectValue("email", "duplicate email", "User with this email already exists");
            }
        }
    }
}
