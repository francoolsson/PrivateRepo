package ProyectoIntegradorSpring.demo.DTO;


import java.util.List;

public class ShoppingCartDTO {

    private String name;
    private Integer price;
    private List<ReceiptDTO> receipts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<ReceiptDTO> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<ReceiptDTO> receipts) {
        this.receipts = receipts;
    }
}
