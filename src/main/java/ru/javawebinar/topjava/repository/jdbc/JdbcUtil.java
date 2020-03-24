package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.AbstractBaseEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

public class JdbcUtil{
    @Autowired
    private static Validator validator;

    public static <T extends AbstractBaseEntity> void validate(T entity, Class<?>...groups) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity, groups);
        if(violations.size() > 0) {
            throw new ConstraintViolationException(violations);
        }
    }

    public static void setValidator(Validator validator) {
        JdbcUtil.validator = validator;
    }

    public static Validator getValidator() {
        return validator;
    }
}
