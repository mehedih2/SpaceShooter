# Space Shooter 🚀

A simple 2D arcade-style space shooter game built in **Java** using **Swing** and **Object-Oriented Programming (OOP)** concepts.

## 🎮 Features

* Player spaceship movement (WASD / Arrow keys)
* Shooting system with cooldown
* Random enemy spawning
* Enemy shooting system
* Collision detection
* Explosion effects
* Score tracking
* Life system
* Game states (Menu, Playing, Game Over)
* Animated starfield background

---

## 🛠 Technologies Used

* Java
* Java Swing
* AWT Graphics
* OOP Principles

---

## 📚 OOP Concepts Used

### 1. Inheritance

Used to create common behavior for all game objects.

```java
Player extends GameObject
Enemy extends GameObject
Bullet extends GameObject
Explosion extends GameObject
```

---

### 2. Abstraction

`GameObject` is an abstract class that defines common methods.

```java
abstract void update();
abstract void draw(Graphics g);
```

---

### 3. Encapsulation

Private fields protect internal game data.

```java
private int score;
private int lives;
private boolean shooting;
```

---

### 4. Polymorphism

Different classes implement their own version of:

```java
update()
draw()
```

---

### 5. Interfaces

Used for threading and keyboard handling.

```java
implements Runnable
implements KeyListener
```

---

## 🎯 Controls

| Key   | Action          |
| ----- | --------------- |
| W / ↑ | Move Up         |
| S / ↓ | Move Down       |
| A / ← | Move Left       |
| D / → | Move Right      |
| Space | Shoot           |
| Enter | Start / Restart |

---

## 📂 Project Structure

```text
SpaceShooter/
│── Main.java
│── GamePanel.java
│── GameObject.java
│── Player.java
│── Enemy.java
│── Bullet.java
│── Explosion.java
│── StarField.java
│── spaceshooter.jar
```

---

## ▶ How to Run

### Run from source:

Compile:

```bash
javac *.java
```

Run:

```bash
java Main
```

---

### Run JAR file:

```bash
java -jar spaceshooter.jar
```

---


## 📷 Gameplay

Destroy enemies, avoid enemy bullets, survive as long as possible, and get the highest score.

---


