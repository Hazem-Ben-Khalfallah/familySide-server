package com.blacknebula.familySide.authentication;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * @param userDtoMono Mono of userDto
     * @return Mono void
     * @should return 200 status
     * @should return valid error status if an exception has been thrown
     */
    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public Mono<Void> connect(@RequestBody Mono<UserDto> userDtoMono) {
        return userService.signIn(userDtoMono);
    }

}