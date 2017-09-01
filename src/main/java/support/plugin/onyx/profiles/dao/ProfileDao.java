package support.plugin.onyx.profiles.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import redis.clients.jedis.Jedis;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.factions.Faction;
import support.plugin.onyx.profiles.GameProfile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by eric on 01/09/2017.
 */
public class ProfileDao {

    private final Gson gson;
    private final Jedis jedis;

    public ProfileDao(String hostname, String port, String auth) {

        gson = new GsonBuilder().setPrettyPrinting().create();
        jedis = new Jedis(hostname, Integer.parseInt(port), 3000);

        if (!(auth == null)) {
            jedis.auth(auth);
        }

    }

    public void insert(GameProfile profile) {

        try (Jedis conn = jedis) {

            conn.set(getKey(profile), gson.toJson(profile));

        }

    }

    public void update(GameProfile profile) {

        try (Jedis conn = jedis) {

            conn.set(getKey(profile), gson.toJson(profile));

        }

    }

    public void delete(GameProfile profile) {

        try (Jedis conn = jedis) {

            conn.del(getKey(profile));

        }

    }

    public void saveAll(List<GameProfile> gameProfiles){

        for(GameProfile profile : gameProfiles){

            update(profile);

        }

    }

    public List<GameProfile> getAll() {

        try (Jedis conn = jedis) {

            return conn.keys("hcf:users:"+ Onyx.getInstance().getSettings().getInt("map.identifier")+":*").stream().map(k -> gson.fromJson(conn.get(k), GameProfile.class)).collect(Collectors.toList());

        }

    }

    public GameProfile getProfile(UUID uuid) {

        for (GameProfile profile : getAll()) {

            if (profile.getUuid() == uuid) {
                return profile;
            }

        }

        return null;

    }

    public String getKey(GameProfile profile) {
        return "hcf:users:"+ Onyx.getInstance().getSettings().getInt("map.identifier")+":" + profile.getUuid().toString();
    }

}
