package support.plugin.onyx.profiles;

import lombok.Getter;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.profiles.dao.ProfileDao;

import java.util.List;
import java.util.UUID;

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
public class ProfileManager {

    @Getter
    private Onyx instance;

    @Getter
    private ProfileDao profileDao;

    @Getter
    private List<GameProfile> gameProfiles;

    public ProfileManager(Onyx instance) {

        this.instance = instance;

        this.profileDao = new ProfileDao(
                instance.getSettings().getString("database.hostname"),
                instance.getSettings().getString("database.port"),
                (instance.getSettings().getString("database.auth_key") == "" ? null : instance.getSettings().getString("database.auth_key"))
        );

        this.gameProfiles = profileDao.getAll(); // Load all saved profiles...

    }

    public void save() {

        profileDao.saveAll(gameProfiles);

    }

    public GameProfile getUser(UUID uuid) {

        for (GameProfile profile : gameProfiles) {

            if (profile.getUuid() == uuid) {
                return profile;
            }

        }

        return null;

    }

    public void createUser(GameProfile profile) {

        gameProfiles.add(profile);
        profileDao.insert(profile);

    }

    public void removeUser(GameProfile profile) {

        gameProfiles.remove(profile);
        profileDao.delete(profile);

    }

}
