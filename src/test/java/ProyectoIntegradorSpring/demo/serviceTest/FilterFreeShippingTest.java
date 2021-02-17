package ProyectoIntegradorSpring.demo.serviceTest;


import ProyectoIntegradorSpring.demo.DAO.Impl.RepositoryDAOImpl;
import ProyectoIntegradorSpring.demo.DAO.RepositoryDAO;
import ProyectoIntegradorSpring.demo.DTO.ArticlesDTO;
import ProyectoIntegradorSpring.demo.Services.Impl.SearchEngineImpl;
import ProyectoIntegradorSpring.demo.Services.SearchEngine;
import ProyectoIntegradorSpring.demo.TestUtils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class FilterFreeShippingTest {

    SearchEngine searchEngine;

    @Mock
    RepositoryDAOImpl repositoryDAO;


    @BeforeEach
    void setUp(){
        searchEngine = new SearchEngineImpl(repositoryDAO);
    }

    @Test
    void FreeShippingTest() throws Exception{


        Map<String,String> filters = new HashMap<>();
        filters.put( "filter","freeShipping" );

        Mockito.when( repositoryDAO.getArticlesDatabase() ).thenReturn(TestUtils.createArticles( "src/test/java/ProyectoIntegradorSpring/demo/TestUtils/ArticlesTest.json" ));
        Mockito.when( repositoryDAO.isAttribute( filters ) ).thenReturn( true );
        Mockito.when( repositoryDAO.returnFilterProducts( filters )).thenCallRealMethod();

        List<ArticlesDTO> results= searchEngine.filterProductsService( filters );
        List<ArticlesDTO> resultsToCompare= TestUtils.createArticles( "src/test/java/ProyectoIntegradorSpring/demo/TestUtils/freeShippingArticlesTest.json" );
        resultsToCompare.stream().forEach( u-> u.setId( null ) );
        Assertions.assertEquals( TestUtils.convertObjectAsString( results ),
                TestUtils.convertObjectAsString(resultsToCompare ));

        for (ArticlesDTO articlesDTO : results){
            System.out.println(articlesDTO.getName());
        }


    }
}
