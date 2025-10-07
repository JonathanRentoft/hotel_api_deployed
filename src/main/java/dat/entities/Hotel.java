package dat.entities;

import dat.dtos.HotelDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "hotel")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id", nullable = false, unique = true)
    private Integer id;

    @Setter
    @Column(name = "hotel_name", nullable = false, unique = true)
    private String hotelName;

    @Setter
    @Column(name = "hotel_address", nullable = false)
    private String hotelAddress;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "hotel_type", nullable = false)
    private HotelType hotelType;

    @OneToMany(mappedBy = "hotel", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Room> rooms = new HashSet<>();

    public Hotel(String hotelName, String hotelAddress, HotelType hotelType) {
        this.hotelName = hotelName;
        this.hotelAddress = hotelAddress;
        this.hotelType = hotelType;
    }

    public Hotel(HotelDTO hotelDTO) {
        this.id = hotelDTO.getId();
        this.hotelName = hotelDTO.getHotelName();
        this.hotelAddress = hotelDTO.getHotelAddress();
        this.hotelType = hotelDTO.getHotelType();
        if (hotelDTO.getRooms() != null) {
            hotelDTO.getRooms().forEach(roomDTO -> rooms.add(new Room(roomDTO)));
        }
    }

    // Bi-directional relationship for all rooms in a hotel
    public void setRooms(Set<Room> rooms) {
        if(rooms != null) {
            this.rooms = rooms;
            for (Room room : rooms) {
                room.setHotel(this);
            }
        }
    }

    // Bi-directional relationship
    public void addRoom(Room room) {
        if ( room != null) {
            this.rooms.add(room);
            room.setHotel(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return Objects.equals(hotelName, hotel.hotelName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hotelName);
    }

    public enum HotelType {
        BUDGET, STANDARD, LUXURY
    }
}
