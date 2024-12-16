package ru.lewis.cases.configuration.type

import org.bukkit.Bukkit
import org.bukkit.Location
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class LocationConfiguration(
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float,
    val world: String
) {

    fun toBukkitLocation(): Location {
        return Location(Bukkit.getWorld(world),x,y,z,yaw,pitch)
    }

}