package support.plugin.onyx.profiles;

import lombok.Getter;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.profiles.dao.ProfileDao;

import java.util.List;
import java.util.UUID;

/**
 * Created by eric on 01/09/2017.
 */
public class ProfileManager {

    @Getter
    private Onyx instance;

    @Getter
    private ProfileDao profileDao;

    @Getter
    private List<GameProfile> gameProfiles;

    public ProfileManager(Onyx instance){

        this.instance = instance;

        this.profileDao = new ProfileDao(
                instance.getSettings().getString("database.hostname"),
                instance.getSettings().getString("database.port"),
                instance.getSettings().getString("database.auth_key")
        );

        this.gameProfiles = profileDao.getAll(); // Load all saved profiles...

    }

    public void save(){

        profileDao.saveAll(gameProfiles);

    }

    public GameProfile getUser(UUID uuid){

        for(GameProfile profile : gameProfiles){

            if(profile.getUuid() == uuid){
                return profile;
            }

        }

        return null;

    }

}
