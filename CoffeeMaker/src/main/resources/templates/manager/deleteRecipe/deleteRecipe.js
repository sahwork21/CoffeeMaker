var app = angular.module('myApp', []);

app.controller('DeleteRecipeController', function($scope, $http) {

    // Function to update the list of recipes by fetching the latest data from the server.
    function updateRecipes() {
        $http.get("/api/v1/recipes").then(function(response) {
            // Log the response data and update the recipes variable in the scope
            console.log(response.data);
            $scope.recipes = response.data;
        });
    }

    // Function to delete a specific recipe.
    $scope.deleteRecipe = function(recipe) {
        console.log("Deleting recipe", recipe.name);
        $http.delete("/api/v1/recipes/" + recipe.name)
            .then(
                function(response) {
                    // Log the response and set submissionSuccess flag to true
                    console.log(response);
                    $scope.submissionSuccess = true;

                    // Update the recipe list
                    updateRecipes();
                },
                function(rejection) {
                    // Log the rejection and set failure flag to true
                    console.log(rejection);
                    $scope.failure = true;

                    // Update the recipe list
                    updateRecipes();
                }
            );
    }

    // Function to delete either a single recipe or all recipes based on the user's selection.
    $scope.del = function() {
        if ($scope.deleteAll) {
            // Delete all recipes individually
            for (var i = 0, len = $scope.recipes.length; i < len; i++) {
                var recipe = $scope.recipes[i];
                $scope.deleteRecipe(recipe);
            }
        } else {
            // Delete a single recipe
            $scope.deleteRecipe({ name: $scope.name });
        }

        // Update the recipe list
        updateRecipes();
    }

    // Initial update of recipes when the controller is initialized
    updateRecipes();
});
