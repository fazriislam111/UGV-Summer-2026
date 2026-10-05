# Travelling Salesman Problem (TSP) using Genetic Algorithm (GA) and Ant Colony Optimization (ACO)

This file explains the code in the Python project line by line in a beginner-friendly way.

---

## 1. The file header and imports

### Line 1–10: Docstring
```python
"""
Travelling Salesman Problem (TSP) using Genetic Algorithm (GA) and Ant Colony Optimization (ACO)

This is a beginner-friendly implementation for undergraduate students.
It solves a small TSP instance using two metaheuristic methods:
1. Genetic Algorithm (selection, crossover, mutation)
2. Ant Colony Optimization (pheromone update)

The program compares both methods and prints the best route and its distance.
"""
```

- This is a multi-line comment block.
- It explains the purpose of the program.
- It helps other students and teachers understand the project quickly.

### Line 12: `import random`
```python
import random
```

- This imports Python’s `random` library.
- We need it to make random choices in the algorithms.
- It is used to shuffle routes, select random cities, and mutate routes.

### Line 13: `import math`
```python
import math
```

- This imports Python’s `math` library.
- We use it to calculate distances using the square-root formula.
- The `math` module gives us tools like `sqrt()`.

---

## 2. Dataset (small example)

### Line 18: `CITY_NAMES = ["A", "B", "C", "D", "E", "F", "G"]`
```python
CITY_NAMES = ["A", "B", "C", "D", "E", "F", "G"]
```

- This creates a list of city names.
- A list is a collection of values stored in order.
- These are the cities the salesman must visit.

### Line 19: `CITY_COORDINATES = {`
```python
CITY_COORDINATES = {
```

- This starts a dictionary.
- A dictionary stores data as key-value pairs.
- Here, each city name is a key, and its coordinates are the value.

### Lines 20–26: Coordinate values
```python
    "A": (0, 0),
    "B": (1, 3),
    "C": (3, 4),
    "D": (5, 2),
    "E": (6, 5),
    "F": (4, 7),
    "G": (2, 6),
```

- These lines store the position of each city on a 2D plane.
- For example, city A is at $(0,0)$ and city B is at $(1,3)$.
- These coordinates are used to calculate the distance between cities.

### Line 27: `}`
```python
}
```

- This closes the dictionary.

---

## 3. Helper functions

### Line 33: `def calculate_distance(city1, city2):`
```python
def calculate_distance(city1, city2):
```

- This defines a function named `calculate_distance`.
- A function is a reusable block of code.
- It takes two inputs: `city1` and `city2`.
- Its job is to calculate the distance between those two cities.

### Line 34: Docstring for the function
```python
    """Return Euclidean distance between two cities."""
```

- This explains what the function does.
- It is useful for readers and future developers.

### Line 35: `x1, y1 = CITY_COORDINATES[city1]`
```python
    x1, y1 = CITY_COORDINATES[city1]
```

- This gets the coordinates of the first city.
- The dictionary `CITY_COORDINATES` is used to look up the city’s pair of values.
- Example input: `city1 = "A"`
- Output: `x1 = 0`, `y1 = 0`

### Line 36: `x2, y2 = CITY_COORDINATES[city2]`
```python
    x2, y2 = CITY_COORDINATES[city2]
```

- This gets the coordinates of the second city.
- Example input: `city2 = "B"`
- Output: `x2 = 1`, `y2 = 3`

### Line 37: Distance calculation
```python
    return round(math.sqrt((x2 - x1) ** 2 + (y2 - y1) ** 2), 2)
```

- This line calculates the straight-line distance between two points.
- It uses the Euclidean distance formula:
  $\sqrt{(x_2-x_1)^2 + (y_2-y_1)^2}$
- `math.sqrt()` finds the square root.
- `** 2` squares the difference.
- `round(..., 2)` keeps the number to 2 decimal places.
- This makes the output cleaner and easier to read.

### Line 40: `def build_distance_matrix(cities):`
```python
def build_distance_matrix(cities):
```

- This defines a function that creates a full table of distances between all cities.
- The input `cities` is the list of city names.

### Line 41: Docstring
```python
    """Create a full distance matrix for all cities."""
```

- This explains the purpose of the function.

### Line 42: `matrix = {}`
```python
    matrix = {}
```

