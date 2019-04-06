package delta.games.lotro.lore.quests.io.xml;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.helpers.AttributesImpl;

import delta.common.utils.io.xml.XmlFileWriterHelper;
import delta.common.utils.io.xml.XmlWriter;
import delta.common.utils.text.EncodingNames;
import delta.games.lotro.common.IdentifiableComparator;
import delta.games.lotro.common.Size;
import delta.games.lotro.common.rewards.io.xml.RewardsXMLWriter;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestDescription.FACTION;
import delta.games.lotro.utils.Proxy;

/**
 * Writes LOTRO quests to XML files.
 * @author DAM
 */
public class QuestXMLWriter
{
  /**
   * Write a file with quests.
   * @param toFile Output file.
   * @param quests Quests to write.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public static boolean writeQuestsFile(File toFile, List<QuestDescription> quests)
  {
    QuestXMLWriter writer=new QuestXMLWriter();
    Collections.sort(quests,new IdentifiableComparator<QuestDescription>());
    boolean ok=writer.writeQuests(toFile,quests,EncodingNames.UTF_8);
    return ok;
  }

  /**
   * Write quests to a XML file.
   * @param outFile Output file.
   * @param quests Quests to write.
   * @param encoding Encoding to use.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean writeQuests(File outFile, final List<QuestDescription> quests, String encoding)
  {
    XmlWriter writer=new XmlWriter()
    {
      @Override
      public void writeXml(TransformerHandler hd) throws Exception
      {
        hd.startElement("","",QuestXMLConstants.QUESTS_TAG,new AttributesImpl());
        for(QuestDescription quest : quests)
        {
          write(hd,quest);
        }
        hd.endElement("","",QuestXMLConstants.QUESTS_TAG);
      }
    };
    XmlFileWriterHelper helper=new XmlFileWriterHelper();
    boolean ret=helper.write(outFile,encoding,writer);
    return ret;
  }

  private void write(TransformerHandler hd, QuestDescription quest) throws Exception
  {
    AttributesImpl questAttrs=new AttributesImpl();

    int id=quest.getIdentifier();
    if (id!=0)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_ID_ATTR,XmlWriter.CDATA,String.valueOf(id));
    }
    String name=quest.getName();
    if (name.length()>0)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_NAME_ATTR,XmlWriter.CDATA,name);
    }
    String category=quest.getCategory();
    if (category!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_CATEGORY_ATTR,XmlWriter.CDATA,category);
    }
    String scope=quest.getQuestScope();
    if (scope!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_SCOPE_ATTR,XmlWriter.CDATA,scope);
    }
    String arc=quest.getQuestArc();
    if (arc!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_ARC_ATTR,XmlWriter.CDATA,arc);
    }
    Integer minLevel=quest.getMinimumLevel();
    if (minLevel!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_MIN_LEVEL_ATTR,XmlWriter.CDATA,String.valueOf(minLevel));
    }
    Integer maxLevel=quest.getMaximumLevel();
    if (maxLevel!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_MAX_LEVEL_ATTR,XmlWriter.CDATA,String.valueOf(maxLevel));
    }
    Size size=quest.getSize();
    if (size!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_SIZE_ATTR,XmlWriter.CDATA,size.name());
    }
    FACTION faction=quest.getFaction();
    if (faction!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_FACTION_ATTR,XmlWriter.CDATA,faction.name());
    }
    boolean repeatable=quest.isRepeatable();
    questAttrs.addAttribute("","",QuestXMLConstants.QUEST_REPEATABLE_ATTR,XmlWriter.CDATA,String.valueOf(repeatable));
    // Instanced?
    boolean instanced=quest.isInstanced();
    if (instanced)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_INSTANCED_ATTR,XmlWriter.CDATA,String.valueOf(instanced));
    }
    // Shareable?
    boolean shareable=quest.isShareable();
    if (!shareable)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_SHAREABLE_ATTR,XmlWriter.CDATA,String.valueOf(shareable));
    }
    // Session play?
    boolean sessionPlay=quest.isSessionPlay();
    if (sessionPlay)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_SESSION_PLAY_ATTR,XmlWriter.CDATA,String.valueOf(sessionPlay));
    }
    // Auto-bestowed?
    boolean autoBestowed=quest.isAutoBestowed();
    if (autoBestowed)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_AUTO_BESTOWED_ATTR,XmlWriter.CDATA,String.valueOf(autoBestowed));
    }
    String description=quest.getDescription();
    if (description!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_DESCRIPTION_ATTR,XmlWriter.CDATA,description);
    }
    String bestower=quest.getBestower();
    if (bestower!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_BESTOWER_ATTR,XmlWriter.CDATA,bestower);
    }
    String bestowerText=quest.getBestowerText();
    if (bestowerText!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_BESTOWER_TEXT_ATTR,XmlWriter.CDATA,bestowerText);
    }
    String objectives=quest.getObjectives();
    if (objectives!=null)
    {
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_OBJECTIVES_ATTR,XmlWriter.CDATA,objectives);
    }

    hd.startElement("","",QuestXMLConstants.QUEST_TAG,questAttrs);

    List<String> requiredClasses=quest.getRequiredClasses();
    if (requiredClasses!=null)
    {
      for(String requiredClass : requiredClasses)
      {
        AttributesImpl attrs=new AttributesImpl();
        attrs.addAttribute("","",QuestXMLConstants.REQUIRED_CLASS_NAME_ATTR,XmlWriter.CDATA,requiredClass);
        hd.startElement("","",QuestXMLConstants.REQUIRED_CLASS_TAG,attrs);
        hd.endElement("","",QuestXMLConstants.REQUIRED_CLASS_TAG);
      }
    }
    
    List<String> requiredRaces=quest.getRequiredRaces();
    if (requiredRaces!=null)
    {
      for(String requiredRace : requiredRaces)
      {
        AttributesImpl attrs=new AttributesImpl();
        attrs.addAttribute("","",QuestXMLConstants.REQUIRED_RACE_NAME_ATTR,XmlWriter.CDATA,requiredRace);
        hd.startElement("","",QuestXMLConstants.REQUIRED_RACE_TAG,attrs);
        hd.endElement("","",QuestXMLConstants.REQUIRED_RACE_TAG);
      }
    }
    // Pre-requisite quests
    List<Proxy<QuestDescription>> prerequisiteQuests=quest.getPrerequisiteQuests();
    for(Proxy<QuestDescription> prerequisiteQuest : prerequisiteQuests)
    {
      writeQuestProxy(hd,prerequisiteQuest,QuestXMLConstants.PREREQUISITE_TAG);
    }
    // Next quest
    Proxy<QuestDescription> nextQuest=quest.getNextQuest();
    writeQuestProxy(hd,nextQuest,QuestXMLConstants.NEXT_QUEST_TAG);
    RewardsXMLWriter.write(hd,quest.getQuestRewards());
    hd.endElement("","",QuestXMLConstants.QUEST_TAG);
  }

  private void writeQuestProxy(TransformerHandler hd, Proxy<QuestDescription> proxy, String tag) throws Exception
  {
    if (proxy!=null)
    {
      AttributesImpl questAttrs=new AttributesImpl();
      int id=proxy.getId();
      questAttrs.addAttribute("","",QuestXMLConstants.QUEST_PROXY_ID_ATTR,XmlWriter.CDATA,String.valueOf(id));
      String name=proxy.getName();
      if (name!=null)
      {
        questAttrs.addAttribute("","",QuestXMLConstants.QUEST_PROXY_NAME_ATTR,XmlWriter.CDATA,name);
      }
      hd.startElement("","",tag,questAttrs);
      hd.endElement("","",tag);
    }
  }
}
