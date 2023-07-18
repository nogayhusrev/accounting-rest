package com.nogayhusrev.accounting_rest.service.impl;


import com.nogayhusrev.accounting_rest.dto.UserDto;
import com.nogayhusrev.accounting_rest.entity.Company;
import com.nogayhusrev.accounting_rest.entity.User;
import com.nogayhusrev.accounting_rest.exception.AccountingProjectException;
import com.nogayhusrev.accounting_rest.mapper.MapperUtil;
import com.nogayhusrev.accounting_rest.repository.UserRepository;
import com.nogayhusrev.accounting_rest.service.SecurityService;
import com.nogayhusrev.accounting_rest.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;
    private final PasswordEncoder passwordEncoder;
    private final SecurityService securityService;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapperUtil, PasswordEncoder passwordEncoder, @Lazy SecurityService securityService) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
        this.passwordEncoder = passwordEncoder;
        this.securityService = securityService;
    }


    @Override
    public UserDto findById(Long userId) throws AccountingProjectException {

        if (isNotRootOrAdmin())
            throw new AccountingProjectException("You Are Not Root Or Admin");


        User user = userRepository.findUserById(userId);

        if (getCurrentUser().getRole().equals("Root"))
            return mapperUtil.convert(user, new UserDto());


        if (user.getCompany().getTitle().equals(getCurrentUser().getCompany().getTitle()))
            return mapperUtil.convert(user, new UserDto());

        throw new AccountingProjectException("User Not Found");


    }

    @Override
    public UserDto getCurrentUser() {
        return securityService.getCurrentUser();
    }


    @Override
    public List<UserDto> findAll() throws AccountingProjectException {

        if (isNotRootOrAdmin())
            throw new AccountingProjectException("You Are Not Root Or Admin");

        List<User> userList;
        if (getCurrentUser().getRole().getDescription().equals("Root")) {
            userList = userRepository.findAllByRole_Description("Admin");
        } else {
            userList = userRepository.findAllByCompany(mapperUtil.convert(securityService.getCurrentUser().getCompany(), new Company()));
        }

        return userList.stream()
                .sorted(Comparator.comparing((User u) -> u.getCompany().getTitle()).thenComparing(u -> u.getRole().getDescription()))
                .map(user -> mapperUtil.convert(user, new UserDto()))
                .map(userDto -> {
                    isOnlyAdmin(userDto);
                    return userDto;
                })
                .collect(Collectors.toList());


    }

    @Override
    public UserDto findByName(String username) throws AccountingProjectException {

        if (isNotRootOrAdmin())
            throw new AccountingProjectException("You Are Not Root Or Admin");

        User user = userRepository.findUserByUsername(username);

        if (getCurrentUser().getRole().equals("Root"))
            return mapperUtil.convert(user, new UserDto());

        if (user.getCompany().getTitle().equals(getCurrentUser().getCompany().getTitle()))
            return mapperUtil.convert(user, new UserDto());

        throw new AccountingProjectException("User Not Found");


    }

    @Override
    public void save(UserDto userDto) throws AccountingProjectException {

        if (isNotRootOrAdmin())
            throw new AccountingProjectException("You Are Not Root Or Admin");

        User user = mapperUtil.convert(userDto, new User());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        if (userRepository.save(user) == null)
            throw new AccountingProjectException("User Not Saved");


        securityService.userCreate(userDto);




    }

    @Override
    public void delete(Long userId) throws AccountingProjectException {

        if (isNotRootOrAdmin())
            throw new AccountingProjectException("You Are Not Root Or Admin");


        UserDto userDto = findById(userId);
        String userName = userDto.getUsername();


        if (userDto.getRole().getDescription().equals("Admin") && adminCount(userDto) == 1)
            throw new AccountingProjectException("USER IS ONLY ADMIN, CANNOT BE DELETED");

        User user = userRepository.findById(userId).get();
        user.setUsername(user.getUsername() + "_" + user.getId() + "_DELETED");


        user.setIsDeleted(true);
        user.setEnabled(false);

        if (userRepository.save(user) == null)
            throw new AccountingProjectException("User Not Deleted From DB");

        securityService.delete(userName);

    }

    @Override
    public void update(UserDto userDto, Long userId) throws AccountingProjectException {

        if (isNotRootOrAdmin())
            throw new AccountingProjectException("You Are Not Root Or Admin");

        User user = userRepository.findUserById(userId);
        userDto.setId(user.getId());

        if (userRepository.save(mapperUtil.convert(userDto, new User())) == null)
            throw new AccountingProjectException("User Not Updated in DB");

        securityService.delete(user.getUsername());
        securityService.userCreate(userDto);


    }

    @Override
    public boolean isExist(UserDto userDto, Long userId) {
        Long idCheck = userRepository.findAll().stream()
                .filter(savedUser -> savedUser.getUsername().equalsIgnoreCase(userDto.getUsername()))
                .filter(savedUser -> savedUser.getId() != userId)
                .count();

        return idCheck > 0;
    }

    @Override
    public boolean isExist(UserDto userDto) {
        return userRepository.findAll().stream()
                .filter(savedUser -> savedUser.getUsername().equalsIgnoreCase(userDto.getUsername()))
                .count() > 0;
    }

    private int adminCount(UserDto userDto) {
        return (int) userRepository.findAllByCompany(mapperUtil.convert(userDto.getCompany(), new Company())).stream()
                .filter(user -> user.getRole().getDescription().equals("Admin"))
                .count();
    }


    private void isOnlyAdmin(UserDto userDto) {
        userDto.setIsOnlyAdmin(userDto.getRole().getDescription().equalsIgnoreCase("Admin") && adminCount(userDto) == 1);
    }

    private boolean isNotRootOrAdmin() {
        return !getCurrentUser().getRole().getDescription().equalsIgnoreCase("Root")
                &&
                !getCurrentUser().getRole().getDescription().equalsIgnoreCase("Admin");
    }

}
