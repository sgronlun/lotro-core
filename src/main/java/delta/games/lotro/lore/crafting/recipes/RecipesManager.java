package delta.games.lotro.lore.crafting.recipes;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import delta.common.utils.text.EncodingNames;
import delta.games.lotro.config.DataFiles;
import delta.games.lotro.config.LotroCoreConfig;
import delta.games.lotro.lore.crafting.Profession;
import delta.games.lotro.lore.crafting.ProfessionComparator;
import delta.games.lotro.lore.crafting.recipes.io.xml.RecipeXMLParser;
import delta.games.lotro.lore.crafting.recipes.io.xml.RecipeXMLWriter;

/**
 * Facade for recipes access.
 * @author DAM
 */
public final class RecipesManager
{
  private static RecipesManager _instance=new RecipesManager(true);

  private Map<Profession,Map<Integer,List<Recipe>>> _recipes;
  private Map<Integer,Recipe> _recipesById;

  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
  public static RecipesManager getInstance()
  {
    return _instance;
  }

  /**
   * Constructor.
   * @param load Indicates if the recipes database shall be loaded or not.
   */
  public RecipesManager(boolean load)
  {
    _recipes=new HashMap<Profession,Map<Integer,List<Recipe>>>();
    _recipesById=new HashMap<Integer,Recipe>();
    if (load)
    {
      loadAllRecipes();
    }
  }

  private void loadAllRecipes()
  {
    File fromFile=LotroCoreConfig.getInstance().getFile(DataFiles.RECIPES);
    loadRecipesFromFile(fromFile);
  }

  /**
   * Get the managed professions.
   * @return the managed professions.
   */
  public List<Profession> getProfessions()
  {
    List<Profession> ret=new ArrayList<Profession>(_recipes.keySet());
    Collections.sort(ret,new ProfessionComparator());
    return ret;
  }

  /**
   * Get the managed tiers for a profession.
   * @param profession Profession to use.
   * @return the managed tiers.
   */
  public List<Integer> getTiers(Profession profession)
  {
    List<Integer> ret=new ArrayList<Integer>();
    Map<Integer,List<Recipe>> tierMap=_recipes.get(profession);
    if (tierMap!=null)
    {
      ret.addAll(tierMap.keySet());
    }
    Collections.sort(ret);
    return ret;
  }

  /**
   * Get the managed recipes for a profession and a tier.
   * @param profession Profession to use.
   * @param tier Tier to use.
   * @return the managed recipes.
   */
  public List<Recipe> getRecipes(Profession profession, int tier)
  {
    List<Recipe> ret=new ArrayList<Recipe>();
    Map<Integer,List<Recipe>> tierMap=_recipes.get(profession);
    if (tierMap!=null)
    {
      List<Recipe> recipes=tierMap.get(Integer.valueOf(tier));
      if (recipes!=null)
      {
        ret.addAll(recipes);
      }
    }
    RecipeUtils.sort(ret);
    return ret;
  }

  /**
   * Load all recipes from a file.
   * @param inputFile Input file.
   */
  private void loadRecipesFromFile(File inputFile)
  {
    RecipeXMLParser parser=new RecipeXMLParser();
    List<Recipe> recipes=parser.loadRecipes(inputFile);
    _recipes.clear();
    for(Recipe recipe : recipes)
    {
      registerRecipe(recipe);
    }
  }

  /**
   * Register a new recipe.
   * @param recipe Recipe to add.
   */
  public void registerRecipe(Recipe recipe)
  {
    // Register in profession/tier map
    Profession profession=recipe.getProfession();
    Map<Integer,List<Recipe>> recipesForProfession=_recipes.get(profession);
    if (recipesForProfession==null)
    {
      recipesForProfession=new HashMap<Integer,List<Recipe>>();
      _recipes.put(profession,recipesForProfession);
    }
    Integer tier=Integer.valueOf(recipe.getTier());
    List<Recipe> recipesForTier=recipesForProfession.get(tier);
    if (recipesForTier==null)
    {
      recipesForTier=new ArrayList<Recipe>();
      recipesForProfession.put(tier,recipesForTier);
    }
    recipesForTier.add(recipe);
    // Register in id map
    _recipesById.put(Integer.valueOf(recipe.getIdentifier()),recipe);
  }

  /**
   * Get a recipe using its identifier.
   * @param recipeId Identifier of the recipe to get.
   * @return A recipe or <code>null</code> if not found.
   */
  public Recipe getRecipeById(int recipeId)
  {
    return _recipesById.get(Integer.valueOf(recipeId));
  }

  /**
   * Get all recipes.
   * @return a list of all recipes.
   */
  public List<Recipe> getAll()
  {
    List<Recipe> ret=new ArrayList<Recipe>();
    for(Profession profession : _recipes.keySet())
    {
      for(Integer tier : _recipes.get(profession).keySet())
      {
        ret.addAll(_recipes.get(profession).get(tier));
      }
    }
    return ret;
  }

  /**
   * Get the number of recipes.
   * @return the number of recipes.
   */
  public int getRecipesCount()
  {
    int ret=0;
    for(Profession profession : _recipes.keySet())
    {
      for(Integer tier : _recipes.get(profession).keySet())
      {
        ret+=_recipes.get(profession).get(tier).size();
      }
    }
    return ret;
  }

  /**
   * Write the managed recipes to the specified file.
   * @param toFile File to write to.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean writeToFile(File toFile)
  {
    List<Recipe> recipes=getAll();
    RecipeUtils.sort(recipes);
    RecipeXMLWriter writer=new RecipeXMLWriter();
    return writer.write(toFile,recipes,EncodingNames.UTF_8);
  }
}
