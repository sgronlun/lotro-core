package delta.games.lotro.character.storage.statistics;

import java.util.List;

import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountOnServer;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.character.storage.StorageUtils;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.statistics.reputation.StorageReputationStats;
import delta.games.lotro.common.statistics.FactionStats;
import delta.games.lotro.common.statistics.items.ItemsStats;
import delta.games.lotro.lore.items.CountedItem;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.reputation.Faction;

/**
 * Simple tool class to compute and show storage statistics for a character.
 * @author DAM
 */
public class MainTestShowStorageStatistics
{
  /**
   * Main method for this tool.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    String serverName="Landroval";
    /*
    CharacterFile toon=CharactersManager.getInstance().getToonById(serverName,"Kargarth");
    CharacterStorage characterStorage=StoragesIO.loadCharacterStorage(toon);
    List<StoredItem> items=StorageUtils.buildCharacterItems(toon,characterStorage);
    */
    String accountName="glorfindel666";
    Account account=AccountsManager.getInstance().getAccountByName(accountName);
    AccountOnServer accountOnServer=account.getServer(serverName);
    List<StoredItem> items=StorageUtils.buildAccountItems(accountOnServer);

    StorageStatistics stats=new StorageStatistics();
    new StorageStatisticsComputer().computeStatistics(items,stats);
    // Item XP
    long totalXP=stats.getTotalItemXP();
    System.out.println("Total item XP: "+totalXP);
    // Reputation stats
    StorageReputationStats reputationStats=stats.getReputationStats();
    for(FactionStats factionStats : reputationStats.getFactionStats())
    {
      Faction faction=factionStats.getFaction();
      int points=factionStats.getPoints();
      System.out.println("Total "+points+" points for faction "+faction.getName());
    }
    // Disenchantment results
    ItemsStats itemStats=stats.getItemStats();
    for(CountedItem<Item> resultItem : itemStats.getItems())
    {
      System.out.println("\t"+resultItem);
    }
    // Total value
    System.out.println("Total value: "+stats.getTotalValue());
  }
}
