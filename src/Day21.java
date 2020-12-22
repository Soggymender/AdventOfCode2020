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
}

public class Day21
{
    List<Dish> dishes = new ArrayList<Dish>();

    HashMap<String, String> allergensToIngredients = new HashMap<String, String>();
    List<String> allAllergens = new ArrayList<String>();

    public static void main( String[] args )
    {
        Day21 dayN = new Day21();

        Instant start = Instant.now();

        dayN.read();
        dayN.pairDown();
        int result = dayN.tally();
        dayN.buildBadList();

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

                    dish.ingredientNames.add(ingredients[i]);

                    for (int j = 1; j < allergens.length; j++) {
                        dish.allergenNames.add(allergens[j]);
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

        /*
        while some ingredients marked safe

            for each dish alergen
            find other dishes with same allergen
            see if there is ONE ingredient in all dishes
                mark that ingredient with the allergen
                add allergen to identified list
                add word to identified list
        */

        Set<String> ingredientsToRemove = new HashSet<String>();
        Set<String> allergensToRemove = new HashSet<String>();

        do {

            ingredientsToRemove.clear();
            allergensToRemove.clear();

            // Iterate the dishes.
            for (Dish dish : dishes) {
                
                // Iterate the allergens.
                for (String allergen : dish.allergenNames) {

                    Set<String> ingredientsInAll = new HashSet<String>();

                    // Iterate the ingredients.
                    for (String ingredient : dish.ingredientNames) {

                        boolean inAll = true;

                        // Iterate the dishes.
                        for (Dish iDish : dishes) {

                            // Check if this dish has the allergen.
                            if (!iDish.allergenNames.contains(allergen))
                                continue;

                            // Check if this dish has the ingredient.
                            if (!iDish.ingredientNames.contains(ingredient)) {
                                inAll = false;
                                break;
                            }
                        }

                        if (inAll) {
                                
                            // This ingredient is in all.
                            // It's a candidate for removal. But are there others?
                            ingredientsInAll.add(ingredient);
                        }
                    }

                    // If there's only one ingredient in all of the dishes with this allergen
                    // then the ingredient and allergen can be removed from the entire menu and we can start over.
                    if (ingredientsInAll.size() == 1) {
                        ingredientsToRemove.add((String)ingredientsInAll.toArray()[0]);
                        allergensToRemove.add(allergen);

                        // For part 2, store the allergen and the food that contains it.
                        allergensToIngredients.put(allergen, (String)ingredientsInAll.toArray()[0]);
                        
                        if (!allAllergens.contains(allergen))
                            allAllergens.add(allergen);
                    }
                }
            }

            // Remove identified ingredients and allergens from the menu.
            for (Dish dish : dishes) {

                for (String ingredient : ingredientsToRemove)
                    dish.ingredientNames.remove(ingredient);
                
                for (String allergen : allergensToRemove)
                    dish.allergenNames.remove(allergen);    
            }

        } while (ingredientsToRemove.size() > 0);
    }

    int tally() {

        int total = 0;

        HashMap<String, Integer>safeIngredients = new HashMap<String, Integer>();

        // Iterate the dishes.
        for (Dish dish : dishes) {

            // Iterate the ingredients.
            for (String ingredient : dish.ingredientNames) {

                if (safeIngredients.containsKey(ingredient)) {
                    Integer val = safeIngredients.get(ingredient);
                    safeIngredients.replace(ingredient, val + 1);
                } else {
                    safeIngredients.put(ingredient, 1); 
                }
            }
        }

        // Count how many times each listed ingredient is in a dish.
        for (String safeIngredient : safeIngredients.keySet()) {

            System.out.println(safeIngredient);
            total += safeIngredients.get(safeIngredient);
        }

        return total;
    }

    void buildBadList()
    {
        Collections.sort(allAllergens);
        
        String badList = "";

        for (String allergen : allAllergens) {

            String ingredient = allergensToIngredients.get(allergen);
            
            if (badList.length() > 0)
                badList += ",";
                
            badList += ingredient;
        }
        
        System.out.println(badList);
    }
}