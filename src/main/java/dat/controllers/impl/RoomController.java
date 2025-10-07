package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.RoomDAO;
import dat.dtos.HotelDTO;
import dat.dtos.RoomDTO;
import dat.exceptions.Message;
import dat.entities.Hotel;
import dat.entities.Room;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.function.BiFunction;

public class RoomController implements IController<RoomDTO, Integer> {

    private RoomDAO dao;

    public RoomController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = RoomDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // entity
        RoomDTO roomDTO = dao.read(id);
        // response
        ctx.res().setStatus(200);
        ctx.json(roomDTO, RoomDTO.class);
    }

    @Override
    public void readAll(Context ctx) {
        // entity
        List<RoomDTO> roomDTOS = dao.readAll();
        // response
        ctx.res().setStatus(200);
        ctx.json(roomDTOS, RoomDTO.class);
    }

    @Override
    public void create(Context ctx) {
        // request
        RoomDTO jsonRequest = validateEntity(ctx);

        int hotelId = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        Boolean hasRoom = validateHotelRoomNumber.apply(jsonRequest.getRoomNumber(), hotelId);

        if (hasRoom) {
            ctx.res().setStatus(400);
            ctx.json(new Message(400, "Room number already in use by hotel"));
            return;
        }

        HotelDTO hotelDTO = dao.addRoomToHotel(hotelId, jsonRequest);
        // response
        ctx.res().setStatus(201);
        ctx.json(hotelDTO, HotelDTO.class);
    }

    @Override
    public void update(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // entity
        RoomDTO roomDTO = dao.update(id, validateEntity(ctx));
        // response
        ctx.res().setStatus(200);
        ctx.json(roomDTO, RoomDTO.class);
    }

    @Override
    public void delete(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // entity
        dao.delete(id);
        // response
        ctx.res().setStatus(204);
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {return dao.validatePrimaryKey(integer);}

    // Checks if the room number is already in use by the hotel
    BiFunction<Integer, Integer, Boolean> validateHotelRoomNumber = (roomNumber, hotelId) -> dao.validateHotelRoomNumber(roomNumber, hotelId);

    @Override
    public RoomDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(RoomDTO.class)
                .check(r -> r.getRoomNumber() != null && r.getRoomNumber() > 0, "Not a valid room number")
                .check(r -> r.getRoomType() != null, "Not a valid room type")
                .check(r -> r.getRoomPrice() != null , "Not a valid price")
                .get();
    }
}
