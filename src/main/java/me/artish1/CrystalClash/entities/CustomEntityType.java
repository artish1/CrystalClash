package me.artish1.CrystalClash.entities;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import net.minecraft.server.v1_15_R1.EntityTypes;
public enum CustomEntityType {

//    SHEEP_GUNNER("Sheep", 91, EntityType.SHEEP, EntitySheep.class, EntityGunSheep.class),
    SHEEP_GUNNER( new CustomEntityTypes<EntityGunSheep>("gunsheep", EntityGunSheep.class,
        EntityTypes.SHEEP, EntityGunSheep::new) ),

    MODEL_ZOMBIE( new CustomEntityTypes<ModelZombie>("modelzombie", ModelZombie.class, EntityTypes.ZOMBIE,
            ModelZombie::new)),
    VILLAGER_MERCHANT(new CustomEntityTypes<EntityVillagerMerchant>("merchantvillager", EntityVillagerMerchant.class,
                    EntityTypes.VILLAGER, EntityVillagerMerchant::new)),
    SUMMONED_ZOMBIE(new CustomEntityTypes<SummonedZombie>("summonedzombie", SummonedZombie.class, EntityTypes.ZOMBIE,
                    SummonedZombie::new)),
    MODEL_SKELETON(new CustomEntityTypes<ModelSkeleton>("modelskeleton", ModelSkeleton.class, EntityTypes.SKELETON,
                    ModelSkeleton::new)),
    SUMMONED_SKELETON(
            new CustomEntityTypes<SummonedSkeleton>("summonedskeleton", SummonedSkeleton.class, EntityTypes.SKELETON,
                    SummonedSkeleton::new)
    ),
    MODEL_SHEEP( new CustomEntityTypes<ModelSheep>("modelsheep", ModelSheep.class, EntityTypes.SHEEP, ModelSheep::new)),
    ENTITY_DEATH(new CustomEntityTypes<EntityDeath>("deathenderman", EntityDeath.class, EntityTypes.ENDERMAN,
            EntityDeath::new));

    private CustomEntityTypes customType;

    private CustomEntityType( CustomEntityTypes<?> customType) {
        this.customType = customType;
    }

    public Entity spawn(Location loc) {
        return getCustomType().spawn(loc);
    }


    public CustomEntityTypes<? > getCustomType() {
        return customType;
    }

    /**
     * Register our entities.
     */
    public static void registerEntities() {
        for(CustomEntityType customEntity : values()){
            customEntity.getCustomType().register();
        }
    }

    /**
     * Unregister our entities to prevent memory leaks. Call on disable.
     */
    public static void unregisterEntities() {
        for(CustomEntityType customEntity : values()){
            customEntity.getCustomType().unregister();
        }
    }

}
