package dev.kalkafox.wolfutils.advancements.critereon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class WolfUtilsTrigger extends SimpleCriterionTrigger<WolfUtilsTrigger.WolfUtilsTriggerInstance> {


    @Override
    public @NotNull Codec<WolfUtilsTriggerInstance> codec() {
        return WolfUtilsTriggerInstance.CODEC;
    }

    public record WolfUtilsTriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<WolfUtilsTriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(WolfUtilsTriggerInstance::player)
        ).apply(instance, WolfUtilsTriggerInstance::new));

        @Override
        public @NotNull Optional<ContextAwarePredicate> player() {
            return Optional.empty();
        }
    }

}
