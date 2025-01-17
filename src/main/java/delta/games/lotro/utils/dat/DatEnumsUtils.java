package delta.games.lotro.utils.dat;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.gear.GearSlot;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;

/**
 * Misc enum utils.
 * @author DAM
 */
public class DatEnumsUtils
{
  private static final String SLOT_PROPERTY_NAME_SEED="Inventory_SlotCache_Eq_";

  /**
   * Get a character class from a DAT enum code.
   * @param id Input code.
   * @return A character class or <code>null</code> if not supported.
   */
  public static CharacterClass getCharacterClassFromId(int id)
  {
    if (id==214) return CharacterClass.BEORNING;
    if (id==40) return CharacterClass.BURGLAR;
    if (id==215) return CharacterClass.BRAWLER;
    if (id==24) return CharacterClass.CAPTAIN;
    if (id==172) return CharacterClass.CHAMPION;
    if (id==23) return CharacterClass.GUARDIAN;
    if (id==162) return CharacterClass.HUNTER;
    if (id==185) return CharacterClass.LORE_MASTER;
    if (id==31) return CharacterClass.MINSTREL;
    if (id==193) return CharacterClass.RUNE_KEEPER;
    if (id==194) return CharacterClass.WARDEN;
    // Monster Play
    if (id==71) return CharacterClass.REAVER;
    if (id==128) return CharacterClass.DEFILER;
    if (id==127) return CharacterClass.WEAVER;
    if (id==179) return CharacterClass.BLACKARROW;
    if (id==52) return CharacterClass.WARLEADER;
    if (id==126) return CharacterClass.STALKER;
    return null;
  }

  /**
   * Get a race from a DAT enum code.
   * @param raceId Input code.
   * @return A race or <code>null</code> if not supported.
   */
  public static Race getRaceFromRaceId(int raceId)
  {
    if (raceId==23) return Race.MAN;
    if (raceId==65) return Race.ELF;
    if (raceId==73) return Race.DWARF;
    if (raceId==81) return Race.HOBBIT;
    if (raceId==114) return Race.BEORNING;
    if (raceId==117) return Race.HIGH_ELF;
    if (raceId==120) return Race.STOUT_AXE_DWARF;
    return null;
  }

  /**
   * Indicates if the given slot code is for an equipped item.
   * @param slotCode Code to use.
   * @return <code>true</code> if equipped, <code>false</code> otherwise.
   */
  public static boolean isEquipped(int slotCode)
  {
    return ((slotCode&1L<<29)!=0);
  }

  /**
   * Indicates if the given slot code means an item in the bags.
   * @param slotCode Code to use.
   * @return <code>true</code> if it does, <code>false</code> otherwise.
   */
  public static boolean isInBags(int slotCode)
  {
    return ((slotCode&1L<<30)!=0);
  }

  /**
   * Indicates if the given slot code means an item in the overflow.
   * @param slotCode Code to use.
   * @return <code>true</code> if it does, <code>false</code> otherwise.
   */
  public static boolean isInOverflow(int slotCode)
  {
    return ((slotCode&1L<<31)!=0);
  }

  /**
   * Get an equipment slot from a DAT enum code.
   * @param slotCode Input code.
   * @return An equipment slot or <code>null</code> if not supported.
   */
  public static GearSlot getEquipmentSlot(long slotCode)
  {
    // See Enum: ContainerSlot, (id=587202798)
    if ((slotCode&1L<<1)!=0) return GearSlot.HEAD;
    if ((slotCode&1L<<2)!=0) return GearSlot.BREAST;
    if ((slotCode&1L<<3)!=0) return GearSlot.LEGS;
    if ((slotCode&1L<<4)!=0) return GearSlot.HANDS;
    if ((slotCode&1L<<5)!=0) return GearSlot.FEET;
    if ((slotCode&1L<<6)!=0) return GearSlot.SHOULDER;
    if ((slotCode&1L<<7)!=0) return GearSlot.BACK;
    if ((slotCode&1L<<8)!=0) return GearSlot.LEFT_WRIST;
    if ((slotCode&1L<<9)!=0) return GearSlot.RIGHT_WRIST;
    if ((slotCode&1L<<10)!=0) return GearSlot.NECK;
    if ((slotCode&1L<<11)!=0) return GearSlot.LEFT_FINGER;
    if ((slotCode&1L<<12)!=0) return GearSlot.RIGHT_FINGER;
    if ((slotCode&1L<<13)!=0) return GearSlot.LEFT_EAR;
    if ((slotCode&1L<<14)!=0) return GearSlot.RIGHT_EAR;
    if ((slotCode&1L<<15)!=0) return GearSlot.POCKET;
    if ((slotCode&1L<<16)!=0) return GearSlot.MAIN_MELEE;
    if ((slotCode&1L<<17)!=0) return GearSlot.OTHER_MELEE;
    if ((slotCode&1L<<18)!=0) return GearSlot.RANGED;
    if ((slotCode&1L<<19)!=0) return GearSlot.TOOL;
    if ((slotCode&1L<<20)!=0) return GearSlot.CLASS_ITEM;
    //if ((slotCode&1L<<21)!=0) return EQUIMENT_SLOT.BRIDLE;
    if ((slotCode&1L<<22)!=0) return GearSlot.MAIN_HAND_AURA;
    if ((slotCode&1L<<23)!=0) return GearSlot.OFF_HAND_AURA;
    if ((slotCode&1L<<24)!=0) return GearSlot.RANGED_AURA;
    /*
    26 => Last
    29 => Mail
    30 => Equipment
    31 => Backpack
    32 => Overflow
   */
    return null;
  }

