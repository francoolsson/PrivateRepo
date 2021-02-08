package ProyectoIntegradorSpring.demo.Model;

import ProyectoIntegradorSpring.demo.DTO.UserDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public class UserFilterFactoryImpl implements UserFilterFactory{

    public Predicate<UserDTO> getUsersFilters(UserDTO userDTO){

        return generateFilters( userDTO ).stream().reduce( a->true, Predicate::and );

    }

    private List<Predicate<UserDTO>> generateFilters (UserDTO userDTO){
        List<Predicate<UserDTO>> filters = new ArrayList<>();

        if (userDTO.getUser() != null)
            filters.add(p->p.getUser().matches( userDTO.getUser() ));

        if (userDTO.getId() != null)
            filters.add(p->p.getId()==userDTO.getId());

        if (userDTO.getName() != null)
            filters.add(p->p.getName().toLowerCase( Locale.ROOT ).matches( userDTO.getName().toLowerCase( Locale.ROOT ) ));

        if (userDTO.getState() != null)
            filters.add(p->p.getState().toLowerCase( Locale.ROOT ).matches( userDTO.getState().toLowerCase( Locale.ROOT ) ));

        //if (userDTO.getCity() != null)
        //    filters.add( p->p.getCity().toLowerCase( Locale.ROOT ) .matches( userDTO.getCity().toLowerCase( Locale.ROOT ) ));

        //if (userDTO.getMail() != null)
        //    filters.add( p-> p.getMail().toLowerCase( Locale.ROOT ).matches( userDTO.getMail().toLowerCase( Locale.ROOT ) ) );


        return filters;

    }



}
