package config;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

public class MariaDBSourceConnectorConfig extends AbstractConfig {
    public MariaDBSourceConnectorConfig(Map originals) {
        super(configDef(), originals);
    }

    public static ConfigDef configDef() {
        return new ConfigDef()
                .define("name",
                        ConfigDef.Type.STRING,
                        ConfigDef.Importance.HIGH,
                        "name of this connector")
                .define("connector.class",
                        ConfigDef.Type.STRING,
                        ConfigDef.Importance.HIGH,
                        "name of the class for this connector")
                .define("topic.prefix",
                        ConfigDef.Type.STRING,
                        ConfigDef.Importance.HIGH,
                        "prefix")
                .define("connection.host",
                        ConfigDef.Type.STRING,
                        ConfigDef.Importance.HIGH,
                        "the host URL")
                .define("connection.port",
                        ConfigDef.Type.INT,
                        ConfigDef.Importance.HIGH,
                        "the host port")
                .define("connection.user",
                        ConfigDef.Type.STRING,
                        ConfigDef.Importance.HIGH,
                        "DB connection user")
                .define("connection.password",
                        ConfigDef.Type.PASSWORD,
                        ConfigDef.Importance.HIGH,
                        "DB connection password")
                .define("db.name",
                        ConfigDef.Type.STRING,
                        ConfigDef.Importance.HIGH,
                        "DB name")
                .define("table.whitelist",
                        ConfigDef.Type.STRING,
                        ConfigDef.Importance.LOW,
                        "List of tables from which to get data")
                .define("timestamp.column.name",
                        ConfigDef.Type.STRING,
                        ConfigDef.Importance.MEDIUM,
                        "timestamp columns");
    }
}
