package matteroverdrive.client.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MOModelLoader implements ICustomModelLoader {
    private static Map<ResourceLocation, IModel> BLOCK_MODELS = Maps.newHashMap();
    private static Map<ResourceLocation, IModel> ITEM_MODELS = Maps.newHashMap();

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return BLOCK_MODELS.containsKey(modelLocation) || ITEM_MODELS.containsKey(modelLocation);
    }

    @Override
    @Nonnull
    public IModel loadModel(ResourceLocation modelLocation) {
        return BLOCK_MODELS.getOrDefault(modelLocation, ITEM_MODELS.getOrDefault(modelLocation, ModelLoaderRegistry.getMissingModel()));
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        BLOCK_MODELS.clear();
        ITEM_MODELS.clear();
        MinecraftForge.EVENT_BUS.post(new MOModelRegistryEvent.Block(this, BLOCK_MODELS));
        MinecraftForge.EVENT_BUS.post(new MOModelRegistryEvent.Item(this, ITEM_MODELS));
    }

    public static class MOModelRegistryEvent extends Event {
        private final MOModelLoader loader;
        private final Map<ResourceLocation, IModel> models;
        private final Type type;

        public MOModelRegistryEvent(MOModelLoader loader, Map<ResourceLocation, IModel> models, Type type) {
            this.loader = loader;
            this.models = models;
            this.type = type;
        }

        public MOModelLoader getLoader() {
            return loader;
        }

        public Map<ResourceLocation, IModel> getModels() {
            return ImmutableMap.copyOf(models);
        }

        public void register(ResourceLocation resourceLocation, IModel model) {
            models.put(resourceLocation, model);
        }

        public Type getType() {
            return type;
        }

        public enum Type {
            BLOCK,
            ITEM;
        }

        public static class Block extends MOModelRegistryEvent {
            public Block(MOModelLoader loader, Map<ResourceLocation, IModel> models) {
                super(loader, models, Type.BLOCK);
            }
        }

        public static class Item extends MOModelRegistryEvent {
            public Item(MOModelLoader loader, Map<ResourceLocation, IModel> models) {
                super(loader, models, Type.ITEM);
            }
        }
    }
}