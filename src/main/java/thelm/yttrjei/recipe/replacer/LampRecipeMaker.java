package thelm.yttrjei.recipe.replacer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import com.unascribed.yttr.content.item.block.LampBlockItem;
import com.unascribed.yttr.crafting.LampRecipe;
import com.unascribed.yttr.init.content.YItems;
import com.unascribed.yttr.mechanics.LampColor;
import com.unascribed.yttr.util.Resolvable;

import it.unimi.dsi.fastutil.booleans.BooleanList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

public class LampRecipeMaker {

	// I don't think this covers all cases but this works good enough
	public static List<CraftingRecipe> createRecipes() {
		ItemStack torch = new ItemStack(Items.REDSTONE_TORCH);
		List<LampRecipe> lRecs = MinecraftClient.getInstance().world.getRecipeManager().
				listAllOfType(RecipeType.CRAFTING).stream().
				filter(LampRecipe.class::isInstance).
				map(LampRecipe.class::cast).
				sorted(Comparator.comparingInt(LampRecipe::getPriority).reversed()).
				toList();
		List<CraftingRecipe> recs = new ArrayList<>();
		for(LampRecipe lRec : lRecs) {
			ItemStack res = lRec.getOutput();
			boolean canInvert = lRec.getIngredients().stream().
					anyMatch(ing -> ing.test(torch));
			if(lRec.getIngredients().stream().
					noneMatch(ing -> !ing.isEmpty() &&
							Arrays.stream(ing.getMatchingStacks()).
							map(ItemStack::getItem).
							allMatch(LampBlockItem.class::isInstance))) {
				BooleanList bList = canInvert ? BooleanList.of(false, true) : BooleanList.of(false);
				for(boolean invert : bList) {
					DefaultedList<Ingredient> ings = DefaultedList.of();
					for(Ingredient ingredient : lRec.getIngredients()) {
						if(ingredient.test(torch)) {
							Stream<ItemStack> transformed = Arrays.stream(ingredient.getMatchingStacks()).
									filter(stack -> stack.getItem() == Items.REDSTONE_TORCH == invert);
							ings.add(Ingredient.ofStacks(transformed));
						}
						else {
							ings.add(ingredient);
						}
					}
					ItemStack newRes = res.copy();
					LampBlockItem.setColor(newRes, LampColor.COLORLESS);
					LampBlockItem.setInverted(newRes, invert);
					for(String s : lRec.getStripTags()) {
						newRes.getNbt().remove(s);
					}
					String newPath = lRec.getId().getPath() + (invert ? "/invert" : lRec.isIgnoredInRecipeBook() ? "" : "/normal");
					recs.add(new ShapedRecipe(
							new Identifier(lRec.getId().getNamespace(), newPath), lRec.getGroup(),
							lRec.getWidth(), lRec.getHeight(),
							ings, newRes));
				}
			}
			else {
				boolean noInvert = res.getItem() == YItems.LAZOR_EMITTER || lRec.getStripTags().contains("Inverted");
				boolean isDye = lRec.getIngredients().stream().
						anyMatch(ing -> !ing.isEmpty() &&
								Arrays.stream(ing.getMatchingStacks()).
								map(ItemStack::getItem).
								allMatch(item -> item instanceof DyeItem ||
										LampColor.BY_ITEM.containsKey(Resolvable.mapKey(item, Registry.ITEM))));
				boolean isInvert = !noInvert && lRec.getIngredients().stream().
						anyMatch(ing -> ing.test(torch));
				BooleanList bList = noInvert ? BooleanList.of(false) : BooleanList.of(false, true);
				for(boolean invert : bList) {
					for(LampColor color : LampColor.CANONICAL_ORDER) {
						if(color == LampColor.COLORLESS && isDye) {
							continue;
						}
						DefaultedList<Ingredient> ings = DefaultedList.of();
						for(Ingredient ingredient : lRec.getIngredients()) {
							Stream<ItemStack> transformed = Arrays.stream(ingredient.getMatchingStacks()).
									flatMap(stack -> {
										Item item = stack.getItem();
										if(item instanceof LampBlockItem) {
											BooleanList aList = noInvert ? BooleanList.of(false, true) : BooleanList.of(invert);
											if(isDye) {
												return aList.stream().flatMap(aInv -> LampColor.CANONICAL_ORDER.stream().
														map(aColor -> {
															ItemStack aStack = stack.copy();
															LampBlockItem.setColor(aStack, aColor);
															LampBlockItem.setInverted(aStack, aInv);
															return aStack;
														}));
											}
											else {
												return aList.stream().map(aInv -> {
													ItemStack aStack = stack.copy();
													LampBlockItem.setColor(aStack, color);
													LampBlockItem.setInverted(aStack, aInv);
													return aStack;
												});
											}
										}
										else if(item instanceof DyeItem dye) {
											if(LampColor.BY_DYE.get(dye.getColor()) != color) {
												return Stream.empty();
											}
											return Stream.of(stack.copy());
										}
										else {
											LampColor byItemColor = LampColor.BY_ITEM.get(Resolvable.mapKey(item, Registry.ITEM));
											if(byItemColor != null && byItemColor != color) {
												return Stream.empty();
											}
											return Stream.of(stack.copy());
										}
									});
							ings.add(Ingredient.ofStacks(transformed));
						}
						ItemStack newRes = res.copy();
						LampBlockItem.setColor(newRes, color);
						if(!noInvert) {
							LampBlockItem.setInverted(newRes, invert ^ isInvert);
						}
						for(String s : lRec.getStripTags()) {
							newRes.getNbt().remove(s);
						}
						String newPath = lRec.getId().getPath() + '/' + color.asString() + (isInvert ^ invert ? "_invert" : "");
						recs.add(new ShapedRecipe(
								new Identifier(lRec.getId().getNamespace(), newPath), lRec.getGroup(),
								lRec.getWidth(), lRec.getHeight(),
								ings, newRes));
					}
				}
			}
		}
		return recs;
	}
}
