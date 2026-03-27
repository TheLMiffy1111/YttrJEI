package thelm.yttrjei.ingredient.serializer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import thelm.yttrjei.YttrJEI;
import thelm.yttrjei.ingredient.FakeModelIngredient;

public class EmiToJeiIngredientDeserializer {

	public static final Map<String, Function<JsonElement, List<Object>>> DESERIALIZERS = new HashMap<>();
	public static final Map<Identifier, BiFunction<Identifier, Long, List<Object>>> TAG_ADAPTERS = new HashMap<>();

	static {
		DESERIALIZERS.put("item", EmiToJeiIngredientDeserializer::deserializeItem);
		DESERIALIZERS.put("fluid", EmiToJeiIngredientDeserializer::deserializeFluid);
		DESERIALIZERS.put("tag", EmiToJeiIngredientDeserializer::deserializeTag);
		DESERIALIZERS.put("list", EmiToJeiIngredientDeserializer::deserializeList);
		DESERIALIZERS.put("jemi", EmiToJeiIngredientDeserializer::deserializeJemi);
		DESERIALIZERS.put("yttr_model", EmiToJeiIngredientDeserializer::deserializeYttrModel);

		TAG_ADAPTERS.put(RegistryKeys.BLOCK.getValue(), EmiToJeiIngredientDeserializer::adaptBlockTag);
		TAG_ADAPTERS.put(RegistryKeys.ITEM.getValue(), EmiToJeiIngredientDeserializer::adaptItemTag);
		TAG_ADAPTERS.put(RegistryKeys.FLUID.getValue(), EmiToJeiIngredientDeserializer::adaptFluidTag);
	}

	public static List<Object> deserialize(JsonElement element) {
		if(element == null || element.isJsonNull()) {
			return List.of();
		}
		try {
			String type;
			if(element.isJsonObject()) {
				JsonObject json = element.getAsJsonObject();
				type = json.get("type").getAsString();
			}
			else if(element.isJsonArray()) {
				type = "list";
			}
			else {
				String[] split = element.getAsString().split(":");
				type = split[0];
				if(type.startsWith("#")) {
					type = "tag";
				}
			}
			return DESERIALIZERS.getOrDefault(type, DESERIALIZERS.get("jemi")).apply(element);
		}
		catch(Exception e) {
			return List.of();
		}
	}

	public static final Pattern STACK_REGEX = Pattern.compile("^([\\w_\\-./]+):([\\w_\\-.]+):([\\w_\\-./]+)(\\{.*\\})?$");
	public static final Pattern TAG_REGEX = Pattern.compile("^#([\\w_\\-.:]+):([\\w_\\-.]+):([\\w_\\-./]+)(\\{.*\\})?$");

	public static List<Object> deserializeItem(JsonElement element) {
		Identifier id = null;
		String nbt = null;
		int amount = 1;
		if(JsonHelper.isString(element)) {
			String s = element.getAsString();
			Matcher m = STACK_REGEX.matcher(s);
			if(m.matches()) {
				id = Identifier.of(m.group(2), m.group(3));
				nbt = m.group(4);
			}
		}
		else if(element.isJsonObject()) {
			JsonObject json = element.getAsJsonObject();
			id = Identifier.tryParse(JsonHelper.getString(json, "id"));
			nbt = JsonHelper.getString(json, "nbt", null);
			amount = JsonHelper.getInt(json, "amount", 1);
		}
		if(id != null) {
			try {
				NbtCompound nbtComp = null;
				if(nbt != null) {
					nbtComp = StringNbtReader.parse(nbt);
				}
				ItemStack stack = new ItemStack(Registries.ITEM.get(id), amount);
				stack.setNbt(nbtComp);
				return List.of(stack);
			}
			catch(Exception e) {
				return List.of();
			}
		}
		return List.of();
	}

	public static List<Object> deserializeFluid(JsonElement element) {
		IPlatformFluidHelper<?> fluidHelper = YttrJEI.jeiHelpers.getPlatformFluidHelper();
		Identifier id = null;
		String nbt = null;
		long amount = fluidHelper.bucketVolume();
		if(JsonHelper.isString(element)) {
			String s = element.getAsString();
			Matcher m = STACK_REGEX.matcher(s);
			if(m.matches()) {
				id = Identifier.of(m.group(2), m.group(3));
				nbt = m.group(4);
			}
		}
		else if(element.isJsonObject()) {
			JsonObject json = element.getAsJsonObject();
			id = Identifier.tryParse(JsonHelper.getString(json, "id"));
			nbt = JsonHelper.getString(json, "nbt", null);
			amount = JsonHelper.getLong(json, "amount", amount);
		}
		if(id != null) {
			try {
				NbtCompound nbtComp = null;
				if(nbt != null) {
					nbtComp = StringNbtReader.parse(nbt);
				}
				return List.of(fluidHelper.create(Registries.FLUID.get(id), amount, nbtComp));
			}
			catch(Exception e) {
				return List.of();
			}
		}
		return List.of();
	}

