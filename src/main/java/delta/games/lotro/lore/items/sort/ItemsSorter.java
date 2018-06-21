package delta.games.lotro.lore.items.sort;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import delta.common.utils.text.EncodingNames;
import delta.games.lotro.character.CharacterEquipment.EQUIMENT_SLOT;
import delta.games.lotro.lore.items.Armour;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.Weapon;
import delta.games.lotro.lore.items.WeaponType;
import delta.games.lotro.lore.items.comparators.ItemNameComparator;
import delta.games.lotro.lore.items.filters.ItemSlotFilter;
import delta.games.lotro.lore.items.io.xml.ItemXMLWriter;

/**
 * Sort items.
 * @author DAM
 */
public class ItemsSorter
{
  private static final String WEAPON="weapon-";
  private static final String ARMOUR="armour-";

  private HashMap<String,List<Item>> _items;

  /**
   * Constructor.
   */
  public ItemsSorter()
  {
    _items=new HashMap<String,List<Item>>();
    for(EquipmentLocation slot : EquipmentLocation.getAll())
    {
      _items.put(slot.getKey(),new ArrayList<Item>());
    }
  }

  /**
   * Get items that fit a slot.
   * @param slot Targeted slot.
   * @return A list of items.
   */
  public List<Item> getItems(EQUIMENT_SLOT slot)
  {
    EquipmentLocation location=slot.getLocation();
    if ((location==EquipmentLocation.HEAD) || (location==EquipmentLocation.SHOULDER)
        || (location==EquipmentLocation.CHEST) || (location==EquipmentLocation.BACK)
        || (location==EquipmentLocation.LEGS) || (location==EquipmentLocation.FEET)
        || (location==EquipmentLocation.HAND))
    {
      return buildArmoursList(location);
    }
    if ((location==EquipmentLocation.EAR) || (location==EquipmentLocation.NECK)
        || (location==EquipmentLocation.WRIST) || (location==EquipmentLocation.FINGER)
        || (location==EquipmentLocation.POCKET) || (location==EquipmentLocation.CLASS_SLOT)
        || (location==EquipmentLocation.TOOL))
    {
      return getBySlot(location);
    }
    if ((location==EquipmentLocation.MAIN_HAND) || (location==EquipmentLocation.OFF_HAND)
        || (location==EquipmentLocation.RANGED_ITEM))
    {
      EquipmentLocation weaponLoc=location;
      if (location==EquipmentLocation.OFF_HAND) weaponLoc=EquipmentLocation.MAIN_HAND;
      List<Item> items=buildWeaponsList(weaponLoc);
      if (location==EquipmentLocation.OFF_HAND)
      {
        List<Item> shields=buildShieldsList();
        items.addAll(shields);
      }
      List<Item> others=getBySlot(location);
      items.addAll(others);
      return items;
    }
    return new ArrayList<Item>();
  }

  private void filterWeapons(List<Item> weapons)
  {
    HashMap<WeaponType,List<Item>> map=new HashMap<WeaponType,List<Item>>();
    List<Item> others=new ArrayList<Item>();
    for(Item item : weapons)
    {
      Weapon weapon=(Weapon)item;
      WeaponType type=weapon.getWeaponType();
      if (type!=null)
      {
        List<Item> list=map.get(type);
        if (list==null)
        {
          list=new ArrayList<Item>();
          map.put(type,list);
        }
        list.add(item);
      }
      else
      {
        others.add(item);
      }
    }
    for(Map.Entry<WeaponType,List<Item>> entry : map.entrySet())
    {
      WeaponType type=entry.getKey();
      List<Item> list=entry.getValue();
      _items.put(WEAPON+type.getKey(),list);
      //System.out.println("\t"+type+": "+list.size());
    }
    if (others.size()>0)
    {
      System.out.println("\tOther weapons: "+others.size());
      for(Item other : others)
      {
        System.out.println(other.dump());
      }
    }
  }

  private void filterArmours(List<Item> armours)
  {
    List<Item> head=new ArrayList<Item>();
    List<Item> shoulders=new ArrayList<Item>();
    List<Item> back=new ArrayList<Item>();
    List<Item> chest=new ArrayList<Item>();
    List<Item> hands=new ArrayList<Item>();
    List<Item> leggings=new ArrayList<Item>();
    List<Item> feet=new ArrayList<Item>();
    List<Item> shields=new ArrayList<Item>();
    List<Item> others=new ArrayList<Item>();
    for(Item item : armours)
    {
      Armour armour=(Armour)item;
      EquipmentLocation location=armour.getEquipmentLocation();
      if (location==EquipmentLocation.HEAD) head.add(armour);
      else if (location==EquipmentLocation.SHOULDER) shoulders.add(armour);
      else if (location==EquipmentLocation.CHEST) chest.add(armour);
      else if (location==EquipmentLocation.HAND) hands.add(armour);
      else if (location==EquipmentLocation.LEGS) leggings.add(armour);
      else if (location==EquipmentLocation.FEET) feet.add(armour);
      else if (location==EquipmentLocation.BACK) back.add(armour);
      else if (location==EquipmentLocation.OFF_HAND) shields.add(armour);
      else
      {
        ArmourType type=armour.getArmourType();
        if ((type==ArmourType.SHIELD) || (type==ArmourType.HEAVY_SHIELD) || (type==ArmourType.WARDEN_SHIELD))
        {
          shields.add(armour);
        }
        else others.add(armour);
      }
    }
    _items.put(ARMOUR+EquipmentLocation.HEAD.getKey(),head);
    _items.put(ARMOUR+EquipmentLocation.SHOULDER.getKey(),shoulders);
    _items.put(ARMOUR+EquipmentLocation.BACK.getKey(),back);
    _items.put(ARMOUR+EquipmentLocation.CHEST.getKey(),chest);
    _items.put(ARMOUR+EquipmentLocation.HAND.getKey(),hands);
    _items.put(ARMOUR+EquipmentLocation.LEGS.getKey(),leggings);
    _items.put(ARMOUR+EquipmentLocation.FEET.getKey(),feet);
    _items.put(ARMOUR+"SHIELD",shields);
    /*
    System.out.println("\tHead: "+head.size());
    System.out.println("\tShoulders: "+shoulders.size());
    System.out.println("\tBack: "+back.size());
    System.out.println("\tChest: "+chest.size());
    System.out.println("\tHands: "+hands.size());
    System.out.println("\tLeggings: "+leggings.size());
    System.out.println("\tFeet: "+feet.size());
    System.out.println("\tShields: "+shields.size());
    */
    if (others.size()>0)
    {
      System.out.println("\tOther armours: "+others.size());
      for(Item other : others)
      {
        System.out.println(other.dump());
      }
    }
  }

