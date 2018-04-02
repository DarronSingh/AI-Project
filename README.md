# AI-Project
This repository contains files for the SOFE3720 final project.

#### Example scenario 1
<img src="https://i.imgur.com/VvFI2Tb.gif" width=640>

#### Example scenario 2
<img src="https://i.imgur.com/ZobhLvc.gif" width=640>

#### Example scenario 3
<img src="https://i.imgur.com/mqfhwDd.gif" width=640>

## TODO
- [X] Agents should follow predetermined path
- [X] Agent should stop if it rear ends another agent
- [X] Agent should reroute if on collision course
- [X] Agents should not overlap when moving to start locations
- [X] Create Broadcast class for private and public broadcasts
  - private should be directed to recipient, will contain coordinates for target
- [X] Before sending message, agents check how many targets recipient has already found
- [X] Agents should reroute based on which wall boundary they are farthest from
- [X] Come up with new default path for scenarios 2 and 3
- [X] CLI to choose scenario, speed and number of iterations on startup
- [X] Generate .csv file based on results of simulation