- This creates an empty dictionary to store the distance data.
- It will act like a table of distances.

### Line 43: `for city in cities:`
```python
    for city in cities:
```

- This loop goes through each city in the list.
- It helps us create one row for each city.

### Line 44: `matrix[city] = {}`
```python
        matrix[city] = {}
```

- This creates a new inner dictionary for the current city.
- Example: `matrix["A"] = {}`

### Line 45: `for other_city in cities:`
```python
        for other_city in cities:
```

- This loops through every city again.
- Now we compare the current city with all the others.

### Line 46: `if city == other_city:`
```python
            if city == other_city:
```

- This checks whether the two cities are the same.
- If they are the same, the distance must be 0.

### Line 47: `matrix[city][other_city] = 0.0`
```python
                matrix[city][other_city] = 0.0
```

- This stores the distance of a city to itself as `0.0`.
- This is necessary because a city has zero distance to itself.

### Line 48: `else:`
```python
            else:
```

- This runs when the two cities are different.

### Line 49: `matrix[city][other_city] = calculate_distance(city, other_city)`
```python
                matrix[city][other_city] = calculate_distance(city, other_city)
```

- This uses the `calculate_distance()` function to compute the real distance between the two cities.
- The result is stored inside the matrix.

### Line 50: `return matrix`
```python
    return matrix
```

- This returns the completed distance table to the caller.

### Line 53: `def route_distance(route, distance_matrix):`
```python
def route_distance(route, distance_matrix):
```

- This defines a function that calculates the total distance of a route.
- `route` is the order of cities to visit.
- `distance_matrix` stores the distance between every pair of cities.

### Line 54: Docstring
```python
    """Compute total tour length for a route."""
```

- This explains that the function measures the total length of the travel route.

### Line 55: `total = 0.0`
```python
    total = 0.0
```

- This creates a variable named `total` and sets it to `0.0`.
- It will be used to add up all the distances.

### Line 56: `for i in range(len(route)):`
```python
    for i in range(len(route)):
```

- This loop runs once for each city in the route.
- `len(route)` gives the number of cities in the route.

### Line 57: `current = route[i]`
```python
        current = route[i]
```

- This takes the current city from the route list.

### Line 58: `nxt = route[(i + 1) % len(route)]`
```python
        nxt = route[(i + 1) % len(route)]
```

- This gets the next city in the route.
- The `% len(route)` part makes the route circular, so after the last city it goes back to the first city.
- This is necessary because the salesman must return to the starting city.

### Line 59: `total += distance_matrix[current][nxt]`
```python
        total += distance_matrix[current][nxt]
```

- This adds the distance from the current city to the next city.
- `distance_matrix[current][nxt]` looks up the distance from one city to the next.

### Line 60: `return round(total, 2)`
```python
    return round(total, 2)
```

- This returns the full route distance rounded to 2 decimal places.

### Line 63: `def print_route(route):`
```python
def print_route(route):
```

- This defines a function that turns a route list into a readable string.

### Line 64: Docstring
```python
    """Display a route in a readable way."""
```

- This explains the purpose of the function.

### Line 65: `return " -> ".join(route + [route[0]])`
```python
    return " -> ".join(route + [route[0]])
```

- This converts the route list into a string such as `A -> B -> C -> A`.
- `route + [route[0]]` adds the starting city at the end so the loop is complete.
- `" -> ".join(...)` joins the cities with arrows.

---

## 4. Genetic Algorithm (GA)

### Line 71: `def create_initial_population(population_size, cities):`
```python
def create_initial_population(population_size, cities):
```

- This defines a function that creates the first group of random routes.
- In GA, this group is called the initial population.

### Line 72: Docstring
```python
    """Create random valid tours for the initial population."""
```

- This explains why the function exists.

### Line 73: `population = []`
```python
    population = []
```

- This creates an empty list to store the routes.

### Line 74: `for _ in range(population_size):`
```python
    for _ in range(population_size):
```

- This loop repeats `population_size` times.
- Each repetition creates one route.

### Line 75: `route = cities.copy()`
```python
        route = cities.copy()
```

- This makes a copy of the city list.
- The `copy()` function prevents changing the original list.

### Line 76: `random.shuffle(route)`
```python
        random.shuffle(route)
```

