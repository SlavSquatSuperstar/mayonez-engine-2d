package mayonez.physics.resolution

import mayonez.math.*
import mayonez.physics.dynamics.*

// Property and function extensions for null receivers

internal val PhysicsBody?.mass: Float
    get() = this?.mass ?: 0f

internal val PhysicsBody?.invMass: Float
    get() = this?.invMass ?: 0f

internal val PhysicsBody?.invAngMass: Float
    get() = this?.invAngMass ?: 0f

internal val PhysicsBody?.static: Boolean
    get() = this?.static ?: true

internal val PhysicsBody?.position: Vec2
    get() = this?.position ?: Vec2()

internal val PhysicsBody?.material: PhysicsMaterial
    get() = this?.material ?: PhysicsMaterial.DEFAULT_MATERIAL

internal fun PhysicsBody?.getPointVelocity(contactPos: Vec2): Vec2 {
    return this?.getPointVelocity(contactPos) ?: Vec2()
}