  /**
   * Get items for a given slot.
   * @param location Targeted slot.
   * @return A list of items.
   */
  public List<Item> getBySlot(EquipmentLocation location)
  {
    List<Item> ret=new ArrayList<Item>();
    List<Item> slotItems=_items.get(location.getKey());
    for(Item item : slotItems)
    {
      ret.add(item);
    }
    return ret;
  }

  private void filterOthers(List<Item> items)
  {
    HashMap<String,List<Item>> lists=new HashMap<String,List<Item>>();
    for(Item item : items)
    {
      String category=item.getSubCategory();
      if (category==null) category="???";
      List<Item> itemsForCategogy=lists.get(category);
      if (itemsForCategogy==null)
      {
        itemsForCategogy=new ArrayList<Item>();
        lists.put(category,itemsForCategogy);
      }
      itemsForCategogy.add(item);
    }
    _items.putAll(lists);
  }

  /**
   * Sort items.
   * @param items Items to sort.
   */
  public void sortItems(Collection<Item> items)
  {
    List<Item> weapons=new ArrayList<Item>();
    List<Item> armours=new ArrayList<Item>();
    List<Item> others=new ArrayList<Item>();

    for(Item item : items)
    {
      if (item instanceof Weapon)
      {
        weapons.add(item);
      }
      else
      {
        if (item instanceof Armour)
        {
          armours.add(item);
        }
        else
        {
          EquipmentLocation location=item.getEquipmentLocation();
          if (location!=null)
          {
            List<Item> list=_items.get(location.getKey());
            list.add(item);
          }
          else
          {
            others.add(item);
          }
        }
      }
    }
    //System.out.println("Weapons: "+weapons.size());
    filterWeapons(weapons);
    //System.out.println("Armours: "+armours.size());
    filterArmours(armours);
    //System.out.println("Others: "+others.size());
    filterOthers(others);
  }

  /**
   * Build a list of armour items for a given slot.
   * @param slot Slot.
   * @return A list of items.
   */
  public List<Item> buildArmoursList(EquipmentLocation slot)
  {
    List<Item> ret=new ArrayList<Item>();
    ItemSlotFilter slotFilter=new ItemSlotFilter(slot);

    List<Item> items=_items.get(ARMOUR+slot.getKey());
    for(Item item : items)
    {
      if (slotFilter.accept(item))
      {
        ret.add(item);
      }
    }
    return ret;
  }

  /**
   * Build a list of weapon items for a given slot.
   * @param slot Slot.
   * @return A list of items.
   */
  public List<Item> buildWeaponsList(EquipmentLocation slot)
  {
    List<Item> ret=new ArrayList<Item>();
    ItemSlotFilter slotFilter=new ItemSlotFilter(slot);
    for(WeaponType weaponType : WeaponType.getAll())
    {
      List<Item> items=_items.get(WEAPON+weaponType.getKey());
      if (items!=null)
      {
        for(Item item : items)
        {
          if (slotFilter.accept(item))
          {
            ret.add(item);
          }
        }
      }
    }
    return ret;
  }

  /**
   * Build a list of shield items.
   * @return A list of items.
   */
  public List<Item> buildShieldsList()
  {
    List<Item> shields=_items.get(ARMOUR+"SHIELD");
    return shields;
  }

  /**
   * Build a list of all essences.
   * @return a list of essence items.
   */
  public List<Item> buildEssencesList()
  {
    List<Item> ret=new ArrayList<Item>();
    for(Map.Entry<String,List<Item>> entry : _items.entrySet())
    {
      String category=entry.getKey();
      if (category.startsWith("Essence"))
      {
        List<Item> items=entry.getValue();
        ret.addAll(items);
      }
    }
    Collections.sort(ret,new ItemNameComparator());
    return ret;
  }

  /**
   * Write sorted items to files.
   * @param rootDir Root directory of files.
   */
  public void writeToFiles(File rootDir)
  {
    for(Map.Entry<String,List<Item>> entry : _items.entrySet())
    {
      String category=entry.getKey();
      List<Item> items=entry.getValue();
      if (items.size()>0)
      {
        String filename=buildCategoryFilename(category);
        File out=new File(rootDir,filename);
        ItemXMLWriter writer=new ItemXMLWriter();
        writer.writeItems(out,items,EncodingNames.UTF_8);
      }
    }
  }

  private String buildCategoryFilename(String category)
  {
    String ret=category+".xml";
    ret=ret.replace(":","_");
    return ret;
  }
}
