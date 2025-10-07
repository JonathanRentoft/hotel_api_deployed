package dat.dtos;

import dat.entities.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class RoomDTO {
    private Integer Id;
    private Integer roomNumber;
    private Integer roomPrice;
    private Room.RoomType roomType;

    public RoomDTO(Room room) {
        this.Id = room.getRoomId();
        this.roomNumber = room.getRoomNumber();
        this.roomPrice = room.getRoomPrice().intValue();
        this.roomType = room.getRoomType();
    }

    public static List<RoomDTO> toRoomDTOList(List<Room> rooms) {
        return List.of(rooms.stream().map(RoomDTO::new).toArray(RoomDTO[]::new));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        RoomDTO roomDTO = (RoomDTO) o;
        return getId().equals(roomDTO.getId()) && getRoomNumber().equals(roomDTO.getRoomNumber()) && getRoomPrice().equals(roomDTO.getRoomPrice()) && getRoomType() == roomDTO.getRoomType();
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getRoomNumber().hashCode();
        result = 31 * result + getRoomPrice().hashCode();
        result = 31 * result + getRoomType().hashCode();
        return result;
    }
}
