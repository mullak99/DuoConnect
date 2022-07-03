package couk.mullak99.duoconnect.mixin;

import couk.mullak99.duoconnect.DuoConnect;
import couk.mullak99.duoconnect.core.Connection;
import couk.mullak99.duoconnect.screen.DuoConnectScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)

public abstract class MultiplayerScreenMixin extends Screen {
    private static final Identifier BUTTON_ICON = new Identifier("duoconnect", "textures/gui/duoconnect.png");
    private static ServerInfo serverOne = new ServerInfo("Server 1", "", false);
    private static ServerInfo serverTwo = new ServerInfo("Server 2", "", false);

    protected MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "init")
    private void addButton(CallbackInfo ci) {
        this.addDrawableChild(new TexturedButtonWidget(this.width / 2 - 178, this.height - 52, 20, 20, 0, 0, 20, BUTTON_ICON, 32, 64, button -> {
            MinecraftClient.getInstance().setScreen(new DuoConnectScreen(this, this::duoConnect, this.serverOne, this.serverTwo));
        }));
    }

    private void duoConnect(boolean confirmedAction) {
        this.client.setScreen(this);
        if (confirmedAction) {
            DuoConnect.LOGGER.info("Connecting to '" + serverOne.address + "' and then '" + serverTwo.address + "'...");
            Connection.doConnection(this, serverOne, serverTwo);
        }
    }
}
