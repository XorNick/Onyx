package support.plugin.onyx.profiles.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import redis.clients.jedis.Jedis;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.profiles.GameProfile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/*
Copyright (c) 2017 PluginManager LTD. All rights reserved.
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge and/or publish copies of the Software,
and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
Any copies of the Software shall stay private and cannot be resold.
Credit to PluginManager LTD shall be expressed in all forms of advertisement and/or endorsement.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

/**
 * Stands for 'Data Access Object', saves and loads data to/from Redis.
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

    /**
     * Inserts data to the keystore as JSON
     *
     * @param profile
     */
    public void insert(GameProfile profile) {

        try (Jedis conn = jedis) {

            conn.set(getKey(profile), gson.toJson(profile));

        }

    }

    /**
     * Updates data inside the keystore as JSON
     * @param profile
     */
    public void update(GameProfile profile) {

        try (Jedis conn = jedis) {

            conn.set(getKey(profile), gson.toJson(profile));

        }

    }

    /**
     * Deletes a profile from the keystore
     * @param profile
     */
    public void delete(GameProfile profile) {

        try (Jedis conn = jedis) {

            conn.del(getKey(profile));

        }

    }

    /**
     * Saves all profiles to the keystore
     * @param gameProfiles
     */
    public void saveAll(List<GameProfile> gameProfiles) {

        for (GameProfile profile : gameProfiles) {

            update(profile);

        }

    }

    /**
     * Gets all profiles from the keystore
     * @return
     */
    public List<GameProfile> getAll() {

        try (Jedis conn = jedis) {

            return conn.keys("onyx:users:" + Onyx.getInstance().getSettings().getInt("map.identifier") + ":*").stream().map(k -> gson.fromJson(conn.get(k), GameProfile.class)).collect(Collectors.toList());

        }

    }

    /**
     * Gets a single profile from the keystore
     * @param uuid
     * @return
     */
    public GameProfile getProfile(UUID uuid) {

        for (GameProfile profile : getAll()) {

            if (profile.getUuid() == uuid) {
                return profile;
            }

        }

        return null;

    }

    /**
     * Ensures that all records are created on the same key 'baseline'..?
     *
     * @param profile
     * @return
     */
    private String getKey(GameProfile profile) {
        return "onyx:users:" + Onyx.getInstance().getSettings().getInt("map.identifier") + ":" + profile.getUuid().toString();
    }

}
