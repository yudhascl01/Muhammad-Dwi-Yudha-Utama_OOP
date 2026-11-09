
package com.MuhammadDwiYudhaUtama.frontend.objects.patterns.bulletStrategy;

import com.MuhammadDwiYudhaUtama.frontend.objects.GameObject;
import com.MuhammadDwiYudhaUtama.frontend.objects.bullets.Bullet;
import com.MuhammadDwiYudhaUtama.frontend.objects.enemies.Enemy;

import java.util.List;

public class HomingBulletMovement
    implements BulletMovementPattern {

    private GameObject target;
    private List<GameObject> entities;

    private float turnSpeed;
    private float maxHomingDuration = 0.8f;

    public HomingBulletMovement(
        GameObject target,
        float turnSpeed
    ) {
        this(target, null, turnSpeed, 0.8f);
    }

    public HomingBulletMovement(
        GameObject target,
        List<GameObject> entities,
        float turnSpeed
    ) {
        this(target, entities, turnSpeed, 0.8f);
    }

    public HomingBulletMovement(
        GameObject target,
        List<GameObject> entities,
        float turnSpeed,
        float maxHomingDuration
    ) {
        this.target = target;
        this.entities = entities;
        this.turnSpeed = turnSpeed;
        this.maxHomingDuration = maxHomingDuration;
    }

    public HomingBulletMovement(
        List<GameObject> entities,
        float turnSpeed
    ) {
        this(null, entities, turnSpeed, 0.8f);
    }

    public HomingBulletMovement(float turnSpeed) {
        this(null, null, turnSpeed, 0.8f);
    }

    public HomingBulletMovement() {
        this(null, null, 360f, 0.8f);
    }

    @Override
    public void move(Bullet bullet, float delta) {
        if (bullet == null || bullet.isDestroyed()) {
            return;
        }

        GameObject activeTarget = target;

        if (!isValidTarget(activeTarget)) {
            activeTarget = findNearestEnemy(bullet);
        }

        boolean isHomingActive =
            bullet.getStateTime() <= maxHomingDuration;

        if (isHomingActive && isValidTarget(activeTarget)) {

            float targetAngle = (float) Math.toDegrees(
                Math.atan2(
                    activeTarget.getY() - bullet.getY(),
                    activeTarget.getX() - bullet.getX()
                )
            );

            float diff =
                (targetAngle - bullet.getAngle()) % 360f;

            if (diff > 180f) {
                diff -= 360f;
            }

            if (diff < -180f) {
                diff += 360f;
            }

            float rotation = Math.signum(diff)
                * Math.min(
                Math.abs(diff),
                turnSpeed * delta
            );

            bullet.setAngle(
                bullet.getAngle() + rotation
            );

        } else if (!isHomingActive) {

            float newSpeed = Math.min(
                600f,
                bullet.getSpeed() + 250f * delta
            );

            bullet.setSpeed(newSpeed);
        }

        float rad = (float) Math.toRadians(
            bullet.getAngle()
        );

        float dx = (float) Math.cos(rad)
            * bullet.getSpeed()
            * delta;

        float dy = (float) Math.sin(rad)
            * bullet.getSpeed()
            * delta;

        bullet.setX(bullet.getX() + dx);
        bullet.setY(bullet.getY() + dy);
    }

    private boolean isValidTarget(GameObject object) {
        if (object == null || object.isDestroyed()) {
            return false;
        }

        if (object instanceof Enemy enemy) {
            return enemy.isAlive();
        }

        return true;
    }

    private GameObject findNearestEnemy(Bullet bullet) {
        if (entities == null) {
            return null;
        }

        GameObject nearest = null;
        float minDistanceSq = Float.MAX_VALUE;

        for (GameObject object : entities) {
            if (
                !(object instanceof Enemy enemy) ||
                    !enemy.isAlive()
            ) {
                continue;
            }

            float dx = enemy.getX() - bullet.getX();
            float dy = enemy.getY() - bullet.getY();

            float distanceSq = dx * dx + dy * dy;

            if (distanceSq < minDistanceSq) {
                minDistanceSq = distanceSq;
                nearest = enemy;
            }
        }

        return nearest;
    }

    public GameObject getTarget() {
        return target;
    }

    public void setTarget(GameObject target) {
        this.target = target;
    }

    public List<GameObject> getEntities() {
        return entities;
    }

    public void setEntities(List<GameObject> entities) {
        this.entities = entities;
    }

    public float getTurnSpeed() {
        return turnSpeed;
    }

    public void setTurnSpeed(float turnSpeed) {
        this.turnSpeed = turnSpeed;
    }

    public float getMaxHomingDuration() {
        return maxHomingDuration;
    }

    public void setMaxHomingDuration(
        float maxHomingDuration
    ) {
        this.maxHomingDuration = maxHomingDuration;
    }
}
