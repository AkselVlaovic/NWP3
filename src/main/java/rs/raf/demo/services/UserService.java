package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Authority;
import rs.raf.demo.model.Roles;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IService<User, Long>, UserDetailsService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> optionalUser = this.userRepository.findByEmail(email);

        if(!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("User with email "+ email +" not found");
        }

        User myUser = optionalUser.get();

        return new org.springframework.security.core.userdetails.User(myUser.getEmail(), myUser.getPassword(),getUserAuthorities(myUser));
    }

    public List<Authority> getUserAuthorities(User user){

        List<Authority> authorities = new ArrayList<>();
        Roles roles = user.getRoles();

        if(roles.getCan_create_users())
            authorities.add(new Authority("can_create_users"));

        if(roles.getCan_read_users())
            authorities.add(new Authority("can_read_users"));

        if(roles.getCan_update_users())
            authorities.add(new Authority("can_update_users"));

        if(roles.getCan_delete_users())
            authorities.add(new Authority("can_delete_users"));

        return authorities;

    }

    @Override
    public <S extends User> S save(S user) {
        return userRepository.save(user);
    }

    public Optional<User> updateUser(User user, Long id) {

        Optional<User> userOptional = userRepository.findById(id);

        if(!userOptional.isPresent())
            return userOptional;

        User returnedUser = userOptional.get();

        returnedUser.setName(user.getName());
        returnedUser.setSurname(user.getSurname());
        returnedUser.setEmail(user.getEmail());

        returnedUser.getRoles().setCan_read_users(user.getRoles().getCan_read_users());
        returnedUser.getRoles().setCan_create_users(user.getRoles().getCan_create_users());
        returnedUser.getRoles().setCan_update_users(user.getRoles().getCan_update_users());
        returnedUser.getRoles().setCan_delete_users(user.getRoles().getCan_delete_users());

        return Optional.ofNullable(userRepository.save(returnedUser));

    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}
