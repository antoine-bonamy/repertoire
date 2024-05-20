package fr.bonamy.repertoire_back.mapper;

import fr.bonamy.repertoire_back.exception.MappingException;
import fr.bonamy.repertoire_back.model.User;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static fr.bonamy.repertoire_back.util.Capitalize.capitalize;

@Component
public class UserMapper {

    public <T> T toDto(User user, Class<T> dtoClass) {
        try {
            List<Object> constructorArgs = new ArrayList<>();
            for (Field field : dtoClass.getDeclaredFields()) {
                Method method = User.class.getMethod("get" + capitalize(field.getName()));
                constructorArgs.add(method.invoke(user));
            }
            Constructor<?> constructor = dtoClass.getDeclaredConstructors()[0];
            return dtoClass.cast(constructor.newInstance(constructorArgs.toArray()));
        } catch (InvocationTargetException
                 | InstantiationException
                 | IllegalAccessException
                 | NoSuchMethodException e) {
            throw new MappingException(e);
        }
    }

    public User toEntity(Object dto) {
        User user = new User();
        try {
            for (Field field : dto.getClass().getDeclaredFields()) {
                Method recordMethod = dto.getClass().getMethod(field.getName());
                Method setMethod = User.class.getMethod("set" + capitalize(field.getName()), field.getType());
                setMethod.invoke(user, recordMethod.invoke(dto));
            }
            return user;
        } catch (NoSuchMethodException
                 | IllegalAccessException
                 | InvocationTargetException e) {
            throw new MappingException(e);
        }
    }

}