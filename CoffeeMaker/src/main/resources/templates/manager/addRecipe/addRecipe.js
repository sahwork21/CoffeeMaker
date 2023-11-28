var app = angular.module('myApp', []);

app.controller('AddRecipeController', function($scope, $http, $q) {

    // Recipe object that is to be sent in POST
    $scope.recipe = {
        name: '',
        price: '',
        ingredients: []
    }

    $scope.selectedIngredient = null;
    $scope.allIngredients = []; // All the ingredients that exist in the inventory
    $scope.availableIngredients = []; // The ingredients to be presented in dropdown (excludes already added ingredients)
    $scope.invalid = {} // Mark fields in the form as invalid. Ingredients are read as invalid[ingredient.name]

    // Function to check if a recipe has a specific ingredient
    $scope.hasIngredient = function(name) {
        let ingredients = $scope.recipe.ingredients;
        for (let index = 0; index < ingredients.length; index++) {
            let ingredient = ingredients[index];
            if (ingredient.name == name) {
                return true;
            }
        }
        return false;
    }

    // Function called when an ingredient is selected
    $scope.ingredientSelected = function() {
        if ($scope.selectedIngredient == undefined) return;
        if ($scope.hasIngredient($scope.selectedIngredient.name)) return;

        $scope.invalid.minimumIngredient = false;

        $scope.recipe.ingredients.push({
            name: $scope.selectedIngredient.name,
            amount: '',
            isInventory: false
        })

        $scope.selectedIngredient = undefined;
        $scope.validateIngredients();
        $scope.filterAvailableIngredients();
    }

    // Function to filter available ingredients
    $scope.filterAvailableIngredients = function() {
        let availableIngredients = [];
        for (ingredient of $scope.allIngredients) {
            if (!$scope.hasIngredient(ingredient.name)) {
                availableIngredients.push(ingredient);
            }
        }

        $scope.availableIngredients = availableIngredients;

        let dropdown = document.getElementById("ingredientDropdown");
        dropdown.value = null;
    }

    // Function to remove an ingredient from the recipe
    $scope.removeIngredient = function(ingredient) {
        let ingredients = $scope.recipe.ingredients;
        let index = ingredients.indexOf(ingredient);
        if (index < 0) return;

        ingredients.splice(index, 1);

        $scope.validateIngredients();
        $scope.filterAvailableIngredients();
    }

    // Fetch inventory data from the server
    $http.get("/api/v1/inventory").then(function(response) {
        let ingredients = response.data.ingredients;
        console.log(response.data);
        for (let i = 0; i < ingredients.length; i++) {
            $scope.allIngredients[i] = {
                'id': ingredients[i].id,
                'name': ingredients[i].name,
                'display': ingredients[i].name
            }
        }

        $scope.filterAvailableIngredients();
    })

    // Function to add a new recipe
    $scope.addRecipe = function() {
        console.log($scope.recipe)
        $scope.success = false;
        $scope.failure = false;

        if ($scope.recipe.ingredients <= 0) return;

        $http.post("/api/v1/recipes", $scope.recipe).then(
            function(success) {
                $scope.reset();
                $scope.success = true;
                $scope.failure = false;
                console.log(success.data.message);
            }, function(rejection) {
                console.log(rejection.data.message);
                $scope.failure = rejection.data.message;
            });
    }

    // Function used by the Input elements, when a textbox is clicked to edit, reset invalid.
    $scope.resetInvalid = function(field) {
        $scope.invalid[field] = false;
    }

    // VALIDATION

    // Generic validation function
    onValidate = function(field, condition) {
        $scope.invalid[field] = !condition;
        return condition;
    }

    // Validation for recipe name
    $scope.validateName = function() {
        return onValidate('name', $scope.recipe.name != null && $scope.recipe.name.length > 0);
    };

    // Validation for recipe price
    $scope.validatePrice = function() {
        return onValidate('price', $scope.recipe.price > 0);
    };

    // Validation for recipe ingredients
    $scope.validateIngredients = function() {
        return onValidate('ingredients', $scope.recipe.ingredients.length > 0);
    }

    // Validation for a specific ingredient
    $scope.validateIngredient = function(ingredient) {
        return onValidate(ingredient.name, ingredient.amount > 0);
    }

    // Validation for the entire recipe
    $scope.validateRecipe = function(recipe) {
        isValid = true;
        if (!$scope.validateName()) isValid = false;
        if (!$scope.validatePrice()) isValid = false;
        if (!$scope.validateIngredients()) isValid = false;

        for (ingredient of recipe.ingredients) {
            if (!$scope.validateIngredient(ingredient)) isValid = false;
        };

        return isValid;
    }

    // VALIDATION

    // Function to submit the form
    $scope.submit = function() {
        if (!$scope.validateRecipe($scope.recipe)) return;
        $scope.addRecipe();
    }

    // Resets the form
    $scope.reset = function() {
        $scope.recipe = {
            name: '',
            price: '',
            ingredients: []
        };

        $scope.filterAvailableIngredients();
    }

    $scope.reset();
});