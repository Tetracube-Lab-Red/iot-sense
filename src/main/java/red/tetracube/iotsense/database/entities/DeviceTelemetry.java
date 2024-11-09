package red.tetracube.iotsense.database.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import red.tetracube.iotsense.enumerations.*;

@Entity
@Table(name = "devices_telemetry")
public class DeviceTelemetry extends PanacheEntityBase {

    @Id
    public UUID id;

    @Column(name = "event_time", nullable = false)
    public LocalDateTime eventTime;

    @Column(name = "telemetry_name", nullable = false)
    public String telemetryName;

    @Enumerated(EnumType.STRING)
    @Column(name = "telemetry_type", nullable = false)
    public TelemetryType unitsClass;

    @Enumerated(EnumType.STRING)
    @Column(name = "units", nullable = false)
    public Units units;

    @JoinColumn(name = "device_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Device.class)
    public Device device;

    @Enumerated(EnumType.STRING)
    @Column(name = "telemetry_value_type", nullable = false)
    public TelemetryValueType telemetryValueType;

    @Column(name = "string_value")
    public String stringValue;

    @Column(name = "long_value")
    public Long longValue;

    @Column(name = "float_value")
    public Float floatValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "switch_value")
    public SwitchStatus switchValue;

    @Column(name = "bool_value")
    public Boolean boolValue;

    @Column(name = "ups_value")
    public UPSStatus upsValue;

}

