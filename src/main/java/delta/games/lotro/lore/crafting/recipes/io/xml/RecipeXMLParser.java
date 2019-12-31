package delta.games.lotro.lore.crafting.recipes.io.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.lore.crafting.recipes.CraftingResult;
import delta.games.lotro.lore.crafting.recipes.Ingredient;
import delta.games.lotro.lore.crafting.recipes.Recipe;
import delta.games.lotro.lore.crafting.recipes.RecipeVersion;
import delta.games.lotro.lore.items.ItemProxy;

/**
 * Parser for quest descriptions stored in XML.
 * @author DAM
 */
public class RecipeXMLParser
{
  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed recipe or <code>null</code>.
   */
  public Recipe parseXML(File source)
  {
    Recipe recipe=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      recipe=parseRecipe(root);
    }
    return recipe;
  }

  /**
   * Load some recipes from an XML file.
   * @param source Source file.
   * @return A possibly empty but not <code>null</code> list of recipes.
   */
  public List<Recipe> loadRecipes(File source)
  {
    List<Recipe> recipes=new ArrayList<Recipe>();
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      List<Element> recipeTags=DOMParsingTools.getChildTagsByName(root,RecipeXMLConstants.RECIPE_TAG);
      for(Element recipeTag : recipeTags)
      {
        Recipe recipe=parseRecipe(recipeTag);
        recipes.add(recipe);
      }
    }
    return recipes;
  }

  private Recipe parseRecipe(Element root)
  {
    Recipe r=new Recipe();

    NamedNodeMap attrs=root.getAttributes();
    // Identifier
    int id=DOMParsingTools.getIntAttribute(attrs,RecipeXMLConstants.RECIPE_ID_ATTR,0);
    r.setIdentifier(id);
    // Name
    String name=DOMParsingTools.getStringAttribute(attrs,RecipeXMLConstants.RECIPE_NAME_ATTR,null);
    r.setName(name);
    // Profession
    String profession=DOMParsingTools.getStringAttribute(attrs,RecipeXMLConstants.RECIPE_PROFESSION_ATTR,null);
    r.setProfession(profession);
    // Tier
    int tier=DOMParsingTools.getIntAttribute(attrs,RecipeXMLConstants.RECIPE_TIER_ATTR,0);
    r.setTier(tier);
    // Category
    String category=DOMParsingTools.getStringAttribute(attrs,RecipeXMLConstants.RECIPE_CATEGORY_ATTR,null);
    r.setCategory(category);
    // XP
    int xp=DOMParsingTools.getIntAttribute(attrs,RecipeXMLConstants.RECIPE_XP_ATTR,0);
    r.setXP(xp);
    // Cooldown
    int cooldown=DOMParsingTools.getIntAttribute(attrs,RecipeXMLConstants.RECIPE_COOLDOWN_ATTR,-1);
    r.setCooldown(cooldown);
    // Single use
    boolean singleUse=DOMParsingTools.getBooleanAttribute(attrs,RecipeXMLConstants.RECIPE_SINGLE_USE_ATTR,false);
    r.setOneTimeUse(singleUse);
    // Guild required
    boolean guildRequired=DOMParsingTools.getBooleanAttribute(attrs,RecipeXMLConstants.RECIPE_GUILD_ATTR,false);
    r.setGuildRequired(guildRequired);

    Element scrollItemElement=DOMParsingTools.getChildTagByName(root,RecipeXMLConstants.SCROLL_ITEM_TAG);
    if (scrollItemElement!=null)
    {
      ItemProxy ref=parseItemRef(scrollItemElement);
      r.setRecipeScroll(ref);
    }

    // Versions
    List<RecipeVersion> versions=new ArrayList<RecipeVersion>();
    List<Element> versionElements=DOMParsingTools.getChildTagsByName(root,RecipeXMLConstants.RECIPE_RESULT_TAG);
    for(Element versionElement : versionElements)
    {
      RecipeVersion version=new RecipeVersion();
      NamedNodeMap versionAttrs=versionElement.getAttributes();
      // Base critical chance
      int baseCriticalChance=DOMParsingTools.getIntAttribute(versionAttrs,RecipeXMLConstants.RECIPE_RESULT_BASE_CRITICAL_CHANCE_ATTR,0);
      version.setBaseCriticalChance((baseCriticalChance!=0)?Integer.valueOf(baseCriticalChance):null);

      // Ingredients
      List<Ingredient> ingredients=new ArrayList<Ingredient>();
      List<Element> ingredientElements=DOMParsingTools.getChildTagsByName(versionElement,RecipeXMLConstants.INGREDIENT_TAG);
      for(Element ingredientElement : ingredientElements)
      {
        Ingredient ingredient=new Ingredient();
        NamedNodeMap ingredientAttrs=ingredientElement.getAttributes();
        // Quantity
        int quantity=DOMParsingTools.getIntAttribute(ingredientAttrs,RecipeXMLConstants.INGREDIENT_QUANTITY_ATTR,1);
        ingredient.setQuantity(quantity);
        // Optional
        boolean optional=DOMParsingTools.getBooleanAttribute(ingredientAttrs,RecipeXMLConstants.INGREDIENT_OPTIONAL_ATTR,false);
        ingredient.setOptional(optional);
        if (optional)
        {
          // Critical chance bonus
          int criticalChanceBonus=DOMParsingTools.getIntAttribute(ingredientAttrs,RecipeXMLConstants.INGREDIENT_CRITICAL_CHANCE_BONUS_ATTR,0);
          ingredient.setCriticalChanceBonus((criticalChanceBonus!=0)?Integer.valueOf(criticalChanceBonus):null);
        }
        // Item reference
        Element ingredientItemRef=DOMParsingTools.getChildTagByName(ingredientElement,RecipeXMLConstants.INGREDIENT_ITEM_TAG);
        if (ingredientItemRef!=null)
        {
          ItemProxy ingredientRef=parseItemRef(ingredientItemRef);
          ingredient.setItem(ingredientRef);
        }
        ingredients.add(ingredient);
      }
      version.setIngredients(ingredients);

      // Results
      List<Element> resultElements=DOMParsingTools.getChildTagsByName(versionElement,RecipeXMLConstants.RESULT_TAG);
      for(Element resultElement : resultElements)
      {
        CraftingResult result=parseResult(resultElement);
        if (result!=null)
        {
          boolean isCritical=result.isCriticalResult();
          if (isCritical)
          {
            version.setCritical(result);
          }
          else
          {
            version.setRegular(result);
          }
        }
      }
      versions.add(version);
    }
    r.setVersions(versions);
    return r;
  }

  private CraftingResult parseResult(Element resultElement)
  {
    NamedNodeMap attrs=resultElement.getAttributes();
    CraftingResult result=new CraftingResult();
    // Quantity
    int quantity=DOMParsingTools.getIntAttribute(attrs,RecipeXMLConstants.RESULT_QUANTITY_ATTR,1);
    result.setQuantity(quantity);
    // Critical
    boolean critical=DOMParsingTools.getBooleanAttribute(attrs,RecipeXMLConstants.RESULT_CRITICAL_ATTR,false);
    result.setCriticalResult(critical);
    // Item reference
    Element resultItemRefElement=DOMParsingTools.getChildTagByName(resultElement,RecipeXMLConstants.RESULT_ITEM_TAG);
    if (resultItemRefElement!=null)
    {
      ItemProxy resultItemRef=parseItemRef(resultItemRefElement);
      result.setItem(resultItemRef);
    }
    return result;
  }

  private ItemProxy parseItemRef(Element itemRef)
  {
    NamedNodeMap attrs=itemRef.getAttributes();
    ItemProxy ref=new ItemProxy();
    // Item id
    int id=DOMParsingTools.getIntAttribute(attrs,RecipeXMLConstants.RECIPE_ITEM_ID_ATTR,0);
    ref.setId(id);
    // Item key
    String key=DOMParsingTools.getStringAttribute(attrs,RecipeXMLConstants.RECIPE_ITEM_KEY_ATTR,null);
    ref.setItemKey(key);
    // Name
    String name=DOMParsingTools.getStringAttribute(attrs,RecipeXMLConstants.RECIPE_ITEM_NAME_ATTR,null);
    ref.setName(name);
    // Icon
    String icon=DOMParsingTools.getStringAttribute(attrs,RecipeXMLConstants.RECIPE_ITEM_ICON_ATTR,null);
    ref.setIcon(icon);
    return ref;
  }
}
