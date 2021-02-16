package ProyectoIntegradorSpring.demo.TestUtils;

import ProyectoIntegradorSpring.demo.DTO.ArticlesDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static List<ArticlesDTO> articlesDTOList() {

        List<ArticlesDTO> articlesDTOList = new ArrayList<>();
        for (Integer i = 0; i < 4; i++) {
            ArticlesDTO articlesDTO = new ArticlesDTO();
            articlesDTO.setName( i.toString() + "Product" );
            articlesDTOList.add( articlesDTO );
        }
        return articlesDTOList;
    }


    public static List<ArticlesDTO> createArticles(String articlesLocation) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<ArticlesDTO> articleDB = mapper.readValue(new File(articlesLocation), new TypeReference<>() {
            });
            return articleDB;
        } catch (IOException e) {
            return null;
        }
    }


    public static String convertObjectAsString(Object objectToTransform) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(objectToTransform);
        return result;
    }


}