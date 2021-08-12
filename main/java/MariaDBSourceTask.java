import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ResultSetConverter;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MariaDBSourceTask extends SourceTask {

    private static final Logger log = LoggerFactory.getLogger(MariaDBSourceTask.class);

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    @Override
    public void start(Map<String, String> props) {
        log.info("태스크 시작 , 프롭스 확인 : " + props);

        String DB_URL = "jdbc:mariadb://" + props.get("connection.host")
                + ":" + props.get("connection.port")
                + "/" + props.get("db.name");
        try {
            String DB_CON_CONFIG = DB_URL + "?user=" +
                    props.get("connection.user") + "&password=" +
                    props.get("connection.password");

            conn = DriverManager.getConnection(DB_CON_CONFIG);

            log.info("DB 연결 성공");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {

        //10초에 한번 SOURCE DB 를 확인
        synchronized (this) {
            this.wait(10000);
        }

        log.info("poll() 호출");

        ArrayList<SourceRecord> records = new ArrayList<>();

        Schema userschema = SchemaBuilder.struct().name("user")
                .field("id", Schema.INT32_SCHEMA)
                .field("name", Schema.STRING_SCHEMA)
                .field("age", Schema.INT32_SCHEMA)
                .build();

        Schema userListSchema = SchemaBuilder.struct()
                .field("data", SchemaBuilder.array(userschema));

        String selectSql = "SELECT id, name, age FROM user WHERE user.STATUS = 'N'";

        try {
            pstmt = conn.prepareStatement(selectSql);
            rs = pstmt.executeQuery();

            List<Struct> structs = ResultSetConverter.convert(rs, userschema);

            Struct userListStruct = new Struct(userListSchema);

            for (Struct struct : structs) {

                String updateSql = "UPDATE user SET status = 'Y', updated_dt = now() WHERE id =";
                updateSql += struct.get("id");
                pstmt = conn.prepareStatement(updateSql);
                int result = pstmt.executeUpdate();
            }

            userListStruct.put("data", structs);

            records.add(new SourceRecord(
                    null,
                    null,
                    "user",
                    userListSchema,
                    userListStruct
            ));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return records;
    }



    @Override
    public void stop() {

    }

    @Override
    public String version() {
        return "0.0.1";
    }
}
