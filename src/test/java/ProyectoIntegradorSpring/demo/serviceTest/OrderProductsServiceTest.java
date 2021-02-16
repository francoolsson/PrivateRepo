package ProyectoIntegradorSpring.demo.serviceTest;


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
public class OrderProductsServiceTest {

    @Mock
    RepositoryDAO repositoryDAO;

    SearchEngine searchEngine;

    @BeforeEach
    void setUp(){
        searchEngine = new SearchEngineImpl(repositoryDAO);
    }

    @Test
    void orderProductTest() throws Exception{

        Mockito.when( repositoryDAO.returnFilterProducts( new HashMap<>() ) ).thenReturn( TestUtils.createArticles( "src/main/java/ProyectoIntegradorSpring/demo/Model/database.json" ));
        Mockito.when( repositoryDAO.isAttribute( new HashMap<>() ) ).thenReturn( true );
        Map<String,String> orderMap=new HashMap<>();

        orderMap.put( "order","0" );
        List<ArticlesDTO> results= searchEngine.filterProductsService( orderMap );
        List<ArticlesDTO> resultsToCompare= TestUtils.createArticles( "src/test/java/ProyectoIntegradorSpring/demo/TestUtils/order0.json" );
        resultsToCompare.stream().forEach( u-> u.setId( null ) );
        Assertions.assertEquals( TestUtils.convertObjectAsString( results ),
                TestUtils.convertObjectAsString(resultsToCompare ));

        orderMap.put( "order","1" );
        results= searchEngine.filterProductsService( orderMap );
        resultsToCompare= TestUtils.createArticles( "src/test/java/ProyectoIntegradorSpring/demo/TestUtils/order1.json" );
        resultsToCompare.stream().forEach( u-> u.setId( null ) );
        Assertions.assertEquals( TestUtils.convertObjectAsString( results ),
                TestUtils.convertObjectAsString(resultsToCompare ));


        orderMap.put( "order","2" );
        results= searchEngine.filterProductsService( orderMap );
        resultsToCompare= TestUtils.createArticles( "src/test/java/ProyectoIntegradorSpring/demo/TestUtils/order2.json" );
        resultsToCompare.stream().forEach( u-> u.setId( null ) );
        Assertions.assertEquals( TestUtils.convertObjectAsString( results ),
                TestUtils.convertObjectAsString(resultsToCompare ));

        orderMap.put( "order","3" );
        results= searchEngine.filterProductsService( orderMap );
        resultsToCompare= TestUtils.createArticles( "src/test/java/ProyectoIntegradorSpring/demo/TestUtils/order3.json" );
        resultsToCompare.stream().forEach( u-> u.setId( null ) );
        Assertions.assertEquals( TestUtils.convertObjectAsString( results ),
                TestUtils.convertObjectAsString(resultsToCompare ));





    }
}
