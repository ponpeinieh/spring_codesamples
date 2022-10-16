This is Spring demo from Udemy Spring & Hibernate course. 
This is extra sample using Spring Boot + Spring Security with customized User table and User registration function

- Note that this sample still use the deprecated `WebSecurityConfigurerAdapter`, so it configures a `AuthenticationManagerBuilder` object and pass in the new `authenticationProvider`, which uses the custom `UserDetailsService` implementation.
- The `UserDetailsService` implementation is the spirit of this sample, since it allows us to use customized user/role table schema.
- For using new component-based config style, we simply autowired the UserDetailsService implementation (@Service annotation will do the job!), of course it needs the passwordEncoder object (eg. BCryptPasswordEncoder) which is autowired too.
- In Spring Boot, there is no need to config authenticationProvider, just also autowires the UserDetailsService implementation. 
    - Just a reminder here, acutally the `HttpSercurity` object has `authenticationProvider​()` and `userDetailsService()` methods, which you can use to pass in these needed implementation objects.
    (I haven't test, but it should also do the job.)

- First the security config 
    - note the `.successHandler(customAuthenticationSuccessHandler)` to register a handler after successful login
        - store the user object in the session

    - DemoSecurityConfig

    ```
    package com.luv2code.springsecurity.demo.config;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

    import com.luv2code.springsecurity.demo.service.UserService;

    @Configuration
    @EnableWebSecurity
    public class DemoSecurityConfig extends WebSecurityConfigurerAdapter {

        // add a reference to our user service
        @Autowired
        private UserService userService;
        
        @Autowired
        private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
        
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(authenticationProvider());
        }
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http.authorizeRequests()
                .antMatchers("/").hasRole("EMPLOYEE")
                .antMatchers("/leaders/**").hasRole("MANAGER")
                .antMatchers("/systems/**").hasRole("ADMIN")
                .and()
                .formLogin()
                    .loginPage("/showMyLoginPage")
                    .loginProcessingUrl("/authenticateTheUser")
                    .successHandler(customAuthenticationSuccessHandler)
                    .permitAll()
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/access-denied");
            
        }
        
        //beans
        //bcrypt bean definition
        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        //authenticationProvider bean definition
        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
            auth.setUserDetailsService(userService); //set the custom user details service
            auth.setPasswordEncoder(passwordEncoder()); //set the password encoder - bcrypt
            return auth;
        }
        
    }
    ```
    - CustomAuthenticationSuccessHandler

    ```
    package com.luv2code.springsecurity.demo.config;

    import java.io.IOException;

    import javax.servlet.ServletException;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import javax.servlet.http.HttpSession;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
    import org.springframework.stereotype.Component;

    import com.luv2code.springsecurity.demo.entity.User;
    import com.luv2code.springsecurity.demo.service.UserService;

    @Component
    public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

        @Autowired
        private UserService userService;
        
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
                throws IOException, ServletException {

            System.out.println("\n\nIn customAuthenticationSuccessHandler\n\n");

            String userName = authentication.getName();
            
            System.out.println("userName=" + userName);

            User theUser = userService.findByUserName(userName);
            
            // now place in the session
            HttpSession session = request.getSession();
            session.setAttribute("user", theUser);
            
            // forward to home page
            
            response.sendRedirect(request.getContextPath() + "/");
        }

    }

    ```

- Entities:
    - User Entity

    ```
    package com.luv2code.springsecurity.demo.entity;

    import javax.persistence.*;
    import java.util.Collection;

    @Entity
    @Table(name = "user")
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Column(name = "username")
        private String userName;

        @Column(name = "password")
        private String password;

        @Column(name = "first_name")
        private String firstName;

        @Column(name = "last_name")
        private String lastName;

        @Column(name = "email")
        private String email;

        @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinTable(name = "users_roles", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "role_id"))
        private Collection<Role> roles;

        public User() {
        }

        public User(String userName, String password, String firstName, String lastName, String email) {
            this.userName = userName;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }
        //and getters and setters
    ```

    - Role Entity

    ```
    package com.luv2code.springsecurity.demo.entity;

    import javax.persistence.Column;
    import javax.persistence.Entity;
    import javax.persistence.GeneratedValue;
    import javax.persistence.GenerationType;
    import javax.persistence.Id;
    import javax.persistence.Table;

    @Entity
    @Table(name = "role")
    public class Role {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Column(name = "name")
        private String name;

        public Role() {
        }

        public Role(String name) {
            this.name = name;
        }

    ```
- DAOs
    - UserDao and RoleDao are the interfaces
    - UserDaoImpl, similar for RoleDaoImpl

    ```
    package com.luv2code.springsecurity.demo.dao;

    import javax.persistence.EntityManager;

    import org.hibernate.Session;
    import org.hibernate.SessionFactory;
    import org.hibernate.query.Query;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Repository;

    import com.luv2code.springsecurity.demo.entity.User;

    @Repository
    public class UserDaoImpl implements UserDao {

        @Autowired
        private EntityManager entityManager;

        @Override
        public User findByUserName(String theUserName) {
            // get the current hibernate session
            Session currentSession = entityManager.unwrap(Session.class);

            // now retrieve/read from database using username
            Query<User> theQuery = currentSession.createQuery("from User where userName=:uName", User.class);
            theQuery.setParameter("uName", theUserName);
            User theUser = null;
            try {
                theUser = theQuery.getSingleResult();
            } catch (Exception e) {
                theUser = null;
            }

            return theUser;
        }

        @Override
        public void save(User theUser) {
            // get current hibernate session
            Session currentSession = entityManager.unwrap(Session.class);

            // create the user ... finally LOL
            currentSession.saveOrUpdate(theUser);
        }

    }
    ```
- Services:
    - UserService interface extends `UserDetailsService` (this interface is defined in Spring Security API)
        - The only method for the implementtation class to override is `public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException` 
        - UserService defines its own methods , ie. findByUserName(), save()
        - loadUserByUsername() return UserDetails object, which is implemented by `org.springframework.security.core.userdetails.User`. 
            - Here we construct the UserDetails object passing in the username, password and a Collection of GrantedAuthority.
            - The constructor can also accept things like `enabled` parameter to handle disabled user.

            ```
            public User​(java.lang.String username,java.lang.String password,
                boolean enabled,
                boolean accountNonExpired,
                boolean credentialsNonExpired,
                boolean accountNonLocked,
                java.util.Collection<? extends GrantedAuthority> authorities)
            ```

    ```
    package com.luv2code.springsecurity.demo.service;

    import com.luv2code.springsecurity.demo.entity.User;
    import com.luv2code.springsecurity.demo.user.CrmUser;
    import org.springframework.security.core.userdetails.UserDetailsService;

    public interface UserService extends UserDetailsService {

        public User findByUserName(String userName);

        public void save(CrmUser crmUser);
    }
    ```

    - Service implementation class
        - `BCryptPasswordEncoder` is the BCrypt encryptor

    ```
    package com.luv2code.springsecurity.demo.service;

    import com.luv2code.springsecurity.demo.dao.RoleDao;
    import com.luv2code.springsecurity.demo.dao.UserDao;
    import com.luv2code.springsecurity.demo.entity.Role;
    import com.luv2code.springsecurity.demo.entity.User;
    import com.luv2code.springsecurity.demo.user.CrmUser;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.util.Arrays;
    import java.util.Collection;
    import java.util.stream.Collectors;

    @Service
    public class UserServiceImpl implements UserService {

        // need to inject user dao
        @Autowired
        private UserDao userDao;

        @Autowired
        private RoleDao roleDao;
        
        @Autowired
        private BCryptPasswordEncoder passwordEncoder;

        @Override
        @Transactional
        public User findByUserName(String userName) {
            // check the database if the user already exists
            return userDao.findByUserName(userName);
        }

        @Override
        @Transactional
        public void save(CrmUser crmUser) {
            User user = new User();
            // assign user details to the user object
            user.setUserName(crmUser.getUserName());
            user.setPassword(passwordEncoder.encode(crmUser.getPassword()));
            user.setFirstName(crmUser.getFirstName());
            user.setLastName(crmUser.getLastName());
            user.setEmail(crmUser.getEmail());

            // give user default role of "employee"
            user.setRoles(Arrays.asList(roleDao.findRoleByName("ROLE_EMPLOYEE")));

            // save user in the database
            userDao.save(user);
        }

        @Override
        @Transactional
        public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
            User user = userDao.findByUserName(userName);
            if (user == null) {
                throw new UsernameNotFoundException("Invalid username or password.");
            }
            return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
                    mapRolesToAuthorities(user.getRoles()));
        }

        private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
            return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        }
    }
    ```

- For doing user registration, we create CrmUser class and corresponding validation class.

    ```
    package com.luv2code.springsecurity.demo.user;

    import javax.validation.constraints.NotNull;
    import javax.validation.constraints.Size;

    import com.luv2code.springsecurity.demo.validation.FieldMatch;
    import com.luv2code.springsecurity.demo.validation.ValidEmail;

    @FieldMatch.List({
        @FieldMatch(first = "password", second = "matchingPassword", message = "The password fields must match")
    })
    public class CrmUser {

        @NotNull(message = "is required")
        @Size(min = 1, message = "is required")
        private String userName;

        @NotNull(message = "is required")
        @Size(min = 1, message = "is required")
        private String password;
        
        @NotNull(message = "is required")
        @Size(min = 1, message = "is required")
        private String matchingPassword;

        @NotNull(message = "is required")
        @Size(min = 1, message = "is required")
        private String firstName;

        @NotNull(message = "is required")
        @Size(min = 1, message = "is required")
        private String lastName;

        @ValidEmail
        @NotNull(message = "is required")
        @Size(min = 1, message = "is required")
        private String email;

        public CrmUser() {

        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getMatchingPassword() {
            return matchingPassword;
        }

        public void setMatchingPassword(String matchingPassword) {
            this.matchingPassword = matchingPassword;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

    }

    ```
- The controller

    ```
    package com.luv2code.springsecurity.demo.controller;

    import java.util.logging.Logger;

    import javax.validation.Valid;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.propertyeditors.StringTrimmerEditor;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.validation.BindingResult;
    import org.springframework.web.bind.WebDataBinder;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.InitBinder;
    import org.springframework.web.bind.annotation.ModelAttribute;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestMapping;

    import com.luv2code.springsecurity.demo.user.CrmUser;
    import com.luv2code.springsecurity.demo.entity.User;
    import com.luv2code.springsecurity.demo.service.UserService;

    @Controller
    @RequestMapping("/register")
    public class RegistrationController {
        
        @Autowired
        private UserService userService;
        
        private Logger logger = Logger.getLogger(getClass().getName());
        
        @InitBinder
        public void initBinder(WebDataBinder dataBinder) {
            
            StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
            
            dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
        }	
        
        @GetMapping("/showRegistrationForm")
        public String showMyLoginPage(Model theModel) {
            
            theModel.addAttribute("crmUser", new CrmUser());
            
            return "registration-form";
        }

        @PostMapping("/processRegistrationForm")
        public String processRegistrationForm(
                    @Valid @ModelAttribute("crmUser") CrmUser theCrmUser, 
                    BindingResult theBindingResult, 
                    Model theModel) {
            
            String userName = theCrmUser.getUserName();
            logger.info("Processing registration form for: " + userName);
            
            // form validation
            if (theBindingResult.hasErrors()){
                return "registration-form";
                }

            // check the database if user already exists
            User existing = userService.findByUserName(userName);
            if (existing != null){
                theModel.addAttribute("crmUser", new CrmUser());
                theModel.addAttribute("registrationError", "User name already exists.");

                logger.warning("User name already exists.");
                return "registration-form";
            }
            
            // create user account        						
            userService.save(theCrmUser);
            
            logger.info("Successfully created user: " + userName);
            
            return "registration-confirmation";		
        }
    }

    ```