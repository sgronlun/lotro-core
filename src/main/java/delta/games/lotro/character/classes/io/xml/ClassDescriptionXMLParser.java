package delta.games.lotro.character.classes.io.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.classes.ClassSkill;
import delta.games.lotro.character.classes.ClassTrait;
import delta.games.lotro.character.classes.InitialGearDefinition;
import delta.games.lotro.character.classes.InitialGearElement;
import delta.games.lotro.character.classes.proficiencies.io.xml.ClassProficienciesXMLParser;
import delta.games.lotro.character.classes.traitTree.TraitTree;
import delta.games.lotro.character.classes.traitTree.TraitTreesManager;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.skills.SkillsManager;
import delta.games.lotro.character.stats.buffs.BuffSpecification;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.character.traits.TraitsManager;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;

/**
 * Parser for class descriptions stored in XML.
 * @author DAM
 */
public class ClassDescriptionXMLParser
{
  /**
   * Parse a class descriptions XML file.
   * @param source Source file.
   * @return List of parsed class descriptions.
   */
  public static List<ClassDescription> parseClassDescriptionsFile(File source)
  {
    List<ClassDescription> descriptions=new ArrayList<ClassDescription>();
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      List<Element> classTags=DOMParsingTools.getChildTagsByName(root,ClassDescriptionXMLConstants.CLASS_TAG);
      for(Element classTag : classTags)
      {
        ClassDescription description=parseClassDescription(classTag);
        descriptions.add(description);
      }
    }
    return descriptions;
  }

  /**
   * Build a class description from an XML tag.
   * @param root Root XML tag.
   * @return A class description.
   */
  private static ClassDescription parseClassDescription(Element root)
  {
    NamedNodeMap attrs=root.getAttributes();
    // Key
    String classKeyStr=DOMParsingTools.getStringAttribute(attrs,ClassDescriptionXMLConstants.CLASS_KEY_ATTR,null);
    CharacterClass characterClass=CharacterClass.getByKey(classKeyStr);
    ClassDescription description=new ClassDescription(characterClass);
    // Icon ID
    int iconId=DOMParsingTools.getIntAttribute(attrs,ClassDescriptionXMLConstants.CLASS_ICON_ID_ATTR,0);
    description.setIconId(iconId);
    // Small icon ID
    int smallIconId=DOMParsingTools.getIntAttribute(attrs,ClassDescriptionXMLConstants.CLASS_SMALL_ICON_ID_ATTR,0);
    description.setSmallIconId(smallIconId);
    // Abbreviation
    String abbreviation=DOMParsingTools.getStringAttribute(attrs,ClassDescriptionXMLConstants.CLASS_ABBREVIATION_ATTR,"");
    description.setAbbreviation(abbreviation);
    // Description
    String descriptionText=DOMParsingTools.getStringAttribute(attrs,ClassDescriptionXMLConstants.CLASS_DESCRIPTION_ATTR,"");
    description.setDescription(descriptionText);
    // Tactical DPS stat name
    String tacticalDpsStatName=DOMParsingTools.getStringAttribute(attrs,ClassDescriptionXMLConstants.CLASS_TACTICAL_DPS_STAT_NAME_ATTR,"");
    description.setTacticalDpsStatName(tacticalDpsStatName);
    // Trait tree
    int traitTreeID=DOMParsingTools.getIntAttribute(attrs,ClassDescriptionXMLConstants.CLASS_TRAIT_TREE_ID_ATTR,0);
    if (traitTreeID!=0)
    {
      TraitTreesManager traitTreesMgr=TraitTreesManager.getInstance();
      TraitTree traitTree=traitTreesMgr.getTraitTree(traitTreeID);
      description.setTraitTree(traitTree);
    }
    // Proficiencies
    ClassProficienciesXMLParser.parseClassProficiencies(root,description.getProficiencies());
    // Traits
    List<Element> classTraitTags=DOMParsingTools.getChildTagsByName(root,ClassDescriptionXMLConstants.CLASS_TRAIT_TAG);
    for(Element classTraitTag : classTraitTags)
    {
      NamedNodeMap traitAttrs=classTraitTag.getAttributes();
      // Min level
      int minLevel=DOMParsingTools.getIntAttribute(traitAttrs,ClassDescriptionXMLConstants.CLASS_TRAIT_MIN_LEVEL_ATTR,1);
      // Trait ID
      int traitId=DOMParsingTools.getIntAttribute(traitAttrs,ClassDescriptionXMLConstants.CLASS_TRAIT_ID_ATTR,0);
      TraitDescription trait=TraitsManager.getInstance().getTrait(traitId);
      ClassTrait classTrait=new ClassTrait(minLevel,trait);
      description.addTrait(classTrait);
    }
    // Skills
    List<Element> classSkillTags=DOMParsingTools.getChildTagsByName(root,ClassDescriptionXMLConstants.CLASS_SKILL_TAG);
    for(Element classSkillTag : classSkillTags)
    {
      NamedNodeMap skillAttrs=classSkillTag.getAttributes();
      // Min level
      int minLevel=DOMParsingTools.getIntAttribute(skillAttrs,ClassDescriptionXMLConstants.CLASS_SKILL_MIN_LEVEL_ATTR,1);
      // Skill ID
      int skillId=DOMParsingTools.getIntAttribute(skillAttrs,ClassDescriptionXMLConstants.CLASS_SKILL_ID_ATTR,0);
      SkillDescription skill=SkillsManager.getInstance().getSkill(skillId);
      ClassSkill classSkill=new ClassSkill(minLevel,skill);
      description.addSkill(classSkill);
    }
    // Initial gear
    InitialGearDefinition initialGear=description.getInitialGear();
    List<Element> gearTags=DOMParsingTools.getChildTagsByName(root,ClassDescriptionXMLConstants.INITIAL_GEAR_ELEMENT_TAG);
    for(Element gearTag : gearTags)
    {
      NamedNodeMap gearAttrs=gearTag.getAttributes();
      InitialGearElement element=new InitialGearElement();
      // Item ID
      int itemId=DOMParsingTools.getIntAttribute(gearAttrs,ClassDescriptionXMLConstants.GEAR_ITEM_ID_ATTR,0);
      element.setItemId(itemId);
      // Race
      Race requiredRace=null;
      String raceKey=DOMParsingTools.getStringAttribute(gearAttrs,ClassDescriptionXMLConstants.GEAR_REQUIRED_RACE_ATTR,null);
      if (raceKey!=null)
      {
        requiredRace=Race.getByKey(raceKey);
        element.setRequiredRace(requiredRace);
      }
      initialGear.addGearElement(element);
    }
    // Default buffs
    List<Element> buffTags=DOMParsingTools.getChildTagsByName(root,ClassDescriptionXMLConstants.DEFAULT_BUFF_TAG);
    for(Element buffTag : buffTags)
    {
      NamedNodeMap buffAttrs=buffTag.getAttributes();
      // Buff ID
      String buffId=DOMParsingTools.getStringAttribute(buffAttrs,ClassDescriptionXMLConstants.DEFAULT_BUFF_ID_ATTR,null);
      // Tier
      int tierValue=DOMParsingTools.getIntAttribute(buffAttrs,ClassDescriptionXMLConstants.DEFAULT_BUFF_TIER,0);
      Integer tier=(tierValue!=0)?Integer.valueOf(tierValue):null;
      BuffSpecification buff=new BuffSpecification(buffId,tier);
      description.addDefaultBuff(buff);
    }
    return description;
  }
}
