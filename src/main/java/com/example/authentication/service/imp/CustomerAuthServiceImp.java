package com.example.authentication.service.imp;

import com.example.authentication.auth.JwtTokenUtil;
import com.example.authentication.model.CustomerRegistrationModel;
import com.example.authentication.model.LoginModel;
import com.example.authentication.model.ResponseModel;
import com.example.authentication.repository.*;
import com.example.authentication.service.CustomerAuthService;
import com.example.authentication.util.CommanUtil;
import com.example.authentication.util.FileUpload;
import com.example.authentication.util.Message;
import com.example.commanentity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerAuthServiceImp implements CustomerAuthService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerAuthServiceImp.class);
    @Value("${const.path}")
    private String folder;


    @Autowired
    private CommanUtil commanUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private CustomerAddressRepository customerAddressRepository;
    @Autowired
    private HobbyRepository hobbyRepository;
    @Autowired
    private CustomerHobbyRepository customerHobbyRepository;

    @Override
    public ResponseModel login(LoginModel loginModel) {
        ResponseModel responseModel;

        try {
            //Load the userdetail of the user where emailID = "Example@gmail.com"
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginModel.getEmailId(),
                            loginModel.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            Customer customer = customerRepository.findByEmailid(loginModel.getEmailId());
            final String token = jwtTokenUtil.generateToken(authentication);

            //set the Bearer token to AdminUser data
            customer.setSession_token(token);
            //update the adminuser to database with token And ExpirationDate
            customerRepository.save(customer);

            responseModel = commanUtil.create(Message.LOGIN_SUCCESS,
                    customer,
                    HttpStatus.OK);

        } catch (BadCredentialsException ex) {
            logger.info("Customer login BadCredentialsException ================== {}", ex.getMessage());
            responseModel = commanUtil.create(Message.LOGIN_ERROR,
                    null,
                    HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            logger.info("Customer Login Exception ================== {}", e.getMessage());
            responseModel = commanUtil.create(Message.DELETE_STATUS_ERROR,
                    null,
                    HttpStatus.BAD_REQUEST);
        }
        return responseModel;
    }

    @Override
    public ResponseModel registration(CustomerRegistrationModel model) {

//             check if user exist with email or username
        int exist = customerRepository.countByEmailid(model.getEmailId());
        if (exist == 0) {
            
            try {
//            get the Customer Object From DTO
                Customer customer = model.getCustomerFromModel();
                customer.setPassword(passwordEncoder.encode(model.getPassword()));
                String str = null;

//            Upload the image
                if (model.getProfileURL() != null)
                    str = new FileUpload().saveFile(folder, model.getProfileURL(), customer.getId());

                customer.setProfile_url(str);

                customerRepository.save(customer);

                insertAddress(customer,model);
                insertHobby(customer, model.getHobby());

                return commanUtil.create(Message.CUSTOMER_REGISTER, null, HttpStatus.OK);

            } catch (Exception e) {
                logger.error("Error Will Registration");
                e.printStackTrace();
                return commanUtil.create(Message.SOMTHING_WRONG, null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } else {
            return commanUtil.create(Message.EMAIL_EXIST, null, HttpStatus.BAD_REQUEST);
        }
    }

    private void insertHobby(Customer customer, List<String> hobby) {
        hobby.forEach(h -> {
            Customer_Hobby customerHobby = new Customer_Hobby();
            customerHobby.setCustomer(customer);
            customerHobby.setHobby(hobbyRepository.findByName(h));
            customerHobbyRepository.save(customerHobby);
        });
    }

    private void insertAddress(Customer customer, CustomerRegistrationModel model) {

//        Get CustomerAddress object From DTO
        Customer_Address customerAddress = model.getCustomerAddressFromModel();
        customerAddress.setCustomer(customer);
//            Fill the Country and State
//                Country country = countryRepository.findByName(model.getCountry());
//                if (country != null)
//                    customerAddress.setCountry(country);
//                State state = stateRepository.findByName(model.getState());
//                if (state != null)
//                    customerAddress.setState(state);

        customerAddressRepository.save(customerAddress);
    }
}
