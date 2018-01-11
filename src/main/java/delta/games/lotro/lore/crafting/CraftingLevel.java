package delta.games.lotro.lore.crafting;

import java.util.HashMap;

/**
 * Represents a level in a crafting profession.
 * @author DAM
 */
public final class CraftingLevel
{
  private static HashMap<Integer,CraftingLevel> _registry=new HashMap<Integer,CraftingLevel>();

  /**
   * Beginner.
   */
  public static final CraftingLevel BEGINNER=new CraftingLevel(0,"Beginner",0,"Beginner",0);
  /**
   * Apprentice.
   */
  public static final CraftingLevel APPRENTICE=new CraftingLevel(1,"Apprentice",200,"Master Apprentice",400);
  /**
   * Journeyman.
   */
  public static final CraftingLevel JOURNEYMAN=new CraftingLevel(2,"Journeyman",280,"Master Journeyman",560);
  /**
   * Expert.
   */
  public static final CraftingLevel EXPERT=new CraftingLevel(3,"Expert",360,"Master Expert",720);
  /**
   * Artisan.
   */
  public static final CraftingLevel ARTISAN=new CraftingLevel(4,"Artisan",440,"Master Artisan",880);
  /**
   * Master.
   */
  public static final CraftingLevel MASTER=new CraftingLevel(5,"Master",520,"Grand Master",1040);
  /**
   * Supreme.
   */
  public static final CraftingLevel SUPREME=new CraftingLevel(6,"Supreme",600,"Supreme Master",1200);
  /**
   * Westfold.
   */
  public static final CraftingLevel WESTFOLD=new CraftingLevel(7,"Westfold",680,"Westfold Master",1360);
  /**
   * Eastemnet.
   */
  public static final CraftingLevel EASTEMNET=new CraftingLevel(8,"Eastemnet",760,"Eastemnet Master",1520);
  /**
   * Westemnet.
   */
  public static final CraftingLevel WESTEMNET=new CraftingLevel(9,"Westemnet",840,"Westemnet Master",1680);
  /**
   * Anórien.
   */
  public static final CraftingLevel ANORIEN=new CraftingLevel(10,"Anórien",840,"Anórien Master",1680);
  /**
   * Doomfold.
   */
  public static final CraftingLevel DOOMFOLD=new CraftingLevel(11,"Doomfold",840,"Doomfold Master",1680);

  /**
   * All crafting tiers, sorted by level.
   */
  public static final CraftingLevel[] ALL_TIERS = {
    BEGINNER, APPRENTICE, JOURNEYMAN, EXPERT, ARTISAN, MASTER, SUPREME,
    WESTFOLD, EASTEMNET, WESTEMNET, ANORIEN, DOOMFOLD
  };

  private int _tier;
  private CraftingLevelTier _proficiency;
  private CraftingLevelTier _mastery;

  private CraftingLevel(int tier, String label, int proficiencyXP, String masteryLabel, int masteryXP)
  {
    _tier=tier;
    _proficiency=new CraftingLevelTier(this,label,proficiencyXP);
    _mastery=new CraftingLevelTier(this,masteryLabel,masteryXP);
    _registry.put(Integer.valueOf(tier),this);
  }

  /**
   * Get the associated tier value.
   * @return A tier number.
   */
  public int getTier()
  {
    return _tier;
  }

  /**
   * Get the proficiency tier.
   * @return the proficiency tier.
   */
  public CraftingLevelTier getProficiency()
  {
    return _proficiency;
  }

  /**
   * Get the mastery tier.
   * @return the mastery tier.
   */
  public CraftingLevelTier getMastery()
  {
    return _mastery;
  }

  @Override
  public String toString()
  {
    return _proficiency.getLabel();
  }

  /**
   * Get a crafting level instance by tier.
   * @param tier Tier of the crafting level to get.
   * @return A crafting level instance or <code>null</code> if <code>tier</code> is not known.
   */
  public static CraftingLevel getByTier(int tier)
  {
    CraftingLevel level=_registry.get(Integer.valueOf(tier));
    return level;
  }

  /**
   * Get the maximum level.
   * @return the maximum level.
   */
  public static CraftingLevel getMaximumLevel()
  {
    return DOOMFOLD;
  }
}