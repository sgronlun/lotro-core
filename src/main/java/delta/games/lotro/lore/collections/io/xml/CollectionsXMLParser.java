package delta.games.lotro.lore.collections.io.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.common.enums.CollectionCategory;
import delta.games.lotro.common.enums.LotroEnum;
import delta.games.lotro.common.enums.LotroEnumsRegistry;
import delta.games.lotro.common.requirements.RaceRequirement;
import delta.games.lotro.common.requirements.io.xml.UsageRequirementXMLConstants;
import delta.games.lotro.common.rewards.io.xml.RewardsXMLParser;
import delta.games.lotro.lore.collections.Collectable;
import delta.games.lotro.lore.collections.CollectionDescription;
import delta.games.lotro.lore.collections.mounts.MountsManager;
import delta.games.lotro.lore.collections.pets.CosmeticPetsManager;

/**
 * Parser for collections stored in XML.
 * @author DAM
 */
public class CollectionsXMLParser
{
  private static final Logger LOGGER=Logger.getLogger(CollectionsXMLParser.class);

  /**
   * Parse collections from an XML file.
   * @param source Source file.
   * @return List of parsed collections.
   */
  public static List<CollectionDescription> parseCollectionsFile(File source)
  {
    List<CollectionDescription> collections=new ArrayList<CollectionDescription>();
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      List<Element> collectionTags=DOMParsingTools.getChildTagsByName(root,CollectionsXMLConstants.COLLECTION_TAG);
      for(Element collectionTag : collectionTags)
      {
        CollectionDescription collection=parseCollection(collectionTag);
        collections.add(collection);
      }
    }
    return collections;
  }

  /**
   * Build a collection from an XML tag.
   * @param root Root XML tag.
   * @return A collection.
   */
  private static CollectionDescription parseCollection(Element root)
  {
    NamedNodeMap attrs=root.getAttributes();
    // Identifier
    int id=DOMParsingTools.getIntAttribute(attrs,CollectionsXMLConstants.COLLECTION_IDENTIFIER_ATTR,0);
    CollectionDescription ret=new CollectionDescription(id);
    // Name
    String name=DOMParsingTools.getStringAttribute(attrs,CollectionsXMLConstants.COLLECTION_NAME_ATTR,"");
    ret.setName(name);
    // Category
    int categoryCode=DOMParsingTools.getIntAttribute(attrs,CollectionsXMLConstants.COLLECTION_CATEGORY_ATTR,0);
    LotroEnum<CollectionCategory> categoryEnum=LotroEnumsRegistry.getInstance().get(CollectionCategory.class);
    CollectionCategory category=categoryEnum.getEntry(categoryCode);
    ret.setCategory(category);
    // Race requirement
    String raceRequirementStr=DOMParsingTools.getStringAttribute(attrs,UsageRequirementXMLConstants.REQUIRED_RACE_ATTR,null);
    RaceRequirement raceRequirement=RaceRequirement.fromString(raceRequirementStr);
    ret.setRaceRequirement(raceRequirement);
    // Elements
    for(Element elementTag : DOMParsingTools.getChildTagsByName(root,CollectionsXMLConstants.ELEMENT_TAG))
    {
      NamedNodeMap elementAttrs=elementTag.getAttributes();
      int elementID=DOMParsingTools.getIntAttribute(elementAttrs,CollectionsXMLConstants.COLLECTION_ELEMENT_ID_ATTR,0);
      Collectable element=findCollectable(elementID);
      if (element!=null)
      {
        ret.addElement(element);
      }
      else
      {
        LOGGER.warn("Collectable not found: "+elementID);
      }
    }
    // Rewards
    RewardsXMLParser.loadRewards(root,ret.getRewards());
    return ret;
  }

  private static Collectable findCollectable(int id)
  {
    MountsManager mountsMgr=MountsManager.getInstance();
    Collectable ret=mountsMgr.getMount(id);
    if (ret==null)
    {
      CosmeticPetsManager petsMgr=CosmeticPetsManager.getInstance();
      ret=petsMgr.getPet(id);
    }
    return ret;
  }
}
