package delta.games.lotro.lore.reputation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Templates for faction levels.
 * @author DAM
 */
public class FactionLevelsTemplates
{
  /**
   * Classic faction.
   */
  public static final String CLASSIC="CLASSIC";
  /**
   * Host of the West.
   */
  public static final String HOW="HOW";
  /**
   * Central Gondor.
   */
  public static final String CENTRAL_GONDOR="CENTRAL_GONDOR";
  /**
   * Guild faction.
   */
  public static final String GUILD="GUILD";
  /**
   * Forochel faction.
   */
  public static final String FOROCHEL="FOROCHEL";
  /**
   * Dol Amroth Districts faction.
   */
  public static final String DOL_AMROTH="DOL_AMROTH";
  /**
   * Hobnanigans.
   */
  public static final String HOBNANIGANS="HOBNANIGANS";
  /**
   * Inn League/Ale Association.
   */
  public static final String ALE_INN="ALE_INN";
  /**
   * Extended-classic faction.
   */
  public static final String EXTENDED_CLASSIC="EXTENDED_CLASSIC";

  private HashMap<String,FactionLevelsTemplate> _templates;

  /**
   * Constructor.
   */
  public FactionLevelsTemplates()
  {
    _templates=new HashMap<String,FactionLevelsTemplate>();
    init();
  }

  /**
   * Get a faction template by key.
   * @param key Key to use.
   * @return A template or <code>null</code> if not found.
   */
  public FactionLevelsTemplate getByKey(String key)
  {
    return _templates.get(key);
  }

  private void init()
  {
    register(buildClassic());
    register(buildHostOfTheWest());
    register(buildCentralGondor());
    register(buildForochel());
    register(buildExtendedClassic());
    register(buildGuild());
    register(buildDolAmroth());
    register(buildHobnanigans());
    register(buildAleInn());
  }

  private void register(FactionLevelsTemplate template)
  {
    _templates.put(template.getKey(),template);
  }

  private FactionLevelsTemplate buildClassic()
  {
    List<FactionLevel> levels=new ArrayList<FactionLevel>();
    levels.add(FactionLevel.NEUTRAL);
    levels.add(FactionLevel.ACQUAINTANCE);
    levels.add(FactionLevel.FRIEND);
    levels.add(FactionLevel.ALLY);
    levels.add(FactionLevel.KINDRED);
    return new FactionLevelsTemplate(CLASSIC,levels);
  }

  private FactionLevelsTemplate buildHostOfTheWest()
  {
    List<FactionLevel> levels=new ArrayList<FactionLevel>();
    levels.add(new FactionLevel("NONE","-",0,0,10000));
    levels.add(new FactionLevel("INITIAL","Initial",1,0,20000));
    levels.add(new FactionLevel("INTERMEDIATE","Intermediate",2,5,25000));
    levels.add(new FactionLevel("ADVANCED","Advanced",3,10,30000));
    levels.add(new FactionLevel("FINAL","Final",4,15,0));
    return new FactionLevelsTemplate(HOW,levels);
  }

  private FactionLevelsTemplate buildAleInn()
  {
    List<FactionLevel> levels=new ArrayList<FactionLevel>();
    levels.add(FactionLevel.ENEMY);
    levels.add(FactionLevel.NEUTRAL);
    levels.add(new FactionLevel("ACQUAINTANCE","Acquaintance",1,0,20000));
    levels.add(new FactionLevel("FRIEND","Friend",2,0,25000));
    levels.add(new FactionLevel("ALLY","Ally",3,0,30000));
    levels.add(new FactionLevel("KINDRED","Kindred",4,0,0));
    return new FactionLevelsTemplate(ALE_INN,levels);
  }

  private FactionLevelsTemplate buildCentralGondor()
  {
    List<FactionLevel> levels=new ArrayList<FactionLevel>();
    levels.add(FactionLevel.NEUTRAL);
    levels.add(new FactionLevel("ACQUAINTANCE","Acquaintance",1,0,20000));
    levels.add(new FactionLevel("FRIEND","Friend",2,0,25000));
    levels.add(new FactionLevel("ALLY","Ally",3,0,30000));
    levels.add(new FactionLevel("KINDRED","Kindred",4,0,0));
    return new FactionLevelsTemplate(CENTRAL_GONDOR,levels);
  }

  private FactionLevelsTemplate buildForochel()
  {
    List<FactionLevel> levels=new ArrayList<FactionLevel>();
    levels.add(FactionLevel.OUTSIDER);
    levels.add(FactionLevel.NEUTRAL);
    levels.add(FactionLevel.ACQUAINTANCE);
    levels.add(FactionLevel.FRIEND);
    levels.add(FactionLevel.ALLY);
    levels.add(FactionLevel.KINDRED);
    return new FactionLevelsTemplate(FOROCHEL,levels);
  }

  private FactionLevelsTemplate buildExtendedClassic()
  {
    List<FactionLevel> levels=new ArrayList<FactionLevel>();
    levels.add(FactionLevel.NEUTRAL);
    levels.add(FactionLevel.ACQUAINTANCE);
    levels.add(FactionLevel.FRIEND);
    levels.add(FactionLevel.ALLY);
    levels.add(FactionLevel.KINDRED);
    levels.add(FactionLevel.RESPECTED);
    levels.add(FactionLevel.HONOURED);
    levels.add(FactionLevel.CELEBRATED);
    return new FactionLevelsTemplate(EXTENDED_CLASSIC,levels);
  }

  private FactionLevelsTemplate buildGuild()
  {
    List<FactionLevel> levels=new ArrayList<FactionLevel>();
    levels.add(new FactionLevel("NONE","-",0,0,0));
    levels.add(new FactionLevel("INITIATE","Guild Initiate",1,0,10000));
    levels.add(new FactionLevel("APPRENTICE","Apprentice of the Guild",2,0,20000));
    levels.add(new FactionLevel("JOURNEYMAN","Journeyman of the Guild",3,0,25000));
    levels.add(new FactionLevel("EXPERT","Expert of the Guild",4,0,30000));
    levels.add(new FactionLevel("ARTISAN","Artisan of the Guild",5,0,45000));
    levels.add(new FactionLevel("MASTER","Master of the Guild",6,0,60000));
    levels.add(new FactionLevel("EASTEMNET MASTER","Eastemnet Master of the Guild",7,0,90000));
    levels.add(new FactionLevel("WESTEMNET MASTER","Westemnet Master of the Guild",8,0,0));
    return new FactionLevelsTemplate(GUILD,levels);
  }

  private FactionLevelsTemplate buildDolAmroth()
  {
    List<FactionLevel> levels=new ArrayList<FactionLevel>();
    levels.add(FactionLevel.NEUTRAL);
    levels.add(new FactionLevel("ACQUAINTANCE","Acquaintance",1,0,0));
    return new FactionLevelsTemplate(DOL_AMROTH,levels);
  }

  private FactionLevelsTemplate buildHobnanigans()
  {
    List<FactionLevel> levels=new ArrayList<FactionLevel>();
    levels.add(new FactionLevel("NONE","-",0,0,0));
    levels.add(new FactionLevel("ROOKIE","Rookie",1,0,10000));
    levels.add(new FactionLevel("LEAGUER","Leaguer",2,0,20000));
    levels.add(new FactionLevel("MAJOR_LEAGUER","Major Leaguer",3,0,25000));
    levels.add(new FactionLevel("ALL_STAR","All-star",4,0,30000));
    levels.add(new FactionLevel("HALL_OF_FAMER","Hall of Famer",5,0,45000));
    return new FactionLevelsTemplate(HOBNANIGANS,levels);
  }
}