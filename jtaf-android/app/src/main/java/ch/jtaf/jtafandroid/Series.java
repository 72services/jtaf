package ch.jtaf.jtafandroid;

import org.json.JSONException;
import org.json.JSONObject;

public class Series {

    private Integer id;
    private String name;


    public Series(JSONObject series) {
        try {
            this.id = Integer.parseInt(series.get("id").toString());
            this.name = series.get("name").toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
