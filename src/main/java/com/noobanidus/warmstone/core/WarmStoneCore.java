package com.noobanidus.warmstone.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Predicate;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(1001)
public class WarmStoneCore implements IFMLLoadingPlugin {
    public static String getAverageGroundLevel;
    public static String addComponentParts;
    public static String canFallThrough;
    public static String onBlockAdded;
    public static String neighborChanged;

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"com.noobanidus.warmstone.core.ClassTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        boolean dev = !(Boolean) data.get("runtimeDeobfuscationEnabled");
        getAverageGroundLevel = dev ? "getAverageGroundLevel" : "func_74889_b";
        addComponentParts = dev ? "addComponentParts" : "func_74875_a";
        canFallThrough = dev ? "canFallThrough" : "func_185759_i";
        onBlockAdded = dev ? "onBlockAdded" : "func_176213_c";
        neighborChanged = dev ? "neighborChanged" : "func_189540_a";
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    public static MethodNode findMethod(ClassNode node, Predicate<MethodNode> finder) {
        for (MethodNode m : node.methods) {
            if (finder.test(m)) return m;
        }

        return null;
    }

    public static Predicate<MethodNode> averageGroundLevelFinder = methodNode -> methodNode.name.equals(WarmStoneCore.getAverageGroundLevel);
    public static Predicate<MethodNode> addComponentPartsFinder = methodNode -> methodNode.name.equals(WarmStoneCore.addComponentParts);
    public static Predicate<MethodNode> canFallThroughFinder = methodNode -> methodNode.name.equals(WarmStoneCore.canFallThrough);
    public static Predicate<MethodNode> onBlockAddedFinder = methodNode -> methodNode.name.equals(WarmStoneCore.onBlockAdded);
    public static Predicate<MethodNode> neighborChangedFinder = methodNode -> methodNode.name.equals(WarmStoneCore.neighborChanged);
}
