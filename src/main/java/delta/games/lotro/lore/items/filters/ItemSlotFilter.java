package delta.games.lotro.lore.items.filters;

import delta.games.lotro.character.gear.GearSlot;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.Weapon;

/**
 * Filter items that can go in a given slot.
 * @author DAM
 */
public class ItemSlotFilter implements ItemFilter
{
  private EquipmentLocation _location;
  private boolean _strict;

  /**
   * Constructor.
   * @param slot Slot to use.
   */
  public ItemSlotFilter(GearSlot slot)
  {
    _location=slot.getLocation();
    _strict=true;
  }

  /**
   * Constructor.
   * @param location Slot to use.
   */
  public ItemSlotFilter(EquipmentLocation location)
  {
    _location=location;
    _strict=false;
  }

  /**
   * Get the managed equipment location.
   * @return An equipment location or <code>null</code>.
   */
  public EquipmentLocation getLocation()
  {
    return _location;
  }

  /**
   * Set the equipment location to use.
   * @param location An equipment location or <code>null</code>.
   */
  public void setLocation(EquipmentLocation location)
  {
    _location=location;
  }

  @Override
  public boolean accept(Item item)
  {
    if ((!_strict) && (_location==null))
    {
      return true;
    }
    EquipmentLocation location=item.getEquipmentLocation();
    if (_location==location)
    {
      return true;
    }
    if (_location==EquipmentLocation.OFF_HAND)
    {
      // If off-hand, allow main hand weapons
      if (item instanceof Weapon)
      {
        return location==EquipmentLocation.MAIN_HAND;
      }
    }
    return false;
  }
}
