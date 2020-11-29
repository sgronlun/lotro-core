package delta.games.lotro.common.stats;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.StatsSetElement;
import delta.games.lotro.utils.FixedDecimalsInteger;

/**
 * Utility methods for stats.
 * @author DAM
 */
public class StatUtils
{
  /**
   * Placeholder for value in description overrides.
   */
  public static final String VALUE_PLACE_HOLDER="{***}";
  /**
   * Special value for "no description".
   */
  public static final String NO_DESCRIPTION="-";

  /**
   * Get a displayable string for a stat value.
   * @param value Value to display.
   * @param percentage Indicates if the stat is a percentage or not.
   * @return A displayable string.
   */
  public static String getStatDisplay(FixedDecimalsInteger value, boolean percentage)
  {
    String valueStr;
    if (value!=null)
    {
      if (percentage)
      {
        valueStr=new DecimalFormat("#.##%").format(value.doubleValue()/100);
      }
      else
      {
        valueStr=String.format("%d",Integer.valueOf(value.intValue()));
      }
    }
    else
    {
      valueStr="-";
    }
    return valueStr;
  }

  /**
   * Get the display lines for some stats.
   * @param stats Stats to display.
   * @return A possibly empty but not <code>null</code> array of stat lines.
   */
  public static String[] getStatsDisplayLines(BasicStatsSet stats)
  {
    List<String> lines=new ArrayList<String>();
    for(StatsSetElement element : stats.getStatElements())
    {
      StatDescription stat=element.getStat();
      if (!stat.isVisible())
      {
        continue;
      }
      String line=getStatDisplay(element);
      if (line==null)
      {
        continue;
      }
      int hasLF=line.indexOf("\n");
      if (hasLF!=-1)
      {
        lines.add(line.substring(0,hasLF));
        lines.add(line.substring(hasLF+1));
      }
      else
      {
        lines.add(line);
      }
    }
    String[] ret=lines.toArray(new String[lines.size()]);
    return ret;
  }

  /**
   * Get the display line for a single stat element.
   * @param element Stat element to display.
   * @return A displayable line or <code>null</code> if display is to be avoided.
   */
  public static String getStatDisplay(StatsSetElement element)
  {
    String line=null;
    StatDescription stat=element.getStat();
    String statName=stat.getName();
    FixedDecimalsInteger value=element.getValue();
    String valueStr=getStatDisplay(value,stat.isPercentage());
    String descriptionOverride=element.getDescriptionOverride();
    if (descriptionOverride!=null)
    {
      if (!NO_DESCRIPTION.equals(descriptionOverride))
      {
        line=descriptionOverride;
        int index=descriptionOverride.indexOf(VALUE_PLACE_HOLDER);
        if (index!=-1)
        {
          line=descriptionOverride.replace(VALUE_PLACE_HOLDER,valueStr);
        }
      }
    }
    else
    {
      line=valueStr+" "+statName;
    }
    return line;
  }

  /**
   * Fix the value of a stat:
   * <ul>
   * <li>percentage stats are given in % values (i.e 1 becomes 100%).
   * <li>regen ?C?R stats are given in units/minutes (i.e x60).
   * </ul>
   * @param stat Targeted stat.
   * @param statValue Raw value.
   * @return the fixed value.
   */
  public static float fixStatValue(StatDescription stat, float statValue)
  {
    if (stat.isPercentage())
    {
      statValue=statValue*100;
    }
    if ((stat==WellKnownStat.ICMR) || (stat==WellKnownStat.ICPR) || (stat==WellKnownStat.OCMR) || (stat==WellKnownStat.OCPR))
    {
      statValue=statValue*60;
    }
    return statValue;
  }
}
