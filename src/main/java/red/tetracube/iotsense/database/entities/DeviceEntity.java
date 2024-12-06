package red.tetracube.iotsense.database.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import red.tetracube.iotsense.enumerations.DeviceType;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "devices")
public class DeviceEntity extends PanacheEntityBase {

    @Id
    public UUID id;

    @Column(name = "internal_name", unique = true, nullable = false)
    public String internalName;

    @Column(name = "human_name", nullable = false)
    public String humanName;

    @Column(name = "hub_id", nullable = false)
    public UUID hubId;

    @Column(name = "room_id")
    public UUID roomId;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false)
    public DeviceType deviceType;

    public static Boolean existsByName(String name) {
        return DeviceEntity.count("humanName", name.trim()) == 1;
    }

    public static List<DeviceEntity> findByHub(UUID hubId) {
        return DeviceEntity.<DeviceEntity>find("hubId", hubId).list();
    }

}
