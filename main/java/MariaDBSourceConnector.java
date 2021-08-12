import config.MariaDBSourceConnectorConfig;
import org.apache.kafka.common.config.Config;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MariaDBSourceConnector extends SourceConnector {

    private static final Logger log = LoggerFactory.getLogger(MariaDBSourceConnector.class);

    private MariaDBSourceConnectorConfig mariaDBSourceConnectorConfig;

    private Map<String, String> configProps;

    @Override
    public void start(Map<String, String> props) {
        log.info("소스 커넥터 시작");
        this.mariaDBSourceConnectorConfig = new MariaDBSourceConnectorConfig(props);
        this.configProps = Collections.unmodifiableMap(props);
        log.info("config props : " + props);
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        log.info("태스크 컨피그 호출");
        final List<Map<String, String>> configs = new ArrayList<>(maxTasks);
        for (int i = 0; i<maxTasks; i++) {
            configs.add(configProps);
        }
        return configs;
    }

    @Override
    public void stop() {

    }

    @Override
    public String version() {
        return "0.0.1";
    }

    @Override
    public Config validate(Map<String, String> connectorConfigs) {
        return super.validate(connectorConfigs);
    }

    @Override
    public Class<? extends Task> taskClass() {
        log.info("태스크 클래스 인식");
        return MariaDBSourceTask.class;
    }

    @Override
    public ConfigDef config() {
        return mariaDBSourceConnectorConfig.configDef();
    }

}
