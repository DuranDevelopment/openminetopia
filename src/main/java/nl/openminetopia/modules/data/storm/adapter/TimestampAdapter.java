package nl.openminetopia.modules.data.storm.adapter;

import com.craftmend.storm.Storm;
import com.craftmend.storm.parser.objects.ParsedField;
import com.craftmend.storm.parser.types.objects.StormTypeAdapter;

import java.sql.Timestamp;

public class TimestampAdapter extends StormTypeAdapter<Timestamp> {

    @Override
    public Timestamp fromSql(ParsedField parsedField, Object sqlValue) {
        if (sqlValue == null) return null;
        return Timestamp.valueOf(sqlValue.toString());
    }

    @Override
    public Object toSql(Storm storm, Timestamp value) {
        if (value == null) return null;
        return value.toString();
    }

    @Override
    public String getSqlBaseType() {
        return "VARCHAR(%max)";
    }

    @Override
    public boolean escapeAsString() {
        return true;
    }
}