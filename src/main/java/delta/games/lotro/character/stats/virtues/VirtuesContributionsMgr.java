package delta.games.lotro.character.stats.virtues;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.computer.StatsStorage;
import delta.games.lotro.character.stats.contribs.StatsContribution;
import delta.games.lotro.character.virtues.VirtueDescription;
import delta.games.lotro.character.virtues.VirtuesManager;
import delta.games.lotro.common.progression.ProgressionsManager;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.utils.maths.Progression;

/**
 * Manager for contributions of virtues to player stats.
 * @author DAM
 */
public final class VirtuesContributionsMgr
{
  private static final Logger LOGGER=Logger.getLogger(VirtuesContributionsMgr.class);

  private static final int RANK_TO_LEVEL_PROGRESSION_ID=1879387583;

  private Progression _rankToLevelProgression;

  private static final VirtuesContributionsMgr _instance=new VirtuesContributionsMgr();

  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
  public static VirtuesContributionsMgr get()
  {
    return _instance;
  }

  /**
   * Constructor.
   */
  private VirtuesContributionsMgr()
  {
    _rankToLevelProgression=ProgressionsManager.getInstance().getProgression(RANK_TO_LEVEL_PROGRESSION_ID);
    if (_rankToLevelProgression==null)
    {
      LOGGER.warn("Could not load progression "+RANK_TO_LEVEL_PROGRESSION_ID+" (virtue rank to level)");
    }
  }

  /**
   * Get the contribution for a given virtue and rank.
   * @param virtue Virtue to use.
   * @param rank Rank (starting at 1).
   * @param passive Passive stats or active stats?
   * @return A stats set or <code>null</code> if not found.
   */
  public BasicStatsSet getContribution(VirtueDescription virtue, int rank, boolean passive)
  {
    BasicStatsSet stats=null;
    // For passives, virtues with rank 0 seem to count as rank 1
    if ((passive) && (rank==0))
    {
      rank=1;
    }
    if (rank>0)
    {
      int level=_rankToLevelProgression.getValue(rank).intValue();
      StatsProvider statsProvider;
      if (passive)
      {
        statsProvider=virtue.getPassiveStatsProvider();
      }
      else
      {
        statsProvider=virtue.getStatsProvider();
      }
      stats=statsProvider.getStats(1,level);
    }
    if (stats==null)
    {
      stats=new BasicStatsSet();
    }
    return stats;
  }

  /**
   * Get stats contribution for a set of virtues.
   * @param virtuesSet Virtues set.
   * @param includeActives Include stats for active virtues or not.
   * @param includePassives Include stats for passive virtues or not.
   * @return Some stats contributions.
   */
  public BasicStatsSet getStatsContributions(VirtuesSet virtuesSet, boolean includeActives, boolean includePassives)
  {
    List<StatsContribution> contribs=getContributions(virtuesSet,includeActives,includePassives);
    StatsStorage storage=new StatsStorage();
    storage.addContribs(contribs);
    return storage.aggregate();
  }

  /**
   * Get stats contribution for a set of virtues.
   * @param virtuesSet Virtues set.
   * @param includeActives Include stats for active virtues or not.
   * @param includePassives Include stats for passive virtues or not.
   * @return Some stats contributions.
   */
  public List<StatsContribution> getContributions(VirtuesSet virtuesSet, boolean includeActives, boolean includePassives)
  {
    List<StatsContribution> ret=new ArrayList<StatsContribution>();
    List<VirtueDescription> virtues=VirtuesManager.getInstance().getAll();
    for(VirtueDescription virtue : virtues)
    {
      int rank=virtuesSet.getVirtueRank(virtue);
      if (includePassives)
      {
        // For passives, virtues with rank 0 seem to count as rank 1
        int rankForPassives=rank;
        if (rankForPassives==0)
        {
          rankForPassives=1;
        }
        BasicStatsSet passiveStats=getContribution(virtue,rankForPassives,true);
        StatsContribution passiveContrib=StatsContribution.getPassiveVirtuesContrib(virtue,rankForPassives,passiveStats);
        ret.add(passiveContrib);
      }
      if (includeActives)
      {
        boolean selected=virtuesSet.isSelected(virtue);
        if (selected)
        {
          int bonus=virtuesSet.getVirtueBonusRank(virtue);
          int rankToUse=rank+bonus;
          BasicStatsSet activeStats=getContribution(virtue,rankToUse,false);
          StatsContribution activeContrib=StatsContribution.getVirtueContrib(virtue,rankToUse,activeStats);
          ret.add(activeContrib);
        }
      }
    }
    return ret;
  }
}
