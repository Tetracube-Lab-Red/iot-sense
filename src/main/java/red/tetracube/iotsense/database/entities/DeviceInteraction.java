package red.tetracube.iotsense.database.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import red.tetracube.iotsense.enumerations.DeviceInteractionClass;
import red.tetracube.iotsense.enumerations.DeviceInteractionType;

@Entity
@Table(name = "devices_interactions")
public class DeviceInteraction extends PanacheEntity{

    @Enumerated(EnumType.STRING)
    @Column(name = "interaction_type", nullable = false)
    public DeviceInteractionType interactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "interaction_class", nullable = true)
    public DeviceInteractionClass interactionClass;

    @JoinColumn(name = "device_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Device.class)
    public Device device;

}

