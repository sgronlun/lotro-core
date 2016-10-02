package delta.games.lotro.character;

import java.io.File;
import java.util.Date;

import delta.common.utils.text.EncodingNames;
import delta.games.lotro.character.io.xml.CharacterSummaryXMLParser;
import delta.games.lotro.character.io.xml.CharacterSummaryXMLWriter;
import delta.games.lotro.character.level.LevelHistory;
import delta.games.lotro.character.level.LevelHistoryComputer;
import delta.games.lotro.character.level.io.xml.LevelHistoryXMLParser;
import delta.games.lotro.character.level.io.xml.LevelHistoryXMLWriter;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogsManager;

/**
 * Character file description.
 * @author DAM
 */
public class CharacterFile
{
  private File _rootDir;
  private CharacterInfosManager _infosManager;
  private CharacterLogsManager _logsManager;
  private LevelHistory _levelHistory;
  private CharacterSummary _summary;

  /**
   * Constructor.
   * @param rootDir Root directory for all toon files.
   */
  public CharacterFile(File rootDir)
  {
    _rootDir=rootDir;
    _infosManager=new CharacterInfosManager(this);
    _logsManager=new CharacterLogsManager(this);
  }

  /**
   * Get the summary for this toon.
   * @return a summary or <code>null</code> if it could not be built!
   */
  public CharacterSummary getSummary()
  {
    if (_summary==null)
    {
      _summary=loadSummary();
    }
    if (_summary==null)
    {
      CharacterSummary summary=_infosManager.buildSummaryFromNewestData();
      if (summary!=null)
      {
        boolean ok=saveSummary(summary);
        if (ok)
        {
          _summary=summary;
        }
      }
    }
    return _summary;
  }

  private CharacterSummary loadSummary()
  {
    CharacterSummary summary=null;
    File summaryFile=getSummaryFile();
    if (summaryFile.exists())
    {
      CharacterSummaryXMLParser parser=new CharacterSummaryXMLParser();
      summary=parser.parseXML(summaryFile);
    }
    return summary;
  }

  /**
   * Save summary to file.
   * @param summary Summary to write.
   * @return <code>true</code> if it was successful, <code>false</code> otherwise.
   */
  public boolean saveSummary(CharacterSummary summary)
  {
    CharacterSummaryXMLWriter writer=new CharacterSummaryXMLWriter();
    File summaryFile=getSummaryFile();
    boolean ok=writer.write(summaryFile,summary,EncodingNames.UTF_8);
    return ok;
  }

  private File getSummaryFile()
  {
    File summaryFile=new File(_rootDir,"summary.xml");
    return summaryFile;
  }

  /**
   * Get the name of the character file.
   * @return a character file name.
   */
  public String getName()
  {
    CharacterSummary summary=getSummary();
    return (_summary!=null)?summary.getName():null;
  }

  /**
   * Get the server name of the character file.
   * @return a character file name.
   */
  public String getServerName()
  {
    CharacterSummary summary=getSummary();
    return (_summary!=null)?summary.getServer():null;
  }

  /**
   * Get a unique identifier for this toon file.
   * @return a string identifier.
   */
  public String getIdentifier()
  {
    return getServerName()+"#"+getName();
  }

  /**
   * Get the root directory of the character's file storage. 
   * @return a root directory.
   */
  public File getRootDir()
  {
    return _rootDir;
  }

  /**
   * Get the level history for this toon.
   * @return A level history.
   */
  public LevelHistory getLevelHistory()
  {
    if (_levelHistory==null)
    {
      _levelHistory=loadLevelHistory();
      if (_levelHistory==null)
      {
        LevelHistoryComputer c=new LevelHistoryComputer();
        _levelHistory=c.buildLevelHistory(this);
        writeLevelHistory();
      }
    }
    return _levelHistory;
  }
  
  private LevelHistory loadLevelHistory()
  {
    LevelHistory history=null;
    File historyFile=getLevelHistoryFile();
    if ((historyFile.exists()) && (historyFile.canRead()))
    {
      LevelHistoryXMLParser parser=new LevelHistoryXMLParser();
      history=parser.parseXML(historyFile);
    }
    return history;
  }

  private void writeLevelHistory()
  {
    File historyFile=getLevelHistoryFile();
    LevelHistoryXMLWriter writer=new LevelHistoryXMLWriter();
    writer.write(historyFile,_levelHistory,EncodingNames.ISO8859_1);
  }

  private File getLevelHistoryFile()
  {
    return new File(_rootDir,"levels.xml");
  }

  /**
   * Get the date of the last character update.
   * @return A date or <code>null</code> if there's no log.
   */
  public Date getLastInfoUpdate()
  {
    Date ret=_infosManager.getLastInfoDate();
    return ret;
  }

  /**
   * Get latest character info.
   * @return A character description or <code>null</code> if an error occurs.
   */
  public CharacterData getLastCharacterInfo()
  {
    CharacterData c=_infosManager.getLastCharacterDescription();
    return c;
  }

  /**
   * Get the date of the last log update.
   * @return A date or <code>null</code> if there's no log.
   */
  public Date getLastLogUpdate()
  {
    Date ret=null;
    File lastFile=_logsManager.getLastLogFile();
    if (lastFile!=null)
    {
      ret=CharacterLogsManager.getDateFromFilename(lastFile.getName());
    }
    return ret;
  }

  /**
   * Get latest character log.
   * @return A character log or <code>null</code> if an error occurs.
   */
  public CharacterLog getLastCharacterLog()
  {
    CharacterLog log=_logsManager.getLastLog();
    return log;
  }

  /**
   * Get the logs manager.
   * @return the logs manager.
   */
  public CharacterLogsManager getLogsManager()
  {
    return _logsManager;
  }

  /**
   * Get the character infos manager.
   * @return the infos manager.
   */
  public CharacterInfosManager getInfosManager()
  {
    return _infosManager;
  }

  /**
   * Indicates if this character has a log file.
   * @return <code>true</code> if it does, <code>false</code> otherwise.
   */
  public boolean hasLog()
  {
    return _logsManager.hasLog();
  }

  @Override
  public String toString()
  {
    return getIdentifier();
  }
}
