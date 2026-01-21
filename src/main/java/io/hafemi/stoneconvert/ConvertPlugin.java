package io.hafemi.stoneconvert;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class ConvertPlugin extends JavaPlugin {
    public ConvertPlugin(@NonNullDecl JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        super.setup();
        this.getCommandRegistry().registerCommand(new ConvertCommand(
                "convert",
                "Convert Cobblestone to Stone and vice versa",
                false
        ));
    }
}
