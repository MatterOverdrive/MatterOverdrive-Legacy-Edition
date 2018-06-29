![alt tag](https://raw.githubusercontent.com/MatterOverdrive/MatterOverdrive/1.12.2/.github/MatterOverdriveLogo.png)

[![Jenkins](https://img.shields.io/jenkins/s/http/jenkins.k-4u.nl/job/MatterOverdrive/.svg?style=for-the-badge)](http://jenkins.k-4u.nl/job/MatterOverdrive/)
[![GitHub issues](https://img.shields.io/github/issues-raw/MatterOverdrive/MatterOverdrive.svg?style=for-the-badge)](https://github.com/MatterOverdrive/MatterOverdrive/issues)
[![](https://img.shields.io/github/issues-pr-raw/MatterOverdrive/MatterOverdrive.svg?style=for-the-badge)](https://github.com/MatterOverdrive/MatterOverdrive/pulls)
[![license](https://img.shields.io/github/license/matteroverdrive/matteroverdrive.svg?style=for-the-badge)](https://github.com/MatterOverdrive/MatterOverdrive/blob/1.12.2/LICENSE.md)
[![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/MatterOverdrive/MatterOverdrive.svg?style=for-the-badge)](https://github.com/MatterOverdrive/MatterOverdrive)
[![GitHub contributors](https://img.shields.io/github/contributors/MatterOverdrive/MatterOverdrive.svg?style=for-the-badge)](https://github.com/MatterOverdrive/MatterOverdrive/graphs/contributors)
[![Discord](https://img.shields.io/discord/364844705864744961.svg?style=for-the-badge)](https://discord.gg/erEyCsW)

##### Current Developer: TheCodedOne

## Table of Contents
* [About](#about)
* [Features](#features)
* [Contacts](#contacts)
* [Issues](#issues)
* [Building](#building)

## About
Matter Overdrive is a Minecraft mod inspired by the popular Sci-fi TV series Star Trek. It dwells in the concept of replicating and transforming one type matter into another.
Although it may seem overpowered, Matter Overdrive takes a more realistic approach and requires the player to build a complex system before even the simplest replication can be achieved.

## Contacts
* [Email](mailto:contact@hrznstudio.com)
* [Website](https://hrnz.studio/mo)
* [Discord](https://discord.gg/PC5GXyQ)

## Features
* [Matter Scanner](https://mo.simeonradivoev.com/items/matter_scanner/), for scanning matter patterns for replication.
* [Replicator](https://mo.simeonradivoev.com/items/replicator/), for transforming materials.
* [Decomposer](https://mo.simeonradivoev.com/items/decomposer/), for breaking down materials to basic form.
* [Transporter](https://mo.simeonradivoev.com/items/transporter/), for beaming up.
* [Phaser](https://mo.simeonradivoev.com/items/phaser/), to set on stun.
* [Fusion Reactors](https://mo.simeonradivoev.com/fusion-reactor/) and [Gravitational Anomaly](https://mo.simeonradivoev.com/items/gravitational_anomaly/)
* Complex Networking for replication control.
* Star Maps, with Galaxies, Stars and Planets
* [Androids](https://mo.simeonradivoev.com/android-guide/), become an Android and learn powerful RPG like abilities, such as Teleportation and Forefield Shields.


![Matter Overdrive Blocks and Items](https://media-elerium.cursecdn.com/attachments/210/237/main_screenshot.png)

## Issues
If you have any crashes, problems or suggestions just open a [new Issue](https://github.com/MatterOverdrive/MatterOverdrive/issues/new).
If your crash or problem was fixed, but is not yet released as a public download you can always download the latest [Dev Build](http://jenkins.k-4u.nl/job/MatterOverdrive/).

## Building
1. Clone this repository via 
  - SSH `git clone git@github.com:MatterOverdrive/MatterOverdrive.git` or 
  - HTTPS `git clone https://github.com/MatterOverdrive/MatterOverdrive.git`
2. Setup workspace (This currently does not work due to a ping timeout on rx14's mvn server.)
  - Decompiled source `gradlew setupDecompWorkspace`
  - Obfuscated source `gradlew setupDevWorkspace`
  - CI server `gradlew setupCIWorkspace`
3. Build `gradlew build`. Jar will be in `build/libs`
4. For core developer: Setup IDE
  - IntelliJ: Import into IDE and execute `gradlew genIntellijRuns` afterwards
  - Eclipse: execute `gradlew eclipse`
  - Don't forget to install [Lombok](https://projectlombok.org)
