package net.lunaire.features;

import net.lunaire.core.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();

    public static void init() {
        modules.clear();

        // 1. FastSwap (Настройка слота)
        Module fastSwap = new Module("FastSwap", Category.COMBAT, 0) {};
        fastSwap.addSetting(new Setting("TargetSlot", 1.0)); // Слот 1-9
        modules.add(fastSwap);

        // 3. FullBright (Стиль: Гамма или Зелье)
        modules.add(new Module("FullBright", Category.VISUAL, GLFW.GLFW_KEY_B) {
            @Override public void onTick() {
                // Эффект ночного зрения без частиц
                mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1000, 0, false, false));
            }
            @Override public void onDisable() { mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION); }
        });

        // 4. NoRender (Настройки всего)
        Module noRender = new Module("NoRender", Category.VISUAL, 0) {};
        noRender.addSetting(new Setting("NoFire", true));
        noRender.addSetting(new Setting("NoShake", true));
        noRender.addSetting(new Setting("NoCrystals", false));
        modules.add(noRender);

        // 5. FreeLook (Свободный обзор)
        modules.add(new Module("FreeLook", Category.MISC, GLFW.GLFW_KEY_LEFT_ALT) {});

        // 6. HitColor (Цвет удара)
        Module hitColor = new Module("HitColor", Category.COMBAT, 0) {};
        hitColor.addSetting(new Setting("Red", 255.0));
        hitColor.addSetting(new Setting("Blue", 0.0));
        modules.add(hitColor);

        // 7. Friends (Цвета)
        Module friends = new Module("Friends", Category.MISC, 0) {};
        friends.addSetting(new Setting("FriendColor", 0x00FF00));
        modules.add(friends);

        // Базовые (Зум и т.д.)
        modules.add(new Module("Zoom", Category.VISUAL, GLFW.GLFW_KEY_C) {});
        modules.add(new Module("Waypoints", Category.MISC, 0) {});
        modules.add(new Module("Macros", Category.MISC, 0) {});
    }

    public static List<Module> getModules() { return modules; }
    public static Module getModule(String name) {
        for (Module m : modules) if (m.name.equalsIgnoreCase(name)) return m;
        return null;
    }
}
