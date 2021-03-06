package com.noobanidus.warmstone.core.transformers;

import com.noobanidus.warmstone.core.CustomClassWriter;
import com.noobanidus.warmstone.core.IWarmTransformer;
import com.noobanidus.warmstone.core.WarmStoneCore;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Collections;
import java.util.List;

public class GroundTransformer implements IWarmTransformer {
    @Override
    public boolean accepts(String name, String transformedName) {
        List<String> classes = Collections.singletonList("net.minecraft.world.gen.structure.StructureVillagePieces$Village");
        for (String clazz : classes) {
            if (clazz.equals(transformedName)) return true;
        }

        return false;
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);

        MethodNode average = WarmStoneCore.findMethod(classNode, WarmStoneCore.averageGroundLevelFinder);
        if (average != null) {
            average.instructions.clear();
            average.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            average.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
            average.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
            average.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/noobanidus/warmstone/core/hooks/GroundHooks", "getAverageGroundLevel", "(Lnet/minecraft/world/gen/structure/StructureVillagePieces$Village;Lnet/minecraft/world/World;Lnet/minecraft/world/gen/structure/StructureBoundingBox;)I", false));
            average.instructions.add(new InsnNode(Opcodes.IRETURN));

            CustomClassWriter writer = new CustomClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            classNode.accept(writer);
            return writer.toByteArray();
        }

        return basicClass;
    }
}