	public static List<Object> deserializeTag(JsonElement element) {
		Identifier registry = null;
		Identifier id = null;
		long amount = 1;
		if(JsonHelper.isString(element)) {
			String s = element.getAsString();
			Matcher m = TAG_REGEX.matcher(s);
			if(m.matches()) {
				registry = Identifier.tryParse(m.group(1));
				id = Identifier.of(m.group(2), m.group(3));
			}
		}
		else if(element.isJsonObject()) {
			JsonObject json = element.getAsJsonObject();
			registry = Identifier.tryParse(json.get("registry").getAsString());
			id = Identifier.tryParse(json.get("id").getAsString());
			amount = JsonHelper.getLong(json, "amount", 1);
		}
		if(registry != null && id != null && TAG_ADAPTERS.containsKey(registry)) {
			return TAG_ADAPTERS.get(registry).apply(id, amount);
		}
		return List.of();
	}

	public static List<Object> deserializeList(JsonElement element) {
		JsonArray ingredientsArray;
		if(element.isJsonObject()) {
			JsonObject json = element.getAsJsonObject();
			ingredientsArray = JsonHelper.asArray(json, "ingredients");
		}
		else if(element.isJsonArray()) {
			ingredientsArray = element.getAsJsonArray();
		}
		else {
			return List.of();
		}
		List<Object> ingredients = new ArrayList<>();
		for(JsonElement ingredientElement : ingredientsArray) {
			ingredients.addAll(deserialize(ingredientElement));
		}
		return ingredients;
	}

	public static List<Object> deserializeJemi(JsonElement element) {
		String uid = null;
		if(JsonHelper.isString(element)) {
			uid = element.getAsString();
			if(uid.startsWith("jemi:")) {
				uid = uid.substring(5);
			}
		}
		else if(element.isJsonObject()) {
			JsonObject json = element.getAsJsonObject();
			uid = JsonHelper.getString(json, "uid");
		}
		if(uid != null) {
			IIngredientManager manager = YttrJEI.jeiHelpers.getIngredientManager();
			Optional<IIngredientType<?>> typeOpt = manager.getIngredientTypeForUid(uid);
			if(typeOpt.isPresent()) {
				IIngredientType<?> type = typeOpt.get();
				Optional<? extends ITypedIngredient<?>> opt = manager.getTypedIngredientByUid(type, uid);
				if(opt.isPresent()) {
					return List.of(opt.get().getIngredient());
				}
			}
		}
		return List.of();
	}

	public static List<Object> deserializeYttrModel(JsonElement element) {
		Identifier id = null;
		if(JsonHelper.isString(element)) {
			String s = element.getAsString();
			Matcher m = STACK_REGEX.matcher(s);
			if(m.matches()) {
				id = Identifier.of(m.group(2), m.group(3));
			}
		}
		if(id != null) {
			return List.of(new FakeModelIngredient(id));
		}
		return List.of();
	}

	public static List<Object> adaptBlockTag(Identifier id, long amount) {
		TagKey<Block> tagKey = TagKey.of(RegistryKeys.BLOCK, id);
		return Registries.BLOCK.getEntryList(tagKey).stream().
				flatMap(named -> named.stream()).
				map(holder -> holder.value()).
				sorted(Comparator.comparingInt(Registries.BLOCK::getRawId)).
				<Object>map(block -> new ItemStack(block, (int)amount)).
				toList();
	}

	public static List<Object> adaptItemTag(Identifier id, long amount) {
		TagKey<Item> tagKey = TagKey.of(RegistryKeys.ITEM, id);
		return Registries.ITEM.getEntryList(tagKey).stream().
				flatMap(named -> named.stream()).
				map(holder -> holder.value()).
				sorted(Comparator.comparingInt(Registries.ITEM::getRawId)).
				<Object>map(item -> new ItemStack(item, (int)amount)).
				toList();
	}

	public static List<Object> adaptFluidTag(Identifier id, long amount) {
		IPlatformFluidHelper<?> fluidHelper = YttrJEI.jeiHelpers.getPlatformFluidHelper();
		long bucket = fluidHelper.bucketVolume();
		TagKey<Fluid> tagKey = TagKey.of(RegistryKeys.FLUID, id);
		return Registries.FLUID.getEntryList(tagKey).stream().
				flatMap(named -> named.stream()).
				map(holder -> holder.value()).
				sorted(Comparator.comparingInt(Registries.FLUID::getRawId)).
				filter(fluid -> fluid.isStill(fluid.getDefaultState())).
				<Object>map(fluid -> fluidHelper.create(fluid, amount * bucket / 81000)).
				toList();
	}
}
