package fr.bonamy.repertoire_back.mapper;

import fr.bonamy.repertoire_back.exception.MappingException;
import fr.bonamy.repertoire_back.model.Contact;
import fr.bonamy.repertoire_back.model.Group;
import fr.bonamy.repertoire_back.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static fr.bonamy.repertoire_back.util.Capitalize.capitalize;

@Component
public class GroupMapper {

    private final ContactMapper contactMapper;
    private final UserMapper userMapper;

    @Autowired
    public GroupMapper(ContactMapper contactMapper, UserMapper userMapper) {
        this.contactMapper = contactMapper;
        this.userMapper = userMapper;
    }

    public <T> T toDto(Group group, Class<T> dtoClass) {
        try {
            List<Object> constructorArgs = new ArrayList<>();
            for (Field field : dtoClass.getDeclaredFields()) {
                if (field.getName().equals("user")) {
                    Class<?> userDtoClass = Arrays.stream(dtoClass.getRecordComponents()).
                            filter(x -> x.getName().contains("user")).findFirst().get().getType();
                    constructorArgs.add(userMapper.toDto(group.getUser(), userDtoClass));
                } else if (field.getName().equals("contacts")) {
                    Class<?> contactDtoClass = findGenericTypeFromField(field);
                    Method method = Group.class.getMethod("get" + capitalize(field.getName()));
                    List<?> contacts = (List<?>) method.invoke(group);
                    if (contacts == null) {
                        constructorArgs.add(new ArrayList<>());
                    } else {
                        constructorArgs.add(contacts.stream()
                                .map(x -> contactMapper.toDto((Contact) x, contactDtoClass))
                                .collect(Collectors.toList()));
                    }
                } else {
                    Method method;
                    if (field.getType().equals(Boolean.class)) {
                        method = Group.class.getMethod("get" + field.getName().substring(2));
                    } else {
                        method = Group.class.getMethod("get" + capitalize(field.getName()));
                    }
                    constructorArgs.add(method.invoke(group));
                }
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

    public Group toEntity(Object dto) {
        try {
            Group group = new Group();
            for (Field field : dto.getClass().getDeclaredFields()) {
                Method recordMethod = dto.getClass().getMethod(field.getName());
                if (field.getName().equals("user")) {
                    Method setMethod = Group.class.getMethod("set" + capitalize(field.getName()), User.class);
                    setMethod.invoke(group, userMapper.toEntity(recordMethod.invoke(dto)));
                } else {
                    Method setMethod;
                    if (field.getType().equals(Boolean.class)) {
                        setMethod =
                                Group.class.getMethod("set" + field.getName().substring(2), field.getType());
                    } else {
                        setMethod =
                                Group.class.getMethod("set" + capitalize(field.getName()), field.getType());
                    }
                    setMethod.invoke(group, recordMethod.invoke(dto));
                }
            }
            return group;
        } catch (NoSuchMethodException
                 | IllegalAccessException
                 | InvocationTargetException e) {
            throw new MappingException(e);
        }
    }

    private Class<?> findGenericTypeFromField(Field field) {
        try {
            return Class.forName(field.getGenericType().getTypeName().substring(List.class.getName().length() + 1,
                    field.getGenericType().getTypeName().length() - 1));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
