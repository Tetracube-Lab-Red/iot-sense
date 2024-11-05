package red.tetracube.iotsense.database.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "devices_telemetry")
public class DeviceTelemetry extends PanacheEntityBase {

    @Id
    public UUID id;

    @Column(name = "event_time", nullable = false)
    public LocalDateTime eventTime;

    @Column(name = "telemetry_name", nullable = false)
    public String telemetryName;

    /*@Enumerated(EnumType.STRING)
    @Column(name = "units_class", nullable = false)
    public UnitsClass unitsClass;*/

  /*  @Enumerated(EnumType.STRING)
    @Column(name = "units", nullable = false)
    public Units units;*/

    @JoinColumn(name = "device_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Device.class)
    public Device device;

    @Column(name = "string_value")
    public String stringValue;

    @Column(name = "int_value")
    public Integer intValue;

    @Column(name = "long_value")
    public Long longValue;

    @Column(name = "double_value")
    public Double doubleValue;

    @Column(name = "float_value")
    public Float floatValue;

  /*  @Enumerated(EnumType.STRING)
    @Column(name = "switch_value")
    public SwitchState switchValue;*/

    @Column(name = "bool_value")
    public Boolean boolValue;

   /* @Column(name = "ups_value")
    public UPSState upsValue;*/

}

