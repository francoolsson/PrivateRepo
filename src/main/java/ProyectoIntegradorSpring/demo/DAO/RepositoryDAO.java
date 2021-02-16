package ProyectoIntegradorSpring.demo.DAO;

import ProyectoIntegradorSpring.demo.DTO.ArticlesDTO;
import ProyectoIntegradorSpring.demo.DTO.ReceiptDTO;
import ProyectoIntegradorSpring.demo.DTO.UserDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface RepositoryDAO {

    Boolean isAttribute (Map<String,String> filters);
    List<ArticlesDTO> returnFilterProducts(Map<String,String> filters);
    Integer newReceiptID ();
    void loadReceiptDatabase(ReceiptDTO receiptDTO);
    List<ReceiptDTO> getReceipts(String user);
    List<ReceiptDTO> getAllReceipts();
    Integer newUserID ();
    void loadUserDatabase(UserDTO userDTO);
    List<UserDTO> getAllUsers();
    Boolean isUser (String user);
    List<UserDTO> filterUsers(UserDTO userDTO);


}
