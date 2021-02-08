package ProyectoIntegradorSpring.demo.DAO;

import ProyectoIntegradorSpring.demo.DTO.ArticlesDTO;
import ProyectoIntegradorSpring.demo.DTO.ReceiptDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface Repository {

    Boolean isAttribute (Map<String,String> filters);
    List<ArticlesDTO> returnFilterProducts(Map<String,String> filters);
    Integer newReceiptID ();
    void loadReceiptDatabase(ReceiptDTO receiptDTO);
    List<ReceiptDTO> getReceipts(String user);
}
