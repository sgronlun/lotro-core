package delta.games.lotro.character.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import delta.games.lotro.character.CharacterEquipment;
import delta.games.lotro.character.CharacterEquipment.EQUIMENT_SLOT;
import delta.games.lotro.character.CharacterEquipment.SlotContents;
import delta.games.lotro.character.stats.contribs.StatsContribution;
import delta.games.lotro.character.stats.contribs.StatsContributionsManager;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary2.LegendaryInstance2;
import delta.games.lotro.lore.items.legendary2.LegendaryInstanceAttrs2;
import delta.games.lotro.lore.items.legendary2.SocketEntryInstance;
import delta.games.lotro.lore.items.legendary2.SocketsSetupInstance;
import delta.games.lotro.lore.items.legendary2.Tracery;
import delta.games.lotro.lore.items.sets.ItemsSet;
import delta.games.lotro.lore.items.sets.ItemsSetsManager;
import delta.games.lotro.lore.items.sets.SetBonus;

/**
 * Computes stats contributions from traceries sets.
 * @author DAM
 */
public class TraceriesSetStatsComputer
{
  private ItemsSetsManager _itemsSetsManager;

  /**
   * Constructor;
   */
  public TraceriesSetStatsComputer()
  {
    _itemsSetsManager=ItemsSetsManager.getInstance();
  }

  /**
   * Get the stats contribution from traceries sets.
   * @param characterLevel Character level.
   * @param equipment Equipment to use.
   * @param contribs Contributions manager (optional).
   * @return some stats.
   */
  public BasicStatsSet getStats(int characterLevel, CharacterEquipment equipment, StatsContributionsManager contribs)
  {
    BasicStatsSet stats=new BasicStatsSet();
    List<SocketEntryInstance> sockets=getTraceries(characterLevel,equipment);
    int nbInstances=sockets.size();
    if (nbInstances==0)
    {
      return stats;
    }
    Map<Integer,List<SocketEntryInstance>> traceriesBySet=getTraceriesBySet(sockets);
    for(Map.Entry<Integer,List<SocketEntryInstance>> entry : traceriesBySet.entrySet())
    {
      int setId=entry.getKey().intValue();
      ItemsSet set=_itemsSetsManager.getSetById(setId);
      boolean isApplicable=StatsComputationUtils.setIsApplicable(set,characterLevel);
      if (!isApplicable)
      {
        continue;
      }
      BasicStatsSet statsForSet=new BasicStatsSet();
      int count=entry.getValue().size();
      int level=getSetLevel(set,entry.getValue());
      for(SetBonus bonus : set.getBonuses())
      {
        int countForBonus=bonus.getPiecesCount();
        if (count>=countForBonus)
        {
          StatsProvider statsProvider=bonus.getStatsProvider();
          BasicStatsSet bonusStats=statsProvider.getStats(1,level);
          statsForSet.addStats(bonusStats);
        }
      }
      if (statsForSet.getStatsCount()>0)
      {
        stats.addStats(statsForSet);
        if (contribs!=null)
        {
          StatsContribution contrib=StatsContribution.getTraceriesSetContrib(set,count,statsForSet);
          contribs.addContrib(contrib);
        }
      }
    }
    return stats;
  }

  private int getSetLevel(ItemsSet set, List<SocketEntryInstance> sockets)
  {
    if (set.useAverageItemLevelForSetLevel())
    {
      int totalItemLevels=0;
      for(SocketEntryInstance socket : sockets)
      {
        int itemLevel=socket.getItemLevel();
        totalItemLevels+=itemLevel;
      }
      int nbSockets=sockets.size();
      return totalItemLevels/nbSockets;
    }
    return set.getSetLevel();
  }

  private Map<Integer,List<SocketEntryInstance>> getTraceriesBySet(List<SocketEntryInstance> sockets)
  {
    Map<Integer,List<SocketEntryInstance>> ret=new HashMap<Integer,List<SocketEntryInstance>>();
    for(SocketEntryInstance socket : sockets)
    {
      Tracery tracery=socket.getTracery();
      int setId=tracery.getSetId();
      if (setId==0)
      {
        continue;
      }
      Integer setKey=Integer.valueOf(setId);
      List<SocketEntryInstance> socketsForSet=ret.get(setKey);
      if (socketsForSet==null)
      {
        socketsForSet=new ArrayList<SocketEntryInstance>();
        ret.put(setKey,socketsForSet);
      }
      socketsForSet.add(socket);
    }
    return ret;
  }

  private List<SocketEntryInstance> getTraceries(int characterLevel, CharacterEquipment equipment)
  {
    List<SocketEntryInstance> ret=new ArrayList<SocketEntryInstance>();
    for(EQUIMENT_SLOT slot : EQUIMENT_SLOT.values())
    {
      SlotContents slotContents=equipment.getSlotContents(slot,false);
      if (slotContents==null)
      {
        continue;
      }
      ItemInstance<?> itemInstance=slotContents.getItem();
      if (itemInstance==null)
      {
        continue;
      }
      boolean isApplicable=StatsComputationUtils.itemIsApplicable(itemInstance);
      if (!isApplicable)
      {
        continue;
      }
      boolean isLegendaryInstance2=(itemInstance instanceof LegendaryInstance2);
      if (!isLegendaryInstance2)
      {
        continue;
      }
      LegendaryInstance2 legInstance=(LegendaryInstance2)itemInstance;
      LegendaryInstanceAttrs2 attrs=legInstance.getLegendaryAttributes();
      // Item level
      int itemLevel=itemInstance.getApplicableItemLevel();
      SocketsSetupInstance sockets=attrs.getSocketsSetup();
      int nbSockets=sockets.getSocketsCount();
      for(int i=0;i<nbSockets;i++)
      {
        SocketEntryInstance entry=sockets.getEntry(i);
        boolean enabled=entry.getTemplate().isEnabled(itemLevel);
        if (!enabled)
        {
          continue;
        }
        Tracery tracery=entry.getTracery();
        if (tracery==null)
        {
          continue;
        }
        boolean isTraceryApplicable=tracery.isApplicable(characterLevel,itemLevel);
        if (isTraceryApplicable)
        {
          ret.add(entry);
        }
      }
    }
    return ret;
  }
}