package thelm.yttrjei.alias;

import java.util.List;

import diy.y2k.yttr.client.YttrClientInit;
import diy.y2k.yttr.init.content.YItems;
import diy.y2k.yttr.init.technical.YOpponents;
import diy.y2k.yttr.mechanics.LampColor;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IIngredientAliasRegistration;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import thelm.yttrjei.ingredient.FakeModelIngredient;

public class YttrAliasMapping {

	public static void addAliases(IIngredientAliasRegistration reg) {
		if(YItems.DIFFRACTOR.isPresent()) {
			addItem(reg, YItems.DIFFRACTOR.get(), "alias.emi.yttr.cloaking", "alias.emi.yttr.invisibility");
		}
		if(YItems.PLATFORMS.isPresent()) {
			addItem(reg, YItems.PLATFORMS.get(), "alias.emi.yttr.flight");
		}
		addItem(reg, YItems.PROJECTOR, "alias.emi.yttr.bifrost", "alias.emi.yttr.angel");
		addItem(reg, YItems.SHIFTER, "alias.emi.yttr.equaltrade", "alias.emi.yttr.shiftingcrust");
		addItem(reg, YItems.REINFORCED_CLEAVER, "alias.emi.yttr.paxel", "alias.emi.yttr.multitool");
		addItem(reg, YItems.CLEAVER, "alias.emi.yttr.carpenter", "alias.emi.yttr.slope", "alias.emi.yttr.wedge", "alias.emi.yttr.vslab");
		addItems(reg,
				List.of(YItems.RIFLE, YItems.RIFLE_REINFORCED, YItems.RIFLE_OVERCLOCKED),
				"alias.emi.yttr.laser", "alias.emi.yttr.blaster", "alias.emi.yttr.gun");
		addItem(reg, YItems.AWARE_HOPPER, "alias.emi.yttr.autoworkbench", "alias.emi.yttr.autocrafter", "alias.emi.yttr.ender", "block.minecraft.crafting_table");
		addItem(reg, YItems.EFFECTOR, "alias.emi.yttr.portablehole");
		addItem(reg, YItems.SNARE, "alias.emi.yttr.safarinet", "alias.emi.yttr.dolly", "alias.emi.yttr.packingtape", "alias.emi.yttr.cardboardbox", "alias.emi.yttr.morb");
		addItems(reg,
				List.of(YItems.CUPROSTEEL_PLATE, YItems.CUPROSTEEL_BIG_PLATE),
				"alias.emi.yttr.obsidplate", "alias.emi.yttr.playerplate");
		addItemStacks(reg,
				LampColor.CANONICAL_ORDER.stream().map(color -> {
					ItemStack lazor = new ItemStack(YItems.LAZOR_EMITTER);
					YOpponents.LAMP_COLOR.set(lazor, color);
					return lazor;
				}).toList(),
				"alias.emi.yttr.laser", "alias.emi.yttr.lazer", "alias.emi.yttr.floodlight", "alias.emi.yttr.wrathlamp");
		addItem(reg, YItems.IR_LAZOR_EMITTER, "alias.emi.yttr.laser", "alias.emi.yttr.lazer", "alias.emi.yttr.floodlight", "alias.emi.yttr.wrathlamp");
		addItem(reg, YItems.RUINED_CONTAINER, "alias.emi.yttr.barrel");
		addItem(reg, YItems.RUINED_DEVICE_BC_1, "alias.emi.yttr.quarry");
		addItem(reg, YItems.RUINED_DEVICE_BC_2, "alias.emi.yttr.rengine");
		addItem(reg, YItems.RUINED_DEVICE_GT_1, "alias.emi.yttr.icentrifuge");
		addItem(reg, YItems.RUINED_DEVICE_RP_1, "alias.emi.yttr.relay");
		addItem(reg, YItems.RUINED_DEVICE_FO_1, "alias.emi.yttr.arb");
		addItem(reg, YItems.RUINED_PIPE, "alias.emi.yttr.pipe");
		addItem(reg, YItems.RUINED_TUBE, "alias.emi.yttr.tube");
		addItem(reg, YItems.RUINED_CONSTRUCT_RC_1, "alias.emi.yttr.cokeoven");
		addItem(reg, YItems.RUINED_CONSTRUCT_RC_2, "alias.emi.yttr.blastfurnace");
		addItems(reg,
				List.of(YItems.CLAMBER_BLOCK, YItems.SOUL_CLAMBER_BLOCK),
				"alias.emi.yttr.abstruse");
		addItem(reg, YItems.DROP_OF_CONTINUITY, "alias.emi.yttr.continuum", "alias.emi.yttr.gambling");
		addItem(reg, YItems.GLITCHWEP, "alias.emi.yttr.sword", "alias.emi.yttr.gun", "alias.emi.yttr.grenade", "alias.emi.yttr.zzazz");
		addItem(reg, YItems.GRASPER, "alias.emi.yttr.vacuum", "alias.emi.yttr.blackhole", "alias.emi.yttr.ender", "block.minecraft.hopper");

		addModel(reg, YttrClientInit.SUPERCOOLED_NEODYMIUM_MODEL, "alias.emi.yttr.flight", "alias.emi.yttr.thaumostatic", "alias.emi.yttr.tiara", "alias.emi.yttr.tiara2");
		addModel(reg, YttrClientInit.VOID_CAULDRON_MODEL, "alias.emi.yttr.incinerator", "alias.emi.yttr.nullifier", "alias.emi.yttr.trash_can");
	}

	public static void addItem(IIngredientAliasRegistration registration, ItemConvertible item, String... aliases) {
		addItemStack(registration, new ItemStack(item), aliases);
	}

	public static void addItemStack(IIngredientAliasRegistration registration, ItemStack stack, String... aliases) {
		registration.addAliases(VanillaTypes.ITEM_STACK, stack, List.of(aliases));
	}

	public static void addItems(IIngredientAliasRegistration registration, List<ItemConvertible> items, String... aliases) {
		addItemStacks(registration, items.stream().map(ItemStack::new).toList(), aliases);
	}

	public static void addItemStacks(IIngredientAliasRegistration registration, List<ItemStack> stacks, String... aliases) {
		registration.addAliases(VanillaTypes.ITEM_STACK, stacks, List.of(aliases));
	}

	public static void addModel(IIngredientAliasRegistration registration, Identifier id, String... aliases) {
		registration.addAliases(FakeModelIngredient.TYPE, new FakeModelIngredient(id), List.of(aliases));
	}
}
