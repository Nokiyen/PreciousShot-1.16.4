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

    var mappedMethodName1 = ASMAPI.mapMethod("func_228051_b_"); // = saveScreenshotRaw


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
                'class': 'net.minecraft.util.ScreenShotHelper',
                'methodName': mappedMethodName1,
                'methodDesc': '(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/shader/Framebuffer;Ljava/util/function/Consumer;)V'
            },
            'transformer': function(method) {
                print("Enter transformer.");

                var arrayLength = method.instructions.size();
                var target_instruction = null;

                //search the target instruction from the entire instructions.
                for(var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);

/*                    if (instruction.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                        if (instruction.owner == "net/minecraftforge/client/event/ScreenshotEvent") {
                            if (instruction.name == "getCancelMessage") {
                                if (instruction.desc == "()Lnet/minecraft/util/text/ITextComponent") {
                                    target_instruction = instruction;
                                    print("Found injection point.");
                                    break;
                                }
                            }
                        }
                    }*/
                    if(instruction.name === "getCancelMessage") {
                        target_instruction = instruction;
                        print("Found injection point.");
                        break;
                    }
                }

                if(target_instruction == null) {
                    print("Failed to detect target.");
                    return method;
                }

                var toInject = new InsnList();
                toInject.add(new InsnNode(Opcodes.POP));
                toInject.add(new InsnNode(Opcodes.RETURN));

                // Inject new instructions just after the target.
                method.instructions.insert(target_instruction, toInject);

                return method;
            }
        }
    }
}