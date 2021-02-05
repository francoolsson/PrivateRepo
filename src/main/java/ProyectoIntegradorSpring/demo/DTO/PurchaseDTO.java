package ProyectoIntegradorSpring.demo.DTO;

import java.util.List;

public class PurchaseDTO {

    private String userName;
    private List<ItemsDTO> articles;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<ItemsDTO> getArticles() {
        return articles;
    }

    public void setArticles(List<ItemsDTO> articles) {
        this.articles = articles;
    }
}
