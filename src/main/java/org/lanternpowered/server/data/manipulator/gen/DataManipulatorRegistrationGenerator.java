package org.lanternpowered.server.data.manipulator.gen;

import static java.lang.String.format;
import static org.lanternpowered.server.data.manipulator.gen.TypeGenerator.newInternalName;
import static org.objectweb.asm.Opcodes.ACC_BRIDGE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_SUPER;
import static org.objectweb.asm.Opcodes.ACC_SYNTHETIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_8;

import org.lanternpowered.server.data.manipulator.AbstractDataManipulatorRegistration;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;

final class DataManipulatorRegistrationGenerator {

    private static final String nAbstractDataManipulatorRegistration = Type.getInternalName(AbstractDataManipulatorRegistration.class);

    public <M extends DataManipulator<M, I>, I extends ImmutableDataManipulator<I, M>> String generate(ClassWriter cw,
            Class<M> manipulatorType, Class<? extends M> manipulatorImplType,
            Class<I> immutableManipulatorType, Class<? extends M> immutableManipulatorImplType) {

        final String dManipulatorType = Type.getDescriptor(manipulatorType);
        final String dImmutableManipulatorType = Type.getDescriptor(immutableManipulatorType);

        final String nManipulatorType = Type.getInternalName(manipulatorType);
        final String nImmutableManipulatorType = Type.getInternalName(immutableManipulatorType);

        final String nManipulatorImplType = Type.getInternalName(manipulatorImplType);
        final String nImmutableManipulatorImplType = Type.getInternalName(immutableManipulatorImplType);

        final String className = newInternalName(nManipulatorType + "Registration");

        MethodVisitor mv;

        cw.visit(V1_8, ACC_PUBLIC + ACC_SUPER, className,
                format("L%s<%s%s>;", nAbstractDataManipulatorRegistration, dManipulatorType, dImmutableManipulatorType),
                nAbstractDataManipulatorRegistration, null);

        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>",
                    "(Lorg/spongepowered/api/plugin/PluginContainer;Ljava/lang/String;Ljava/lang/String;)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitLdcInsn(Type.getType(manipulatorType));
            mv.visitLdcInsn(Type.getType(immutableManipulatorType));
            mv.visitMethodInsn(INVOKESPECIAL, "org/lanternpowered/server/data/manipulator/AbstractDataManipulatorRegistration", "<init>",
                    "(Lorg/spongepowered/api/plugin/PluginContainer;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Class;)V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(6, 4);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "createMutable", format("()%s", dManipulatorType), null, null);
            mv.visitCode();
            mv.visitTypeInsn(NEW, nManipulatorImplType);
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, nManipulatorImplType, "<init>", "()V", false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "createImmutable", format("()%s", dImmutableManipulatorType), null, null);
            mv.visitCode();
            mv.visitTypeInsn(NEW, nImmutableManipulatorImplType);
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, nImmutableManipulatorImplType, "<init>", "()V", false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "copyMutable", format("(%s)%s", dManipulatorType, dManipulatorType), null, null);
            mv.visitCode();
            mv.visitTypeInsn(NEW, nManipulatorImplType);
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, nManipulatorImplType, "<init>",
                    format("(%s)V", dManipulatorType), false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "toMutable", format("(%s)%s", dImmutableManipulatorType, dManipulatorType), null, null);
            mv.visitCode();
            mv.visitTypeInsn(NEW, nManipulatorImplType);
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, nManipulatorImplType, "<init>", format("(%s)V", dImmutableManipulatorType), false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "toImmutable", format("(%s)%s", dManipulatorType, dImmutableManipulatorType), null, null);
            mv.visitCode();
            mv.visitTypeInsn(NEW, nImmutableManipulatorImplType);
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, nImmutableManipulatorImplType, "<init>",
                    format("(%s)V", dManipulatorType), false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "toImmutable",
                    "(Lorg/spongepowered/api/data/manipulator/DataManipulator;)Lorg/spongepowered/api/data/manipulator/ImmutableDataManipulator;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitTypeInsn(CHECKCAST, nManipulatorType);
            mv.visitMethodInsn(INVOKEVIRTUAL, className,
                    "toImmutable", format("(%s)%s", dManipulatorType, dImmutableManipulatorType), false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "toMutable",
                    "(Lorg/spongepowered/api/data/manipulator/ImmutableDataManipulator;)Lorg/spongepowered/api/data/manipulator/DataManipulator;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitTypeInsn(CHECKCAST, nImmutableManipulatorType);
            mv.visitMethodInsn(INVOKEVIRTUAL, className,
                    "toMutable", format("(%s)%s", dImmutableManipulatorType, dManipulatorType), false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "copyMutable",
                    "(Lorg/spongepowered/api/data/manipulator/DataManipulator;)Lorg/spongepowered/api/data/manipulator/DataManipulator;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitTypeInsn(CHECKCAST, nManipulatorType);
            mv.visitMethodInsn(INVOKEVIRTUAL, className,
                    "copyMutable", format("(%s)%s", dManipulatorType, dManipulatorType), false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "createImmutable",
                    "()Lorg/spongepowered/api/data/manipulator/ImmutableDataManipulator;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEVIRTUAL, className,
                    "createImmutable", format("()%s", dImmutableManipulatorType), false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "createMutable",
                    "()Lorg/spongepowered/api/data/manipulator/DataManipulator;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEVIRTUAL, className,
                    "createMutable", format("()%s", dManipulatorType), false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        cw.visitEnd();
        return className.replace('/', '.');
    }
}