- This randomly rearranges the order of cities.
- This creates a random route.

### Line 77: `population.append(route)`
```python
        population.append(route)
```

- This adds the new random route into the population list.

### Line 78: `return population`
```python
    return population
```

- This returns the full population of random routes.

### Line 81: `def tournament_selection(population, costs):`
```python
def tournament_selection(population, costs):
```

- This defines a function for choosing a parent route.
- In GA, parents are selected to create new routes.

### Line 82: Docstring
```python
    """Choose one parent using tournament selection."""
```

- This explains the selection method.

### Line 83: `index1 = random.randint(0, len(population) - 1)`
```python
    index1 = random.randint(0, len(population) - 1)
```

- This picks a random index from the population list.
- `random.randint()` generates a random integer inside the given range.
- The range starts at `0` and ends at the last index of the list.

### Line 84: `index2 = random.randint(0, len(population) - 1)`
```python
    index2 = random.randint(0, len(population) - 1)
```

- This picks a second random index.
- It is used to compare two possible parent routes.

### Line 86: `if costs[index1] <= costs[index2]:`
```python
    if costs[index1] <= costs[index2]:
```

- This compares the costs of the two candidate routes.
- Lower cost means a shorter route, which is better.
- The symbol `<=` means “less than or equal to”.

### Line 87: `return population[index1]`
```python
        return population[index1]
```

- If the first candidate is better or equal, return that route.

### Line 88: `return population[index2]`
```python
    return population[index2]
```

- Otherwise return the second route.

### Line 91: `def order_crossover(parent1, parent2):`
```python
def order_crossover(parent1, parent2):
```

- This function creates a new child route from two parent routes.
- This is one of the main steps of crossover in GA.

### Line 92: Docstring
```python
    """Create a child using Order Crossover (OX)."""
```

- Explains the method used.

### Line 93: `size = len(parent1)`
```python
    size = len(parent1)
```

- Stores the number of cities in the parent route.

### Line 94: `start = random.randint(0, size - 1)`
```python
    start = random.randint(0, size - 1)
```

- Picks a random starting index for the crossover section.

### Line 95: `end = random.randint(start, size - 1)`
```python
    end = random.randint(start, size - 1)
```

- Picks a random ending index after the start.

### Line 97: `child = [None] * size`
```python
    child = [None] * size
```

- Creates a list for the child route.
- `None` means the positions are empty at first.

### Line 98: `child[start:end + 1] = parent1[start:end + 1]`
```python
    child[start:end + 1] = parent1[start:end + 1]
```

- Copies a section from the first parent into the child.
- This keeps some part of the parent route.

### Line 100: `remaining = [city for city in parent2 if city not in child]`
```python
    remaining = [city for city in parent2 if city not in child]
```

- This collects the cities from the second parent that are not already in the child.
- This helps fill the missing positions.

### Line 101: `index = 0`
```python
    index = 0
```

- Starts from the beginning of the `remaining` list.

### Line 102: `for i in range(size):`
```python
    for i in range(size):
```

- Loops through each position in the child route.

### Line 103: `if child[i] is None:`
```python
        if child[i] is None:
```

- Checks whether the child position is still empty.

### Line 104: `child[i] = remaining[index]`
```python
            child[i] = remaining[index]
```

- Fills the empty position with the next city from `remaining`.

### Line 105: `index += 1`
```python
            index += 1
```

- Moves to the next city in the remaining list.

### Line 107: `return child`
```python
    return child
```

- Returns the newly created child route.

### Line 110: `def mutate_route(route, mutation_rate):`
```python
def mutate_route(route, mutation_rate):
```

- Defines mutation in the GA.
- Mutation changes the route slightly to introduce diversity.

### Line 111: Docstring
```python
    """Apply mutation by swapping two cities."""
```

- Explains that mutation is done by swapping cities.

### Line 112: `if random.random() < mutation_rate:`
```python
    if random.random() < mutation_rate:
```

- Decides whether mutation should happen.
- `random.random()` gives a number between `0` and `1`.
- If it is smaller than the mutation rate, mutation occurs.

### Line 113: `i = random.randint(0, len(route) - 1)`
```python
        i = random.randint(0, len(route) - 1)
```

- Picks a random index in the route.

### Line 114: `j = random.randint(0, len(route) - 1)`
```python
        j = random.randint(0, len(route) - 1)
```

