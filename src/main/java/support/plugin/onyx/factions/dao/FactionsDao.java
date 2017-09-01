package support.plugin.onyx.factions.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import redis.clients.jedis.Jedis;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.factions.Faction;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/*

Copyright (c) 2017 PluginManager LTD

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

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

            return conn.keys("onyx:factions:" + Onyx.getInstance().getSettings().getInt("map.identifier") + ":*").stream().map(k -> gson.fromJson(conn.get(k), Faction.class)).collect(Collectors.toList());

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
        return "onyx:factions:" + Onyx.getInstance().getSettings().getInt("map.identifier") + ":" + faction.getFactionId().toString();
    }

}
