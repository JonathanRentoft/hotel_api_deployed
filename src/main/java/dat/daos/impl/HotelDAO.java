package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.HotelDTO;
import dat.entities.Hotel;
import dat.entities.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class HotelDAO implements IDAO<HotelDTO, Integer> {

    private static HotelDAO instance;
    private static EntityManagerFactory emf;

    Set<Room> calRooms = getCalRooms();
    Set<Room> hilRooms = getHilRooms();

    public static HotelDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new HotelDAO();
        }
        return instance;
    }

    @Override
    public HotelDTO read(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Hotel hotel = em.find(Hotel.class, integer);
            return new HotelDTO(hotel);
        }
    }

    @Override
    public List<HotelDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<HotelDTO> query = em.createQuery("SELECT new dat.dtos.HotelDTO(h) FROM Hotel h", HotelDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public HotelDTO create(HotelDTO hotelDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Hotel hotel = new Hotel(hotelDTO);
            em.persist(hotel);
            em.getTransaction().commit();
            return new HotelDTO(hotel);
        }
    }

    @Override
    public HotelDTO update(Integer integer, HotelDTO hotelDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Hotel h = em.find(Hotel.class, integer);
            h.setHotelName(hotelDTO.getHotelName());
            h.setHotelAddress(hotelDTO.getHotelAddress());
            h.setHotelType(hotelDTO.getHotelType());
            Hotel mergedHotel = em.merge(h);
            em.getTransaction().commit();
            return mergedHotel != null ? new HotelDTO(mergedHotel) : null;
        }
    }

    @Override
    public void delete(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Hotel hotel = em.find(Hotel.class, integer);
            if (hotel != null) {
                em.remove(hotel);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Hotel hotel = em.find(Hotel.class, integer);
            return hotel != null;
        }
    }

    public void populate() {
        try (var em = emf.createEntityManager()) {
            em
                    .getTransaction()
                    .begin();
            Hotel california = new Hotel("Hotel California", "California", Hotel.HotelType.LUXURY);
            Hotel hilton = new Hotel("Hilton", "Copenhagen", Hotel.HotelType.STANDARD);
            california.setRooms(calRooms);
            hilton.setRooms(hilRooms);
            em.persist(california);
            em.persist(hilton);
            em
                    .getTransaction()
                    .commit();
        }
    }

    private static Set<Room> getCalRooms() {
        Room r100 = new Room(100, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r101 = new Room(101, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r102 = new Room(102, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r103 = new Room(103, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r104 = new Room(104, new BigDecimal(3200), Room.RoomType.DOUBLE);
        Room r105 = new Room(105, new BigDecimal(4500), Room.RoomType.SUITE);

        Room[] roomArray = {r100, r101, r102, r103, r104, r105};
        return Set.of(roomArray);
    }

    private static Set<Room> getHilRooms() {
        Room r111 = new Room(111, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r112 = new Room(112, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r113 = new Room(113, new BigDecimal(2520), Room.RoomType.SINGLE);
        Room r114 = new Room(114, new BigDecimal(2520), Room.RoomType.DOUBLE);
        Room r115 = new Room(115, new BigDecimal(3200), Room.RoomType.DOUBLE);
        Room r116 = new Room(116, new BigDecimal(4500), Room.RoomType.SUITE);

        Room[] roomArray = {r111, r112, r113, r114, r115, r116};
        return Set.of(roomArray);
    }
}
