package delta.games.lotro.lore.consumables.io.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.common.enums.ItemClass;
import delta.games.lotro.common.enums.LotroEnum;
import delta.games.lotro.common.enums.LotroEnumsRegistry;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.common.stats.io.xml.StatsProviderXMLParser;
import delta.games.lotro.lore.consumables.Consumable;

/**
 * Parser for consumables stored in XML.
 * @author DAM
 */
public class ConsumableXMLParser
{
  /**
   * Parse consumables from an XML file.
   * @param source Source file.
   * @return List of parsed consumables.
   */
  public static List<Consumable> parseConsumablesFile(File source)
  {
    List<Consumable> consumables=new ArrayList<Consumable>();
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      List<Element> consumableTags=DOMParsingTools.getChildTagsByName(root,ConsumableXMLConstants.CONSUMABLE_TAG);
      for(Element consumableTag : consumableTags)
      {
        Consumable consumable=parseConsumable(consumableTag);
        consumables.add(consumable);
      }
    }
    return consumables;
  }

  /**
   * Build a consumable from an XML tag.
   * @param root Root XML tag.
   * @return A consumable.
   */
  private static Consumable parseConsumable(Element root)
  {
    NamedNodeMap attrs=root.getAttributes();
    // Identifier
    int id=DOMParsingTools.getIntAttribute(attrs,ConsumableXMLConstants.CONSUMABLE_IDENTIFIER_ATTR,0);
    // Name
    String name=DOMParsingTools.getStringAttribute(attrs,ConsumableXMLConstants.CONSUMABLE_NAME_ATTR,null);
    // Icon ID
    String iconId=DOMParsingTools.getStringAttribute(attrs,ConsumableXMLConstants.CONSUMABLE_ICON_ID_ATTR,null);
    // Category
    ItemClass itemClass=null;
    int itemClassCode=DOMParsingTools.getIntAttribute(attrs,ConsumableXMLConstants.CONSUMABLE_CLASS_ATTR,-1);
    if (itemClassCode>=0)
    {
      LotroEnum<ItemClass> itemClassEnum=LotroEnumsRegistry.getInstance().get(ItemClass.class);
      itemClass=itemClassEnum.getEntry(itemClassCode);
    }
    Consumable consumable=new Consumable(id,name,iconId,itemClass);
    // Stats
    StatsProvider statsProvider=consumable.getProvider();
    StatsProviderXMLParser.parseStatsProvider(root,statsProvider);
    return consumable;
  }
}
