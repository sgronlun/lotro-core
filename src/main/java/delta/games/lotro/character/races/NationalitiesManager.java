package delta.games.lotro.character.races;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import delta.games.lotro.character.races.io.xml.NationalityDescriptionXMLParser;
import delta.games.lotro.config.DataFiles;
import delta.games.lotro.config.LotroCoreConfig;

/**
 * Facade for access to nationalities descriptions.
 * @author DAM
 */
public class NationalitiesManager
{
  private static final Logger LOGGER=Logger.getLogger(NationalitiesManager.class);

  private static NationalitiesManager _instance=null;

  private HashMap<Integer,Nationality> _cache;

  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
  public static NationalitiesManager getInstance()
  {
    if (_instance==null)
    {
      _instance=new NationalitiesManager();
      _instance.loadAll();
    }
    return _instance;
  }

  /**
   * Constructor.
   */
  private NationalitiesManager()
  {
    _cache=new HashMap<Integer,Nationality>(10);
  }

  /**
   * Load all.
   */
  private void loadAll()
  {
    _cache.clear();
    LotroCoreConfig cfg=LotroCoreConfig.getInstance();
    File nationalitiesFile=cfg.getFile(DataFiles.NATIONALITIES);
    long now=System.currentTimeMillis();
    List<Nationality> nationalityDescriptions=NationalityDescriptionXMLParser.parseNationalitiesFile(nationalitiesFile);
    for(Nationality nationalityDescription : nationalityDescriptions)
    {
      _cache.put(Integer.valueOf(nationalityDescription.getIdentifier()),nationalityDescription);
    }
    long now2=System.currentTimeMillis();
    long duration=now2-now;
    LOGGER.info("Loaded "+_cache.size()+" nationalities in "+duration+"ms.");
  }

  /**
   * Get a nationality description using its code.
   * @param code Code of the nationality to get.
   * @return A nationality description or <code>null</code> if not found.
   */
  public Nationality getNationalityDescription(int code)
  {
    return _cache.get(Integer.valueOf(code));
  }
}
