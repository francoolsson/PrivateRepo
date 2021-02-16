package ProyectoIntegradorSpring.demo.controllerTest;

import ProyectoIntegradorSpring.demo.Controllers.RestController;
import ProyectoIntegradorSpring.demo.DTO.ArticlesDTO;
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


@SpringBootTest
class AllProductsControllerMockTest {

	@Mock
	private SearchEngine searchEngine;

	RestController restController;

	@BeforeEach
	void setUp(){
		restController= new RestController( searchEngine );
	}


	@Test
	void shouldGetAllProducts(){
		List<ArticlesDTO> articlesDTOList = TestUtils.articlesDTOList();

		Mockito.when( searchEngine.allProductsService() ).thenReturn( articlesDTOList );

		List<ArticlesDTO> returnedArticles = restController.getProducts(new HashMap<>());

		Assertions.assertEquals( returnedArticles, articlesDTOList);
	}



}
