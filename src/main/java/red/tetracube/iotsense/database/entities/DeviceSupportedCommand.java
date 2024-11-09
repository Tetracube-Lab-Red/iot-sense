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
import red.tetracube.iotsense.enumerations.DeviceCommandType;

@Entity
@Table(name = "devices_interactions")
public class DeviceSupportedCommand extends PanacheEntity{

    @Enumerated(EnumType.STRING)
    @Column(name = "device_command_type", nullable = true)
    public DeviceCommandType deviceCommandType;

    @JoinColumn(name = "device_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Device.class)
    public Device device;

}