- Picks another random index.

### Line 115: `route[i], route[j] = route[j], route[i]`
```python
        route[i], route[j] = route[j], route[i]
```

- Swaps the two cities in the route.
- This creates a small mutation.

### Line 116: `return route`
```python
    return route
```

- Returns the mutated route.

### Line 119: `def run_genetic_algorithm(distance_matrix, population_size=40, generations=80, mutation_rate=0.08):`
```python
def run_genetic_algorithm(distance_matrix, population_size=40, generations=80, mutation_rate=0.08):
```

- This is the main function for the Genetic Algorithm.
- It accepts the distance table and some settings.
- `population_size=40` means 40 routes are created each generation.
- `generations=80` means the algorithm repeats 80 times.
- `mutation_rate=0.08` means mutation happens with 8% chance.

### Line 120: Docstring
```python
    """Run the Genetic Algorithm and return the best route and history."""
```

- Explains what the function returns.

### Line 121: `cities = list(CITY_NAMES)`
```python
    cities = list(CITY_NAMES)
```

- Converts the city names into a list.
- This gives the algorithm a list it can shuffle and work with.

### Line 122: `population = create_initial_population(population_size, cities)`
```python
    population = create_initial_population(population_size, cities)
```

- Calls the function that creates the initial random population.

### Line 124: `best_route = None`
```python
    best_route = None
```

- Sets the best route to nothing at the beginning.

### Line 125: `best_cost = float("inf")`
```python
    best_cost = float("inf")
```

- Sets the best cost to infinity.
- This ensures the first route will become the initial best route.

### Line 126: `history = []`
```python
    history = []
```

- Creates an empty list to store the best cost after each generation.

### Line 128: `for _ in range(generations):`
```python
    for _ in range(generations):
```

- Repeats the GA process for the given number of generations.

### Line 129: `costs = [route_distance(route, distance_matrix) for route in population]`
```python
        costs = [route_distance(route, distance_matrix) for route in population]
```

- Calculates the cost of every route in the population.
- This uses the `route_distance()` function.

### Line 131: `for route, cost in zip(population, costs):`
```python
        for route, cost in zip(population, costs):
```

- Loops through each route and its cost together.
- `zip()` pairs the two lists item by item.

### Line 132: `if cost < best_cost:`
```python
            if cost < best_cost:
```

- Checks whether the current route is better than the best one found so far.

### Line 133: `best_cost = cost`
```python
                best_cost = cost
```

- Saves the new best cost.

### Line 134: `best_route = route.copy()`
```python
                best_route = route.copy()
```

- Saves a copy of the best route.

### Line 136: `history.append(best_cost)`
```python
        history.append(best_cost)
```

- Stores the best cost in the history list.

### Line 138: `new_population = []`
```python
        new_population = []
```

- Creates an empty list for the next generation.

### Line 141: `new_population.append(best_route.copy())`
```python
        new_population.append(best_route.copy())
```

- Keeps the current best route in the next generation.

### Line 143: `while len(new_population) < population_size:`
```python
        while len(new_population) < population_size:
```

- Keeps creating children until the next generation is full.

### Line 144: `parent1 = tournament_selection(population, costs)`
```python
            parent1 = tournament_selection(population, costs)
```

- Chooses the first parent route using tournament selection.

### Line 145: `parent2 = tournament_selection(population, costs)`
```python
            parent2 = tournament_selection(population, costs)
```

- Chooses the second parent route.

### Line 147: `child1 = order_crossover(parent1, parent2)`
```python
            child1 = order_crossover(parent1, parent2)
```

- Creates the first child by mixing the two parents.

### Line 148: `child2 = order_crossover(parent2, parent1)`
```python
            child2 = order_crossover(parent2, parent1)
```

- Creates the second child from the parents in reverse order.

### Line 150: `child1 = mutate_route(child1, mutation_rate)`
```python
            child1 = mutate_route(child1, mutation_rate)
```

- Applies mutation to the first child.

### Line 151: `child2 = mutate_route(child2, mutation_rate)`
```python
            child2 = mutate_route(child2, mutation_rate)
```

- Applies mutation to the second child.

### Line 153: `new_population.append(child1)`
```python
            new_population.append(child1)
```

- Adds the first child to the new generation.

