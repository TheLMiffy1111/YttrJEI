package thelm.yttrjei;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Streams;
import com.unascribed.lib39.machination.Lib39Machination;
import com.unascribed.lib39.machination.recipe.PistonSmashingRecipe;
import com.unascribed.lib39.machination.recipe.SoakingRecipe;
import com.unascribed.yttr.Yttr;
import com.unascribed.yttr.client.YttrClientInit;
import com.unascribed.yttr.client.resource.RuinedRecipeResourceMetadata;
import com.unascribed.yttr.client.screen.handled.CanFillerScreen;
import com.unascribed.yttr.client.screen.handled.CentrifugeScreen;
import com.unascribed.yttr.client.screen.handled.ProjectTableScreen;
import com.unascribed.yttr.client.screen.handled.RafterScreen;
import com.unascribed.yttr.content.item.DropOfContinuityItem;
import com.unascribed.yttr.crafting.CentrifugingRecipe;
import com.unascribed.yttr.crafting.LampRecipe;
import com.unascribed.yttr.crafting.VoidFilteringRecipe;
import com.unascribed.yttr.init.YHandledScreens;
import com.unascribed.yttr.init.content.YEnchantments;
import com.unascribed.yttr.init.content.YItems;
import com.unascribed.yttr.init.technical.YRecipeTypes;
import com.unascribed.yttr.inventory.CanFillerScreenHandler;
import com.unascribed.yttr.inventory.CentrifugeScreenHandler;
import com.unascribed.yttr.inventory.ProjectTableScreenHandler;
import com.unascribed.yttr.mechanics.rifle.RifleMode;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.BlockItem;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import thelm.yttrjei.gui.render.FakeModelIngredientRenderer;
import thelm.yttrjei.ingredient.FakeModelIngredient;
import thelm.yttrjei.ingredient.FakeModelIngredientHelper;
import thelm.yttrjei.ingredient.subtype.AmmoCanItemSubtypeInterpreter;
import thelm.yttrjei.ingredient.subtype.LampItemSubtypeInterpreter;
import thelm.yttrjei.ingredient.subtype.PotionItemSubtypeInterpreter;
import thelm.yttrjei.ingredient.subtype.SnareItemSubtypeInterpreter;
import thelm.yttrjei.recipe.ContinuityGiftRecipe;
import thelm.yttrjei.recipe.ForgottenCraftingRecipe;
import thelm.yttrjei.recipe.category.CentrifugingRecipeCategory;
import thelm.yttrjei.recipe.category.ContinuityGiftRecipeCategory;
import thelm.yttrjei.recipe.category.FillingRecipeCategory;
import thelm.yttrjei.recipe.category.ForgottenCraftingRecipeCategory;
import thelm.yttrjei.recipe.category.PistonSmashingRecipeCategory;
import thelm.yttrjei.recipe.category.ShatteringRecipeCategory;
import thelm.yttrjei.recipe.category.SoakingRecipeCategory;
import thelm.yttrjei.recipe.category.VoidFilteringRecipeCategory;
import thelm.yttrjei.recipe.replacer.LampRecipeMaker;
import thelm.yttrjei.recipe.transfer.RafterRecipeTransferInfo;

public class YttrJEI implements IModPlugin {

	public static final Identifier UID = new Identifier("yttrjei:yttr");
	public static final Logger LOGGER = LogManager.getLogger();

	public static IJeiHelpers jeiHelpers;
	public static IJeiRuntime jeiRuntime;

	public static final RecipeType<PistonSmashingRecipe> PISTON_SMASHING = new RecipeType<>(new Identifier("lib39:piston_smashing"), PistonSmashingRecipe.class);
	public static final RecipeType<SoakingRecipe> SOAKING = new RecipeType<>(new Identifier("lib39:soaking"), SoakingRecipe.class);

	public static final RecipeType<CentrifugingRecipe> CENTRIFUGING = new RecipeType<>(Yttr.id("centrifuging"), CentrifugingRecipe.class);
	public static final RecipeType<VoidFilteringRecipe> VOID_FILTERING = new RecipeType<>(Yttr.id("void_filtering"), VoidFilteringRecipe.class);
	@SuppressWarnings("rawtypes")
	public static final RecipeType<Recipe<?>> SHATTERING = new RecipeType(Yttr.id("shattering"), Recipe.class);

	public static final RecipeType<RifleMode> FILLING = new RecipeType<>(Yttr.id("filling"), RifleMode.class);
	public static final RecipeType<ContinuityGiftRecipe> CONTINUITY_GIFTS = new RecipeType<>(Yttr.id("continuity_gifts"), ContinuityGiftRecipe.class);
	public static final RecipeType<ForgottenCraftingRecipe> FORGOTTEN_CRAFTING = new RecipeType<>(Yttr.id("forgotten_crafting"), ForgottenCraftingRecipe.class);

	public static final RecipeType<CraftingRecipe> RAFTING = new RecipeType<>(Yttr.id("rafting"), CraftingRecipe.class);

