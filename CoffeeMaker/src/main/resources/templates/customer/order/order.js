var app = angular.module('myApp', []);


app.controller('OrderController', function($scope, $http, $q) {
	$scope.hidePopup = true;
	$scope.selectedRecipe = null;
	$scope.payment = null;
	$scope.recipes = [];
	
	$scope.fetchRecipes = function() {
		$http.get("/api/v1/recipes").then(function(response) {
			$scope.recipes = response.data;			
		});
	}
	
	$scope.onRecipeSelect = function(recipe) {
		$scope.selectedRecipe = recipe;
		$scope.hidePopup = false;
	}
	
	$scope.cancelPopup = function() {
		$scope.hidePopup = true;
	}
	
	$scope.orderRecipe = function() {
		$scope.success = false;
		$scope.failure = false;
		$http.put("/api/v1/makecoffee/" + $scope.selectedRecipe.name, $scope.payment).then(function(success) {
			$scope.success = true;
			$scope.cancelPopup();
		}, function (rejection) {
			$scope.failure = true;
			console.error("Error while ordering coffee.");
		});
		
	}
	
	$scope.fetchRecipes();
});