### Line 154: `if len(new_population) < population_size:`
```python
            if len(new_population) < population_size:
```

- Checks whether there is still space in the new population.

### Line 155: `new_population.append(child2)`
```python
                new_population.append(child2)
```

- Adds the second child if space is available.

### Line 157: `population = new_population`
```python
        population = new_population
```

- Replaces the old population with the newly built one.

### Line 159: `return best_route, best_cost, history`
```python
    return best_route, best_cost, history
```

- Returns the best route, its cost, and the history of improvement.

---

## 5. Ant Colony Optimization (ACO)

### Line 165: `def run_ant_colony_optimization(distance_matrix, ants=30, iterations=60, alpha=1.0, beta=3.0, rho=0.1, q=100.0):`
```python
def run_ant_colony_optimization(distance_matrix, ants=30, iterations=60, alpha=1.0, beta=3.0, rho=0.1, q=100.0):
```

- This defines the main function for Ant Colony Optimization.
- It takes the distance matrix and some parameters.
- `ants=30` means 30 ants build routes.
- `iterations=60` means the process repeats 60 times.
- `alpha`, `beta`, `rho`, and `q` control how ants choose paths and how pheromones change.

### Line 166: Docstring
```python
    """Run Ant Colony Optimization and return the best route and history."""
```

- Explains the purpose of the function.

### Line 167: `cities = list(CITY_NAMES)`
```python
    cities = list(CITY_NAMES)
```

- Creates a list of cities so the algorithm can process them.

### Line 168: `n = len(cities)`
```python
    n = len(cities)
```

- Stores the number of cities.

### Line 171: `pheromone = [[1.0 for _ in range(n)] for _ in range(n)]`
```python
    pheromone = [[1.0 for _ in range(n)] for _ in range(n)]
```

- Creates a 2D list called a pheromone matrix.
- It stores pheromone levels between city pairs.
- Each value starts at `1.0`.

### Line 174: `heuristic = [[0.0 for _ in range(n)] for _ in range(n)]`
```python
    heuristic = [[0.0 for _ in range(n)] for _ in range(n)]
```

- Creates another 2D list for heuristic information.
- This stores how attractive a path is based on distance.

### Line 175: `city_index = {city: i for i, city in enumerate(cities)}`
```python
    city_index = {city: i for i, city in enumerate(cities)}
```

- Creates a dictionary mapping each city name to its index.
- Example: `{"A": 0, "B": 1, ...}`
- This helps the code quickly access rows and columns.

### Line 177: `for i in range(n):`
```python
    for i in range(n):
```

- Loops over all city rows.

### Line 178: `for j in range(n):`
```python
        for j in range(n):
```

- Loops over all city columns.

### Line 179: `if i != j:`
```python
            if i != j:
```

- Avoids the diagonal of the matrix.
- We do not want the distance from a city to itself.

### Line 180: `heuristic[i][j] = 1.0 / distance_matrix[cities[i]][cities[j]]`
```python
                heuristic[i][j] = 1.0 / distance_matrix[cities[i]][cities[j]]
```

- Calculates the heuristic value as the inverse of distance.
- Shorter distances receive higher heuristic values.

### Line 182: `best_route = None`
```python
    best_route = None
```

- Starts with no best route yet.

### Line 183: `best_cost = float("inf")`
```python
    best_cost = float("inf")
```

- Sets the best cost to infinity.

### Line 184: `history = []`
```python
    history = []
```

- Creates a list to record the best cost over iterations.

### Line 186: `for _ in range(iterations):`
```python
    for _ in range(iterations):
```

- Repeats the ACO process for the chosen number of iterations.

### Line 187: `solutions = []`
```python
        solutions = []
```

- Creates an empty list to store the routes built by ants.

### Line 189: `for _ in range(ants):`
```python
        for _ in range(ants):
```

- Lets each ant build one route.

### Line 190: `start_city = random.choice(cities)`
```python
            start_city = random.choice(cities)
```

- Chooses a random starting city for the ant.

### Line 191: `visited = [start_city]`
```python
            visited = [start_city]
```

- Starts the ant’s route with the chosen city.

### Line 192: `current_city = start_city`
```python
            current_city = start_city
```

- Keeps track of the city the ant is currently at.

