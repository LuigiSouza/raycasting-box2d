# raycasting_box2d
Some tests with libgdx/box2d to implement raycasting game

## Introduction
Thats my little tests about get a raycasting game like doom, with 2d fisics but with 3d graphics. Got some issues about the rays casted in World.raycast() returning some further fxture and creating some visual bugs
## Considerations
Code already print walls based on rays distance between a wall and the player, but as I said, something is taking a wrong distance and taking a further distance. Not a clean code (sem tempo irmão)

- Image example, should create a solid wall, but print these interspersed lines (yellow represents one direction and red the other to create depth)
![Image](https://github.com/LuigiSouza/raycasting_box2d/blob/master/raycastTest.png?raw=true)
