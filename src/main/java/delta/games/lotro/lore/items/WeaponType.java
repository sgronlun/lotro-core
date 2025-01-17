package delta.games.lotro.lore.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import delta.games.lotro.lore.items.comparators.WeaponTypeComparator;

/**
 * Weapon type.
 * @author DAM
 */
public final class WeaponType
{
  private static final HashMap<String,WeaponType> KEY_MAP=new HashMap<String,WeaponType>();

  /**
   * One-handed Sword.
   */
  public static final WeaponType ONE_HANDED_SWORD=new WeaponType("ONE_HANDED_SWORD","One-handed Sword",false,false);
  /**
   * Two-handed Sword.
   */
  public static final WeaponType TWO_HANDED_SWORD=new WeaponType("TWO_HANDED_SWORD","Two-handed Sword",false,true);
  /**
   * One-handed Axe.
   */
  public static final WeaponType ONE_HANDED_AXE=new WeaponType("ONE_HANDED_AXE","One-handed Axe",false,false);
  /**
   * Two-handed Axe.
   */
  public static final WeaponType TWO_HANDED_AXE=new WeaponType("TWO_HANDED_AXE","Two-handed Axe",false,true);
  /**
   * One-handed Hammer.
   */
  public static final WeaponType ONE_HANDED_HAMMER=new WeaponType("ONE_HANDED_HAMMER","One-handed Hammer",false,false);
  /**
   * Two-handed Hammer.
   */
  public static final WeaponType TWO_HANDED_HAMMER=new WeaponType("TWO_HANDED_HAMMER","Two-handed Hammer",false,true);
  /**
   * One-handed Club.
   */
  public static final WeaponType ONE_HANDED_CLUB=new WeaponType("ONE_HANDED_CLUB","One-handed Club",false,false);
  /**
   * Two-handed Club.
   */
  public static final WeaponType TWO_HANDED_CLUB=new WeaponType("TWO_HANDED_CLUB","Two-handed Club",false,true);
  /**
   * One-handed Mace.
   */
  public static final WeaponType ONE_HANDED_MACE=new WeaponType("ONE_HANDED_MACE","One-handed Mace",false,false);
  /**
   * Dagger.
   */
  public static final WeaponType DAGGER=new WeaponType("DAGGER","Dagger",false,false);
  /**
   * Staff.
   */
  public static final WeaponType STAFF=new WeaponType("STAFF","Staff",false,false);
  /**
   * Halberd.
   */
  public static final WeaponType HALBERD=new WeaponType("HALBERD","Halberd",false,true);
  /**
   * Bow.
   */
  public static final WeaponType BOW=new WeaponType("BOW","Bow",true,false);
  /**
   * Crossbow.
   */
  public static final WeaponType CROSSBOW=new WeaponType("CROSSBOW","Crossbow",true,false);
  /**
   * Javelin.
   */
  public static final WeaponType JAVELIN=new WeaponType("JAVELIN","Javelin",true,false);
  /**
   * Spear.
   */
  public static final WeaponType SPEAR=new WeaponType("SPEAR","Spear",false,false);
  /**
   * Rune-stone.
   */
  public static final WeaponType RUNE_STONE=new WeaponType("RUNE_STONE","Rune-stone",false,true);
  /**
   * Battle gauntlets.
   */
  public static final WeaponType BATTLE_GAUNTLETS=new WeaponType("BATTLE_GAUNTLETS","Battle-gauntlets",false,true);
  /**
   * Other.
   */
  public static final WeaponType OTHER=new WeaponType("OTHER","Other",false,false);

  private String _key;
  private String _name;
  private boolean _ranged;
  private boolean _twoHanded;

  private WeaponType(String key, String name, boolean ranged, boolean twoHanded)
  {
    _key=key;
    _name=name;
    _ranged=ranged;
    _twoHanded=twoHanded;
    KEY_MAP.put(key,this);
  }

  /**
   * Get the weapon type key.
   * @return A key.
   */
  public String getKey()
  {
    return _key;
  }

  /**
   * Get the weapon type name.
   * @return A name.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Indicates if this weapon type is a ranged weapon or not.
   * @return <code>true</code> if it is ranged, <code>false</code> otherwise.
   */
  public boolean isRanged()
  {
    return _ranged;
  }

  /**
   * Indicates if this weapon type is a two-handed weapon or not.
   * @return <code>true</code> if it is two-handed, <code>false</code> otherwise.
   */
  public boolean isTwoHanded()
  {
    return _twoHanded;
  }

  @Override
  public String toString()
  {
    return _name;
  }

  /**
   * Get a weapon type using its key.
   * @param key Key of weapon type.
   * @return A weapon type instance or <code>null</code> if not found.
   */
  public static WeaponType getWeaponTypeByKey(String key)
  {
    return KEY_MAP.get(key);
  }

  /**
   * Get all instances of this class.
   * @return an array of all instances of this class.
   */
  public static List<WeaponType> getAll()
  {
    List<WeaponType> ret=new ArrayList<WeaponType>(KEY_MAP.values());
    Collections.sort(ret,new WeaponTypeComparator());
    return ret;
  }
}
