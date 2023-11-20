

var app = angular.module('myApp', []);
app.controller('DeleteRecipeController', function($scope, $http) {

	function updateRecipes() {
		$http.get("/api/v1/recipes").then(function(response) {
			$scope.recipes = response.data;
		});
	}

	function deleteRecipe(recipe) {
		console.log("Deleting recipe", recipe);
		$http.delete("/api/v1/recipes/" + recipe)
			.then(
				function(response) {
					console.log(response);
					$scope.submissionSuccess = true;


					updateRecipes();
				},
				function(rejection) {
					console.error('Error while deleting recipe');
					console.log(rejection);

					$scope.submissionSuccess = false;

					// Update recipe list
					$http.get("/api/v1/recipes").then(function(response) {
						$scope.recipes = response.data;
					});
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