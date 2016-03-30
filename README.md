# AIMA4e-Java (JDK 8+) [![Build Status](https://travis-ci.org/aimacode/aima-java.svg?branch=AIMA4e)](https://travis-ci.org/aimacode/aima-java)
Java implementation of algorithms from Norvig And Russell's "Artificial Intelligence - A Modern Approach 4th Edition."

---
## NOTE: This is an in progress complete rewrite of the algorithms, leveraging JDK 8's new language features, from the AIMA3e branch (currently master branch). This will also become the new master branch once the 4th edition of "Artificial Intelligence - A Modern Approach" is published.

---
## Index of Implemented Algorithms
<table style="width:100%">
   <tbody>
   <tr>
       <td><b>Fig</b></td>
       <td><b>Page</b></td>
       <td><b>Name (in book)</b></td>
       <td><b>Code</b></td>
   </tr>
   <tr>
       <td>2.1</td>
       <td>35</td>
       <td>Agent</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/api/agent/Agent.java">Agent</a></td>
   </tr>
   <tr>
       <td>2.3</td>
       <td>36</td>
       <td>Table-Driven-Vacuum-Agent</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/environment/vacuum/agent/TableDrivenVacuumAgent.java">TableDrivenVacuumAgent</a></td>
   </tr>
   <tr>
       <td>2.7</td>
       <td>47</td>
       <td>Table-Driven-Agent</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/api/agent/TableDrivenAgent.java">TableDrivenAgent</a></td>
   </tr>
   <tr>
       <td>2.8</td>
       <td>48</td>
       <td>Reflex-Vacuum-Agent</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/environment/vacuum/agent/ReflexVacuumAgent.java">ReflexVacuumAgent</a></td>
   </tr>
   <tr>
       <td>2.10</td>
       <td>49</td>
       <td>Simple-Reflex-Agent</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/api/agent/SimpleReflexAgent.java">SimpleReflexAgent</a></td>
   </tr>
   <tr>
       <td>2.12</td>
       <td>51</td>
       <td>Model-Based-Reflex-Agent</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/api/agent/ModelBasedReflexAgent.java">ModelBasedReflexAgent</a></td>
   </tr>
   <tr>
       <td>3</td>
       <td>66</td>
       <td>Problem</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/api/search/Problem.java">Problem</a></td>
   </tr>
   <tr>
       <td>3.1</td>
       <td>67</td>
       <td>Simple-Problem-Solving-Agent</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/api/agent/SimpleProblemSolvingAgent.java">SimpleProblemSolvingAgent</a></td>
   </tr>
   <tr>
       <td>3.2</td>
       <td>68</td>
       <td>Romania</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/environment/map2d/SimplifiedRoadMapOfPartOfRomania.java">SimplifiedRoadMapOfPartOfRomania</a></td>
   </tr>
   <tr>
       <td>3.7</td>
       <td>77</td>
       <td>Tree-Search</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/api/search/GeneralTreeSearch.java">GeneralTreeSearch</a></td>
   </tr>
   <tr>
       <td>3.7</td>
       <td>77</td>
       <td>Graph-Search</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/api/search/GeneralGraphSearch.java">GeneralGraphSearch</a></td>
   </tr>
   <tr>
       <td>3.10</td>
       <td>79</td>
       <td>Node</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/api/search/Node.java">Node</a></td>
   </tr>
   <tr>
       <td>3.11</td>
       <td>82</td>
       <td>Breadth-First-Search</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/api/search/uninformed/graph/BreadthFirstGraphSearch.java">BreadthFirstGraphSearch</a></td>
   </tr>
   <tr>
       <td>3.14</td>
       <td>84</td>
       <td>Uniform-Cost-Search</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/api/search/uninformed/graph/UniformCostGraphSearch.java">UniformCostGraphSearch</a></td>
   </tr>
   <tr>
       <td>3</td>
       <td>85</td>
       <td>Depth-first Search</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/search/uninformed/graph/BasicDepthFirstGraphSearch.java">BasicDepthFirstGraphSearch</a></td>
   </tr>
   <tr>
       <td>3.17</td>
       <td>88</td>
       <td>Depth-Limited-Search</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/api/search/uninformed/tree/RecursiveDepthLimitedTreeSearch.java">RecursiveDepthLimitedTreeSearch</a></td>
   </tr>
   <tr>
       <td>3.18</td>
       <td>89</td>
       <td>Iterative-Deepening-Search</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/api/search/uninformed/tree/IterativeDeepeningSearch.java">IterativeDeepeningSearch</a></td>
   </tr>
   <tr>
       <td>3</td>
       <td>92</td>
       <td>Best-First search</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/api/search/informed/graph/BestFirstGraphSearch.java">BestFirstGraphSearch</a></td>
   </tr>
   <tr>
       <td>3</td>
       <td>92</td>
       <td>Greedy best-First search</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/api/search/informed/graph/GreedyBestFirstGraphSearch.java">GreedyBestFirstGraphSearch</a></td>
   </tr>
   <tr>
       <td>3</td>
       <td>93</td>
       <td>A* Search</td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/api/search/informed/graph/AStarGraphSearch.java">AStarGraphSearch</a></td>
   </tr>
   <tr>
       <td>3.26</td>
       <td>99</td>
       <td>Recursive-Best-First-Search </td>
       <td><a href="https://github.com/aimacode/aima-java/blob/AIMA4e/core/src/main/java/aima/core/api/search/informed/tree/RecursiveBestFirstTreeSearch.java">RecursiveBestFirstTreeSearch</a></td>
   </tr>
   </tbody>
