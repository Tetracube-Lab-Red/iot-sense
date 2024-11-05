package red.tetracube.iotsense.database.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import red.tetracube.iotsense.enumerations.DeviceType;

@Entity
@Table(name = "devices")
public class Device extends PanacheEntity {

    @Column(name = "internal_name", unique = true, nullable = false)
    public String internalName;

    @Column(name = "slug", unique = true, nullable = false)
    public String slug;

    @Column(name = "human_name", nullable = false)
    public String humanName;

    @Column(name = "hubSlug", nullable = true)
    public String hubSlug;

    @Column(name = "room_slug", nullable = true)
    public String roomSlug;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false)
    public DeviceType deviceType;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "device", orphanRemoval = true, targetEntity = DeviceInteraction.class)
    public List<DeviceInteraction> deviceInteractions;

    public static Device getByInternalName(String internalName) {
        return Device.<Device>find("internalName", internalName)
                .firstResult();
    }

}
