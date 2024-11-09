package red.tetracube.iotsense.database.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import red.tetracube.iotsense.enumerations.DeviceType;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "devices")
public class Device extends PanacheEntityBase {

    @Id
    public UUID id;

    @Column(name = "internal_name", unique = true, nullable = false)
    public String internalName;

    @Column(name = "slug", unique = true, nullable = false)
    public String slug;

    @Column(name = "human_name", nullable = false)
    public String humanName;

    @Column(name = "hubSlug", nullable = false)
    public String hubSlug;

    @Column(name = "room_slug")
    public String roomSlug;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false)
    public DeviceType deviceType;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "device", orphanRemoval = true, targetEntity = DeviceSupportedCommand.class)
    public List<DeviceSupportedCommand> deviceSupportedCommands;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "device", targetEntity = DeviceTelemetry.class)
    public List<DeviceTelemetry> deviceTelemetries;

    public static Boolean existsBySlug(String slug) {
        return Device.count("slug", slug) == 1;
    }

}
