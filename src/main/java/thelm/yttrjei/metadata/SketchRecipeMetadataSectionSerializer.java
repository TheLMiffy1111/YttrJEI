package thelm.yttrjei.metadata;

import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.resource.metadata.ResourceMetadataReader;
import thelm.yttrjei.ingredient.serializer.EmiToJeiIngredientDeserializer;

public class SketchRecipeMetadataSectionSerializer implements ResourceMetadataReader<SketchRecipeMetadataSection> {

	public static final SketchRecipeMetadataSectionSerializer INSTANCE = new SketchRecipeMetadataSectionSerializer();

	@Override
	public String getKey() {
		return "yttr:sketch_recipe";
	}

	@Override
	public SketchRecipeMetadataSection fromJson(JsonObject json) {
		List<List<Object>> inputs = json.getAsJsonArray("inputs").asList().stream().map(EmiToJeiIngredientDeserializer::deserialize).filter(l -> !l.isEmpty()).toList();
		List<List<Object>> catalysts = json.getAsJsonArray("catalysts").asList().stream().map(EmiToJeiIngredientDeserializer::deserialize).filter(l -> !l.isEmpty()).toList();
		List<List<Object>> outputs = json.getAsJsonArray("outputs").asList().stream().map(EmiToJeiIngredientDeserializer::deserialize).filter(l -> !l.isEmpty()).toList();
		return new SketchRecipeMetadataSection(inputs, catalysts, outputs);
	}
}
