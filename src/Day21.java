import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;
import java.util.regex.*;

class Dish
{   
    Set<String> ingredientNames = new HashSet<String>();
    Set<String> allergenNames = new HashSet<String>();

    List<Ingredient> ingredients = new ArrayList<Ingredient>();
}

class Ingredient
{
    String name;
    Set<String> allergenNames = new HashSet<String>();
}

public class Day21
{
    List<Dish> dishes = new ArrayList<Dish>();

    public static void main( String[] args )
    {
        Day21 dayN = new Day21();

        Instant start = Instant.now();

        dayN.read();
        dayN.pairDown();
        int result = dayN.tally();

        System.out.println(result);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }

    public void read() {
    
        // Parse lines.
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("day21_input.txt"));

            String line;
     
            line = reader.readLine();
            while (line != null) {
 
                String[] parts = line.split("\\(");
                
                String[] ingredients = parts[0].split(" ");
                String[] allergens = parts[1].split(" |, |\\)");

                Dish dish = new Dish();
                dishes.add(dish);

                for (int i = 0; i < ingredients.length; i++) {

                    Ingredient ingredient = new Ingredient();
                    dish.ingredients.add(ingredient);

                    ingredient.name = ingredients[i];

                    dish.ingredientNames.add(ingredient.name);

                    for (int j = 1; j < allergens.length; j++) {
                        dish.allergenNames.add(allergens[j]);
            
                        ingredient.allergenNames.add(allergens[j]);
                    }
                }

                line = reader.readLine();
            }
           
            reader.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void pairDown() {

        for (Dish dish : dishes) {

            for (Ingredient ingredient : dish.ingredients) {

                Set<String> toRemove = new HashSet<String>();

                for (String allergen : ingredient.allergenNames) {

                    // If this allergen is listed in another food,
                    // but this ingredient is not, this allergen is not in this ingredient.

                    boolean foundAllergen = false;
                    boolean foundIngredient = false;

                    for (Dish iDish : dishes) {
                        if (iDish == dish)
                            continue;
                        
                        for (String dishAllergen : iDish.allergenNames) {

                            // This dish also contains the allergen.
                            if (dishAllergen.equals(allergen)) {

                                foundAllergen = true;

                                // Check this dish's ingredients.
                                if (iDish.ingredientNames.contains(ingredient.name)) {
                                    foundIngredient = true;
                                    break;
                                }
                            }
                        }

                        if (foundIngredient)
                            break;
                    }

                    if (foundAllergen && !foundIngredient) {
                        toRemove.add(allergen);
                    }
                }

                for (String allergen : toRemove)
                    ingredient.allergenNames.remove(allergen);
            }
        }
    }

    int tally() {

        int total = 0;

        Set<String>safeIngredients = new HashSet<String>();

        // Iterate the dishes.
        for (Dish dish : dishes) {

            // Iterate the ingredients.
            for (Ingredient ingredient : dish.ingredients) {

                boolean empty = true;
                // If this ingredient does not have allergens
                for (String allergen : ingredient.allergenNames) {
                    // Add it to a list.
                    if (allergen.length() > 0) {
                        empty = false;
                        break;
                    }
                }

                if (empty) {
                    safeIngredients.add(ingredient.name);
                }//// else if (safeIngredients.contains(ingredient.name))
                 //   safeIngredients.remove(ingredient.name);
            }
        }

        // Count how many times each listed ingredient is in a dish.
        for (String safeIngredient : safeIngredients) {

            System.out.println(safeIngredient);

            for (Dish dish : dishes) {
                if (dish.ingredientNames.contains(safeIngredient))
                    total++;
            }
        }

        return total;
    }
}