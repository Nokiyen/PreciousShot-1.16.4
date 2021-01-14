/*
    In default, if you cancel ScreenShotEvent, Forge automatically sends a chat message to the player.
    This asm skip the message.
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
    if(event.isCanceled()) {
        messageConsumer.accept(event.getCancelMessage());
    }
    else {
        ~~~~~
    }

    [modified byte code]
    L8
        LINENUMBER 64 L8
        ALOAD 9
        INVOKEVIRTUAL net/minecraftforge/client/event/ScreenshotEvent.isCanceled ()Z
        IFEQ L9
    L10
        LINENUMBER 65 L10
        ALOAD 5
        ALOAD 9
        INVOKEVIRTUAL net/minecraftforge/client/event/ScreenshotEvent.getCancelMessage ()Lnet/minecraft/util/text/ITextComponent;

        //NEW INJECTED CODE//
        POP
        RETURN
        //END//

        INVOKEINTERFACE java/util/function/Consumer.accept (Ljava/lang/Object;)V (itf)
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