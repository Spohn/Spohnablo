Jonathan Spohn - Spohnablo - Final AI Project

******READ ME FILE********

Run FinalProject.java to execute program

The objective of this game is to navigate to the purple exit. You should fight as many Mobs or Monsters as you
can. This will increase you chances of survival as with each level the Mobs will become smarter and more aggresive
via the classifier.

Movement is handled from the wasd keys or the arrow keys. You will notice that you can move directly into walls
from two sides, but not from the other two. This is because the Hero and walls are drawn with width and length
but are still using the top left corner of there image for collision detection

You can toggle the Fog of War so you may see all the enemies and entire dungeon from the options menu. Same with
the Traps.

**********Possible Bugs*************

There are two known issues, neither of them serious, or AI related.

The first issue is when loading a new level: the Mobs may sometimes seem to teleport. This is because they are in
process of finding a new position via A* as the level loads. So after the level loads and they are given the
new position from that map the A* finishes executing and relocated them to where they would of moved from the 
previous map.

The second issue I believe is fixed but it hard to tell because of random placement. The exit may sometimes spawn
at the same spot as a wall where you may not move into. You will have to restart the game if this occurs. I think
this was fixed but if not I apologize for the inconvience.