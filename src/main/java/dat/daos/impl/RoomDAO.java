package dat.daos.impl;


import dat.daos.IDAO;
import dat.dtos.HotelDTO;
import dat.dtos.RoomDTO;
import dat.entities.Hotel;
import dat.entities.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RoomDAO implements IDAO<RoomDTO, Integer> {

    private static RoomDAO instance;
    private static EntityManagerFactory emf;

    public static RoomDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new RoomDAO();
        }
        return instance;
    }

    public HotelDTO addRoomToHotel(Integer hotelId, RoomDTO roomDTO ) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Room room = new Room(roomDTO);
            Hotel hotel = em.find(Hotel.class, hotelId);
            hotel.addRoom(room);
            em.persist(room);
            Hotel mergedHotel = em.merge(hotel);
            em.getTransaction().commit();
            return new HotelDTO(mergedHotel);
        }
    }

    @Override
    public RoomDTO read(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Room room = em.find(Room.class, integer);
            return room != null ? new RoomDTO(room) : null;
        }
    }

    @Override
    public List<RoomDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<RoomDTO> query = em.createQuery("SELECT new dat.dtos.RoomDTO(r) FROM Room r", RoomDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public RoomDTO create(RoomDTO roomDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Room room = new Room(roomDTO);
            em.persist(room);
            em.getTransaction().commit();
            return new RoomDTO(room);
        }
    }

    @Override
    public RoomDTO update(Integer integer, RoomDTO roomDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Room r = em.find(Room.class, integer);
            r.setRoomNumber(roomDTO.getRoomNumber());
            r.setRoomType(roomDTO.getRoomType());
            r.setRoomPrice(BigDecimal.valueOf(roomDTO.getRoomPrice()));
            Room mergedRoom = em.merge(r);
            em.getTransaction().commit();
            return new RoomDTO(mergedRoom);
        }
    }

    @Override
    public void delete(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Room room = em.find(Room.class, integer);
            if (room != null){
                em.remove(room);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Room room = em.find(Room.class, integer);
            return room != null;
        }
    }

    public Function<Integer, Boolean> validateHotelRoomNumber = (roomNumber) -> {
        try (EntityManager em = emf.createEntityManager()) {
            Room room = em.find(Room.class, roomNumber);
            return room != null;
        }
    };

    public Boolean validateHotelRoomNumber(Integer roomNumber, Integer hotelId) {
        try (EntityManager em = emf.createEntityManager()) {
            Hotel hotel = em.find(Hotel.class, hotelId);
            return hotel.getRooms().stream().anyMatch(r -> r.getRoomNumber().equals(roomNumber));
        }
    }

}
