package nourl.mythicmetals.misc;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class RecievedCombustionFromCreeper extends AbstractCriterion<RecievedCombustionFromCreeper.Conditions> {
    public static final Identifier ID = RegistryHelper.id("recieved_combustion_from_creeper");

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new Conditions();
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity entity) {
        this.trigger(entity, conditions -> true);
    }

    public static class Conditions extends AbstractCriterionConditions {
        public Conditions() {
            super(ID, EntityPredicate.Extended.EMPTY);
        }
    }
}
