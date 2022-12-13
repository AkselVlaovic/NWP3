package rs.raf.demo.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.UserRepository;

@Component
public class BootstrapData implements CommandLineRunner {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public BootstrapData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        User u1 = new User();
        u1.setName("Aksel");
        u1.setSurname("Vlaović");
        u1.setEmail("aksel@raf.rs");
        u1.setPassword(passwordEncoder.encode("aksel123"));
        u1.getRoles().setCan_create_users(true);
        u1.getRoles().setCan_read_users(true);
        u1.getRoles().setCan_update_users(true);
        u1.getRoles().setCan_delete_users(true);

        User u2 = new User();
        u2.setName("Marija");
        u2.setSurname("Antunović");
        u2.setEmail("marija@raf.rs");
        u2.setPassword(passwordEncoder.encode("marija123"));
        u2.getRoles().setCan_read_users(true);
        u2.getRoles().setCan_create_users(true);

        User u3 = new User();
        u3.setName("Janko");
        u3.setSurname("Čepić");
        u3.setEmail("janko@raf.rs");
        u3.setPassword(passwordEncoder.encode("janko123"));
        u3.getRoles().setCan_read_users(true);

        User u4 = new User();
        u4.setName("Ranko");
        u4.setSurname("Ranković");
        u4.setEmail("ranko@raf.rs");
        u4.setPassword(passwordEncoder.encode("ranko123"));

        userRepository.save(u1);
        userRepository.save(u2);
        userRepository.save(u3);
        userRepository.save(u4);


    }

}
