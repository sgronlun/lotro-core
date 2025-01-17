package delta.games.lotro.character.races;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import delta.games.lotro.character.races.io.xml.RaceDescriptionXMLParser;
import delta.games.lotro.common.Race;
import delta.games.lotro.config.DataFiles;
import delta.games.lotro.config.LotroCoreConfig;

/**
 * Facade for access to race descriptions.
 * @author DAM
 */
public class RacesManager
{
  private static final Logger LOGGER=Logger.getLogger(RacesManager.class);

  private static RacesManager _instance=null;

  private HashMap<Race,RaceDescription> _cache;

  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
  public static RacesManager getInstance()
  {
    if (_instance==null)
    {
      _instance=new RacesManager();
      _instance.loadAll();
    }
    return _instance;
  }

  /**
   * Constructor.
   */
  private RacesManager()
  {
    _cache=new HashMap<Race,RaceDescription>(10);
  }

  /**
   * Load all.
   */
  private void loadAll()
  {
    _cache.clear();
    LotroCoreConfig cfg=LotroCoreConfig.getInstance();
    File racesFile=cfg.getFile(DataFiles.RACES);
    long now=System.currentTimeMillis();
    List<RaceDescription> raceDescriptions=RaceDescriptionXMLParser.parseRaceDescriptionsFile(racesFile);
    for(RaceDescription raceDescription : raceDescriptions)
    {
      _cache.put(raceDescription.getRace(),raceDescription);
    }
    long now2=System.currentTimeMillis();
    long duration=now2-now;
    LOGGER.info("Loaded "+_cache.size()+" races in "+duration+"ms.");
  }

  /**
   * Get a race description using its key.
   * @param race Race to get.
   * @return A race description or <code>null</code> if not found.
   */
  public RaceDescription getRaceDescription(Race race)
  {
    RaceDescription ret=null;
    ret=_cache.get(race);
    return ret;
  }

  /**
   * Get race and gender using the avatar ID.
   * @param avatarID Avatar ID.
   * @return A race and gender, or <code>null</code> if not found.
   */
  public RaceGender findByAvatarID(int avatarID)
  {
    for(Race race : Race.ALL_RACES)
    {
      RaceDescription raceDescription=getRaceDescription(race);
      // Male?
      RaceGender maleGender=raceDescription.getMaleGender();
      if ((maleGender!=null) && (maleGender.getAvatarId()==avatarID))
      {
        return maleGender;
      }
      // Female?
      RaceGender femaleGender=raceDescription.getFemaleGender();
      if ((femaleGender!=null) && (femaleGender.getAvatarId()==avatarID))
      {
        return femaleGender;
      }
    }
    return null;
  }
}