	@Override
	public Identifier getPluginUid() {
		return UID;
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		if(checkDisabled()) {
			return;
		}

		PotionItemSubtypeInterpreter potion = new PotionItemSubtypeInterpreter();
		registration.registerSubtypeInterpreter(YItems.MERCURIAL_POTION, potion);
		registration.registerSubtypeInterpreter(YItems.MERCURIAL_SPLASH_POTION, potion);

		LampItemSubtypeInterpreter lamp = new LampItemSubtypeInterpreter();
		registration.registerSubtypeInterpreter(YItems.LAZOR_EMITTER, lamp);
		registration.registerSubtypeInterpreter(YItems.LAMP, lamp);
		registration.registerSubtypeInterpreter(YItems.FIXTURE, lamp);
		registration.registerSubtypeInterpreter(YItems.CAGE_LAMP, lamp);
		registration.registerSubtypeInterpreter(YItems.PANEL, lamp);

		SnareItemSubtypeInterpreter snare = new SnareItemSubtypeInterpreter();
		registration.registerSubtypeInterpreter(YItems.SNARE, snare);

		AmmoCanItemSubtypeInterpreter ammo = new AmmoCanItemSubtypeInterpreter();
		registration.registerSubtypeInterpreter(YItems.AMMO_CAN, ammo);
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registration) {
		if(checkDisabled()) {
			return;
		}

		List<FakeModelIngredient> ingredients = List.of(
				new FakeModelIngredient(YttrClientInit.SUPERCOOLED_NEODYMIUM_MODEL),
				new FakeModelIngredient(YttrClientInit.VOID_CAULDRON_MODEL));
		registration.register(FakeModelIngredient.TYPE, ingredients, FakeModelIngredientHelper.INSTANCE, FakeModelIngredientRenderer.INSTANCE);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		jeiHelpers = registration.getJeiHelpers();

		if(checkDisabled()) {
			return;
		}

		registration.addRecipeCategories(new PistonSmashingRecipeCategory());
		registration.addRecipeCategories(new CentrifugingRecipeCategory());
		registration.addRecipeCategories(new SoakingRecipeCategory());
		registration.addRecipeCategories(new VoidFilteringRecipeCategory());
		registration.addRecipeCategories(new ShatteringRecipeCategory());

		registration.addRecipeCategories(new FillingRecipeCategory());
		registration.addRecipeCategories(new ContinuityGiftRecipeCategory());
		registration.addRecipeCategories(new ForgottenCraftingRecipeCategory());
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		if(checkDisabled()) {
			return;
		}

		RecipeManager recipeManager = MinecraftClient.getInstance().world.getRecipeManager();
		ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();

		registration.addRecipes(PISTON_SMASHING, recipeManager.listAllOfType(Lib39Machination.RecipeTypes.PISTON_SMASHING));
		registration.addRecipes(SOAKING, recipeManager.listAllOfType(Lib39Machination.RecipeTypes.SOAKING));

		registration.addRecipes(CENTRIFUGING, recipeManager.listAllOfType(YRecipeTypes.CENTRIFUGING));
		List<VoidFilteringRecipe> voidFilteringRecipes =
				recipeManager.listAllOfType(YRecipeTypes.VOID_FILTERING).
				stream().
				filter(r -> !r.isHidden()).
				sorted(Comparator.comparingDouble(VoidFilteringRecipe::getChance).reversed()).
				toList();
		registration.addRecipes(VOID_FILTERING, voidFilteringRecipes);
		List<Recipe<?>> shatteringRecipes = Streams.concat(
				recipeManager.listAllOfType(YRecipeTypes.SHATTERING).stream(),
				recipeManager.listAllOfType(net.minecraft.recipe.RecipeType.STONECUTTING).
				stream().
				filter(r -> {
					return r.getOutput().getCount() == 1 &&
							r.getOutput().getItem() instanceof BlockItem &&
							!r.getIngredients().isEmpty();
				}),
				recipeManager.listAllOfType(net.minecraft.recipe.RecipeType.CRAFTING).
				stream().
				filter(r -> {
					return r.fits(1, 1) &&
							!r.getIngredients().isEmpty() &&
							Arrays.stream(r.getIngredients().get(0).getMatchingStacks()).
							anyMatch(s -> s.getItem() instanceof BlockItem);	
				})).
				toList();
		registration.addRecipes(SHATTERING, shatteringRecipes);

		registration.addRecipes(FILLING, RifleMode.VALUES);
		double giftChance = 100D / DropOfContinuityItem.getPossibilities(true, null).size();
		List<ContinuityGiftRecipe> giftRecipes = DropOfContinuityItem.getPossibilities(true, null).
				stream().
				sorted(Comparator.comparingInt(Registry.ITEM::getRawId)).
				map(item -> new ContinuityGiftRecipe(item, giftChance)).
				toList();
		registration.addRecipes(CONTINUITY_GIFTS, giftRecipes);
		List<ForgottenCraftingRecipe> forgottenRecipes = new ArrayList<>();
		for(Map.Entry<Identifier, Resource> entry : resourceManager.findResources("textures/gui/ruined_recipe", id -> id.getPath().endsWith(".png")).entrySet()) {
			Identifier id = entry.getKey();
			Identifier itemId = new Identifier(id.getNamespace(), id.getPath().substring(27, id.getPath().length() - 4));
			if((!itemId.getNamespace().equals("yttr") ||
					!itemId.getPath().equals("border") &&
					!itemId.getPath().equals("overlay")) &&
					Registry.ITEM.containsId(itemId)) {
				Optional<RuinedRecipeResourceMetadata> meta = Optional.empty();
				try {
					meta = entry.getValue().getMetadata().decode(RuinedRecipeResourceMetadata.READER);
				}
				catch(IOException e) {}
				forgottenRecipes.add(new ForgottenCraftingRecipe(itemId, meta));
			}
		}
		registration.addRecipes(FORGOTTEN_CRAFTING, forgottenRecipes);

		registration.addRecipes(RecipeTypes.CRAFTING, LampRecipeMaker.createRecipes());

		registration.addItemStackInfo(new ItemStack(YItems.DRY_ICE), buildInfo("dry_ice"));
		registration.addItemStackInfo(new ItemStack(YItems.GLITCHWEP), buildInfo("glitchwep"));
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		if(checkDisabled()) {
			return;
		}

		registration.addRecipeTransferHandler(CentrifugeScreenHandler.class, YHandledScreens.CENTRIFUGE, CENTRIFUGING, 0, 1, 6, 36);

		registration.addRecipeTransferHandler(CanFillerScreenHandler.class, YHandledScreens.CAN_FILLER, FILLING, 0, 3, 5, 36);

		registration.addRecipeTransferHandler(ProjectTableScreenHandler.class, YHandledScreens.PROJECT_TABLE, RecipeTypes.CRAFTING, 1, 9, 10, 54);
		registration.addRecipeTransferHandler(new RafterRecipeTransferInfo());
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		if(checkDisabled()) {
			return;
		}

		registration.addRecipeCatalyst(new ItemStack(Items.PISTON), PISTON_SMASHING);
		registration.addRecipeCatalyst(new ItemStack(Items.STICKY_PISTON), PISTON_SMASHING);
		registration.addRecipeCatalyst(new ItemStack(YItems.CENTRIFUGE), CENTRIFUGING);
		registration.addRecipeCatalyst(new ItemStack(YItems.VOID_FILTER), VOID_FILTERING);
		registration.addRecipeCatalyst(EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(YEnchantments.SHATTERING_CURSE, 1)), SHATTERING);

