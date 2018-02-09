package matteroverdrive.client.render;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

@SideOnly(Side.CLIENT)
public class ShaderManager {

    @SuppressWarnings("unchecked")
    private static ShaderResourcePack shaderPack = new ShaderResourcePack();
    private static Field _listShaders;

    public static void initPack() {
        ((List<IResourcePack>) ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "field_110449_ao", "defaultResourcePacks")).add(shaderPack);
    }

    public static void preInit() {
        ((SimpleReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(shaderPack);
    }

    public static void checkList() {
        if (_listShaders == null) {
            _listShaders = ReflectionHelper.findField(ShaderGroup.class, "field_148031_d", "listShaders");
        }
    }

    public static void loadBlackholeAndRunRender(Runnable runnable) {
        checkList();
        if (Minecraft.getMinecraft().world != null) {
            EntityRenderer er = Minecraft.getMinecraft().entityRenderer;

            if (!er.isShaderActive()) {
                er.loadShader(new ResourceLocation("mo", "shaders/blackhole.json"));
            } else {
                er.stopUseShader();
            }
        }
    }

    public static class ShaderResourcePack implements IResourcePack, IResourceManagerReloadListener {

        private final Map<ResourceLocation, String> loadedData = new HashMap<>();

        protected boolean validPath(ResourceLocation location) {
            return location.getResourceDomain().equals("mo") && location.getResourcePath().startsWith("shaders/");
        }

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager) {
            loadedData.clear();
        }

        @Override
        public InputStream getInputStream(ResourceLocation location) throws IOException {
            if (validPath(location)) {
                String s = loadedData.computeIfAbsent(location, loc -> {
                    InputStream in = ShaderManager.class.getResourceAsStream("/" + location.getResourcePath());
                    StringBuilder data = new StringBuilder();
                    try (Scanner scan = new Scanner(in)) {
                        while (scan.hasNextLine()) {
                            data.append(scan.nextLine()).append('\n');
                        }
                    }
                    return data.toString();
                });

                return new ByteArrayInputStream(s.getBytes());
            }
            throw new FileNotFoundException(location.toString());
        }

        @Override
        public boolean resourceExists(ResourceLocation location) {
            return validPath(location) && ShaderManager.class.getResource("/" + location.getResourcePath()) != null;
        }

        @Override
        public Set<String> getResourceDomains() {
            return ImmutableSet.of("mo");
        }

        @Nullable
        @Override
        public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException {
            if ("pack".equals(metadataSectionName)) {
                return (T) new PackMetadataSection(new TextComponentString("MatterOverdrive shaders"), 3);
            }
            return null;
        }

        @Override
        public BufferedImage getPackImage() throws IOException {
            throw new FileNotFoundException("pack.png");
        }

        @Override
        public String getPackName() {
            return "MatterOverdrive Shaders Pack";
        }
    }
}