### Line 194: `while len(visited) < n:`
```python
            while len(visited) < n:
```

- Continues until all cities have been visited.

### Line 195: `current_index = city_index[current_city]`
```python
                current_index = city_index[current_city]
```

- Gets the index of the current city for the pheromone matrix.

### Line 196: `unvisited = [city for city in cities if city not in visited]`
```python
                unvisited = [city for city in cities if city not in visited]
```

- Creates a list of cities that have not been visited yet.

### Line 198: `probabilities = []`
```python
                probabilities = []
```

- Creates an empty list to store possible next moves.

### Line 199: `total = 0.0`
```python
                total = 0.0
```

- Starts a variable used to add the probability values.

### Line 201: `for city in unvisited:`
```python
                for city in unvisited:
```

- Loops through each possible next city.

### Line 202: `city_pos = city_index[city]`
```python
                    city_pos = city_index[city]
```

- Gets the index of the candidate city.

### Line 203: `value = (pheromone[current_index][city_pos] ** alpha) * (heuristic[current_index][city_pos] ** beta)`
```python
                    value = (pheromone[current_index][city_pos] ** alpha) * (heuristic[current_index][city_pos] ** beta)
```

- Computes the probability weight for moving to the next city.
- It uses pheromone level and heuristic value.
- Higher pheromone and shorter distance means higher chance.

### Line 204: `probabilities.append((city, value))`
```python
                    probabilities.append((city, value))
```

- Stores the city and its weight together as a pair.

### Line 205: `total += value`
```python
                    total += value
```

- Adds the weight to the total probability sum.

### Line 207: `if total == 0:`
```python
                if total == 0:
```

- Checks whether the total probability is zero.
- If yes, the program chooses randomly.

### Line 208: `next_city = random.choice(unvisited)`
```python
                    next_city = random.choice(unvisited)
```

- Randomly picks one of the unvisited cities.

### Line 210: `r = random.uniform(0, total)`
```python
                    r = random.uniform(0, total)
```

- Generates a random number between `0` and the total probability.

### Line 211: `cumulative = 0.0`
```python
                    cumulative = 0.0
```

- Starts a running sum for choosing a city based on probability.

### Line 212: `next_city = None`
```python
                    next_city = None
```

- Resets the chosen city to nothing initially.

### Line 213: `for city, value in probabilities:`
```python
                    for city, value in probabilities:
```

- Loops through all the probability pairs.

### Line 214: `cumulative += value`
```python
                        cumulative += value
```

- Adds the weight to the cumulative sum.

### Line 215: `if cumulative >= r:`
```python
                        if cumulative >= r:
```

- Checks whether the random number has been reached.
- If yes, that city is selected.

### Line 216: `next_city = city`
```python
                            next_city = city
```

- Stores the chosen next city.

### Line 217: `break`
```python
                            break
```

- Stops the loop once the city is chosen.

### Line 219: `if next_city is None:`
```python
                    if next_city is None:
```

- Checks whether no city was selected.

### Line 220: `next_city = probabilities[-1][0]`
```python
                        next_city = probabilities[-1][0]
```

- Uses the last probability entry as a backup choice.

### Line 222: `visited.append(next_city)`
```python
                visited.append(next_city)
```

- Adds the selected city to the ant’s route.

### Line 223: `current_city = next_city`
```python
                current_city = next_city
```

- Updates the current city for the next step.

### Line 225: `solutions.append(visited)`
```python
            solutions.append(visited)
```

- Stores the completed route built by the ant.

### Line 227: `cost = route_distance(visited, distance_matrix)`
```python
            cost = route_distance(visited, distance_matrix)
```

- Calculates the route length of the current ant’s tour.

### Line 228: `if cost < best_cost:`
```python
            if cost < best_cost:
```

- Checks whether this ant found a better route than the current best.

### Line 229: `best_cost = cost`
```python
                best_cost = cost
```

- Saves the improved route length.

### Line 230: `best_route = visited.copy()`
```python
                best_route = visited.copy()
```

- Stores a copy of the best route.

### Line 232: `# Evaporation`
```python
        # Evaporation
```

- A comment that explains the next process.

### Line 233: `for i in range(n):`
```python
        for i in range(n):
```

- Loops through each pheromone value row.

### Line 234: `for j in range(n):`
```python
            for j in range(n):
```

