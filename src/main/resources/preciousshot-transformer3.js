/*
    cancel esc key when shooting mod or panorama mod is open.
 */
function initializeCoreMod() {

    //You can't use let or const in your asm javascript. I don't know why.
    var ASMAPI = Java.type("net.minecraftforge.coremod.api.ASMAPI");
    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
    var InsnList = Java.type("org.objectweb.asm.tree.InsnList");
    var InsnNode = Java.type("org.objectweb.asm.tree.InsnNode");
    var VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");
    var MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");
    var AbstractInsnNode = Java.type("org.objectweb.asm.tree.AbstractInsnNode");
    var JumpInsnNode = Java.type("org.objectweb.asm.tree.JumpInsnNode");
    var LabelNode = Java.type("org.objectweb.asm.tree.LabelNode");
    var FrameNode = Java.type("org.objectweb.asm.tree.FrameNode");

    var INVOKEVIRTUAL = Opcodes.INVOKEVIRTUAL
    var IFEQ = Opcodes.IFEQ;
    var LABEL = AbstractInsnNode.LABEL;
    var GOTO = Opcodes.GOTO;

    var mappedMethodName1 = ASMAPI.mapMethod("func_197961_a"); // = onKeyInput
    var mappedMethodName2 = ASMAPI.mapMethod("func_71385_j"); // = displayInGameMenu


    /*
    [normal code]
    if (key == 256) {
        boolean flag2 = InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), 292);
        this.mc.displayInGameMenu(flag2);
    }

    [modified code]
    if (key == 256) {
        boolean flag2 = InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), 292);
        if(!ModeManager.isShootingModeOpen()) {
            this.mc.displayInGameMenu(flag2);
        }
    }
    */
    return {
        'coremodmethod': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.KeyboardListener',
                'methodName': mappedMethodName1,
                'methodDesc': '(JIIII)V'
            },
            'transformer': function(method) {
                print("Enter transformer3.");

                var targetVirtual;
                var arrayLength = method.instructions.size();
                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);
                    if(instruction.getOpcode() == INVOKEVIRTUAL) {
                        if(instruction.owner == "net/minecraft/client/Minecraft") {
                            if(instruction.name == mappedMethodName2) {
                                if(instruction.desc == "(Z)V") {
                                    if(instruction.itf == false) {
                                        targetVirtual = instruction;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if (!targetVirtual) {
                    print("can't get targetVirtual.");
                    return;
                }

                var labelBeforeVirtual;
                for (i = method.instructions.indexOf(targetVirtual); i >= 0; --i) {
                    var instruction = method.instructions.get(i);
                    if (instruction.getType() == LABEL) {
                        labelBeforeVirtual = instruction;
                        break;
                    }
                }
                if (!labelBeforeVirtual) {
                    return;
                }

                var labelAfterVirtual;
                for (i = method.instructions.indexOf(targetVirtual); i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);
                    if (instruction.getType() == LABEL) {
                        labelAfterVirtual = instruction;
                        break;
                    }
                }
                if (!labelAfterVirtual) {
                    return;
                }

                var newLabel = new LabelNode();

                var toInject = new InsnList();

                toInject.add(new MethodInsnNode(
                        //int opcode
                        Opcodes.INVOKESTATIC,
                        //String owner
                        "noki/preciousshot/mode/ModeManager",
                        //String name
                        "isShootingModeOpen",
                        //String descriptor
                        "()Z",
                        //boolean isInterface
                        false
                ));
                toInject.add(new JumpInsnNode(Opcodes.IFNE, newLabel));
                method.instructions.insert(labelBeforeVirtual, toInject);

                var secondInject = new InsnList();
                secondInject.add(newLabel);
//                secondInject.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
//                secondInject.add(new InsnNode(Opcodes.POP));
//                secondInject.add(new InsnNode(Opcodes.POP));
                method.instructions.insert(targetVirtual, secondInject);
//                toInject.add(new InsnNode(Opcodes.RETURN));

//                method.instructions.insertBefore(targetVirtual, toInject);

                /*                // Inject new instructions just after the target.
                                method.instructions.insert(labelBeforeVirtual, toInject);

                                var secondInject = new InsnList();
                                secondInject.add(newLabel);
                                toInject.add(new InsnNode(Opcodes.POP));

                                method.instructions.insertBefore(labelAfterVirtual, secondInject);*/

                return method;
            }
        }
    }
}