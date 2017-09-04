package support.plugin.onyx.timer.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import redis.clients.jedis.Jedis;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.timer.timers.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
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
public class TimerDao {

    private final Gson gson;
    private final Jedis jedis;

    public TimerDao(String hostname, String port, String auth) {

        gson = new GsonBuilder().setPrettyPrinting().create();
        jedis = new Jedis(hostname, Integer.parseInt(port), 3000);

        if (!(auth == null)) {
            jedis.auth(auth);
        }

    }

    public void insert(Timer timer) {

        try (Jedis conn = jedis) {

            conn.set(getKey(timer), gson.toJson(timer));

        }

    }

    public void update(Timer timer) {

        try (Jedis conn = jedis) {

            conn.set(getKey(timer), gson.toJson(timer));

        }

    }

    public void delete(Timer timer) {

        try (Jedis conn = jedis) {

            conn.del(getKey(timer));

        }

    }

    public void saveAll(ConcurrentHashMap<UUID, List<Timer>> timers) {

        try (Jedis conn = jedis) {

            for (UUID playerUuid : timers.keySet()) {

                List<Timer> tmpTimers = timers.get(playerUuid);

                for (Timer timer : tmpTimers) {

                    update(timer);

                }

            }

        }

    }

    public ConcurrentHashMap<UUID, List<Timer>> getAll() {

        ConcurrentHashMap<UUID, List<Timer>> activeTimers = new ConcurrentHashMap<>();

        try (Jedis conn = jedis) {

            List<Timer> timers = conn.keys("onyx:timers:" + Onyx.getInstance().getSettings().getInt("map.identifier") + ":*").stream().map(k -> gson.fromJson(conn.get(k), Timer.class)).collect(Collectors.toList());

            for (Timer timer : timers) {

                if (activeTimers.contains(timer.getPlayer())) {
                    List<Timer> tmpTimers = activeTimers.get(timer.getPlayer());
                    tmpTimers.add(timer);

                    activeTimers.remove(timer.getPlayer());
                    activeTimers.put(timer.getPlayer(), tmpTimers);
                } else {
                    List<Timer> tmpTimers = new ArrayList<>();
                    tmpTimers.add(timer);

                    activeTimers.put(timer.getPlayer(), tmpTimers);
                }

            }

        }

        return activeTimers;

    }


    public String getKey(Timer timer) {
        return "onyx:timers:" + Onyx.getInstance().getSettings().getInt("map.identifier") + ":" + timer.getPlayer().toString() + ":" + timer.getType().toString();
    }

}
