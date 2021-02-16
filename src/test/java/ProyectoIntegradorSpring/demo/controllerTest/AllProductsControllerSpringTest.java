package ProyectoIntegradorSpring.demo.controllerTest;

import ProyectoIntegradorSpring.demo.DAO.RepositoryDAO;
import ProyectoIntegradorSpring.demo.DTO.ArticlesDTO;
import ProyectoIntegradorSpring.demo.TestUtils.TestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
public class AllProductsControllerSpringTest {


    @MockBean
    private RepositoryDAO repositoryDAO;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSearchAllArticles() throws Exception {
        List<ArticlesDTO> articles = TestUtils.createArticles("src/main/java/ProyectoIntegradorSpring/demo/Model/database.json");
        Mockito.when(repositoryDAO.returnFilterProducts(new HashMap<>())).thenReturn(articles);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/articles"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json( TestUtils.convertObjectAsString( articles )));
    }
}