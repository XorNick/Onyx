package support.plugin.onyx.timer.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.entity.Player;
import org.omg.CORBA.TIMEOUT;
import redis.clients.jedis.Jedis;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.factions.Faction;
import support.plugin.onyx.timer.ITimer;
import support.plugin.onyx.timer.TimerType;
import support.plugin.onyx.timer.timers.Timer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
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

    public void saveAll(ConcurrentHashMap<UUID, Set<Timer>> timers){

        try(Jedis conn = jedis){

            for(UUID playerUuid : timers.keySet()){

                Set<Timer> tmpTimers = timers.get(playerUuid);

                for(Timer timer : tmpTimers){

                    update(timer);

                }

            }

        }

    }

    public ConcurrentHashMap<UUID, Set<Timer>> getAll() {

        ConcurrentHashMap<UUID, Set<Timer>> activeTimers = new ConcurrentHashMap<>();

        try (Jedis conn = jedis) {

            List<Timer> timers =  conn.keys("hcf:timers:"+ Onyx.getInstance().getSettings().getInt("map.identifier")+":*").stream().map(k -> gson.fromJson(conn.get(k), Timer.class)).collect(Collectors.toList());

            for(Timer timer : timers){

                if(activeTimers.contains(timer.getPlayer())){
                    Set<Timer> tmpTimers = activeTimers.get(timer.getPlayer());
                    tmpTimers.add(timer);

                    activeTimers.remove(timer.getPlayer());
                    activeTimers.put(timer.getPlayer(), tmpTimers);
                }else{
                    Set<Timer> tmpTimers = new HashSet<>();
                    tmpTimers.add(timer);

                    activeTimers.put(timer.getPlayer(), tmpTimers);
                }

            }

        }

        return activeTimers;

    }

    /*public Timer getTimer(Player player, TimerType timerType) {

        for (ITimer timer : getAll().get(player.getUniqueId())) {

            if(timer.getPlayer() == player.getUniqueId() && timer.getType() == timerType){
                return timer;
            }

        }

        return null;

    }*/

    public String getKey(Timer timer) {
        return "hcf:timers:"+ Onyx.getInstance().getSettings().getInt("map.identifier")+":" + timer.getPlayer().toString() + ":" + timer.getType().toString();
    }

}
