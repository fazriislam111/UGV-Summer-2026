"""
Travelling Salesman Problem (TSP) using Genetic Algorithm (GA) and Ant Colony Optimization (ACO)

This is a beginner-friendly implementation for undergraduate students.
It solves a small TSP instance using two metaheuristic methods:
1. Genetic Algorithm (selection, crossover, mutation)
2. Ant Colony Optimization (pheromone update)

The program compares both methods and prints the best route and its distance.
"""

import random
import math

# -----------------------------
# 1. Dataset (small example)
# -----------------------------
CITY_NAMES = ["A", "B", "C", "D", "E", "F", "G"]
CITY_COORDINATES = {
    "A": (0, 0),
    "B": (1, 3),
    "C": (3, 4),
    "D": (5, 2),
    "E": (6, 5),
    "F": (4, 7),
    "G": (2, 6),
}


# -----------------------------
# 2. Helper functions
# -----------------------------
def calculate_distance(city1, city2):
    """Return Euclidean distance between two cities."""
    x1, y1 = CITY_COORDINATES[city1]
    x2, y2 = CITY_COORDINATES[city2]
    return round(math.sqrt((x2 - x1) ** 2 + (y2 - y1) ** 2), 2)


def build_distance_matrix(cities):
    """Create a full distance matrix for all cities."""
    matrix = {}
    for city in cities:
        matrix[city] = {}
        for other_city in cities:
            if city == other_city:
                matrix[city][other_city] = 0.0
            else:
                matrix[city][other_city] = calculate_distance(city, other_city)
    return matrix


def route_distance(route, distance_matrix):
    """Compute total tour length for a route."""
    total = 0.0
    for i in range(len(route)):
        current = route[i]
        nxt = route[(i + 1) % len(route)]
        total += distance_matrix[current][nxt]
    return round(total, 2)


def print_route(route):
    """Display a route in a readable way."""
    return " -> ".join(route + [route[0]])


# -----------------------------
# 3. Genetic Algorithm (GA)
# -----------------------------
def create_initial_population(population_size, cities):
    """Create random valid tours for the initial population."""
    population = []
    for _ in range(population_size):
        route = cities.copy()
        random.shuffle(route)
        population.append(route)
    return population


def tournament_selection(population, costs):
    """Choose one parent using tournament selection."""
    index1 = random.randint(0, len(population) - 1)
    index2 = random.randint(0, len(population) - 1)

    if costs[index1] <= costs[index2]:
        return population[index1]
    return population[index2]


def order_crossover(parent1, parent2):
    """Create a child using Order Crossover (OX)."""
    size = len(parent1)
    start = random.randint(0, size - 1)
    end = random.randint(start, size - 1)

    child = [None] * size
    child[start:end + 1] = parent1[start:end + 1]

    remaining = [city for city in parent2 if city not in child]
    index = 0
    for i in range(size):
        if child[i] is None:
            child[i] = remaining[index]
            index += 1

    return child


def mutate_route(route, mutation_rate):
    """Apply mutation by swapping two cities."""
    if random.random() < mutation_rate:
        i = random.randint(0, len(route) - 1)
        j = random.randint(0, len(route) - 1)
        route[i], route[j] = route[j], route[i]
    return route


def run_genetic_algorithm(distance_matrix, population_size=40, generations=80, mutation_rate=0.08):
    """Run the Genetic Algorithm and return the best route and history."""
    cities = list(CITY_NAMES)
    population = create_initial_population(population_size, cities)

    best_route = None
    best_cost = float("inf")
    history = []

    for _ in range(generations):
        costs = [route_distance(route, distance_matrix) for route in population]

        for route, cost in zip(population, costs):
            if cost < best_cost:
                best_cost = cost
                best_route = route.copy()

        history.append(best_cost)

        new_population = []

        # Keep the best route from the current generation
        new_population.append(best_route.copy())

        while len(new_population) < population_size:
            parent1 = tournament_selection(population, costs)
            parent2 = tournament_selection(population, costs)

            child1 = order_crossover(parent1, parent2)
            child2 = order_crossover(parent2, parent1)

            child1 = mutate_route(child1, mutation_rate)
            child2 = mutate_route(child2, mutation_rate)

            new_population.append(child1)
            if len(new_population) < population_size:
                new_population.append(child2)

        population = new_population

    return best_route, best_cost, history


