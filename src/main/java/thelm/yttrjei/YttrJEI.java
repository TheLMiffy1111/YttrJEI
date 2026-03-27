package thelm.yttrjei;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Streams;
import com.unascribed.yttr.Yttr;
import com.unascribed.yttr.client.RuinedRecipeResourceMetadata;
import com.unascribed.yttr.client.screen.handled.CanFillerScreen;
import com.unascribed.yttr.client.screen.handled.CentrifugeScreen;
import com.unascribed.yttr.client.screen.handled.ProjectTableScreen;
import com.unascribed.yttr.client.screen.handled.RafterScreen;
import com.unascribed.yttr.content.item.DropOfContinuityItem;
import com.unascribed.yttr.crafting.CentrifugingRecipe;
import com.unascribed.yttr.crafting.LampRecipe;
import com.unascribed.yttr.crafting.PistonSmashingRecipe;
import com.unascribed.yttr.crafting.SoakingRecipe;
import com.unascribed.yttr.crafting.VoidFilteringRecipe;
import com.unascribed.yttr.init.YBlocks;
import com.unascribed.yttr.init.YEnchantments;
import com.unascribed.yttr.init.YItems;
import com.unascribed.yttr.init.YRecipeTypes;
import com.unascribed.yttr.inventory.CentrifugeScreenHandler;
import com.unascribed.yttr.inventory.ProjectTableScreenHandler;
import com.unascribed.yttr.mechanics.rifle.RifleMode;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.BlockItem;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import thelm.yttrjei.gui.handler.SubTabExtraAreaHandler;
import thelm.yttrjei.ingredient.subtype.AmmoCanItemSubtypeInterpreter;
import thelm.yttrjei.ingredient.subtype.LampItemSubtypeInterpreter;
import thelm.yttrjei.ingredient.subtype.PotionItemSubtypeInterpreter;
import thelm.yttrjei.ingredient.subtype.SnareItemSubtypeInterpreter;
import thelm.yttrjei.recipe.ContinuityGiftRecipe;
import thelm.yttrjei.recipe.FillingRecipe;
import thelm.yttrjei.recipe.ForgottenCraftingRecipe;
import thelm.yttrjei.recipe.ShatteringRecipeWrapper;
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

	public static final RecipeType<PistonSmashingRecipe> PISTON_SMASHING = createRecipeType(Yttr.id("piston_smashing"), PistonSmashingRecipe.class);
	public static final RecipeType<CentrifugingRecipe> CENTRIFUGING = createRecipeType(Yttr.id("centrifuging"), CentrifugingRecipe.class);
	public static final RecipeType<SoakingRecipe> SOAKING = createRecipeType(Yttr.id("soaking"), SoakingRecipe.class);
	public static final RecipeType<VoidFilteringRecipe> VOID_FILTERING = createRecipeType(Yttr.id("void_filtering"), VoidFilteringRecipe.class);
	public static final RecipeType<ShatteringRecipeWrapper> SHATTERING = createRecipeType(Yttr.id("shattering"), ShatteringRecipeWrapper.class);

	public static final RecipeType<FillingRecipe> FILLING = createRecipeType(Yttr.id("filling"), FillingRecipe.class);
	public static final RecipeType<ContinuityGiftRecipe> CONTINUITY_GIFTS = createRecipeType(Yttr.id("continuity_gifts"), ContinuityGiftRecipe.class);
	public static final RecipeType<ForgottenCraftingRecipe> FORGOTTEN_CRAFTING = createRecipeType(Yttr.id("forgotten_crafting"), ForgottenCraftingRecipe.class);

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

		registration.addRecipes(PISTON_SMASHING, recipeManager.listAllOfType(YRecipeTypes.PISTON_SMASHING));
		registration.addRecipes(CENTRIFUGING, recipeManager.listAllOfType(YRecipeTypes.CENTRIFUGING));
		registration.addRecipes(SOAKING, recipeManager.listAllOfType(YRecipeTypes.SOAKING));
		List<VoidFilteringRecipe> voidFilteringRecipes =
				recipeManager.listAllOfType(YRecipeTypes.VOID_FILTERING).
				stream().
				filter(r -> !r.isHidden()).
				sorted(Comparator.comparingDouble(VoidFilteringRecipe::getChance).reversed()).
				toList();
		registration.addRecipes(VOID_FILTERING, voidFilteringRecipes);
		List<ShatteringRecipeWrapper> shatteringRecipes = Streams.concat(
				recipeManager.listAllOfType(YRecipeTypes.SHATTERING).
				stream().
				map(ShatteringRecipeWrapper::new),
				recipeManager.listAllOfType(net.minecraft.recipe.RecipeType.STONECUTTING).
				stream().
				filter(r -> {
					return r.getOutput().getCount() == 1 &&
							r.getOutput().getItem() instanceof BlockItem &&
							!r.getIngredients().isEmpty();
				}).
				map(ShatteringRecipeWrapper::new),
				recipeManager.listAllOfType(net.minecraft.recipe.RecipeType.CRAFTING).
				stream().
				filter(r -> {
					return r.fits(1, 1) &&
							!r.getIngredients().isEmpty() &&
							Arrays.stream(r.getIngredients().get(0).getMatchingStacks()).
							anyMatch(s -> s.getItem() instanceof BlockItem);	
				}).
				map(ShatteringRecipeWrapper::new)).
				toList();
		registration.addRecipes(SHATTERING, shatteringRecipes);

		registration.addRecipes(FILLING, RifleMode.VALUES.stream().map(FillingRecipe::new).toList());
		double giftChance = 100D / DropOfContinuityItem.getPossibilities().size();
		List<ContinuityGiftRecipe> giftRecipes = DropOfContinuityItem.getPossibilities().
				stream().
				sorted(Comparator.comparingInt(Registry.ITEM::getRawId)).
				map(item -> new ContinuityGiftRecipe(item, giftChance)).
				toList();
		registration.addRecipes(CONTINUITY_GIFTS, giftRecipes);
		List<ForgottenCraftingRecipe> forgottenRecipes = new ArrayList<>();
		for(Identifier id : resourceManager.findResources("textures/gui/ruined_recipe", path -> path.endsWith(".png"))) {
			Identifier itemId = new Identifier(id.getNamespace(), id.getPath().substring(27, id.getPath().length() - 4));
			if((!itemId.getNamespace().equals("yttr") ||
					!itemId.getPath().equals("border") &&
					!itemId.getPath().equals("overlay")) &&
					Registry.ITEM.containsId(itemId)) {
				RuinedRecipeResourceMetadata meta = null;
				try {
					meta = resourceManager.getResource(id).getMetadata(RuinedRecipeResourceMetadata.READER);
				}
				catch(IOException e) {}
				forgottenRecipes.add(new ForgottenCraftingRecipe(itemId, meta));
			}
		}
		registration.addRecipes(FORGOTTEN_CRAFTING, forgottenRecipes);

		registration.addRecipes(RecipeTypes.CRAFTING, LampRecipeMaker.createRecipes());
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		if(checkDisabled()) {
			return;
		}

		registration.addRecipeTransferHandler(CentrifugeScreenHandler.class, CENTRIFUGING, 0, 1, 6, 36);

		//registration.addRecipeTransferHandler(CanFillerScreenHandler.class, FILLING, 0, 3, 5, 36);

		registration.addRecipeTransferHandler(ProjectTableScreenHandler.class, RecipeTypes.CRAFTING, 1, 9, 10, 54);
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

		registration.addGuiContainerHandler(CreativeInventoryScreen.class, new SubTabExtraAreaHandler());
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		YttrJEI.jeiRuntime = jeiRuntime;

		if(checkDisabled()) {
			return;
		}

		jeiRuntime.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, List.of(
				new ItemStack(YBlocks.RAFTER),
				new ItemStack(YBlocks.GIANT_COBBLESTONE),
				new ItemStack(YItems.SPATULA)));
		jeiRuntime.getRecipeManager().hideRecipes(RecipeTypes.CRAFTING,
				MinecraftClient.getInstance().world.getRecipeManager().
				listAllOfType(net.minecraft.recipe.RecipeType.CRAFTING).
				stream().
				filter(LampRecipe.class::isInstance).
				toList());
	}

	public static <R> RecipeType<R> createRecipeType(Identifier uid, Class<? extends R> recipeClass) {
		RecipeType<R> recipeType = new RecipeType<>(uid, recipeClass);
		return recipeType;
	}

	public boolean checkDisabled() {
		return false;
	}
}
