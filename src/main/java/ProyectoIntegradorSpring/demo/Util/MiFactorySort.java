package ProyectoIntegradorSpring.demo.Util;

import ProyectoIntegradorSpring.demo.Exceptions.BadSortPropertiesException;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class MiFactorySort {

    private Properties properties = new Properties();
    private Class cls;
    public Sorter setProperties(){
        try {
            properties.load( new FileInputStream( new File( "src/main/java/ProyectoIntegradorSpring/demo/Util/MiFactorySort.properties" ) ) );
            cls = Class.forName( properties.get( "sorter" ).toString() );
            return (Sorter) cls.getDeclaredConstructor().newInstance();
        }catch(Exception e){throw new BadSortPropertiesException("Bad Sort Configuration");}
    }
}

