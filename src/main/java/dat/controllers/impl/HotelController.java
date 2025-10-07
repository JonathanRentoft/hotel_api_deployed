package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.HotelDAO;
import dat.dtos.HotelDTO;
import dat.entities.Hotel;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HotelController implements IController<HotelDTO, Integer> {

    private final HotelDAO dao;

    public HotelController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = HotelDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx)  {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // DTO
        HotelDTO hotelDTO = dao.read(id);
        // response
        ctx.res().setStatus(200);
        ctx.json(hotelDTO, HotelDTO.class);
    }

    @Override
    public void readAll(Context ctx) {
        // List of DTOS
        List<HotelDTO> hotelDTOS = dao.readAll();
        // response
        ctx.res().setStatus(200);
        ctx.json(hotelDTOS, HotelDTO.class);
    }

    @Override
    public void create(Context ctx) {
        // request
        HotelDTO jsonRequest = ctx.bodyAsClass(HotelDTO.class);
        // DTO
        HotelDTO hotelDTO = dao.create(jsonRequest);
        // response
        ctx.res().setStatus(201);
        ctx.json(hotelDTO, HotelDTO.class);
    }

    @Override
    public void update(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // dto
        HotelDTO hotelDTO = dao.update(id, validateEntity(ctx));
        // response
        ctx.res().setStatus(200);
        ctx.json(hotelDTO, Hotel.class);
    }

    @Override
    public void delete(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        dao.delete(id);
        // response
        ctx.res().setStatus(204);
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        return dao.validatePrimaryKey(integer);
    }

    @Override
    public HotelDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(HotelDTO.class)
                .check( h -> h.getHotelAddress() != null && !h.getHotelAddress().isEmpty(), "Hotel address must be set")
                .check( h -> h.getHotelName() != null && !h.getHotelName().isEmpty(), "Hotel name must be set")
                .check( h -> h.getHotelType() != null, "Hotel type must be set")
                .get();
    }

    public void populate(Context ctx) {
        dao.populate();
        ctx.res().setStatus(200);
        ctx.json("{ \"message\": \"Database has been populated\" }");
    }
}

