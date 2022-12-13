package rs.raf.demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.Authority;
import rs.raf.demo.model.User;
import rs.raf.demo.services.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService,PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = "/all",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllUsers(){

        boolean authorized = false;
        Collection<? extends GrantedAuthority> grantedAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        for(GrantedAuthority authority : grantedAuthorities) {

            if(authority.getAuthority().equals("can_read_users") || authority.getAuthority().equals("can_create_users") || authority.getAuthority().equals("can_update_users") || authority.getAuthority().equals("can_delete_users"))
                authorized = true;

        }

        if(!authorized)
            return ResponseEntity.status(403).body("User doesn't have permission to read users!");

        return ResponseEntity.ok().body(userService.findAll());

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody User user){

        boolean authorized = false;
        Collection<? extends GrantedAuthority> grantedAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        for(GrantedAuthority authority : grantedAuthorities) {

            if(authority.getAuthority().equals("can_create_users"))
                authorized = true;

        }

        if(!authorized)
            return ResponseEntity.status(403).body("User doesn't have permission to create a user!");

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return ResponseEntity.ok().body(userService.save(user));

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserById(@RequestParam("userId") Long userId){

        boolean authorized = false;
        Collection<? extends GrantedAuthority> grantedAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        for(GrantedAuthority authority : grantedAuthorities) {

            if(authority.getAuthority().equals("can_read_users") || authority.getAuthority().equals("can_create_users") || authority.getAuthority().equals("can_update_users") || authority.getAuthority().equals("can_delete_users"))
                authorized = true;

        }

        if(!authorized)
            return ResponseEntity.status(403).body("User doesn't have permission to read users!");

        Optional<User> optionalUser = userService.findById(userId);

        if(optionalUser.isPresent())
            return ResponseEntity.ok(optionalUser.get());

        return ResponseEntity.notFound().build();

    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@RequestBody User user,@RequestParam("userId") Long userId){

        boolean authorized = false;
        Collection<? extends GrantedAuthority> grantedAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        for(GrantedAuthority authority : grantedAuthorities) {

            if(authority.getAuthority().equals("can_update_users"))
                authorized = true;
        }

        if(!authorized)
            return ResponseEntity.status(403).body("User doesn't have permission to update users!");

        Optional<User> optionalUser = userService.updateUser(user,userId);

        if(!optionalUser.isPresent())
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok().body(optionalUser.get());

    }

    @DeleteMapping( path = "/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id){

        boolean authorized = false;
        Collection<? extends GrantedAuthority> grantedAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        for(GrantedAuthority authority : grantedAuthorities) {

            if(authority.getAuthority().equals("can_delete_users"))
                authorized = true;

        }

        if(!authorized)
            return ResponseEntity.status(403).body("User doesn't have permission to delete users!");

        userService.deleteById(id);

        return ResponseEntity.ok().build();

    }


}
