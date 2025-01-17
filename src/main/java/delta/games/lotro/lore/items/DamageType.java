package delta.games.lotro.lore.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import delta.games.lotro.lore.items.comparators.DamageTypeComparator;

/**
 * Damage type.
 * @author DAM
 */
public final class DamageType
{
  private static final HashMap<String,DamageType> _map=new HashMap<String,DamageType>();
  private static final HashMap<String,DamageType> _keyMap=new HashMap<String,DamageType>();

  /**
   * Common.
   */
  public static final DamageType COMMON=new DamageType("COMMON","Common");
  /**
   * Westernesse.
   */
  public static final DamageType WESTERNESSE=new DamageType("WESTERNESSE","Westernesse");
  /**
   * Ancient dwarf.
   */
  public static final DamageType ANCIENT_DWARF=new DamageType("ANCIENT_DWARF","Ancient Dwarf-make");
  /**
   * Beleriand.
   */
  public static final DamageType BELERIAND=new DamageType("BELERIAND","Beleriand");
  /**
   * Fire.
   */
  public static final DamageType FIRE=new DamageType("FIRE","Fire");
  /**
   * Shadow.
   */
  public static final DamageType SHADOW=new DamageType("SHADOW","Shadow");
  /**
   * Light.
   */
  public static final DamageType LIGHT=new DamageType("LIGHT","Light");
  /**
   * Lightning.
   */
  public static final DamageType LIGHTNING=new DamageType("LIGHTNING","Lightning");
  /**
   * Frost.
   */
  public static final DamageType FROST=new DamageType("FROST","Frost");

  private String _key;
  private String _name;

  private DamageType(String key, String name)
  {
    _key=key;
    _keyMap.put(key,this);
    _name=name;
    _map.put(name,this);
  }

  /**
   * Get the damage type key.
   * @return A key.
   */
  public String getKey()
  {
    return _key;
  }

  /**
   * Get the damage type name.
   * @return A name.
   */
  public String getName()
  {
    return _name;
  }

  @Override
  public String toString()
  {
    return _name;
  }

  /**
   * Get a damage type using its name.
   * @param name Name of damage type.
   * @return A damage type instance or <code>null</code> if not found.
   */
  public static DamageType getDamageTypeByName(String name)
  {
    return _map.get(name);
  }

  /**
   * Get a damage type using its key.
   * @param key Key of damage type.
   * @return A damage type instance or <code>null</code> if not found.
   */
  public static DamageType getDamageTypeByKey(String key)
  {
    return _keyMap.get(key);
  }

  /**
   * Get all instances of this class.
   * @return an array of all instances of this class.
   */
  public static DamageType[] getAll()
  {
    List<DamageType> types=new ArrayList<DamageType>(_keyMap.values());
    Collections.sort(types,new DamageTypeComparator());
    return types.toArray(new DamageType[types.size()]);
  }
}
