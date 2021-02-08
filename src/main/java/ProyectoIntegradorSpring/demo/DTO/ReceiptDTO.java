package ProyectoIntegradorSpring.demo.DTO;

import java.util.List;

public class ReceiptDTO{

    private Integer id;
    private String user;
    private String status;
    private Integer price;
    private List<ArticlesDTO> articles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<ArticlesDTO> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticlesDTO> articles) {
        this.articles = articles;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
