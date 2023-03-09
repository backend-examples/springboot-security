package impl;

import com.domain.pojo.Role;
import com.domain.vo.RegistrationForm;
import mapper.RoleRepository;
import mapper.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterImpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean registerUser (RegistrationForm form) {

        Role role = roleRepository.findRoleById(form.getRole());

        if (role != null) {
            userRepository.saveWithoutId(form.toUser(passwordEncoder, role.getRoleName()));
        } else {
            userRepository.saveWithoutId(form.toUser(passwordEncoder, null));
        }

        return true;
    }
}