# -----------------------------
# 4. Ant Colony Optimization (ACO)
# -----------------------------
def run_ant_colony_optimization(distance_matrix, ants=30, iterations=60, alpha=1.0, beta=3.0, rho=0.1, q=100.0):
    """Run Ant Colony Optimization and return the best route and history."""
    cities = list(CITY_NAMES)
    n = len(cities)

    # Pheromone matrix; start with a small positive value
    pheromone = [[1.0 for _ in range(n)] for _ in range(n)]

    # Heuristic information: shorter distances get higher desirability
    heuristic = [[0.0 for _ in range(n)] for _ in range(n)]
    city_index = {city: i for i, city in enumerate(cities)}

    for i in range(n):
        for j in range(n):
            if i != j:
                heuristic[i][j] = 1.0 / distance_matrix[cities[i]][cities[j]]

    best_route = None
    best_cost = float("inf")
    history = []

    for _ in range(iterations):
        solutions = []

        for _ in range(ants):
            start_city = random.choice(cities)
            visited = [start_city]
            current_city = start_city

            while len(visited) < n:
                current_index = city_index[current_city]
                unvisited = [city for city in cities if city not in visited]

                probabilities = []
                total = 0.0

                for city in unvisited:
                    city_pos = city_index[city]
                    value = (pheromone[current_index][city_pos] ** alpha) * (heuristic[current_index][city_pos] ** beta)
                    probabilities.append((city, value))
                    total += value

                if total == 0:
                    next_city = random.choice(unvisited)
                else:
                    r = random.uniform(0, total)
                    cumulative = 0.0
                    next_city = None
                    for city, value in probabilities:
                        cumulative += value
                        if cumulative >= r:
                            next_city = city
                            break

                    if next_city is None:
                        next_city = probabilities[-1][0]

                visited.append(next_city)
                current_city = next_city

            solutions.append(visited)

            cost = route_distance(visited, distance_matrix)
            if cost < best_cost:
                best_cost = cost
                best_route = visited.copy()

        # Evaporation
        for i in range(n):
            for j in range(n):
                pheromone[i][j] *= (1.0 - rho)

        # Global update with best route
        if best_route is not None:
            for i in range(n):
                current = best_route[i]
                nxt = best_route[(i + 1) % n]
                current_index = city_index[current]
                next_index = city_index[nxt]
                pheromone[current_index][next_index] += q / best_cost

        history.append(best_cost)

    return best_route, best_cost, history


# -----------------------------
# 5. Compare both methods
# -----------------------------
def main():
    random.seed(42)

    distance_matrix = build_distance_matrix(CITY_NAMES)

    print("=" * 60)
    print("Travelling Salesman Problem (TSP) using GA and ACO")
    print("=" * 60)
    print("\nCities:")
    for city in CITY_NAMES:
        print(f"- {city}: {CITY_COORDINATES[city]}")
    print("\nDistance matrix created successfully.\n")

    print("Running Genetic Algorithm...")
    ga_route, ga_cost, ga_history = run_genetic_algorithm(distance_matrix)
    print("GA Best Route:", print_route(ga_route))
    print("GA Best Distance:", ga_cost)
    print("GA Convergence History:", ga_history)
    print("GA Improvement:", round(ga_history[0] - ga_history[-1], 2))
    print()

    print("Running Ant Colony Optimization...")
    aco_route, aco_cost, aco_history = run_ant_colony_optimization(distance_matrix)
    print("ACO Best Route:", print_route(aco_route))
    print("ACO Best Distance:", aco_cost)
    print("ACO Convergence History:", aco_history)
    print("ACO Improvement:", round(aco_history[0] - aco_history[-1], 2))
    print()

    print("Comparison:")
    if ga_cost < aco_cost:
        print("Genetic Algorithm found a shorter route.")
    elif aco_cost < ga_cost:
        print("Ant Colony Optimization found a shorter route.")
    else:
        print("Both methods found the same route length.")

    print("\nProject completed successfully.")


if __name__ == "__main__":
    main()
