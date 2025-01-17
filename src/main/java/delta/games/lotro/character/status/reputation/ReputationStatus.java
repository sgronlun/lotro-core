package delta.games.lotro.character.status.reputation;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionLevel;

/**
 * Reputation status for a single toon.
 * @author DAM
 */
public class ReputationStatus
{
  private HashMap<Faction,FactionStatus> _stats;
  private ReputationDeedsStatus _deedsStatus;
  private int _acquiredLotroPoints;

  /**
   * Constructor.
   */
  public ReputationStatus()
  {
    _stats=new HashMap<Faction,FactionStatus>();
    _deedsStatus=new ReputationDeedsStatus();
  }

  /**
   * Update the data derived from reputation status (deeds and LOTRO points count).
   */
  public void update()
  {
    _deedsStatus.update(this);
    ReputationLotroPointsComputer lpComputer=new ReputationLotroPointsComputer();
    _acquiredLotroPoints=lpComputer.compute(this);
  }

  /**
   * Get the status of reputation deeds.
   * @return the status of reputation deeds.
   */
  public ReputationDeedsStatus getDeedsStatus()
  {
    return _deedsStatus;
  }

  /**
   * Get the reputation status for a given faction.
   * @param faction Targeted faction.
   * @return A reputation status object, or <code>null</code> if the toon does not
   * have the given faction status.
   */
  public FactionStatus getFactionStatus(Faction faction)
  {
    FactionStatus stat=_stats.get(faction);
    return stat;
  }

  /**
   * Get the reputation status for a given faction (create it if necessary).
   * @param faction Targeted faction.
   * @return A reputation status object.
   */
  public FactionStatus getOrCreateFactionStat(Faction faction)
  {
    FactionStatus stat=_stats.get(faction);
    if (stat==null)
    {
      stat=new FactionStatus(faction);
      _stats.put(faction,stat);
    }
    return stat;
  }

  /**
   * Update current faction level for a given faction.
   * @param faction Faction to use.
   * @param increase Increase or decrease.
   */
  public void updateFaction(Faction faction, boolean increase)
  {
    FactionStatus factionStatus=getOrCreateFactionStat(faction);
    FactionLevel currentLevel=factionStatus.getFactionLevel();
    FactionLevel[] levels=faction.getLevels();
    if (increase)
    {
      if (currentLevel==null)
      {
        factionStatus.setFactionLevel(levels[0]);
        factionStatus.setReputation(Integer.valueOf(levels[0].getRequiredReputation()));
      }
      else
      {
        FactionLevel nextLevel=faction.getNext(currentLevel);
        if (nextLevel!=null)
        {
          factionStatus.setFactionLevel(nextLevel);
          factionStatus.setReputation(Integer.valueOf(nextLevel.getRequiredReputation()));
        }
      }
    }
    else
    {
      if (currentLevel==levels[0])
      {
        factionStatus.setFactionLevel(null);
        factionStatus.setReputation(null);
      }
      else if (currentLevel!=null)
      {
        FactionLevel previousLevel=faction.getPrevious(currentLevel);
        if (previousLevel!=null)
        {
          factionStatus.setFactionLevel(previousLevel);
          factionStatus.setReputation(Integer.valueOf(previousLevel.getRequiredReputation()));
        }
      }
    }
  }

  /**
   * Get the number of acquired LOTRO points.
   * @return A LOTRO points count.
   */
  public int getAcquiredLotroPoints()
  {
    return _acquiredLotroPoints;
  }

  /**
   * Dump the contents of this object to the given stream.
   * @param ps Output stream to use.
   */
  public void dump(PrintStream ps)
  {
    ps.println("Reputation status:");
    List<Faction> factions=new ArrayList<Faction>(_stats.keySet());
    //Collections.sort(factions);
    for(Faction faction : factions)
    {
      FactionStatus stat=getFactionStatus(faction);
      stat.dump(ps);
    }
  }
}