- Loops through each column.

### Line 235: `pheromone[i][j] *= (1.0 - rho)`
```python
                pheromone[i][j] *= (1.0 - rho)
```

- Reduces all pheromone levels slightly.
- This is called evaporation.
- It prevents old pheromone from dominating the algorithm forever.

### Line 237: `# Global update with best route`
```python
        # Global update with best route
```

- Comment explaining that pheromone values will be updated based on the best route.

### Line 238: `if best_route is not None:`
```python
        if best_route is not None:
```

- Checks whether a best route exists.

### Line 239: `for i in range(n):`
```python
            for i in range(n):
```

- Loops through the route positions.

### Line 240: `current = best_route[i]`
```python
                current = best_route[i]
```

- Gets the current city from the best route.

### Line 241: `nxt = best_route[(i + 1) % n]`
```python
                nxt = best_route[(i + 1) % n]
```

- Gets the next city in the route.

### Line 242: `current_index = city_index[current]`
```python
                current_index = city_index[current]
```

- Converts the city name to its index.

### Line 243: `next_index = city_index[nxt]`
```python
                next_index = city_index[nxt]
```

- Converts the next city name to its index.

### Line 244: `pheromone[current_index][next_index] += q / best_cost`
```python
                pheromone[current_index][next_index] += q / best_cost
```

- Increases the pheromone on the best route.
- Better routes get more pheromone.

### Line 246: `history.append(best_cost)`
```python
        history.append(best_cost)
```

- Stores the best cost for this iteration.

### Line 248: `return best_route, best_cost, history`
```python
    return best_route, best_cost, history
```

- Returns the best route, cost, and history.

---

## 6. Compare both methods

### Line 254: `def main():`
```python
def main():
```

- Defines the main function.
- This is where the program starts its execution.

### Line 255: `random.seed(42)`
```python
    random.seed(42)
```

- Sets the random seed to `42`.
- This makes the random results repeatable.
- So the same outcome appears every time the program runs.

### Line 257: `distance_matrix = build_distance_matrix(CITY_NAMES)`
```python
    distance_matrix = build_distance_matrix(CITY_NAMES)
```

- Calls the distance matrix builder.
- The result is stored in `distance_matrix`.

### Line 259: `print("=" * 60)`
```python
    print("=" * 60)
```

- Prints 60 equal signs.
- This makes the output look neat and organized.

### Line 260: `print("Travelling Salesman Problem (TSP) using GA and ACO")`
```python
    print("Travelling Salesman Problem (TSP) using GA and ACO")
```

- Prints the title of the project.

### Line 262: `print("\nCities:")`
```python
    print("\nCities:")
```

- Prints the heading “Cities:”.
- `\n` creates a new line before the text.

### Line 263: `for city in CITY_NAMES:`
```python
    for city in CITY_NAMES:
```

- Loops through all city names and prints each one.

### Line 264: `print(f"- {city}: {CITY_COORDINATES[city]}")`
```python
        print(f"- {city}: {CITY_COORDINATES[city]}")
```

- Prints each city and its coordinates.
- `f"..."` is a formatted string.
- It inserts the value of `city` and its coordinates into the output.

### Line 265: `print("\nDistance matrix created successfully.\n")`
```python
    print("\nDistance matrix created successfully.\n")
```

- Prints a message that confirms the matrix has been created.

### Line 267: `print("Running Genetic Algorithm...")`
```python
    print("Running Genetic Algorithm...")
```

- Prints a message before the GA starts.

### Line 268: `ga_route, ga_cost, ga_history = run_genetic_algorithm(distance_matrix)`
```python
    ga_route, ga_cost, ga_history = run_genetic_algorithm(distance_matrix)
```

- Calls the GA function.
- The returned values are assigned to three variables:
  - `ga_route`: best route found by GA
  - `ga_cost`: best distance found by GA
  - `ga_history`: list of best costs over generations

### Line 269: `print("GA Best Route:", print_route(ga_route))`
```python
    print("GA Best Route:", print_route(ga_route))
```

- Prints the GA best route.
- `print_route(ga_route)` converts the list into a readable route string.

### Line 270: `print("GA Best Distance:", ga_cost)`
```python
    print("GA Best Distance:", ga_cost)
```

- Prints the best distance found by GA.

