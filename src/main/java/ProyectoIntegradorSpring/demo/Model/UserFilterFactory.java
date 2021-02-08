package ProyectoIntegradorSpring.demo.Model;

import ProyectoIntegradorSpring.demo.DTO.UserDTO;

import java.util.function.Predicate;

public interface UserFilterFactory {

    Predicate<UserDTO> getUsersFilters(UserDTO userDTO);

}
