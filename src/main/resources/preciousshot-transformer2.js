/*
    add mod controllable chat click event.
 */
function initializeCoreMod() {

    //You can't use let or const in your asm javascript. I don't know why.
    var ASMAPI = Java.type("net.minecraftforge.coremod.api.ASMAPI");
    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
    var InsnList = Java.type("org.objectweb.asm.tree.InsnList");
    var InsnNode = Java.type("org.objectweb.asm.tree.InsnNode");
    var VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");
    var MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");

    var mappedMethodName1 = ASMAPI.mapMethod("func_230455_a_"); // = handleComponentClicked


    /*
    [normal code]

    [modified byte code]
    */
    return {
        'coremodmethod': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.gui.screen.Screen',
                'methodName': mappedMethodName1,
                'methodDesc': '(Lnet/minecraft/util/text/Style;)Z'
            },
            'transformer': function(method) {
                print("Enter transformer2.");

                var arrayLength = method.instructions.size();
                var target_instruction = null;

                var toInject = new InsnList();
                toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
                toInject.add(new MethodInsnNode(
                        //int opcode
                        Opcodes.INVOKESTATIC,
                        //String owner
                        "noki/preciousshot/asm/ASMChatClickEvent",
                        //String name
                        "handleComponentClick",
                        //String descriptor
                        "(Lnet/minecraft/util/text/Style;)V",
                        //boolean isInterface
                        false
                ));

                // Inject new instructions just after the target.
                method.instructions.insert(toInject);

                return method;
            }
        }
    }
}