</table>

---
## TODO (REMEMBER - KEEP IT SIMPLE SIMPLE SIMPLE!!!! :)
### CURRENT (new demo GUI)
* Re-design uninformed and informed search hierarchy:
    * SearchFunction
        * TreeSearch (sub-package)
            * GeneralTreeSearch (fig 3.7) x
                * BasicGeneralTreeSearch x
                    * BasicDepthFirstTreeSearch
                      (pg. 86 'modified at no extra memory cost so that it checks new states against those on the path from the root to the current node; to avoid infinite loops)
                * UniformCostTreeSearch  (ensure does not need to be at same level as GeneralTreeSearch)
                    * BasicUniformCostTreeSearch
            * BreadthFirstTreeSearch (variant of General tree search which checks if solution before adding to frontier)
                * BasicBreadthFirstTreeSearch   
            * recursive
                * RecursiveDepthLimitedTreeSearch (fig 3.17) x
                    * BasicRecursiveDepthLimitedTreeSearch x
                * IterariveDeepeningSearch x
                    * BasicIterativeDeepeningSearch x
                * RecursiveBestFirstTreeSearch (fig 3.26) x
                    * BasicRecursiveBestFirstTreeSearch   x
            * BestFirstTreeSearch (pg 92)
                * GreedyBestFirstTreeSearch
                    * BasicGreedyBestFirstTreeSearch
                * AStarTreeSearch
                    * BasicAStarTreeSearch                     
        * GraphSearch (Marker Interface or sub-package)
            * GeneralGraphSearch (fig 3.7) x
                * BasicGeneralGraphSearch x
                    * BasicDepthFirstGraphSearch x
            * BreadthFirstGraphSearch (fig 3.11) x
                * BasicBreadthFirstGraphSearch x
            * UniformCostGraphSearch (fig 3.14) x
                * BasicUniformCostGraphSearch x
            * BestFirstGraphSearch (pg 92 - implementation identical to fig 3.14 except for cost) x
                * GreedyBestFirstGraphSearch  x
                    * BasicGreedyBestFirstGraphSearch  x
                * AStartGraphSearch x
                    * BasicAStartGraphSearch x
* Tree-Search demo
    * Add additional tree search algorithms to simulate 
    * Add visualizations specific to recursive tree search algorithms
    * Configure rectangular problem
        * Change size of grid
        * Specify order of actions.
            * Clockwise
            * Anti-Clockwise
            * Random
                * Check box for 'each time' new random sequence.
            * User Selected (via groups of toggle buttons).
    * Add additional problems
        * Binary search tree (i.e. fig 3.12 and also take into account fig 3.16)
            * Possibly show before Rectangular problem in demo
        * 2D Map (i.e. Map of Romania)
            * A* display contours (fig 3.25)
        * Informed search
            * Display heuristic information summary pane (fig 3.22)
* Graph-Search demo
    * Summary information related to explored set.
* GUI demo
    * Mark each algorithm (search) with icons indicating complexity, optimality, time and space complexity (chp 3 pg 80 and fig 3.21).

### LATER

#### Chapter 3 'core' module.
* Uniform-Cost-Search need a better mechanism for determining s containment in the
  queue and remove a node with a higher s cost.
* Recursive-Best-First-Search - look to improve/tidy up implementation.
* BasicRecursiveBestFirstSearchTest
* 3 	90	Bidirectional search
* BasicBestFirstGraphSearchTest
* BasicGreedyestFirstGraphSearchTest
* BasicAStarGraphSearchTest
* BasicIterativeDeepeningSearchTest
* BasicDepthLimitedSearchTest
* BasicDepthFirstSearchTest
* BasicUniformCostSearchTest
* BreadthFirstSearchTest - additional tests.
* BasicGraphSearchTest
* BasicTreeSearchTest
* BasicProblemTest
* BasicSimpleProblemSolvingAgentTest

#### Chapter 2 'core' module.
* BasicModelBasedReflexAgentTest
* BasicTableDrivenAgentTest

#### Chapter 2 'extra' module.
* Environment defintion: Consider specifying Dimensions in API, see pg. 42.
* Environment Simulator referenced on pg. 45 (this will be a refactor of the a lot of the environment stuff
  in aima3e-core).

---

## USEFUL RESOURCES
### GUI
* http://docs.oracle.com/javase/8/javase-clienttechnologies.htm
* https://bitbucket.org/controlsfx/controlsfx/overview
* http://fxexperience.com/
* https://bitbucket.org/Jerady/fontawesomefx
* http://fortawesome.github.io/Font-Awesome/get-started/
* http://www.pythontutor.com/
* https://svgsalamander.java.net/docs/use.html
* http://blog.netopyr.com/2012/03/09/creating-a-sprite-animation-with-javafx/
* http://www.mrlonee.com/?p=319
* http://harmoniccode.blogspot.com/
* https://rterp.wordpress.com/2014/07/28/adding-custom-javafx-component-to-scene-builder-2-0-part-2/