		registration.addRecipeCatalyst(new ItemStack(YItems.CAN_FILLER), FILLING);
		registration.addRecipeCatalyst(new ItemStack(YItems.DROP_OF_CONTINUITY), CONTINUITY_GIFTS);

		registration.addRecipeCatalyst(new ItemStack(YItems.PROJECT_TABLE), RecipeTypes.CRAFTING);
		registration.addRecipeCatalyst(new ItemStack(YItems.CENTRIFUGE), RecipeTypes.FUELING);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		if(checkDisabled()) {
			return;
		}

		registration.addRecipeClickArea(CentrifugeScreen.class, 67, 20, 12, 45, CENTRIFUGING, RecipeTypes.FUELING);
		registration.addRecipeClickArea(CentrifugeScreen.class, 79, 34, 45, 13, CENTRIFUGING, RecipeTypes.FUELING);
		registration.addRecipeClickArea(CentrifugeScreen.class, 97, 47, 12, 45, CENTRIFUGING, RecipeTypes.FUELING);
		registration.addRecipeClickArea(CentrifugeScreen.class, 52, 65, 45, 12, CENTRIFUGING, RecipeTypes.FUELING);

		registration.addRecipeClickArea(CanFillerScreen.class, 80, 57, 16, 21, FILLING);

		registration.addRecipeClickArea(ProjectTableScreen.class, 90, 35, 22, 16, RecipeTypes.CRAFTING);
		registration.addRecipeClickArea(RafterScreen.class, 115, 73, 22, 16, RecipeTypes.CRAFTING);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		YttrJEI.jeiRuntime = jeiRuntime;

		if(checkDisabled()) {
			return;
		}

		jeiRuntime.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, List.of(
				new ItemStack(YItems.RAFTER),
				new ItemStack(YItems.GIANT_COBBLESTONE),
				new ItemStack(YItems.SPATULA)));
		jeiRuntime.getRecipeManager().hideRecipes(RecipeTypes.CRAFTING,
				MinecraftClient.getInstance().world.getRecipeManager().
				listAllOfType(net.minecraft.recipe.RecipeType.CRAFTING).
				stream().
				filter(LampRecipe.class::isInstance).
				toList());
	}

	public Text[] buildInfo(String key) {
		String infoKey = "yttr.info." + key;
		return IntStream.iterate(1, i -> I18n.hasTranslation(infoKey + "." + i), i -> i + 1).
				mapToObj(i -> Text.translatable(infoKey + "." + i)).
				toArray(Text[]::new);
	}

	public boolean checkDisabled() {
		if(FabricLoader.getInstance().isModLoaded("emi")) {
			LOGGER.warn("YttrJEI is disabled with EMI as Yttr has native EMI support");
			return true;
		}
		return false;
	}
}
