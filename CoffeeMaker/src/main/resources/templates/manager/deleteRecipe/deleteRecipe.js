

var app = angular.module('myApp', []);
app.controller('DeleteRecipeController', function($scope, $http) {

	function updateRecipes() {
		$http.get("/api/v1/recipes").then(function(response) {
			console.log(response.data);
			$scope.recipes = response.data;
		});
	}

	$scope.deleteRecipe = function(recipe) {
		console.log("Deleting recipe", recipe.name);
		$http.delete("/api/v1/recipes/" + recipe.name)
			.then(
				function(response) {
					console.log(response);
					$scope.submissionSuccess = true;


					updateRecipes();
				},
				function(rejection) {
					console.log(rejection);

					$scope.failure = true;

					// Update recipe list
					updateRecipes();
				}
			);
	}

	$scope.del = function() {
		if ($scope.deleteAll) {
			for (var i = 0, len = $scope.recipes.length; i < len; i++) {
				var recipe = $scope.recipes[i];
				deleteRecipe(recipe.name);
			}
		} else {
			deleteRecipe($scope.name);
		}

		updateRecipes();
	}

	updateRecipes();
	
	
});