package cz.uhk.zemanpe2.semproject.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class YyyyMmDdDateTypeAdapter extends TypeAdapter<Date> {
    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        out.value(new SimpleDateFormat("yyyy-MM-dd").format(value));
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        return null;
    }
}
