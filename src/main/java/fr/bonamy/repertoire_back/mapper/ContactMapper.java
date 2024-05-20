package fr.bonamy.repertoire_back.mapper;

import fr.bonamy.repertoire_back.exception.MappingException;
import fr.bonamy.repertoire_back.model.Contact;
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
public class ContactMapper {

    private final OrganizationMapper organizationMapper;
    private final UserMapper userMapper;

    @Autowired
    public ContactMapper(OrganizationMapper organizationMapper, UserMapper userMapper) {
        this.organizationMapper = organizationMapper;
        this.userMapper = userMapper;
    }


    public <T> T toDto(Contact contact, Class<T> dtoClass) {
        try {
            List<Object> constructorArgs = new ArrayList<>();
            for (Field field : dtoClass.getDeclaredFields()) {
                if (field.getName().equals("user")) {
                    Class<?> userDtoClass = Arrays.stream(dtoClass.getRecordComponents()).
                            filter(x -> x.getName().contains("user")).findFirst().get().getType();
                    constructorArgs.add(userMapper.toDto(contact.getUser(), userDtoClass));
                } else if (field.getName().equals("organization")) {
                    Class<?> organizationDtoClass = Arrays.stream(dtoClass.getRecordComponents()).
                            filter(x -> x.getName().contains("organization")).findFirst().get().getType();
                    constructorArgs.add(organizationMapper.toDto(contact.getOrganization(), organizationDtoClass));
                } else {
                    Method method;
                    if (field.getType().equals(Boolean.class)) {
                        method = Contact.class.getMethod("get" + field.getName().substring(2));
                    } else {
                        method = Contact.class.getMethod("get" + capitalize(field.getName()));
                    }
                    constructorArgs.add(method.invoke(contact));
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

    public Contact toEntity(Object dto) {
        try {
            Contact contact = new Contact();
            for (Field field : dto.getClass().getDeclaredFields()) {
                Method recordMethod = dto.getClass().getMethod(field.getName());
                if (field.getName().equals("user")) {
                    Method setMethod = Contact.class.getMethod("set" + capitalize(field.getName()), User.class);
                    setMethod.invoke(contact, userMapper.toEntity(recordMethod.invoke(dto)));
                } else if (field.getName().equals("organization")) {
                    Method setMethod = Contact.class.getMethod("set" + capitalize(field.getName()), Organization.class);
                    setMethod.invoke(contact, organizationMapper.toEntity(recordMethod.invoke(dto)));
                } else {
                    Method setMethod;
                    if (field.getType().equals(Boolean.class)) {
                        setMethod =
                                Contact.class.getMethod("set" + field.getName().substring(2), field.getType());
                    } else {
                        setMethod =
                                Contact.class.getMethod("set" + capitalize(field.getName()), field.getType());
                    }
                    setMethod.invoke(contact, recordMethod.invoke(dto));

                }
            }
            return contact;
        } catch (NoSuchMethodException
                 | IllegalAccessException
                 | InvocationTargetException e) {
            throw new MappingException(e);
        }
    }

}
