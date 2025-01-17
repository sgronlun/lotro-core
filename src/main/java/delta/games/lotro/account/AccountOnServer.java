package delta.games.lotro.account;

import java.io.File;

import delta.common.utils.misc.Preferences;

/**
 * Provides access to data related to an account on a server.
 * @author DAM
 */
public class AccountOnServer
{
  private Account _account;
  private String _serverName;
  private File _rootDir;
  private Preferences _preferences;

  /**
   * Constructor.
   * @param account Parent account.
   * @param serverName Server name.
   */
  public AccountOnServer(Account account, String serverName)
  {
    _account=account;
    _serverName=serverName;
    if (_serverName==null)
    {
      _serverName="";
    }
    _rootDir=new File(account.getRootDir(),_serverName);
    File preferencesDir=new File(_rootDir,"preferences");
    _preferences=new Preferences(preferencesDir);
  }

  /**
   * Get the parent account.
   * @return an account.
   */
  public Account getAccount()
  {
    return _account;
  }

  /**
   * Get the name of the managed server.
   * @return a server name.
   */
  public String getServerName()
  {
    return _serverName;
  }

  /**
   * Get the root directory of the account/server file storage. 
   * @return a root directory.
   */
  public File getRootDir()
  {
    return _rootDir;
  }

  /**
   * Get the preferences for this account/server.
   * @return the preferences for this account/server.
   */
  public Preferences getPreferences()
  {
    return _preferences;
  }

  @Override
  public String toString()
  {
    String accountName=(_account!=null)?_account.getName():"?";
    return accountName+"@"+_serverName;
  }
}
