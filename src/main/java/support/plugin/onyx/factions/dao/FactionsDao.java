package support.plugin.onyx.factions.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import redis.clients.jedis.Jedis;
import support.plugin.onyx.factions.Faction;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by eric on 31/08/2017.
 */
public class FactionsDao {

    private final Gson gson;
    private final Jedis jedis;

    public FactionsDao(String hostname, String port, String auth) {

        gson = new GsonBuilder().setPrettyPrinting().create();
        jedis = new Jedis(hostname, Integer.parseInt(port), 3000);

        if (!(auth == null)) {
            jedis.auth(auth);
        }

    }

    public void insert(Faction faction) {

        try (Jedis conn = jedis) {

            conn.set(getKey(faction), gson.toJson(faction));

        }

    }

    public void update(Faction faction) {

        try (Jedis conn = jedis) {

            conn.set(getKey(faction), gson.toJson(faction));

        }

    }

    public void delete(Faction faction) {

        try (Jedis conn = jedis) {

            conn.del(getKey(faction));

        }

    }

    public List<Faction> getAll() {

        try (Jedis conn = jedis) {

            return conn.keys("hcf:factions:*").stream().map(k -> gson.fromJson(conn.get(k), Faction.class)).collect(Collectors.toList());

        }

    }

    public Faction getFaction(UUID uuid) {

        for (Faction faction : getAll()) {

            if (faction.getFactionId() == uuid) {
                return faction;
            }

        }

        return null;

    }

    public String getKey(Faction faction) {
        return "hcf:factions:" + faction.getFactionId().toString();
    }

}
