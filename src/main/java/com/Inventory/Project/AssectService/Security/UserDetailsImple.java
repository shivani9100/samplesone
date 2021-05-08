package com.Inventory.Project.AssectService.Security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.Inventory.Project.AssectService.Employee.Employee;
import com.Inventory.Project.AssectService.Employee.EmployeeRepository;
import com.Inventory.Project.AssectService.Employee.Role;

@Service
public class UserDetailsImple implements UserDetailsService {

	@Autowired
	private EmployeeRepository userRepository;

	@Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       
        Employee editProfile = userRepository.findByEmail(email);
        //Set<com.Inventory.Project.AssectService.Employee.Role> roles = editProfile.getRoles();

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : editProfile.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new User(editProfile.getEmail(),
                editProfile.getPassword(), grantedAuthorities);
    }

}