### Line 271: `print("GA Convergence History:", ga_history)`
```python
    print("GA Convergence History:", ga_history)
```

- Prints the history showing how the best cost changed over generations.

### Line 272: `print("GA Improvement:", round(ga_history[0] - ga_history[-1], 2))`
```python
    print("GA Improvement:", round(ga_history[0] - ga_history[-1], 2))
```

- Calculates how much improvement happened.
- `ga_history[0]` is the first value, and `ga_history[-1]` is the last value.
- Their difference shows whether the algorithm improved.

### Line 274: `print()`
```python
    print()
```

- Prints a blank line to make the output easier to read.

### Line 276: `print("Running Ant Colony Optimization...")`
```python
    print("Running Ant Colony Optimization...")
```

- Prints a message before the ACO algorithm starts.

### Line 277: `aco_route, aco_cost, aco_history = run_ant_colony_optimization(distance_matrix)`
```python
    aco_route, aco_cost, aco_history = run_ant_colony_optimization(distance_matrix)
```

- Calls the ACO function.
- The returned values are assigned to:
  - `aco_route`
  - `aco_cost`
  - `aco_history`

### Line 278: `print("ACO Best Route:", print_route(aco_route))`
```python
    print("ACO Best Route:", print_route(aco_route))
```

- Prints the best route found by ACO.

### Line 279: `print("ACO Best Distance:", aco_cost)`
```python
    print("ACO Best Distance:", aco_cost)
```

- Prints the best distance found by ACO.

### Line 280: `print("ACO Convergence History:", aco_history)`
```python
    print("ACO Convergence History:", aco_history)
```

- Prints the ACO convergence history.

### Line 281: `print("ACO Improvement:", round(aco_history[0] - aco_history[-1], 2))`
```python
    print("ACO Improvement:", round(aco_history[0] - aco_history[-1], 2))
```

- Calculates the improvement of the ACO algorithm.

### Line 283: `print()`
```python
    print()
```

- Prints another blank line for readability.

### Line 285: `print("Comparison:")`
```python
    print("Comparison:")
```

- Prints a heading for the comparison section.

### Line 286: `if ga_cost < aco_cost:`
```python
    if ga_cost < aco_cost:
```

- Compares the final GA and ACO costs.
- If GA produced a shorter route, this condition is true.

### Line 287: `print("Genetic Algorithm found a shorter route.")`
```python
        print("Genetic Algorithm found a shorter route.")
```

- Prints the result if GA did better.

### Line 288: `elif aco_cost < ga_cost:`
```python
    elif aco_cost < ga_cost:
```

- Checks whether ACO performed better.

### Line 289: `print("Ant Colony Optimization found a shorter route.")`
```python
        print("Ant Colony Optimization found a shorter route.")
```

- Prints the result if ACO did better.

### Line 290: `else:`
```python
    else:
```

- Runs when both methods produced the same length route.

### Line 291: `print("Both methods found the same route length.")`
```python
        print("Both methods found the same route length.")
```

- Prints the message when the results are equal.

### Line 293: `print("\nProject completed successfully.")`
```python
    print("\nProject completed successfully.")
```

- Prints the final success message.

### Line 296: `if __name__ == "__main__":`
```python
if __name__ == "__main__":
```

- This is a common Python pattern.
- It ensures the program runs only when the file is executed directly.

### Line 297: `main()`
```python
    main()
```

- Calls the `main()` function to start the project.

---

## Final summary

This project uses two metaheuristic methods to solve the TSP:

1. Genetic Algorithm (GA)
   - Creates many random routes
   - Keeps the best ones
   - Combines them and mutates them to improve the solution

2. Ant Colony Optimization (ACO)
   - Simulates ants moving through paths
   - Uses pheromone trails to guide the search
   - Strengthens better routes over time

The program compares both methods and shows which route is shortest.

---

## Important beginner concepts used in this code

- Variable: stores a value
- List: stores many values in order
- Dictionary: stores data as key-value pairs
- Function: reusable block of code
- Loop: repeats something many times
- `if` statement: makes a decision
- Return value: sends information back from a function

---

## How to run the program

Open the terminal in the project folder and run:

```bash
python3 "Project-5 Travelling Salesman Problem using GA and ACO Project.py"
```

This will print the best routes and distances found by both methods.
