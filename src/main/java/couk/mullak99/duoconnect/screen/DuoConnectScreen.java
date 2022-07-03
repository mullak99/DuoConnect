package couk.mullak99.duoconnect.screen;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DirectConnectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

@Environment(value= EnvType.CLIENT)
public class DuoConnectScreen extends Screen {
    private static final Text ENTER_IP_TEXT = Text.translatable("duoconnect.serverAddresses");
    private ButtonWidget selectServerButton;
    private TextFieldWidget addressFieldOne;
    private TextFieldWidget addressFieldTwo;
    private final BooleanConsumer callback;
    private final Screen parent;
    private final ServerInfo serverEntryOne;
    private final ServerInfo serverEntryTwo;

    public DuoConnectScreen(Screen parent, BooleanConsumer callback, ServerInfo serverOne, ServerInfo serverTwo) {
        super(Text.translatable("duoconnect.connectScreen"));
        this.parent = parent;
        this.callback = callback;
        this.serverEntryOne = serverOne;
        this.serverEntryTwo = serverTwo;
    }

    @Override
    public void tick() {
        this.addressFieldOne.tick();
        this.addressFieldTwo.tick();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER)
        {
            if (this.selectServerButton.active && this.getFocused() == this.addressFieldOne || this.getFocused() == this.addressFieldTwo) {
                this.joinServers();
                return true;
            }
            else
            {
                if (this.getFocused() == this.addressFieldOne) {
                    this.setFocused(this.addressFieldTwo);
                    return true;
                }
                else if (this.getFocused() == this.addressFieldTwo) {
                    this.setFocused(this.addressFieldOne);
                    return true;
                }
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected void init() {
        this.client.keyboard.setRepeatEvents(true);
        this.selectServerButton = this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 96 + 12, 200, 20, Text.translatable("duoconnect.joinServers"), button -> this.joinServers()));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 120 + 12, 200, 20, ScreenTexts.CANCEL, button -> this.callback.accept(false)));

        // Address 1
        this.addressFieldOne = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 116, 200, 20, Text.translatable("addServer.enterIp"));
        this.addressFieldOne.setMaxLength(128);
        this.addressFieldOne.setTextFieldFocused(true);
        this.addressFieldOne.setChangedListener(text -> this.onAddressFieldChanged());
        this.addSelectableChild(this.addressFieldOne);

        // Address 2
        this.addressFieldTwo = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 140, 200, 20, Text.translatable("addServer.enterIp"));
        this.addressFieldTwo.setMaxLength(128);
        this.addressFieldTwo.setChangedListener(text -> this.onAddressFieldChanged());
        this.addSelectableChild(this.addressFieldTwo);

        this.setInitialFocus(this.addressFieldOne);
        this.onAddressFieldChanged();
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String stringOne = this.addressFieldOne.getText();
        String stringTwo = this.addressFieldOne.getText();
        this.init(client, width, height);
        this.addressFieldOne.setText(stringOne);
        this.addressFieldTwo.setText(stringTwo);
    }

    private void joinServers() {
        if (this.selectServerButton.active) {
            this.serverEntryOne.address = this.addressFieldOne.getText();
            this.serverEntryTwo.address = this.addressFieldTwo.getText();
            this.callback.accept(true);
        }
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

    @Override
    public void removed() {
        this.client.keyboard.setRepeatEvents(false);
        this.client.options.write();
    }

    private void onAddressFieldChanged() {
        this.selectServerButton.active = ServerAddress.isValid(this.addressFieldOne.getText()) && ServerAddress.isValid(this.addressFieldTwo.getText());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        DirectConnectScreen.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        DirectConnectScreen.drawTextWithShadow(matrices, this.textRenderer, ENTER_IP_TEXT, this.width / 2 - 100, 100, 0xA0A0A0);
        this.addressFieldOne.render(matrices, mouseX, mouseY, delta);
        this.addressFieldTwo.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
