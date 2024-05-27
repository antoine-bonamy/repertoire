package fr.bonamy.repertoire_back.mapper;

import fr.bonamy.repertoire_back.exception.MappingException;
import fr.bonamy.repertoire_back.model.Organization;
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

import static fr.bonamy.repertoire_back.util.Capitalize.capitalize;

@Component
public class OrganizationMapper {

    private final UserMapper userMapper;

    @Autowired
    public OrganizationMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public <T> T toDto(Organization organization, Class<T> dtoClass) {
        try {
            List<Object> constructorArgs = new ArrayList<>();
            for (Field field : dtoClass.getDeclaredFields()) {
                if (field.getName().equals("user") && organization.getUser() != null) {
                    Class<?> userDtoClass = Arrays.stream(dtoClass.getRecordComponents()).
                            filter(x -> x.getName().contains("user")).findFirst().get().getType();
                    constructorArgs.add(userMapper.toDto(organization.getUser(), userDtoClass));
                } else {
                    Method method;
                    if (field.getType().equals(Boolean.class)) {
                        method = Organization.class.getMethod("get" + field.getName().substring(2));
                    } else {
                        method = Organization.class.getMethod("get" + capitalize(field.getName()));
                    }
                    constructorArgs.add(method.invoke(organization));
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

    public Organization toEntity(Object dto) {
        try {
            Organization organization = new Organization();
            for (Field field : dto.getClass().getDeclaredFields()) {
                Method recordMethod = dto.getClass().getMethod(field.getName());
                if (field.getName().equals("user")) {
                    Method setMethod = Organization.class.getMethod("set" + capitalize(field.getName()), User.class);
                    setMethod.invoke(organization, userMapper.toEntity(recordMethod.invoke(dto)));
                } else {
                    Method setMethod;
                    if (field.getType().equals(Boolean.class)) {
                        setMethod =
                                Organization.class.getMethod("set" + field.getName().substring(2), field.getType());
                    } else {
                        setMethod =
                                Organization.class.getMethod("set" + capitalize(field.getName()), field.getType());
                    }
                    setMethod.invoke(organization, recordMethod.invoke(dto));

                }
            }
            return organization;
        } catch (NoSuchMethodException
                 | IllegalAccessException
                 | InvocationTargetException e) {
            throw new MappingException(e);
        }
    }

}