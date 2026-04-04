package thelm.yttrjei;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Streams;
import com.unascribed.lib39.machination.Lib39Machination;
import com.unascribed.lib39.machination.recipe.PistonSmashingRecipe;
import com.unascribed.lib39.machination.recipe.SoakingRecipe;

import diy.y2k.yttr.Yttr;
import diy.y2k.yttr.client.YttrClientInit;
import diy.y2k.yttr.client.resource.RuinedRecipeResourceMetadata;
import diy.y2k.yttr.client.screen.handled.CanFillerScreen;
import diy.y2k.yttr.client.screen.handled.CentrifugeScreen;
import diy.y2k.yttr.client.screen.handled.ProjectTableScreen;
import diy.y2k.yttr.client.screen.handled.RafterScreen;
import diy.y2k.yttr.content.recipe.CentrifugingRecipe;
import diy.y2k.yttr.content.recipe.LampRecipe;
import diy.y2k.yttr.content.recipe.PinchingRecipe;
import diy.y2k.yttr.content.recipe.ShapedRaftingRecipe;
import diy.y2k.yttr.content.recipe.VoidFilteringRecipe;
import diy.y2k.yttr.init.YHandledScreens;
import diy.y2k.yttr.init.content.YEnchantments;
import diy.y2k.yttr.init.content.YItems;
import diy.y2k.yttr.init.technical.YRecipeTypes;
import diy.y2k.yttr.inventory.CanFillerScreenHandler;
import diy.y2k.yttr.inventory.CentrifugeScreenHandler;
import diy.y2k.yttr.inventory.ProjectTableScreenHandler;
import diy.y2k.yttr.mechanics.rifle.RifleMode;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IIngredientAliasRegistration;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.BlockItem;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import thelm.yttrjei.alias.YttrAliasMapping;
import thelm.yttrjei.gui.render.FakeModelIngredientRenderer;
import thelm.yttrjei.ingredient.FakeModelIngredient;
import thelm.yttrjei.ingredient.FakeModelIngredientHelper;
import thelm.yttrjei.ingredient.subtype.AmmoCanItemSubtypeInterpreter;
import thelm.yttrjei.ingredient.subtype.LampItemSubtypeInterpreter;
import thelm.yttrjei.ingredient.subtype.PotionItemSubtypeInterpreter;
import thelm.yttrjei.ingredient.subtype.SnareItemSubtypeInterpreter;
import thelm.yttrjei.metadata.SketchRecipeMetadataSection;
import thelm.yttrjei.metadata.SketchRecipeMetadataSectionSerializer;
import thelm.yttrjei.recipe.ContinuityGiftRecipe;
import thelm.yttrjei.recipe.ForgottenCraftingRecipe;
import thelm.yttrjei.recipe.SketchRecipe;
import thelm.yttrjei.recipe.category.CentrifugingRecipeCategory;
import thelm.yttrjei.recipe.category.ContinuityGiftRecipeCategory;
import thelm.yttrjei.recipe.category.FillingRecipeCategory;
import thelm.yttrjei.recipe.category.ForgottenCraftingRecipeCategory;
import thelm.yttrjei.recipe.category.PinchingRecipeCategory;
import thelm.yttrjei.recipe.category.PistonSmashingRecipeCategory;
import thelm.yttrjei.recipe.category.ShatteringRecipeCategory;
import thelm.yttrjei.recipe.category.SketchRecipeCategory;
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
	public static final RecipeType<PinchingRecipe> PINCHING = new RecipeType<>(Yttr.id("pinching"), PinchingRecipe.class);

	public static final RecipeType<RifleMode> FILLING = new RecipeType<>(Yttr.id("filling"), RifleMode.class);
	public static final RecipeType<ContinuityGiftRecipe> CONTINUITY_GIFTS = new RecipeType<>(Yttr.id("continuity_gifts"), ContinuityGiftRecipe.class);
	public static final RecipeType<ForgottenCraftingRecipe> FORGOTTEN_CRAFTING = new RecipeType<>(Yttr.id("forgotten_crafting"), ForgottenCraftingRecipe.class);
	public static final RecipeType<SketchRecipe> SKETCHES = new RecipeType<>(Yttr.id("sketches"), SketchRecipe.class);

	public static final RecipeType<ShapedRaftingRecipe> RAFTING = new RecipeType<>(Yttr.id("rafting"), ShapedRaftingRecipe.class);

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
	public void registerIngredientAliases(IIngredientAliasRegistration registration) {
		if(checkDisabled()) {
			return;
		}

		YttrAliasMapping.addAliases(registration);
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
		registration.addRecipeCategories(new PinchingRecipeCategory());

		registration.addRecipeCategories(new FillingRecipeCategory());
		registration.addRecipeCategories(new ContinuityGiftRecipeCategory());
		registration.addRecipeCategories(new ForgottenCraftingRecipeCategory());
		registration.addRecipeCategories(new SketchRecipeCategory());
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		if(checkDisabled()) {
			return;
		}

		RecipeManager recipeManager = MinecraftClient.getInstance().world.getRecipeManager();
		DynamicRegistryManager registryAccess = MinecraftClient.getInstance().world.getRegistryManager();
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
					return r.getOutput(registryAccess).getCount() == 1 &&
							r.getOutput(registryAccess).getItem() instanceof BlockItem &&
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
		registration.addRecipes(PINCHING, recipeManager.listAllOfType(YRecipeTypes.PINCHING));

		registration.addRecipes(FILLING, RifleMode.VALUES);
		registration.addRecipes(CONTINUITY_GIFTS, List.of(new ContinuityGiftRecipe()));
		List<ForgottenCraftingRecipe> forgottenRecipes = new ArrayList<>();
		ResourceFinder forgottenFormat = new ResourceFinder("textures/gui/ruined_recipe", ".png");
		for(Map.Entry<Identifier, Resource> entry : forgottenFormat.findResources(resourceManager).entrySet()) {
			Identifier id = forgottenFormat.toResourceId(entry.getKey());
			if((!id.getNamespace().equals("yttr") ||
					!id.getPath().equals("border") &&
					!id.getPath().equals("overlay")) &&
					Registries.ITEM.containsId(id)) {
				Optional<RuinedRecipeResourceMetadata> meta = Optional.empty();
				try {
					meta = entry.getValue().getMetadata().decode(RuinedRecipeResourceMetadata.READER);
				}
				catch(IOException e) {}
				forgottenRecipes.add(new ForgottenCraftingRecipe(id, meta));
			}
		}
		registration.addRecipes(FORGOTTEN_CRAFTING, forgottenRecipes);
		List<SketchRecipe> sketchRecipes = new ArrayList<>();
		ResourceFinder sketchFormat = new ResourceFinder("textures/gui/sketch_recipe", ".png");
		for(Map.Entry<Identifier, Resource> entry : sketchFormat.findResources(resourceManager).entrySet()) {
			Identifier id = forgottenFormat.toResourceId(entry.getKey());
			Optional<SketchRecipeMetadataSection> meta = Optional.empty();
			try {
				meta = entry.getValue().getMetadata().decode(SketchRecipeMetadataSectionSerializer.INSTANCE);
			}
			catch(Exception e) {}
			sketchRecipes.add(new SketchRecipe(id, meta));
		}
		registration.addRecipes(SKETCHES, sketchRecipes);

		registration.addRecipes(RecipeTypes.CRAFTING, LampRecipeMaker.createRecipes());
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

		registration.addRecipeCatalyst(Items.PISTON, PISTON_SMASHING);
		registration.addRecipeCatalyst(Items.STICKY_PISTON, PISTON_SMASHING);
		registration.addRecipeCatalyst(YItems.CENTRIFUGE, CENTRIFUGING);
		registration.addRecipeCatalyst(YItems.VOID_FILTER, VOID_FILTERING);
		registration.addRecipeCatalyst(EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(YEnchantments.SHATTERING_CURSE, 1)), SHATTERING);
		registration.addRecipeCatalyst(YItems.PINCH_POINT, PINCHING);

		registration.addRecipeCatalyst(YItems.CAN_FILLER, FILLING);
		registration.addRecipeCatalyst(YItems.DROP_OF_CONTINUITY, CONTINUITY_GIFTS);

		registration.addRecipeCatalyst(YItems.PROJECT_TABLE, RecipeTypes.CRAFTING);
		registration.addRecipeCatalyst(YItems.CENTRIFUGE, RecipeTypes.FUELING);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		if(checkDisabled()) {
			return;
		}

		registration.addRecipeClickArea(CentrifugeScreen.class, 67, 24, 12, 45, CENTRIFUGING, RecipeTypes.FUELING);
		registration.addRecipeClickArea(CentrifugeScreen.class, 79, 38, 45, 13, CENTRIFUGING, RecipeTypes.FUELING);
		registration.addRecipeClickArea(CentrifugeScreen.class, 97, 51, 12, 45, CENTRIFUGING, RecipeTypes.FUELING);
		registration.addRecipeClickArea(CentrifugeScreen.class, 52, 69, 45, 12, CENTRIFUGING, RecipeTypes.FUELING);

		registration.addRecipeClickArea(CanFillerScreen.class, 82, 26, 21, 16, FILLING);

		registration.addRecipeClickArea(ProjectTableScreen.class, 90, 30, 22, 16, RecipeTypes.CRAFTING);
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

	public boolean checkDisabled() {
		if(FabricLoader.getInstance().isModLoaded("emi")) {
			LOGGER.warn("YttrJEI is disabled with EMI as Yttr has native EMI support");
			return true;
		}
		return false;
	}
}
