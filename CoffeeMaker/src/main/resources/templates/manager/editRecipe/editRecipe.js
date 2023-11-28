var app = angular.module('myApp', []);

app.controller('EditRecipeController', function($scope, $http) {

    // Initialize scope variables
    $scope.allRecipes = [];
    $scope.selectedRecipe = null;
    $scope.invalid = {
        minimumIngredient: false // Set to true to highlight add ingredient and show error
    };

    $scope.allIngredients = [];
    $scope.availableIngredients = [];

    // Function to fetch recipes from the server
    $scope.getRecipes = function() {
        $http.get("/api/v1/recipes").then(function(response) {
            // Update allRecipes data with the fetched data
            $scope.allRecipes = response.data;
        });
    }

    // Function to update a recipe on the server
    $scope.putRecipe = function() {
        $scope.success = false;
        $scope.failure = false;

        if (!$scope.validateRecipe($scope.selectedRecipe)) return;

        // Delete the id from the ingredients because the backend considers ALL ingredients as new ingredients.
        for (let i = 0; i < $scope.selectedRecipe.ingredients.length; i++) {
            $scope.selectedRecipe.ingredients[i].id = null;
        }

        $http.put("/api/v1/recipes/" + $scope.selectedRecipe.name, $scope.selectedRecipe).then(
            function(response) {
                // Update success and fetch recipes
                $scope.success = true;
                $scope.failure = false;
                $scope.getRecipes();
                console.log(response.data.message);
            }, function(rejection) {
                // Handle failure and set error message
                $scope.failure = true;
                $scope.success = false;
                $scope.error = "Failed to update recipe";
                console.error("Error while updating inventory: " + rejection.data.message);
            });
    }

    // Function to fetch ingredients from the server
    $scope.getIngredients = function() {
        $http.get("/api/v1/inventory").then(function(response) {
            // Update allIngredients data with the fetched data
            $scope.allIngredients = response.data.ingredients;
        });
    }

    // Function to filter available ingredients
    $scope.filterAvailableIngredients = function() {
        $scope.availableIngredients = $scope.allIngredients.filter(function(ingredient) {
            return !$scope.hasIngredient(ingredient.name);
        });
    }

    // Function to check if a recipe has a specific ingredient
    $scope.hasIngredient = function(name) {
        let ingredients = $scope.selectedRecipe.ingredients;
        for (let index = 0; index < ingredients.length; index++) {
            let ingredient = ingredients[index];
            if (ingredient.name == name) {
                return true;
            }
        }
        return false;
    }

    // Function called when a recipe is selected
    $scope.recipeSelected = function() {
        if ($scope.selectedRecipe == undefined) {
            $scope.failure = false;
            return;
        }
        $scope.clearErrors();
        $scope.filterAvailableIngredients();
    }

    // Function called when an ingredient is selected
    $scope.ingredientSelected = function() {
        if ($scope.selectedIngredient == undefined) return;
        if ($scope.hasIngredient($scope.selectedIngredient.name)) return;

        $scope.invalid.minimumIngredient = false;

        // Add the selected ingredient to the recipe
        $scope.selectedRecipe.ingredients.push({
            name: $scope.selectedIngredient.name,
            amount: '',
            isInventory: false
        });

        $scope.selectedIngredient = undefined;
        $scope.noIngredients = false;
        $scope.filterAvailableIngredients();
    }

    // Function to remove an ingredient from the recipe
    $scope.removeIngredient = function(ingredient) {
        let ingredients = $scope.selectedRecipe.ingredients;
        let index = ingredients.indexOf(ingredient);
        if (index < 0) return;

        ingredients.splice(index, 1);

        if (ingredients.length <= 0) {
            $scope.noIngredients = true;
        }

        $scope.filterAvailableIngredients();
    }

    // Validation functions
    onValidate = function(field, condition) {
        $scope.invalid[field] = !condition;
        return condition;
    }

    $scope.validatePrice = function() {
        return onValidate('price', $scope.selectedRecipe.price > 0);
    };

    $scope.validateIngredients = function() {
        return onValidate('ingredients', $scope.selectedRecipe.ingredients.length > 0);
    }

    $scope.validateIngredient = function(ingredient) {
        return onValidate(ingredient.name, ingredient.amount > 0);
    }

    $scope.validateRecipe = function() {
        isValid = true;
        if (!$scope.validatePrice()) isValid = false;
        if (!$scope.validateIngredients()) isValid = false;
        for (ingredient of $scope.selectedRecipe.ingredients) {
            if (!$scope.validateIngredient(ingredient)) {
                isValid = false;
            }
        }
        return isValid;
    }

    // Function to submit the form
    $scope.submit = function() {
        if (!$scope.validateRecipe()) return;
        $scope.putRecipe();
    }

    // Function to clear errors
    $scope.clearErrors = function() {
        $scope.success = false;
        $scope.failure = false;
        $scope.invalid.minimumIngredient = false;
    }

    // Function to reset the form
    $scope.reset = function() {
        $scope.selectedRecipe = undefined;
        $scope.clearErrors();
    }

    // Initialize the form and fetch data
    $scope.reset();
    $scope.getRecipes();
    $scope.getIngredients();
    
    console.log($scope);
});