  /**
   * Get the equipment slots from a slot bitfield.
   * @param slotCode Input code.
   * @return A opossibly empty but never <code>null</code> list of equipment slots.
   */
  public static List<GearSlot> getEquipmentSlots(long slotCode)
  {
    List<GearSlot> ret=new ArrayList<GearSlot>();
    // See Enum: ContainerSlot, (id=587202798)
    if ((slotCode&1L<<1)!=0) ret.add(GearSlot.HEAD);
    if ((slotCode&1L<<2)!=0) ret.add(GearSlot.BREAST);
    if ((slotCode&1L<<3)!=0) ret.add(GearSlot.LEGS);
    if ((slotCode&1L<<4)!=0) ret.add(GearSlot.HANDS);
    if ((slotCode&1L<<5)!=0) ret.add(GearSlot.FEET);
    if ((slotCode&1L<<6)!=0) ret.add(GearSlot.SHOULDER);
    if ((slotCode&1L<<7)!=0) ret.add(GearSlot.BACK);
    if ((slotCode&1L<<8)!=0) ret.add(GearSlot.LEFT_WRIST);
    if ((slotCode&1L<<9)!=0) ret.add(GearSlot.RIGHT_WRIST);
    if ((slotCode&1L<<10)!=0) ret.add(GearSlot.NECK);
    if ((slotCode&1L<<11)!=0) ret.add(GearSlot.LEFT_FINGER);
    if ((slotCode&1L<<12)!=0) ret.add(GearSlot.RIGHT_FINGER);
    if ((slotCode&1L<<13)!=0) ret.add(GearSlot.LEFT_EAR);
    if ((slotCode&1L<<14)!=0) ret.add(GearSlot.RIGHT_EAR);
    if ((slotCode&1L<<15)!=0) ret.add(GearSlot.POCKET);
    if ((slotCode&1L<<16)!=0) ret.add(GearSlot.MAIN_MELEE);
    if ((slotCode&1L<<17)!=0) ret.add(GearSlot.OTHER_MELEE);
    if ((slotCode&1L<<18)!=0) ret.add(GearSlot.RANGED);
    if ((slotCode&1L<<19)!=0) ret.add(GearSlot.TOOL);
    if ((slotCode&1L<<20)!=0) ret.add(GearSlot.CLASS_ITEM);
    //if ((slotCode&1L<<21)!=0) ret.add(EQUIMENT_SLOT.BRIDLE;
    if ((slotCode&1L<<22)!=0) ret.add(GearSlot.MAIN_HAND_AURA);
    if ((slotCode&1L<<23)!=0) ret.add(GearSlot.OFF_HAND_AURA);
    if ((slotCode&1L<<24)!=0) ret.add(GearSlot.RANGED_AURA);
    /*
    26 => Last
    29 => Mail
    30 => Equipment
    31 => Backpack
    32 => Overflow
   */
    return ret;
  }

  /**
   * Get the slot associated to the given property name.
   * @param propertyName A property name.
   * @return A slot, if any.
   */
  public static GearSlot getSlotFromPropertyName(String propertyName)
  {
    if (propertyName.startsWith(SLOT_PROPERTY_NAME_SEED))
    {
      String slotKey=propertyName.substring(SLOT_PROPERTY_NAME_SEED.length());
      if ("Earring1".equals(slotKey)) return GearSlot.LEFT_EAR;
      else if ("Earring2".equals(slotKey)) return GearSlot.RIGHT_EAR;
      else if ("Bracelet1".equals(slotKey)) return GearSlot.LEFT_WRIST;
      else if ("Bracelet2".equals(slotKey)) return GearSlot.RIGHT_WRIST;
      else if ("Ring1".equals(slotKey)) return GearSlot.LEFT_FINGER;
      else if ("Ring2".equals(slotKey)) return GearSlot.RIGHT_FINGER;
      else if ("Pocket1".equals(slotKey)) return GearSlot.POCKET;
      else if ("Necklace".equals(slotKey)) return GearSlot.NECK;
      else if ("Head".equals(slotKey)) return GearSlot.HEAD;
      else if ("Shoulder".equals(slotKey)) return GearSlot.SHOULDER;
      else if ("Chest".equals(slotKey)) return GearSlot.BREAST;
      else if ("Back".equals(slotKey)) return GearSlot.BACK;
      else if ("Gloves".equals(slotKey)) return GearSlot.HANDS;
      else if ("Legs".equals(slotKey)) return GearSlot.LEGS;
      else if ("Boots".equals(slotKey)) return GearSlot.FEET;
      else if ("Weapon_Primary".equals(slotKey)) return GearSlot.MAIN_MELEE;
      else if ("Weapon_Secondary".equals(slotKey)) return GearSlot.OTHER_MELEE;
      else if ("Weapon_Ranged".equals(slotKey)) return GearSlot.RANGED;
      else if ("CraftTool".equals(slotKey)) return GearSlot.TOOL;
      else if ("Class".equals(slotKey)) return GearSlot.CLASS_ITEM;
      //else if ("Mounted".equals(slotKey)) return EQUIMENT_SLOT.BRIDLE;
    }
    return null;
  }
}
